package com.tcs.com.gmsvisionocr.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.tcs.com.gmsvisionocr.R;

/**
 * Created by 1033826 on 12/7/2016.
 */

public class OCRMainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button LiveCameraocr;
    private Button Uploadimageocr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_ocr);

        LiveCameraocr = (Button) findViewById(R.id.LiveCameraocr);
        Uploadimageocr = (Button) findViewById(R.id.Uploadimageocr);
        LiveCameraocr.setOnClickListener(this);
        Uploadimageocr.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.LiveCameraocr){

            Intent intent = new Intent(this, LiveCameraOCRActivity.class);
            startActivity(intent);
        }

        if(v.getId() == R.id.Uploadimageocr){

            Intent intent = new Intent(this, OCRStaticimageActivity.class);
            startActivity(intent);
        }

    }
}
