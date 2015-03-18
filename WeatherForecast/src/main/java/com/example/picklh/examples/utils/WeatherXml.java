package com.example.picklh.examples.utils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

/**
 * Created by picklh on 2015/2/2.
 */
public class WeatherXml {
    private static String url="http://www.google.com/ig/api?&weather=";

    public static String getWeatherXml(String city){
        NameValuePair param=new BasicNameValuePair("city",city);
        String xml= HttpUtil.get(url+city);
        return xml;
    }

}
