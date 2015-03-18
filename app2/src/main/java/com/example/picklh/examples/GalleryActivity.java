package com.example.picklh.examples;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;


public class GalleryActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final int[] imageIds={
                R.drawable.bbta,R.drawable.bbtb,R.drawable.bbtc,
                R.drawable.bbtd,R.drawable.bbte,R.drawable.bbtf,
                R.drawable.bbtg
        };

        Gallery gl=(Gallery)findViewById(R.id.gl);

        BaseAdapter ba=new BaseAdapter() {
            @Override
            public int getCount() {
                return imageIds.length;
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ImageView iv=new ImageView(GalleryActivity.this);
                iv.setImageResource(imageIds[position]);
                iv.setScaleType(ImageView.ScaleType.FIT_XY);
                iv.setLayoutParams(new Gallery.LayoutParams(188,250));
                return iv;
            }
        };

        gl.setAdapter(ba);
        gl.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Gallery gl=(Gallery)findViewById(R.id.gl);
                gl.setSelection(position);
            }
        });
    }


}
