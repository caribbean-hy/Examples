package com.example.picklh.examples;

import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

/**
 * Home activity for app navigation code samples.
 */

public class AppNavHomeActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_examples);
        setListAdapter(new SampleAdapter(querySampleActivities()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.examples, menu);
        return true;
    }

    @Override
    protected void onListItemClick(ListView lv, View v, int pos, long id) {
        SampleInfo info = (SampleInfo) getListAdapter().getItem(pos);//跳转到其他sample
        startActivity(info.intent);
    }

    //取出过滤器中action name为action_main的
    protected List<SampleInfo> querySampleActivities() {
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.setPackage(getPackageName());//package name 为com.example.picklh.examples
        intent.addCategory(Intent.CATEGORY_SAMPLE_CODE);//取出过滤器中action category为CATEGORY_SAMPLE_CODE的

        PackageManager pm = getPackageManager();
        List<ResolveInfo> infos = pm.queryIntentActivities(intent, 0);//读取过滤器中所有符合条件的activities

        ArrayList<SampleInfo> samples = new ArrayList<SampleInfo>();
        final int count = infos.size();
        for (int i = 0; i < count; i++) {
            final ResolveInfo info = infos.get(i);
            final CharSequence labelSeq = info.loadLabel(pm);//取label name
            String label = labelSeq != null ? labelSeq.toString() : info.activityInfo.name;

            Intent target = new Intent();
            target.setClassName(info.activityInfo.applicationInfo.packageName, info.activityInfo.name);//com.example.picklh.examples com.example.picklh.examples.SimpleUpActivity
            SampleInfo sample = new SampleInfo(label, target);
            samples.add(sample);
        }

        return samples;
    }

    static class SampleInfo {
        String name;
        Intent intent;

        SampleInfo(String name, Intent intent) {
            this.name = name;
            this.intent = intent;
        }
    }

    class SampleAdapter extends BaseAdapter {
        private List<SampleInfo> mItems;

        public SampleAdapter(List<SampleInfo> items) {
            mItems = items;
        }

        @Override
        public int getCount() {
            return mItems.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(android.R.layout.simple_list_item_1, parent, false);
                convertView.setTag(convertView.findViewById(android.R.id.text1));
            }
            TextView tv = (TextView) convertView.getTag();
            tv.setText(mItems.get(position).name);
            return convertView;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Object getItem(int position) {
            return mItems.get(position);
        }
    }
}
