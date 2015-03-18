package com.example.picklh.examples.activity;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.picklh.examples.R;
import com.example.picklh.examples.model.Weather;
import com.example.picklh.examples.utils.DomParseWeather;
import com.example.picklh.examples.utils.WeatherXml;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    Activity context=this;
    private EditText city;
    private Button button;
    private ListView listView;
    private Drawable[] images;
    private String[] dates;
    private String[] temperatures;
    private String[] conditions;
    private double[] low;
    private double[] high;
    private WeatherAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        city=(EditText)findViewById(R.id.city);
        button=(Button)findViewById(R.id.button);
        listView=(ListView)findViewById(R.id.list);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();

                adapter=new WeatherAdapter();
            }
        });
    }

    final class ViewHolder{
        ImageView image;
        TextView date;
        TextView temperature;
        TextView condition;
    }

    class WeatherAdapter extends BaseAdapter{

        WeatherAdapter(){ }

        @Override
        public int getCount() {
            return dates.length;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView == null){
                LayoutInflater inflater=context.getLayoutInflater();
                convertView=inflater.inflate(R.layout.weather_list_row,null);

                holder=new ViewHolder();
                holder.image=(ImageView) convertView.findViewById(R.id.image);
                holder.date=(TextView) convertView.findViewById(R.id.day_of_week);
                holder.temperature=(TextView) convertView.findViewById(R.id.temperature);
                holder.condition=(TextView) convertView.findViewById(R.id.condition);

                convertView.setTag(holder);
            }else{
                holder=(ViewHolder) convertView.getTag();
            }

            holder.image.setImageDrawable(images[position]);
            holder.date.setText(dates[position]);
            holder.temperature.setText(temperatures[position]);
            holder.condition.setText(conditions[position]);

            return convertView;
        }
    }

    private void initData(){
        String xmlStr= WeatherXml.getWeatherXml(city.getText().toString());
        Log.i("TAG", xmlStr);
        List<Weather> ws= DomParseWeather.getWeatherFromXml(xmlStr);

        images=new Drawable[ws.size()];
        dates=new String[ws.size()];
        temperatures = new String[ws.size()];
        conditions = new String[ws.size()];
        low = new double[ws.size()];
        high = new double[ws.size()];

        Weather w= null;

        for (int i=0; i<ws.size();i++){
            w=ws.get(i);
            images[i]=loadImage(w.getImageUrl());
            dates[i]=w.getDay_of_week();
            conditions[i]=w.getCondition();
            low[i] = Integer.parseInt(w.getLow_temperature());
            high[i] = Integer.parseInt(w.getHigh_temperature());
        }

        dataShift();
    }


    private void dataShift() {

        for (int i = 0; i < dates.length; i++) {
            if ("Mon".equals(dates[i])) {
                dates[i] = "Mon";

            } else if ("Tue".equals(dates[i])) {
                dates[i] = "���Tue�";

            } else if ("Wed".equals(dates[i])) {
                dates[i] = "Wed";

            } else if ("Thu".equals(dates[i])) {
                dates[i] = "Thu";

            } else if ("Fri".equals(dates[i])) {
                dates[i] = "Fri";

            } else if ("Sat".equals(dates[i])) {
                dates[i] = "Sat";

            } else if ("Sun".equals(dates[i])) {
                dates[i] = "Sun";
            }
        }

        for (int i = 0; i < conditions.length; i++) {

            if ("Chance of Rain".equals(conditions[i])) {
                conditions[i] = "Chance of Rain";

            } else if ("Clear".equals(conditions[i])) {
                conditions[i] = "Clear";

            } else if ("Partly Sunny".equals(conditions[i])) {
                conditions[i] = "Partly Sunny";

            } else if ("Mostly Sunny".equals(conditions[i])) {
                conditions[i] = "Mostly Sunny";
            }
        }

        for (int i = 0; i < temperatures.length; i++) {
            Log.i("TAG", low[i] + "TAG");

            low[i] = 5d / 9d * (low[i] - 32);
            Log.i("TAG", low[i] + "----------");
            high[i] = 5d / 9d * (high[i] - 32);
            Log.i("TAG", high[i] + "----------****");
            temperatures[i] = String.valueOf(low[i]).substring(0, 2)
                    + "-" + String.valueOf(high[i]).substring(0, 2);
        }
    }

    private Drawable loadImage(String imageUrl){
        try{
            return Drawable.createFromStream((InputStream) new URL("http://www.google.com" +
                    imageUrl).getContent(), "image");
        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
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
