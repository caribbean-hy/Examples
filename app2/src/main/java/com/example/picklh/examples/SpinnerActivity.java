package com.example.picklh.examples;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class SpinnerActivity extends Activity {

    int[] drawableIds={R.drawable.basketball,R.drawable.football,R.drawable.volleyball};
    int[] msgIds={R.string.lq,R.string.zq,R.string.pq};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spinner);

        Spinner sp=(Spinner)findViewById(R.id.Spinner01);

        BaseAdapter ba=new BaseAdapter() {
            @Override
            public int getCount() {
                return drawableIds.length;
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
                LinearLayout ll=new LinearLayout(SpinnerActivity.this);
                ImageView iv=new ImageView(SpinnerActivity.this);
                iv.setImageDrawable(getResources().getDrawable(drawableIds[position]));
                iv.setScaleType(ImageView.ScaleType.FIT_XY);
                ll.addView(iv);

                TextView tv=new TextView(SpinnerActivity.this);
                tv.setText(" "+getResources().getString(msgIds[position]));
                tv.setTextSize(16);
                ll.addView(tv);
                return ll;
            }
        };

        sp.setAdapter(ba);

        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView tv=(TextView)findViewById(R.id.TextView01);
                LinearLayout ll=(LinearLayout)view;
                TextView tvn=(TextView)ll.getChildAt(1);
                tv.setText("您的爱好是："+tvn.getText());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }




}
