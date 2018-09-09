package com.example.alex.colorapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnTakeAPicture;
    private Button btnSaveThePicture;
    private ImageView imgPhoto;
    private SeekBar redSeekbar;
    private SeekBar greenSeekbar;
    private SeekBar blueSeekbar;
    private TextView txtRedColorValue;
    private TextView txtGreenColorValue;
    private TextView txtBlueColorValue;

    private static final int CAMERA_INTENT_REQUEST_CODE = 1000;

    Bitmap bitmap;

    private Colorful colorful;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnTakeAPicture = (Button) findViewById(R.id.btnTakePicture);
        btnSaveThePicture = (Button) findViewById(R.id.btnSavePicture);
        imgPhoto = (ImageView) findViewById(R.id.imgPhoto);
        redSeekbar = (SeekBar) findViewById(R.id.redColorSeekBar);
        greenSeekbar = (SeekBar) findViewById(R.id.greenColorSeekBar);
        blueSeekbar = (SeekBar) findViewById(R.id.blueColorSeekbar);
        txtRedColorValue = (TextView) findViewById(R.id.txtRedColorValue);
        txtGreenColorValue = (TextView) findViewById(R.id.txtGreenColorValue);
        txtBlueColorValue = (TextView) findViewById(R.id.txtBlueColorValue);

        btnTakeAPicture.setOnClickListener(MainActivity.this);
        btnSaveThePicture.setOnClickListener(MainActivity.this);

        ColorizationHandler colorizationHandler = new ColorizationHandler();

        redSeekbar.setOnSeekBarChangeListener(colorizationHandler);
        greenSeekbar.setOnSeekBarChangeListener(colorizationHandler);
        blueSeekbar.setOnSeekBarChangeListener(colorizationHandler);

    }

    @Override
    public void onClick(View view) {

        if(view.getId() == R.id.btnTakePicture){

            int permissionResult = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA);
            if(permissionResult == PackageManager.PERMISSION_GRANTED){

                PackageManager packageManager = getPackageManager();
                if(packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)){

                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent,CAMERA_INTENT_REQUEST_CODE );

                }else{

                    Toast.makeText(MainActivity.this,"Your device does not have CAMERA", Toast.LENGTH_SHORT).show();

                }

            } else {

                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.CAMERA},1);
            }


        } else if(view.getId() == R.id.btnSavePicture){


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CAMERA_INTENT_REQUEST_CODE && resultCode == RESULT_OK){

            Bundle bundle = data.getExtras();
            bitmap = (Bitmap) bundle.get("data");

            colorful = new Colorful(bitmap, 0.0f, 0.0f, 0.0f);

            imgPhoto.setImageBitmap(bitmap);
        }
    }

    private class ColorizationHandler implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            if (b){
                if (seekBar == redSeekbar) {

                    colorful.setRedColorValue(i / 100.0f);
                    redSeekbar.setProgress((int) (100.0f * (colorful.getRedColorValue())));
                    txtRedColorValue.setText(colorful.getRedColorValue() + "");

                } else if (seekBar == greenSeekbar) {

                    colorful.setGreenColorValue(i / 100.0f);
                    greenSeekbar.setProgress((int) (100.0f * (colorful.getGreenColorValue())));
                    txtGreenColorValue.setText(colorful.getGreenColorValue() + "");

                } else if (seekBar == blueSeekbar) {

                    colorful.setBlueColorValue(i / 100.0f);
                    blueSeekbar.setProgress((int) (100.0f * (colorful.getBlueColorValue())));
                    txtBlueColorValue.setText(colorful.getBlueColorValue() + "");
                }

                bitmap = colorful.returnColorizedBitmap();
                imgPhoto.setImageBitmap(bitmap);
            }
        }


        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }

}
