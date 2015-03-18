package com.example.picklh.examples;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class OutsideTaskActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.outside_task);
    }

    public void onViewContent(View v){
        Intent intent = new Intent(Intent.ACTION_VIEW)
                .setType("application/x-example")
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);//这个Activity和它之上的都将关闭，以至于用户不能再返回到它们，但是可以回到之前的Activity
        startActivity(intent);
    }
}
