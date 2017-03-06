package com.github.aliakseikaraliou.shoplist.ui.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.github.aliakseikaraliou.shoplist.R;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class BarcodeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        final SurfaceView surfaceView = (SurfaceView) findViewById(R.id.activity_barcodecamera_surface);
        final BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.EAN_8 | Barcode.EAN_13)
                .build();
        final CameraSource cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .build();
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceCreated(final SurfaceHolder holder) {
                try {
                    // TODO: 05.03.2017 fix security exception
                    cameraSource.start(surfaceView.getHolder());
                } catch (IOException | SecurityException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(final SurfaceHolder holder, final int format, final int width, final int height) {

            }

            @Override
            public void surfaceDestroyed(final SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {

            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(final Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> detectedItems = detections.getDetectedItems();
                if (detectedItems.size() > 0) {
                    Toast.makeText(BarcodeActivity.this, detectedItems.valueAt(0).displayValue, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
