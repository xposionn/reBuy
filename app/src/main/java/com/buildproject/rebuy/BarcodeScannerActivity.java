package com.buildproject.rebuy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.Button;

import com.wonderkiln.camerakit.CameraView;

public class BarcodeScannerActivity extends AppCompatActivity {

    CameraView cameraView;
    Button btnDetectBarcode;

    @Override
    protected void onResume() {
        super.onResume();
        cameraView.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraView.stop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scanner);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        cameraView = (CameraView) findViewById(R.id.camerview);
        btnDetectBarcode = (Button)findViewById(R.id.btn_capture_barcode);
    }
}
