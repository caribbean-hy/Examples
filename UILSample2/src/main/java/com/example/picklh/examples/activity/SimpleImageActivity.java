package com.example.picklh.examples.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.example.picklh.examples.Constants;
import com.example.picklh.examples.R;
import com.example.picklh.examples.fragment.ImageGridFragment;
import com.example.picklh.examples.fragment.ImageListFragment;
import com.example.picklh.examples.fragment.ImagePagerFragment;

public class SimpleImageActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int frIndex=getIntent().getIntExtra(Constants.Extra.FRAGMENT_INDEX,0);
        Fragment fr;
        String tag;
        int titleRes;

        switch (frIndex){
            default:
            case ImageListFragment.INDEX:
                tag = ImageListFragment.class.getSimpleName();
                fr = getSupportFragmentManager().findFragmentByTag(tag);
                if (fr == null) {
                    fr = new ImageListFragment();
                }

                titleRes = R.string.ac_name_image_list;
                break;
            case ImageGridFragment.INDEX:
                tag=ImageGridFragment.class.getSimpleName();
                fr=getSupportFragmentManager().findFragmentByTag(tag);
                if(fr==null){
                    fr=new ImageGridFragment();
                }
                titleRes=R.string.ac_name_image_grid;
                break;
            case ImagePagerFragment.INDEX:
                tag = ImagePagerFragment.class.getSimpleName();
                fr = getSupportFragmentManager().findFragmentByTag(tag);
                if (fr == null) {
                    fr = new ImagePagerFragment();
                    fr.setArguments(getIntent().getExtras());
                }
                titleRes = R.string.ac_name_image_pager;
                break;
        }

        setTitle(titleRes);
        getSupportFragmentManager().beginTransaction().replace(android.R.id.content,fr,tag).commit();

    }
}
