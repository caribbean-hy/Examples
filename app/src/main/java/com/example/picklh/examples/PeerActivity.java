package com.example.picklh.examples;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.picklh.examples.R;

public class PeerActivity extends Activity {

    private static final String EXTRA_PEER_COUNT="com.example.picklh.examples.EXTRA_PEER_COUNT";

    private int mPeerCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.peer);

        mPeerCount=getIntent().getIntExtra(EXTRA_PEER_COUNT, 0) + 1;
        TextView tv=(TextView) findViewById(R.id.peer_counter);
        tv.setText("Peer count:".toString() + mPeerCount);
    }


    //btn onclick 事件
    public void onLaunchPeer(View v){
        Intent target=new Intent(this,PeerActivity.class);
        target.putExtra(EXTRA_PEER_COUNT,mPeerCount);//把参数和值传递到新的页面
        startActivity(target);//启动新的activity
    }
}
