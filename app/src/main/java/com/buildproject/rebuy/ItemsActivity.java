package com.buildproject.rebuy;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.buildproject.rebuy.Modules.ItemInList;
import com.buildproject.rebuy.Modules.ListOfItems;

import java.util.List;
import java.util.Objects;

public class ItemsActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private String list_id;
    private ListOfItems.Permission permission;
    private String displayName;

    private SensorManager mSensorManager;
    private float mAccel; // acceleration apart from gravity
    private float mAccelCurrent; // current acceleration including gravity
    private float mAccelLast; // last acceleration including gravity
    private  final int SENSOR_RATE = 20;

    private final SensorEventListener mSensorListener = new SensorEventListener() {

        public void onSensorChanged(SensorEvent se) {
            float x = se.values[0];
            float y = se.values[1];
            float z = se.values[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt((double) (x*x + y*y + z*z));
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta; // perform low-cut filter

            if (mAccel > SENSOR_RATE) {
                addByBarcode();
            }
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        list_id = Objects.requireNonNull(getIntent().getExtras()).getString("list_id");
        permission = (ListOfItems.Permission) getIntent().getExtras().getSerializable("permission");
        displayName = getIntent().getExtras().getString("display_name");
        setTitle(Objects.requireNonNull(getIntent().getExtras()).getString("list_title"));

        setContentView(R.layout.activity_items);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycleview_items);
        new FirebaseDBadapterItems(list_id).readItems(new FirebaseDBadapterItems.DataStatus() {
            @Override
            public void DataIsLoaded(List<ItemInList> items, List<String> keys) {
                new ItemsRecyclerView_Config().setConfig(mRecyclerView, ItemsActivity.this, items, keys, list_id,permission);
            }

            @Override
            public void DataIsInserted() {

            }

            @Override
            public void DataIsUpdated() {

            }

            @Override
            public void DataIsDeleted() {

            }
        });

        //For shake sensor
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;
    }

    public void moveToAddItemActivity(View view) {
        if (permission == ListOfItems.Permission.EDITOR || permission == ListOfItems.Permission.OWNER) {
            Intent i = new Intent(getApplicationContext(), AddItemActivity.class);
            i.putExtra("list_id", list_id);
            i.putExtra("display_name",displayName);
            startActivity(i);
        }else{
            Toast.makeText(this, "You have no permission to add a new Item", Toast.LENGTH_SHORT).show();
        }
    }

    public void editList(View view) {
        if (permission == ListOfItems.Permission.EDITOR || permission == ListOfItems.Permission.OWNER) {
            Intent i = new Intent(getApplicationContext(), EditListActivity.class);
            i.putExtra("list_id", list_id);
            i.putExtra("permission",permission);
            startActivity(i);
        }else{
            Toast.makeText(this, "You have no permission to edit this list", Toast.LENGTH_SHORT).show();
        }
    }

    public void addByBarcode(View v) {
        addByBarcode();
    }

    public void addByBarcode(){
        Intent i = new Intent(getApplicationContext(), BarcodeScannerActivity.class);
        i.putExtra("item_name",""); //will get itemName from DB

        //will be used to forward extras to "Add Item" activity:
        i.putExtra("list_id", list_id);
        i.putExtra("display_name",displayName);
        startActivity(i);
    }

}
