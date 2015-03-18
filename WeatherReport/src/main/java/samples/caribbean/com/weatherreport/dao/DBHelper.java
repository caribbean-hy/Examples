package samples.caribbean.com.weatherreport.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by picklh on 2015/2/27.
 */
public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context, String dataname){
        super(context,dataname,null,2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public String[] getAllProvinces(){
        String[] columns={"name"};

        SQLiteDatabase db=getReadableDatabase();

        //查询获得游标
        Cursor cursor=db.query("provinces",columns, null,null,null,null,null);
        columns=null;
        int count=cursor.getCount();
        String[] provinces=new String[count];
        count=0;
        while (!cursor.isLast()){
            cursor.moveToNext();
            provinces[count]=cursor.getString(0);
            count++;
        }
        cursor.close();
        db.close();
        return provinces;
    }

    public List<String[][]> getAllCityAndCode(String[] groups){

        int length=groups.length;
        SQLiteDatabase db=getReadableDatabase();
        String[][] city=new String[length][];
        String[][] city_code=new String[length][];
        int count;

        for (int i=0;i<length;i++){

            //查询获得游标
            Cursor cursor = db.query("citys",new String[]{"name","city_num"},"province_id=?", new String[]{String.valueOf(i)},null,null,null);
            count=cursor.getCount();
            city[i]=new String[count];
            city_code[i]=new String[count];
            count=0;
            while (!cursor.isLast()){
                cursor.moveToNext();
                city[i][count]=cursor.getString(0);
                city_code[i][count]=cursor.getString(1);

                count++;
            }
            cursor.close();
        }
        db.close();
        List<String[][]> result=new ArrayList<String[][]>();
        result.add(city);
        result.add(city_code);
        return result;
    }

    public String getCityCodeByName(String city_str){
        SQLiteDatabase db=getReadableDatabase();
        Cursor cursor=db.query("citys",new String[]{"city_num"},"name=?",new String[]{city_str},null,null,null);
        String cityCode=null;
        if(!cursor.isLast()){
            cursor.moveToNext();
            cityCode=cursor.getString(0);
        }
        cursor.close();
        db.close();
        return cityCode;
    }
}
