package com.example.picklh.examples;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.picklh.examples.R;

public class ContentViewActivity extends Activity {

    public static final String EXTRA_TEXT="com.example.picklh.examples.EXTRA_TEXT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_view);

        Intent intent = getIntent();
        if(Intent.ACTION_VIEW.equals(intent.getAction())){
            TextView tv=(TextView) findViewById(R.id.status_text);
            tv.setText("Viewing content from haha 😄");
        }else if(intent.hasExtra(EXTRA_TEXT)){
            TextView tv=(TextView) findViewById(R.id.status_text);
            tv.setText(intent.getStringExtra(EXTRA_TEXT));
        }
    }


}
