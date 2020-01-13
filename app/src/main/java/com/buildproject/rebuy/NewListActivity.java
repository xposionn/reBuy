package com.buildproject.rebuy;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.buildproject.rebuy.Modules.ItemInList;
import com.buildproject.rebuy.Modules.ListOfItems;
import com.buildproject.rebuy.Modules.User;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NewListActivity extends AppCompatActivity {
    private static final String TAG = NewListActivity.class.getName();
    GoogleSignInAccount account;

    private TextView uid;
    private ImageButton saveButton;
    private String priority="Normal";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnewlist);

        account = getIntent().getParcelableExtra("account");
        uid = findViewById(R.id.uid);
        saveButton = findViewById(R.id.save_list_btn);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Get text and value from the activity
                ListOfItems listOfItems = new ListOfItems();
                listOfItems.setPriority(priority);
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
                listOfItems.setAddedTime(sdf.format(new Date()));

                String titleName = uid.getText().toString();
                if (titleName.isEmpty()) {
                    Toast toast = Toast.makeText(view.getContext(), "Enter list name!", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                else
                    listOfItems.setTitleName(titleName);

                listOfItems.setOwner(account.getId());

                new FirebaseDBadapter().addList(listOfItems, new FirebaseDBadapter.DataStatus() {
                    @Override
                    public void DataIsLoaded(List<ListOfItems> lists, List<String> keys) {

                    }

                    @Override
                    public void DataIsInserted() {
                        Toast.makeText(NewListActivity.this, "List Added!", Toast.LENGTH_SHORT).show();
                        //TODO start itemsActivity
                    }

                    @Override
                    public void DataIsUpdated() {

                    }

                    @Override
                    public void DataIsDeleted() {

                    }
                });
            }
        });

    }

    public void priorityLowPressed(View v){
        priority = "Low";
    }

    public void priorityNormalPressed(View v){
        priority = "Normal";
    }

    public void priorityHighPressed(View v){
        priority = "High";
    }

    public void notCreateList(View view) {
        super.onBackPressed();
    }
}
