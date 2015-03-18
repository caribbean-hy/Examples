package com.example.picklh.examples.fragment;


import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.SpinnerAdapter;

import com.example.picklh.examples.Constants;
import com.example.picklh.examples.R;
import com.example.picklh.examples.activity.SimpleImageActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

import java.nio.charset.MalformedInputException;
import java.util.zip.Inflater;

public class ImageGalleryFragment extends BaseFragment {

    public static final int INDEX=3;

    String[] imageUrls=Constants.IMAGES;
    DisplayImageOptions options;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_stub)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_error)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }

    @SuppressWarnings("deprecation") //选择性地取消特定方法中的警告，deprecation使用了不赞成使用的类或方法时的警告
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fr_image_gallery,container,false);
        Gallery gallery = (Gallery)rootView.findViewById(R.id.gallery);
        ((Gallery)gallery).setAdapter(new ImageAdapter());
        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                startImagePagerActivity(position);
            }
        });
        return rootView;
    }

    private void startImagePagerActivity(int position){
        Intent intent=new Intent(getActivity(),SimpleImageActivity.class);
        intent.putExtra(Constants.Extra.FRAGMENT_INDEX,ImagePagerFragment.INDEX);
        intent.putExtra(Constants.Extra.IMAGE_POSITION,position);
        startActivity(intent);
    }

    private class ImageAdapter extends BaseAdapter{

        private LayoutInflater inflater;

        ImageAdapter(){
            inflater=LayoutInflater.from(getActivity());
        }

        @Override
        public int getCount() {
            return imageUrls.length;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            ImageView imageView=(ImageView)convertView;
            if(imageView==null){
                imageView=(ImageView)inflater.inflate(R.layout.item_gallery_image,viewGroup,false);

            }
            ImageLoader.getInstance().displayImage(imageUrls[position],imageView,options);
            return imageView;
        }
    }
}

