package com.example.picklh.examples.fragment;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.picklh.examples.Constants;
import com.example.picklh.examples.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.zip.Inflater;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImageListFragment extends AbsListViewBaseFragment {

    public static final int INDEX=0;
    String[] imageUrls= Constants.IMAGES;
    DisplayImageOptions options;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        options=new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_stub)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_error)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .displayer(new RoundedBitmapDisplayer(20))
                .build();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView=inflater.inflate(R.layout.fr_image_list, container, false);
        listView=(ListView) rootView.findViewById(android.R.id.list);
        ((ListView) listView).setAdapter(new ImageAdapter());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                startImagePagerActivity(position);
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

    private static class  ViewHolder{
        TextView text;
        ImageView image;
    }

    class ImageAdapter extends BaseAdapter{

        private LayoutInflater inflater;
        private ImageLoadingListener animateFirstListener=new AnimateFirstDisplayListener();

        ImageAdapter(){
            inflater=LayoutInflater.from(getActivity());
        }

        public int getCount(){
            return imageUrls.length;
        }

        public Object getItem(int position){
            return position;
        }

        public long getItemId(int position){
            return position;
        }

        public View getView(final int position, View convertView, ViewGroup parent){
            View view=convertView;
            final ViewHolder holder;
            if(convertView == null){
                view=inflater.inflate(R.layout.item_list_image,parent,false);
                holder=new ViewHolder();
                holder.image=(ImageView) view.findViewById(R.id.image);
                holder.text=(TextView)view.findViewById(R.id.text);
                view.setTag(holder);
            }else{
                holder=(ViewHolder)view.getTag();
            }
            holder.text.setText("Item"+(position+1));
            ImageLoader.getInstance().displayImage(imageUrls[position],holder.image,options,animateFirstListener);
            return view;
        }
    }

    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener{
        static final List<String> displayedImages= Collections.synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage){
            if(loadedImage!=null){
                ImageView imageView=(ImageView) view;
                boolean firstDisplay=!displayedImages.contains(imageUri);
                if(firstDisplay){
                    FadeInBitmapDisplayer.animate(imageView,500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }

}
