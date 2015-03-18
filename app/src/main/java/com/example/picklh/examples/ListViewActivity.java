package com.example.picklh.examples;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


public class ListViewActivity extends Activity {

    //所有资源图片
    int[] drawableIds={R.drawable.andy,R.drawable.bill,R.drawable.edgar,R.drawable.torvalds,R.drawable.turing};
    //所有资源字符串
    int[] msgIds={R.string.andy,R.string.bill,R.string.edgar,R.string.torvalds,R.string.turing};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        ListView lv=(ListView)findViewById(R.id.lv);

        BaseAdapter ba=new BaseAdapter() {
            @Override
            public int getCount() {
                return drawableIds.length;
            }

            @Override
            public Object getItem(int arg) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                LinearLayout ll=new LinearLayout(ListViewActivity.this);//初始化视图对象
                ll.setBackgroundColor(Color.BLUE);
                ll.setOrientation(LinearLayout.HORIZONTAL);
                ll.setPadding(5,5,5,5);
                ImageView ii=new ImageView(ListViewActivity.this);//初始化imgview
                ii.setImageDrawable(getResources().getDrawable(drawableIds[position]));
                ii.setScaleType(ImageView.ScaleType.FIT_XY);
                ii.setLayoutParams(new Gallery.LayoutParams(100,98));
                ll.addView(ii);

                TextView tv=new TextView(ListViewActivity.this);
                tv.setText(getResources().getText(msgIds[position]));
                tv.setTextSize(24);
                tv.setTextColor(ListViewActivity.this.getResources().getColor(R.color.black));
                tv.setPadding(5,5,5,5);
                tv.setGravity(Gravity.LEFT);
                ll.addView(tv);
                return ll;
            }
        };

        lv.setAdapter(ba);

        lv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView tv=(TextView)findViewById(R.id.txtV);
                LinearLayout ll=(LinearLayout)view;
                TextView tvn=(TextView)ll.getChildAt(1);
                StringBuilder sb=new StringBuilder();
                sb.append("您选择了:");
                sb.append(tvn.getText());
                tv.setText(sb.toString().split("\\n")[0]);//信息设置进主界面的textview中
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv=(TextView)findViewById(R.id.txtV);
                LinearLayout ll=(LinearLayout)view;
                TextView tvn=(TextView)ll.getChildAt(1);
                StringBuilder sb=new StringBuilder();
                sb.append("您选择了:");
                sb.append(tvn.getText());
                tv.setText(sb.toString().split("\\n")[0]);//信息设置进主界面的textview中
            }
        });
    }


}
