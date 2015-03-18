package com.example.picklh.examples;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GridViewActivity extends Activity {

    //资源图片
    int[] drawableIds={R.drawable.andy,R.drawable.bill,R.drawable.edgar,R.drawable.torvalds,R.drawable.turing,};
    //资源介绍
    int[] nameIds={R.string.andy2,R.string.bill2,R.string.edgar2,R.string.torvalds2,R.string.turing2};

    int[] msgIds={R.string.andydis,R.string.billdis,R.string.edgardis,R.string.torvaldsdis,R.string.turingdis};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grid_view);

        GridView gv=(GridView)this.findViewById(R.id.gv);
        SimpleAdapter sca=new SimpleAdapter(this,
                generateDataList(),//数据list
                R.layout.gird_row,//行对应layout id
                new String[]{"col1","col2","col3"},//列名列表
                new int[]{R.id.gvIv,R.id.gvTvName,R.id.gvTvMsg}//列对应控件ID
        );
        gv.setAdapter(sca);

        gv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView tv=(TextView)findViewById(R.id.gvTvTitle);
                LinearLayout ll=(LinearLayout)view;
                TextView tvn=(TextView)ll.getChildAt(1);
                TextView tvnl=(TextView)ll.getChildAt(2);
                tv.setText(tvn.getText()+" "+tvnl.getText());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv=(TextView)findViewById(R.id.gvTvTitle);
                LinearLayout ll=(LinearLayout)view;
                TextView tvn=(TextView)ll.getChildAt(1);
                TextView tvnl=(TextView)ll.getChildAt(2);
                tv.setText(tvn.getText()+" "+tvnl.getText());
            }
        });
    }

    protected List<? extends Map<String, ?>> generateDataList(){
        ArrayList<Map<String,Object>> list=new ArrayList<Map<String, Object>>();
        int rowCounter=drawableIds.length;
        for (int i=0; i<rowCounter;i++){
            HashMap<String,Object> hashMap=new HashMap<String, Object>();
            hashMap.put("col1",drawableIds[i]);//第一列为图片
            hashMap.put("col2",this.getResources().getString(nameIds[i]));//姓名
            hashMap.put("col3",getResources().getString(msgIds[i]));//描述

            list.add(hashMap);
        }
        return list;
    }
}
