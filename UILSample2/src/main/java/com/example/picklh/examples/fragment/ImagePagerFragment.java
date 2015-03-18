package com.example.picklh.examples.fragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.picklh.examples.Constants;
import com.example.picklh.examples.R;
import com.example.picklh.examples.activity.SimpleImageActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 */
public class ImagePagerFragment extends Fragment {

    public static final int INDEX=2;
    String[] imageUrls=Constants.IMAGES;
    DisplayImageOptions options;

    @Override
    public void onResume(){
        super.onResume();
        options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_error)
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .displayer(new FadeInBitmapDisplayer(300))
                .build();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        View rootView=inflater.inflate(R.layout.fr_image_pager,container,false);
        ViewPager pager=(ViewPager)rootView.findViewById(R.id.pager);
        pager.setAdapter(new ImageAdapter());
        pager.setCurrentItem(getArguments().getInt(Constants.Extra.IMAGE_POSITION,0));
        return rootView;
    }

    private class ImageAdapter extends PagerAdapter{

        private LayoutInflater inflater;

        ImageAdapter(){
            inflater=LayoutInflater.from(getActivity());
        }

        @Override
        public int getCount() {
            return imageUrls.length;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View imageLayout=inflater.inflate(R.layout.item_pager_image,container,false);
            assert imageLayout!=null;
            ImageView imageView=(ImageView) imageLayout.findViewById(R.id.image);
            final ProgressBar spinner=(ProgressBar) imageLayout.findViewById(R.id.loading);

            ImageLoader.getInstance().displayImage(imageUrls[position],imageView,options,new SimpleImageLoadingListener(){
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    spinner.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    String message=null;
                    switch (failReason.getType()){
                        case IO_ERROR:
                            message = "Input/Output error";
                            break;
                        case DECODING_ERROR:
                            message = "Image can't be decoded";
                            break;
                        case NETWORK_DENIED:
                            message = "Downloads are denied";
                            break;
                        case OUT_OF_MEMORY:
                            message = "Out Of Memory error";
                            break;
                        case UNKNOWN:
                            message = "Unknown error";
                            break;
                    }
                    Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();

                    spinner.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    spinner.setVisibility(View.GONE);
                }
            });

            container.addView(imageLayout,0);
            return imageLayout;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

    }

}
