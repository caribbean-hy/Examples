package com.example.picklh.examples;

import android.app.Activity;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.picklh.examples.R;

public class InterstitialMessageActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.interstitial_message);
    }

    public void onViewContent(View v){
        TaskStackBuilder.create(this)
                .addParentStack(ContentViewActivity.class)
                .addNextIntent(new Intent(this,ContentViewActivity.class)
                        .putExtra(ContentViewActivity.EXTRA_TEXT, "From Interstitial Notification"))
                .startActivities();
        finish();
    }
}
