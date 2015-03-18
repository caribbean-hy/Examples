package samples.caribbean.com.readdatafromdb;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Handler;

import samples.caribbean.com.readdatafromdb.adapter.MyListAdapter;
import samples.caribbean.com.readdatafromdb.dao.DBHelper;


public class MainActivity extends ActionBarActivity {

    private ExpandableListView provinceList;
    private String[] groups;//all province
    private String[][]cityCodes;//城市的编码
    private String[][] cityName;//对应的城市
    private MyListAdapter adapter;//自定义的伸缩列表适配器

    //自定义的伸缩列表适配器
    //private MyListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        provinceList=(ExpandableListView)findViewById(R.id.provinceList);

        //gpsView.setAdapter(new GPSListAdapter());

        String dirPath= "/data/data/samples.caribbean.com.readdatafromdb/db_weather.db/";
        File file= new File(dirPath);
        boolean isFirstRun = false;
        //如果文件不存在说明是第一次动行
        if(!file.exists()) {
            importInitDatabase();
        }

        //增强用户体验，在加载城市列表时显示进度对话框
        final ProgressDialog dialog = getProgressDialog("", "正在加载城市列表...");
        dialog.show();
        //伸缩性列表的加载处理类
        final MyHandler mHandler = new MyHandler();
        new Thread(new Runnable() {
            public void run() {
                //查询处理数据库,装载伸展列表
                DBHelper dbHelper = new DBHelper(MainActivity.this, "db_weather.db");
                groups = dbHelper.getAllProvinces();
                List<String[][]> result = dbHelper.getAllCityAndCode(groups);
                cityName = result.get(0);
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
    private void importInitDatabase(){
        //数据库的目录
        String dirPath="/data/data/samples.caribbean.com.readdatafromdb/databases";
        File dir=new File(dirPath);
        if(!dir.exists()){
            dir.mkdir();
        }

        //数据库文件
        File dbfile=new File(dir,"db_weather.db");
        try{
            if(!dbfile.exists()){
                dbfile.createNewFile();
            }

            //加载欲导入的数据库
            InputStream is =this.getApplicationContext().getResources().openRawResource(R.raw.db_weather);
            FileOutputStream fos=new FileOutputStream(dbfile);
            byte[] buffere=new byte[is.available()];
            is.read(buffere);
            fos.write(buffere);
            is.close();
            fos.close();
        }catch (FileNotFoundException ex){
            ex.printStackTrace();
        }catch (IOException ex){
            ex.printStackTrace();
        }

    }

    private ProgressDialog getProgressDialog(String title, String content){
        //实例化进度条对话框ProgressDialog
        ProgressDialog dialog=new ProgressDialog(this);

        //可以不显示标题
        dialog.setTitle(title);
        dialog.setIndeterminate(true);
        dialog.setMessage(content);
        dialog.setCancelable(true);
        return dialog;
    }

    private class  MyHandler extends android.os.Handler{
        public void handleMessage(Message msg){
            super.handleMessage(msg);

            //在伸缩性的列表中显示数据库中的省份与城市
            adapter=new MyListAdapter(MainActivity.this,provinceList,groups,cityName);
            provinceList.setAdapter(adapter);


        }
    }

    class GPSListAdapter extends BaseAdapter{

        private Context context;

        @Override
        public int getCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            Resources resources=context.getResources();
            return resources.getString(R.string.locale_city);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null) {
                LayoutInflater layout=LayoutInflater.from(context);
                convertView = layout.inflate(R.layout.gps, null);
            }
            return convertView;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
