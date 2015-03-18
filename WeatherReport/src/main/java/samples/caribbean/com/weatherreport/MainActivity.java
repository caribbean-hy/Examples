package samples.caribbean.com.weatherreport;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import samples.caribbean.com.weatherreport.adapter.cityListAdapter;
import samples.caribbean.com.weatherreport.dao.DBHelper;


public class MainActivity extends Activity {
    private TextView LocationResult, today_weather_forecast;
    private Button startLocation;

    private SharedPreferences preferences;
    private String city_str;
    private TextView city_text;
    private Button city_btn;
    //定义省份可伸缩性列表
    private ExpandableListView provinceList;
    private String[] groups;//all province
    private String[][] cityName; //对应的城市
    private String[][] cityCode;//城市编码
    private cityListAdapter adapter;
    boolean isFirstRun = false;

    private LocationClientOption.LocationMode tempMode = LocationMode.Hight_Accuracy;
    private String tempcoor = "gcj02";
    private LocationClient mLocationClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        city_text = (TextView) findViewById(R.id.city);
        //ImageView img = (ImageView) findViewById(R.id.imageView1);
        //setImageResource(img, "http://api.k780.com:88/upload/weather/d/1.gif");

        //检查是否已将城市数据库db_weather.db导入到包samples.caribbean.com.weatherreport的database目录下
        String filePath = "/data/data/samples.caribbean.com.weatherreport/db_weather.db/";
        File file = new File(filePath);
        //如果文件不存在，说明是第一次运行
        if (!file.exists()) {
            isFirstRun = true;
        }
        city_btn = (Button) findViewById(R.id.city_button);
        city_btn.setOnClickListener(new ClickEvent());

        mLocationClient = ((LocationApplication) getApplication()).mLocationClient;
        LocationResult = (TextView) findViewById(R.id.textView1);
        today_weather_forecast = (TextView) findViewById(R.id.today_weather_forecast);
        ((LocationApplication) getApplication()).mLocationResult = LocationResult;
        ((LocationApplication) getApplication()).mAddr = today_weather_forecast;
        startLocation = (Button) findViewById(R.id.addfence);
        startLocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                InitLocation();

