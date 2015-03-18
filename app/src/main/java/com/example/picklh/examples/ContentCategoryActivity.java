package com.example.picklh.examples;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.picklh.examples.R;

public class ContentCategoryActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_category);
    }

    public void onViewContent(View v){
        Intent target=new Intent(this,ContentViewActivity.class);
        startActivity(target);
    }

}
