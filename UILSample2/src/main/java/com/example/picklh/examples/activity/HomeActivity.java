package com.example.picklh.examples.activity;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.picklh.examples.Constants;
import com.example.picklh.examples.R;
import com.example.picklh.examples.fragment.ImageGridFragment;
import com.example.picklh.examples.fragment.ImageListFragment;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.utils.L;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class HomeActivity extends Activity {

    private static final String TEST_FILE_NAME ="Universal Image Loader @#&=+-_.,!()~'%20.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_main);

        File testImageOnScard=new File("/mnt/sdcard", TEST_FILE_NAME);
        if(!testImageOnScard.exists()){
            copyTestImageToSdCard(testImageOnScard);
        }
    }

    private void copyTestImageToSdCard(final File testImageOnSdCard){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    InputStream inputStream=getAssets().open(TEST_FILE_NAME);
                    FileOutputStream fileOutputStream=new FileOutputStream(testImageOnSdCard);
                    byte[] buffer=new byte[8192];
                    int read;
                    try{
                        while((read=inputStream.read())!=-1){
                            fileOutputStream.write(buffer, 0, read);
                        }
                    }finally {
                        fileOutputStream.flush();
                        fileOutputStream.close();
                        inputStream.close();
                    }
                }catch (IOException e){
                    L.w("Can't copy test image onto SD card");
                }
            }
        });
    }

    public void onImageListClick(View view){
        Intent intent=new Intent(this,SimpleImageActivity.class);
        intent.putExtra(Constants.Extra.FRAGMENT_INDEX, ImageListFragment.INDEX);
        startActivity(intent);
    }

    public void onImageGridClick(View view){
        Intent intent=new Intent(this, SimpleImageActivity.class);
        intent.putExtra(Constants.Extra.FRAGMENT_INDEX, ImageGridFragment.INDEX);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case R.id.item_clear_memory_cache:
                ImageLoader.getInstance().clearMemoryCache();
                return true;
            case R.id.item_clear_disc_cache:
                ImageLoader.getInstance().clearDiskCache();
                return true;
            default:
                return false;
        }

    }
}
