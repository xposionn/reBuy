package com.buildproject.rebuy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.buildproject.rebuy.Modules.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventListener;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;

import java.util.List;
import java.util.Objects;

import dmax.dialog.SpotsDialog;

public class BarcodeScannerActivity extends AppCompatActivity {

    CameraView cameraView;
    Button btnDetectBarcode;
    AlertDialog waitingDialog;
    String itemNameFromDialog;
    AlertDialog.Builder itemNameDialogBuilder;
    String list_id;
    String displayName;

    String itemName;
    String detectedItemBarcode;

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
        itemName = Objects.requireNonNull(getIntent().getExtras()).getString("item_name");
        list_id = getIntent().getExtras().getString("list_id");
        displayName = getIntent().getExtras().getString("display_name");

        cameraView = (CameraView) findViewById(R.id.camerview);
        btnDetectBarcode = (Button)findViewById(R.id.btn_capture_barcode);

        waitingDialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Please wait..")
                .setCancelable(false)
                .build();

        itemNameDialogBuilder = new AlertDialog.Builder(this);
        itemNameDialogBuilder.setTitle("Add item name for this barcode");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        itemNameDialogBuilder.setView(input);
        // Set up the buttons
        itemNameDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                itemNameFromDialog = input.getText().toString();
                //save to db:
                if(itemNameFromDialog!=null && !itemNameFromDialog.isEmpty()) {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("barcodes");
                    myRef.child(Objects.requireNonNull(detectedItemBarcode)).setValue(itemNameFromDialog);
                    Toast.makeText(BarcodeScannerActivity.this, "Saved " + itemNameFromDialog + " in database!", Toast.LENGTH_SHORT).show();
                }
                //move to add item with added item name:
                Intent i = new Intent(getApplicationContext(), AddItemActivity.class);
                i.putExtra("list_id", list_id);
                i.putExtra("display_name",displayName);
                i.putExtra("barcode_name",itemNameFromDialog);
                startActivity(i);
            }
        });
        itemNameDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });


        btnDetectBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraView.start();
                cameraView.captureImage();
            }
        });

        cameraView.addCameraKitListener(new CameraKitEventListener() {
            @Override
            public void onEvent(CameraKitEvent cameraKitEvent) {

            }

            @Override
            public void onError(CameraKitError cameraKitError) {

            }

            @Override
            public void onImage(CameraKitImage cameraKitImage) {
                waitingDialog.show();
                Bitmap bitmap = cameraKitImage.getBitmap();
                bitmap = Bitmap.createScaledBitmap(bitmap, cameraView.getWidth(),cameraView.getHeight(),false);
                cameraView.stop();

                runImageDetector(bitmap);
            }

            @Override
            public void onVideo(CameraKitVideo cameraKitVideo) {

            }
        });
    }

    private void runImageDetector(Bitmap bitmap) {
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);
        FirebaseVisionBarcodeDetectorOptions options = new FirebaseVisionBarcodeDetectorOptions.Builder()
                .setBarcodeFormats(
                        FirebaseVisionBarcode.FORMAT_EAN_8,
                        FirebaseVisionBarcode.FORMAT_EAN_13
                )
                .build();
        FirebaseVisionBarcodeDetector detector = FirebaseVision.getInstance().getVisionBarcodeDetector(options);

        detector.detectInImage(image)
                .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionBarcode>>() {
                    @Override
                    public void onSuccess(List<FirebaseVisionBarcode> firebaseVisionBarcodes) {
                        processResult(firebaseVisionBarcodes);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(BarcodeScannerActivity.this, e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });

    }

    //inner class
    class ValueEventListenerWithBarcode implements ValueEventListener {
        private String barcode;
        private String list_id;
        private String displayName;


        ValueEventListenerWithBarcode(String barcodeValue){
            super();
            this.barcode = barcodeValue;

        }

        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            boolean found = false;
            for(DataSnapshot keyNode : dataSnapshot.getChildren()){
                if(keyNode.getKey().equals(this.barcode)){
                    found = true;
                    Intent i = new Intent(getApplicationContext(), AddItemActivity.class);
                    i.putExtra("list_id", list_id);
                    i.putExtra("display_name",displayName);
                    i.putExtra("barcode_name",keyNode.getValue(String.class));
                    startActivity(i);
                    break;
                }
            }
            if(!found){
                Toast.makeText(BarcodeScannerActivity.this, "Item not found.",Toast.LENGTH_SHORT).show();
                itemNameDialogBuilder.show();
            }

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    }

    private void processResult(List<FirebaseVisionBarcode> firebaseVisionBarcodes) {


        for(FirebaseVisionBarcode item : firebaseVisionBarcodes){
            int value_type = item.getValueType();
            switch (value_type){
                case FirebaseVisionBarcode.TYPE_PRODUCT:
                {
                    Toast.makeText(BarcodeScannerActivity.this, "Detected: "+item.getRawValue(),Toast.LENGTH_SHORT).show();
                    detectedItemBarcode = item.getRawValue();
                    if(itemName!=null && !itemName.isEmpty()){
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference("barcodes");
                        myRef.child(Objects.requireNonNull(item.getRawValue())).setValue(itemName);
                        Toast.makeText(BarcodeScannerActivity.this, "Saved " + itemName + " in database!",Toast.LENGTH_SHORT).show();
                        super.onBackPressed();
                    }else if(itemName!=null && itemName.equals("")){
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference("barcodes");
                        myRef.addListenerForSingleValueEvent(new ValueEventListenerWithBarcode(item.getRawValue()));

                    }else{
                        //nothing atm. should not get here.
                        Toast.makeText(BarcodeScannerActivity.this, "Something bad happened.",Toast.LENGTH_SHORT).show();

                    }

                    // Alert dialog:
//                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
//                    builder.setMessage(item.getRawValue());
//                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            dialogInterface.dismiss();
//                        }
//                    });
//                    androidx.appcompat.app.AlertDialog dialog = builder.create();
//                    dialog.show();

                }
                break;

                default:
                    Toast.makeText(BarcodeScannerActivity.this, "Different Type. ID = " + value_type,Toast.LENGTH_SHORT).show();
                    System.out.println("Default Type. ID = " + value_type);
                    break;
            }
        }
        waitingDialog.dismiss();
    }
}
