package com.buildproject.rebuy;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.buildproject.rebuy.Modules.ListOfItems;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.List;

public class EditListActivity extends AppCompatActivity {
    private static final String TAG = EditListActivity.class.getName();
    GoogleSignInAccount account;

    private TextView uid;
    private ImageButton saveButton;
    private EditText editorNum;
    private ImageButton editorButton;
    private EditText viewerNum;
    private ImageButton viewerButton;
    private String priority="Normal";
    private final int SEND_SMS_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_list);

        //account = getIntent().getParcelableExtra("account");
        account = GoogleSignIn.getLastSignedInAccount(this);
        uid = findViewById(R.id.uid);
        saveButton = findViewById(R.id.save_list_btn);
        editorButton = findViewById(R.id.addEditor);
        editorNum = findViewById(R.id.editorNumberText);
        viewerButton = findViewById(R.id.addViewer);
        viewerNum = findViewById(R.id.viewerNumberText);


        editorButton.setEnabled(false);
        viewerButton.setEnabled(false);
        if (checkSMSPermission(Manifest.permission.SEND_SMS)) {
            editorButton.setEnabled(true);
            viewerButton.setEnabled(true);
        }
        else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS}, SEND_SMS_PERMISSION_REQUEST_CODE);
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Get text and value from the activity
                ListOfItems listOfItems = new ListOfItems();
                listOfItems.setPriority(priority);

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
                        Toast.makeText(EditListActivity.this, "List Added!", Toast.LENGTH_SHORT).show();
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


//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference ref = database.getReference("lists");
//        //getting data from firebase
//        ValueEventListener eventListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for(DataSnapshot ds : dataSnapshot.getChildren()) {
//                    if(ds.getKey().equals(account.getId())) {
//                        String product = ds.getKey();
//                        Log.d(TAG, product);
//                    }
//                }
//            }
//            @Override
//            public void onCancelled(DatabaseError databaseError) {}
//        };
//        ref.addListenerForSingleValueEvent(eventListener);

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



    public void sendSMS(ListOfItems.Permission permission) {
        String permission_id;
        String number = "";
        if (permission == ListOfItems.Permission.EDITOR) {
            permission_id = "0";
            number = editorNum.getText().toString();
        } else if (permission == ListOfItems.Permission.VIEWER) {
            permission_id = "1";
            number = viewerNum.getText().toString();
        } else {
            permission_id = "-1";
        }

        if (number.isEmpty()) {
            return;
        }
        if (checkSMSPermission(Manifest.permission.SEND_SMS)) {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(number, null, permission.toString(), null, null);
            Toast.makeText(this, "Invitation sent", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkSMSPermission(String smsPermission) {
        int check = ContextCompat.checkSelfPermission(this, smsPermission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }

    public void sendSMSEditor(View view) {
        sendSMS(ListOfItems.Permission.EDITOR);
        System.out.println("editor click");

    }

    public void sendSMSViewer(View view) {
        sendSMS(ListOfItems.Permission.VIEWER);
        System.out.println("viewer click");
    }

}
