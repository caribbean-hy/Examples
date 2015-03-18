package com.example.picklh.examples;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;


public class MainActivity extends Activity {

    private static final String TAG="ActivityLifecycle";
    private String mString;
    private EditText myEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myEditText=(EditText)findViewById(R.id.editText);

        Log.e(TAG,"start onCreate...");
    }

    protected void onStart(){
        super.onStart();
        Log.e(TAG,"start onStart...");
    }

    //当按home键后，再次启动应用，要恢复先前状态
    protected void onRestart(){
        super.onRestart();

        myEditText.setText(mString);
        Log.e(TAG,"start onRestart...");
    }

    protected void onResume(){
        super.onResume();
        Log.e(TAG,"start onResume...");
    }

    //被home键时，在pause方法里，将输入的值赋给mString
    protected void onPause(){
        super.onPause();

        mString = myEditText.getText().toString();
        Log.e(TAG,"start onPause...");
    }

    protected void onStop(){
        super.onStop();
        Log.e(TAG,"start onStop...");
    }

    protected void onDestroy(){
        super.onDestroy();
        Log.e(TAG,"start onDestroy...");
    }

}
