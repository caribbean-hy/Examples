package com.example.picklh.examples;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ImgViewActivity extends Activity {

    TextView txtV;
    ImageView imgV;
    Button btnPre;
    Button btnNext;

    int[] imgId = {
            R.drawable.p01,
            R.drawable.p02,
            R.drawable.p03,
            R.drawable.p04,
            R.drawable.p05,
    };

    int curImgId = 0;

    public View.OnClickListener viewClickListerner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == btnPre) {
                curImgId = (curImgId - 1 + imgId.length) % imgId.length;
            } else {
                curImgId=(curImgId+1)%imgId.length;
            }
            imgV.setImageResource(imgId[curImgId]);
            txtV.setText(imgId[curImgId]);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.img_view);

        txtV=(TextView)findViewById(R.id.txtV);

        imgV = (ImageView) findViewById(R.id.imgView);

        btnPre = (Button) findViewById(R.id.btnPre);
        btnNext = (Button) findViewById(R.id.btnNext);

        btnPre.setOnClickListener(viewClickListerner);
        btnNext.setOnClickListener(viewClickListerner);

    }


}
