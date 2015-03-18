package com.example.picklh.examples;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.picklh.examples.R;

public class ViewFromOtherTaskActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_from_other_task);
    }

    public void onLaunchOtherTask(View v){
        Intent target=new Intent(this, OutsideTaskActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
        startActivity(target);//启动新的activity
    }
}
