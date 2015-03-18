package samples.caribbean.com.readdatafromdb;

import android.app.Activity;
import android.app.ProgressDialog;
import android.appwidget.AppWidgetManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import samples.caribbean.com.readdatafromdb.adapter.MyListAdapter;
import samples.caribbean.com.readdatafromdb.dao.DBHelper;

/**
 * Created by picklh on 2015/2/9.
 */
public class SetCityActivity extends Activity {

    //定义的一个省份可伸缩性的列表
    private ExpandableListView provinceList;

    //省份
    private String[] groups;
    //对应的城市
    private String[][] childs;

    //城市的编码
    private String[][] cityCodes;

    //自定义的伸缩列表适配器
    private MyListAdapter adapter;

    //记录应用程序widget的ID
    private int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        provinceList= (ExpandableListView)findViewById(R.id.provinceList);



        //增强用户体验，在加载城市列表时显示进度对话框
        final ProgressDialog dialog = getProgressDialog("", "正在加载城市列表...");
        dialog.show();
        //伸缩性列表的加载处理类
        final MyHandler mHandler = new MyHandler();
        new Thread(new Runnable() {
            public void run() {
                //查询处理数据库,装载伸展列表
                DBHelper dbHelper = new DBHelper(SetCityActivity.this, "db_weather.db");
                groups = dbHelper.getAllProvinces();
                List<String[][]> result = dbHelper.getAllCityAndCode(groups);
                childs = result.get(0);
                cityCodes = result.get(1);
                //交给Handler对象加载列表
                Message msg = new Message();
                mHandler.sendMessage(msg);
                dialog.cancel();
                dialog.dismiss();
            }
        }).start();
    }


    //得到一个进度对话框
    public ProgressDialog getProgressDialog(String title, String content) {
        //实例化进度条对话框ProgressDialog
        ProgressDialog dialog=new ProgressDialog(this);

        //可以不显示标题
        dialog.setTitle(title);
        dialog.setIndeterminate(true);
        dialog.setMessage(content);
        dialog.setCancelable(true);
        return dialog;
    }



    //用于处理装载伸缩性列表的处理类
    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            //在伸缩性的列表中显示数据库中的省份与城市
            adapter=new MyListAdapter(SetCityActivity.this, provinceList, groups, childs);
            provinceList.setAdapter(adapter);


        }
    }

}