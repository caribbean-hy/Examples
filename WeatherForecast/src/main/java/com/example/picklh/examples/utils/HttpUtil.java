package com.example.picklh.examples.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOError;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Created by picklh on 2015/2/2.
 */
public class HttpUtil {

    private static final String CHARSET="HTTP.UTF_8";

    private HttpUtil(){

    }

    public static String get(String url){
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(url);

        try {
            HttpResponse response = client.execute(request);
            if(response.getStatusLine().getStatusCode()==200){
                HttpEntity entity=response.getEntity();
                return EntityUtils.toString(entity, CHARSET);
            }
        }catch (ClientProtocolException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }

        return null;
    }

}
