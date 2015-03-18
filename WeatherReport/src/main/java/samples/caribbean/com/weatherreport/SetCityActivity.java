package samples.caribbean.com.weatherreport;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.ExpandableListView.OnChildClickListener;

import samples.caribbean.com.weatherreport.adapter.cityListAdapter;
import samples.caribbean.com.weatherreport.dao.DBHelper;

/**
 *
 * @author i-zqluo
 * 一个设置城市的Activity
 */
public class SetCityActivity extends Activity {
    //定义的一个省份可伸缩性的列表
    private ExpandableListView provinceList;
    //定义的用于过滤的文本输入框
    private TextView filterText;

    //定义的一个记录城市码的SharedPreferences文件名
    public static final String CITY_NAME_FILE="city_name";

    //城市的编码
    private String[][] cityCodes;
    //省份
    private String[] groups;
    //对应的城市
    private String[][] childs;

    //自定义的伸缩列表适配器
    private cityListAdapter adapter;

    //记录应用程序widget的ID
    private int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city_layout);

        provinceList= (ExpandableListView)findViewById(R.id.provinceList);

        //为过滤输入文本框添加事件
        filterText = (TextView) findViewById(R.id.filterField);
        filterText.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                CharSequence filterContent = filterText.getText();
                //设置列表数据过滤结果显示
                adapter.getFilter().filter(filterContent);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

        });

        //得到MainActivity或Widget传过来的intent
        Intent intent =getIntent();
        //通过判断MainActivity传过来的isFirstRun来确定是否为第一次运行
        boolean isFirstRun = intent.getBooleanExtra("isFirstRun", false);

        //通过接收Bundle来判断Widget中传递过来的WidgetId
        Bundle extras = intent.getExtras();


        //如果为true说明是第一次运行
        if(isFirstRun) {
            //导入城市编码数据库
            importInitDatabase();

            //显示一个对话框说明为第一次运行
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setMessage("由于本程序是第一次运行，请选择您需要了解天气的城市").setPositiveButton("确定", null);
//            AlertDialog dialog = builder.create();
//            dialog.show();
        }

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

    //将res/raw中的城市数据库导入到安装的程序中的database目录下
    private void importInitDatabase() {
        //数据库目录
        String filePath = "/data/data/samples.caribbean.com.weatherreport/databases";
        File dir = new File(filePath);
        if (!dir.exists()) {
            dir.mkdir();
        }

        //数据库文件
        File dbfile = new File(dir, "db_weather.db");
        try {
            if (!dbfile.exists()) {
                dir.createNewFile();
            }

            //加载欲导入的数据库
            InputStream inputStream = this.getApplicationContext().getResources().openRawResource(R.raw.db_weather);
            FileOutputStream fileOutputStream = new FileOutputStream(dbfile);
            byte[] buffere = new byte[inputStream.available()];
            inputStream.read(buffere);
            fileOutputStream.write(buffere);
            inputStream.close();
            fileOutputStream.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

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
            adapter=new cityListAdapter(SetCityActivity.this, provinceList, groups, childs);
            provinceList.setAdapter(adapter);

            //为其子列表选项添加单击事件
            provinceList.setOnChildClickListener(new OnChildClickListener() {

                @Override
                public boolean onChildClick(ExpandableListView parent, View v,
                                            int groupPosition, int childPosition, long id) {
                    //自动跳至天气的显示界面MainActivity

                    //========得到单击的城市码=======
                    //得到城市名
                    String cityName = (String)adapter.getChild(groupPosition, childPosition);
                    //从数据库中得到城市码
                    //DBHelper dbHelper = new DBHelper(SetCityActivity.this, "db_weather.db");
                    //String cityCode = dbHelper.getCityCodeByName(cityName);

                    Dialog dialog = getProgressDialog("", "正在加载天气...");
                    dialog.show();
                    GoToMainActivity thread = new GoToMainActivity(cityName, dialog);
                    thread.start();

                    return false;
                }
            });
        }
    }

    //处理用户选择好城市后的跳转到MainActivity
    private class GoToMainActivity extends Thread {

        //保证跳转的城市码
        private String cityName;
        //跳转后显示的进度对话框
        private Dialog dialog;

        public GoToMainActivity(String cityName, Dialog dialog) {
            this.cityName = cityName;
            this.dialog = dialog;
        }

        public void run() {

            /*
                Context.MODE_PRIVATE：为默认操作模式,代表该文件是私有数据,只能被应用本身访问,在该模式下,写入的内容会覆盖原文件的内容
                Context.MODE_APPEND：模式会检查文件是否存在,存在就往文件追加内容,否则就创建新文件.
                    MODE_WORLD_READABLE和MODE_WORLD_WRITEABLE 用来控制其他应用是否有权限读写该文件.
                MODE_WORLD_READABLE：表示当前文件可以被其他应用读取.
                MODE_WORLD_WRITEABLE：表示当前文件可以被其他应用写入

                //得到一个私有的SharedPreferences文件编辑对象
                SharedPreferences.Editor edit = getSharedPreferences(CITY_NAME_FILE, MODE_PRIVATE).edit();
                //将城市码保存
                edit.putString("name", cityName);
                edit.commit();
            */

            //回退到天气情况显示Activity，并传回城市名
            Intent intent = getIntent();
            intent.putExtra("name",cityName);
            SetCityActivity.this.setResult(0, intent);

            SetCityActivity.this.finish();
            dialog.cancel();
            dialog.dismiss();
        }
    }
}

