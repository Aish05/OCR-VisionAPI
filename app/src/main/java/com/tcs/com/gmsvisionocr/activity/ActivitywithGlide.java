package com.tcs.com.gmsvisionocr.activity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.tcs.com.gmsvisionocr.R;

/**
 * Created by 1033826 on 12/7/2016.
 */

public class ActivitywithGlide extends AppCompatActivity {
    private TextView textView;
    private static int RESULT_LOAD_IMAGE = 1;
    private Context context;

    String TAG = "MAIN ACTIVITY";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_image);
        context = this;


        Button buttonLoadImage = (Button) findViewById(R.id.uploadimage);
        textView = (TextView) findViewById(R.id.textimage);

        buttonLoadImage.setOnClickListener(new View.OnClickListener() {

            /**
             * on click method of the upload button
             * @param arg0
             */
            @Override
            public void onClick(View arg0) {

                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

    }


    /**
     * In this method, we check if the activity that was triggered was indeed Image Gallery
     * (It is common to trigger different intents from the same activity and expects result from each).
     * For this we used RESULT_LOAD_IMAGE integer that we passed previously to startActivityForResult() method
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        final ImageView imageView = (ImageView) findViewById(R.id.imgView);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {

                int myWidth = 512;
                int myHeight = 384;

                Glide.with(this)
                        .load(uri)
                        .asBitmap()
                        .into(new SimpleTarget<Bitmap>(myWidth, myHeight) {
                            @Override
                            public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                                // Do your work with bitmap here.
                                imageView.setImageBitmap(bitmap);

                                if (bitmap != null) {

                                    TextRecognizer textRecognizer = new TextRecognizer.Builder(context).build();

                                    if (!textRecognizer.isOperational()) {
                                        // Note: The first time that an app using a Vision API is installed on a
                                        // device, GMS will download a native libraries to the device in order to do detection.
                                        // Usually this completes before the app is run for the first time.  But if that
                                        // download has not yet completed, then the above call will not detect any text,
                                        // barcodes, or faces.
                                        // isOperational() can be used to check if the required native libraries are currently
                                        // available.  The detectors will automatically become operational once the library
                                        // downloads complete on device.
                                        Log.w(TAG, "Detector dependencies are not yet available.");

                                        // Check for low storage.  If there is low storage, the native library will not be
                                        // downloaded, so detection will not become operational.
                                        IntentFilter lowstorageFilter = new IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW);
                                        boolean hasLowStorage = registerReceiver(null, lowstorageFilter) != null;

                                        if (hasLowStorage) {
                                            Toast.makeText(context, "Low Storage", Toast.LENGTH_LONG).show();
                                            Log.w(TAG, "Low Storage");
                                        }
                                    }


                                    Frame imageFrame = new Frame.Builder()
                                            .setBitmap(bitmap)
                                            .build();


                                    SparseArray<TextBlock> textBlocks = textRecognizer.detect(imageFrame);


                                    for (int i = 0; i < textBlocks.size(); i++) {

                                        TextBlock textBlock = textBlocks.get(textBlocks.keyAt(i));

                                        String text = textBlock.getValue();

                                        textView.setText(text);

                                        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();

                                    }
                                }

                            }
                        });


            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