                if (startLocation.getText().equals(getString(R.string.startlocation))) {
                    mLocationClient.start();
                    startLocation.setText(getString(R.string.stoplocation));
                } else {
                    mLocationClient.stop();

                    //preferences = getSharedPreferences("weather", MODE_PRIVATE);
                    //city_str = preferences.getString("city", "101210101");//杭州
                    String temp = LocationResult.getText().toString();
                    String city = "";
                    if (temp != null && temp.length() > 0) {
                        if (temp.lastIndexOf("市") > -1) {
                            city = temp.replace("市", "");
                        }
                        refresh(city);
                    }

                    startLocation.setText(getString(R.string.startlocation));
                }
            }
        });
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        mLocationClient.stop();
        super.onStop();
    }


    private void InitLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(tempMode);//设置定位模式
        option.setCoorType(tempcoor);//返回的定位结果是百度经纬度，默认值gcj02
        int span = 1000 * 60; //1min
        option.setScanSpan(span);//设置发起定位请求的间隔时间为5000ms
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
    }

    class ClickEvent implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v == city_btn) {
                //show_dialog();

                Intent intent = new Intent(MainActivity.this, SetCityActivity.class);
                intent.putExtra("isFirstRun", isFirstRun);
                startActivityForResult(intent, 0);
            }
        }
    }

    @Override //得到城市页面的回馈
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*  //得到城市的编码
            SharedPreferences sp = getSharedPreferences(SetCityActivity.CITY_NAME_FILE, MODE_PRIVATE);
            String cityName = sp.getString("name", "");
        */
        String cityName = data.getStringExtra("name");
        if (cityName != null && cityName.trim().length() != 0) {

            //从网上更新新的天气
            city_text.setText(cityName);
            LocationResult.setText(cityName);
            refresh(cityName);
        } else {
            //如果是没有城市码的回退，则退出程序
            MainActivity.this.finish();
        }
    }

    /*
    * 用dialog的形式加载城市列表

    //用于处理装载伸缩性列表的处理类
    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            //在伸缩性的列表中显示数据库中的省份与城市
            adapter = new cityListAdapter(MainActivity.this, provinceList, groups, cityName);
            provinceList.setAdapter(adapter);


            //为其子列表选项添加单击事件
            provinceList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

                @Override
                public boolean onChildClick(ExpandableListView parent, View v,
                                            int groupPosition, int childPosition, long id) {
                    //========得到单击的城市码=======
                    //得到城市名
                    city_str = (String) adapter.getChild(groupPosition, childPosition);
                    //从数据库中得到城市码
//                    DBHelper dbHelper = new DBHelper(MainActivity.this, "db_weather.db");
//                    String cityCode = dbHelper.getCityCodeByName(city_str);
                    v.setBackgroundColor(getResources().getColor(R.color.blue));

                    return true;
                }

            });
        }
    }

    private void show_dialog() {
        final View view = LayoutInflater.from(this).inflate(R.layout.city_layout, null);
        provinceList = (ExpandableListView) view.findViewById(R.id.provinceList);

        final MyHandler myHandler = new MyHandler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                //查询处理数据库，装载伸展列表
                DBHelper dbHelper = new DBHelper(MainActivity.this, "db_weather.db");
                groups = dbHelper.getAllProvinces();
                List<String[][]> result = dbHelper.getAllCityAndCode(groups);
                cityName = result.get(0);
                cityCode = result.get(1);

                //交给Handler对象加载列表
                Message msg = new Message();
                myHandler.sendMessage(msg);
            }
        }).start();

        //选择城市对话框
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("请选择所属城市");
        dialog.setView(view);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                city_text.setText(city_str);
                LocationResult.setText(city_str);
                writeSharpPreference(city_str);
                refresh(city_str);
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    * */

    private Thread thread;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    JSONArray weather = (JSONArray) msg.obj;
                    refreshUI(weather);
                    try {
                        thread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
            }
        }
    };


    public void refresh(final String city) {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String url, code;
                if (city.equals("杭州")) {
                    //url = "http://m.weather.com.cn/data/101210101.html";
                    //未来6天气象信息数据，数据来自国家气象局天气网,每小时更新一次
                    url = "http://api.k780.com:88/?app=weather.future&weaid=101210101&appkey=13254&sign=60d7dd679717855b1f9d1508abfe01fb&format=json";
                } else {
                    DBHelper dbHelper = new DBHelper(MainActivity.this, "db_weather.db");
                    code = dbHelper.getCityCodeByName(city);
                    url = "http://api.k780.com:88/?app=weather.future&weaid=" + code + "&appkey=13254&sign=60d7dd679717855b1f9d1508abfe01fb&format=json";
                }
                HttpClient client = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(url);
                HttpResponse response;
                String sbuf = null;
                try {
                    response = client.execute(httpGet);
                    HttpEntity httpEntity = response.getEntity();
                    if (httpEntity != null) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(httpEntity.getContent(), "utf-8"));
                        while ((sbuf = br.readLine()) != null) {
                            if (sbuf.contains(city)) {
                                JSONObject object = new JSONObject(sbuf);
                                JSONArray data = (JSONArray) object.getJSONArray("result");
                                Message msg = new Message();
                                msg.what = 1;
                                msg.obj = data;
                                handler.sendMessage(msg);
                            } else {
                                handler.post(runnableUi);
                            }
                        }
                    }
                } catch (IOException ex) {
                    handler.post(runnableUi);
                    ex.printStackTrace();
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
            }
        });
        thread.start();
    }

    // 构建Runnable对象，在runnable中更新界面
    Runnable runnableUi = new Runnable() {
        @Override
        public void run() {

            Toast toast = Toast.makeText(getApplicationContext(), "无该数据或网络异常,请稍后重试", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            //更新界面
//            today_weather_forecast = (TextView) findViewById(R.id.today_weather_forecast);
//            today_weather_forecast.setText("无该数据/网络异常，请稍后再试");
        }

    };

    private void refreshUI(final JSONArray jsonArray) {
        try {
            JSONObject jsonData = (JSONObject) jsonArray.get(0);
            TextView city_text = (TextView) findViewById(R.id.city_text);
            city_text.setText(jsonData.getString("citynm"));

            TextView today_weather_forecast = (TextView) findViewById(R.id.today_weather_forecast);
            today_weather_forecast.setText("今日天气");

            // 取得<string>15℃/21℃</string>中的数据
            TextView temperature = (TextView) findViewById(R.id.temperature);
            temperature.setText(jsonData.getString("temperature"));

            // 取得<string>微风</string>今日天气风向情况
            TextView wind = (TextView) findViewById(R.id.wind);
            wind.setText(jsonData.getString("wind"));

            // 取得<string>小于3级</string>今日天气风速情况
            TextView winp = (TextView) findViewById(R.id.winp);
            winp.setText(jsonData.getString("winp"));

            // 取得<string>2014-07-30,星期三</string>中的数据
            TextView days = (TextView) findViewById(R.id.days);
            days.setText(jsonData.getString("days") + "," + jsonData.getString("week"));

            // 取得<string>多云转晴</string>
            TextView weather = (TextView) findViewById(R.id.weather);
            weather.setText(jsonData.getString("weather"));

            ImageView imageView = (ImageView) findViewById(R.id.imageView1);
            //http://api.k780.com:88/upload/weather/d/4.gif
            String img = jsonData.getString("weather_icon");
            imageView.setImageResource(parseIcon(img.split("/")[img.split("/").length - 1]));

            ListView lv = (ListView) findViewById(R.id.listView);

            BaseAdapter ba = new BaseAdapter() {
                @Override
                public int getCount() {
                    return jsonArray.length() - 1;
                }

                @Override
                public Object getItem(int arg) {
                    return null;
                }

                @Override
                public long getItemId(int position) {
                    return 0;
                }

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {

                    try {
                        JSONObject jsonData = (JSONObject) jsonArray.get(++position);

                        LinearLayout ll = new LinearLayout(MainActivity.this);//初始化视图对象
                        ll.setOrientation(LinearLayout.HORIZONTAL);
                        ll.setPadding(5, 5, 5, 5);
                        ll.setBackground(getResources().getDrawable(R.drawable.content_small_bg));

//                        ImageView ii = new ImageView(MainActivity.this);//初始化imgview
//                        ii.setImageDrawable(getResources().getDrawable(R.drawable.a_00));
//                        String img = jsonData.getString("weather_icon");
//                        ii.setImageResource(parseIcon(img.split("/")[img.split("/").length - 1]));
//                        ii.setScaleType(ImageView.ScaleType.FIT_XY);
//                        ii.setLayoutParams(new Gallery.LayoutParams(70, 65));
//                        ii.setPadding(20, 0, 0, 0);
//                        ll.addView(ii);

                        TextView date = new TextView(MainActivity.this);
                        date.setText(setDate(jsonData.getString("days")) + " " + jsonData.getString("week"));
                        date.setTextSize(14);
                        date.setTextColor(MainActivity.this.getResources().getColor(R.color.black));
                        date.setPadding(20, 0, 0, 40);
                        date.setGravity(Gravity.BOTTOM);
                        ll.addView(date);

                        TextView weather = new TextView(MainActivity.this);
                        weather.setText(jsonData.getString("weather"));
                        weather.setTextSize(16);
                        weather.setTextColor(MainActivity.this.getResources().getColor(R.color.black));
                        weather.setPadding(20, 0, 0, 0);
                        weather.setGravity(Gravity.CENTER_VERTICAL);
                        ll.addView(weather);

                        TextView temperature = new TextView(MainActivity.this);
                        temperature.setText(jsonData.getString("temperature"));
                        temperature.setTextSize(16);
                        temperature.setTextColor(MainActivity.this.getResources().getColor(R.color.black));
                        temperature.setPadding(20, 0, 0, 0);
                        temperature.setGravity(Gravity.CENTER_VERTICAL);
                        ll.addView(temperature);

                        return ll;
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }
                    return null;
                }
            };

            lv.setAdapter(ba);

        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    private void setImageResource(ImageView imgView, String URL_PATH) {

        try {
            InputStream inputStream = null;
            URL url = new URL(URL_PATH);                    //服务器地址
            if (url != null) {
                //打开连接
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setConnectTimeout(3000);//设置网络连接超时的时间为3秒
                httpURLConnection.setRequestMethod("GET");        //设置请求方法为GET
                httpURLConnection.setDoInput(true);                //打开输入流
                int responseCode = httpURLConnection.getResponseCode();    // 获取服务器响应值
                if (responseCode == HttpURLConnection.HTTP_OK) {        //正常连接
                    inputStream = httpURLConnection.getInputStream();        //获取输入流
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    imgView.setImageBitmap(bitmap);
                }
            }
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    //2015-03-07
    private String setDate(String day) {
        String result = "";
        if (day != null && day.length() > 0) {
            String[] days = day.split("-");
            result = Integer.parseInt(days[1]) + "月" + Integer.parseInt(days[2]) + "日";
        }
        return result;
    }

    private int parseIcon(String strIcon) {

        if (strIcon == null)
            return -1;
        if ("0.gif".equals(strIcon))
            return R.drawable.a_0;
        if ("1.gif".equals(strIcon))
            return R.drawable.a_1;
        if ("2.gif".equals(strIcon))
            return R.drawable.a_2;
        if ("3.gif".equals(strIcon))
            return R.drawable.a_3;
        if ("4.gif".equals(strIcon))
            return R.drawable.a_4;
        if ("5.gif".equals(strIcon))
            return R.drawable.a_5;
        if ("6.gif".equals(strIcon))
            return R.drawable.a_6;
        if ("7.gif".equals(strIcon))
            return R.drawable.a_7;
        if ("8.gif".equals(strIcon))
            return R.drawable.a_8;
        if ("9.gif".equals(strIcon))
            return R.drawable.a_9;
        if ("10.gif".equals(strIcon))
            return R.drawable.a_10;
        if ("11.gif".equals(strIcon))
            return R.drawable.a_11;
        if ("12.gif".equals(strIcon))
            return R.drawable.a_12;
        if ("13.gif".equals(strIcon))
            return R.drawable.a_13;
        if ("14.gif".equals(strIcon))
            return R.drawable.a_14;
        if ("15.gif".equals(strIcon))
            return R.drawable.a_15;
        if ("16.gif".equals(strIcon))
            return R.drawable.a_16;
        if ("17.gif".equals(strIcon))
            return R.drawable.a_17;
        if ("18.gif".equals(strIcon))
            return R.drawable.a_18;
        if ("19.gif".equals(strIcon))
            return R.drawable.a_19;
        if ("20.gif".equals(strIcon))
            return R.drawable.a_20;
        if ("21.gif".equals(strIcon))
            return R.drawable.a_21;
        if ("22.gif".equals(strIcon))
            return R.drawable.a_22;
        if ("23.gif".equals(strIcon))
            return R.drawable.a_23;
        if ("24.gif".equals(strIcon))
            return R.drawable.a_24;
        if ("25.gif".equals(strIcon))
            return R.drawable.a_25;
        if ("26.gif".equals(strIcon))
            return R.drawable.a_26;
        if ("27.gif".equals(strIcon))
            return R.drawable.a_27;
        if ("28.gif".equals(strIcon))
            return R.drawable.a_28;
        if ("29.gif".equals(strIcon))
            return R.drawable.a_29;
        if ("30.gif".equals(strIcon))
            return R.drawable.a_30;
        if ("31.gif".equals(strIcon))
            return R.drawable.a_31;
        return 0;
    }


    private void writeSharpPreference(String string) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("city", string);
        editor.commit();
    }

}
