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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.buildproject.rebuy.Modules.ListOfItems;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class EditListActivity extends AppCompatActivity {
    private static final String TAG = EditListActivity.class.getName();
    GoogleSignInAccount account;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReferenceList;
    private ListOfItems current_list;

    private String list_id;
    private EditText listName;
    private TextView ownerName;
    private ImageButton saveButton;

    private String priority="Normal";
    private ImageButton greenBtn;
    private ImageButton yellowBtn;
    private ImageButton redBtn;

    private EditText editorNum;
    private ImageButton editorButton;
    private EditText viewerNum;
    private ImageButton viewerButton;
    private final int SEND_SMS_PERMISSION_REQUEST_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_list);

        //account = getIntent().getParcelableExtra("account");
        account = GoogleSignIn.getLastSignedInAccount(this);
        listName = findViewById(R.id.uid);
        ownerName = findViewById(R.id.edit_list_ownerName);

        saveButton = findViewById(R.id.save_list_btn);
        editorButton = findViewById(R.id.addEditor);
        editorNum = findViewById(R.id.editorNumberText);
        viewerButton = findViewById(R.id.addViewer);
        viewerNum = findViewById(R.id.viewerNumberText);
        list_id = getIntent().getStringExtra("list_id");
        greenBtn = findViewById(R.id.edit_list_green_btn);
        yellowBtn = findViewById(R.id.edit_list_yellow_btn);
        redBtn = findViewById(R.id.edit_list_red_btn);

        mDatabase = FirebaseDatabase.getInstance();
        mReferenceList = mDatabase.getReference("lists").child(list_id);

        mReferenceList.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                current_list = dataSnapshot.getValue(ListOfItems.class);
                listName.setText(current_list.getTitleName());
                setPriority(current_list.getPriority());
                //TODO set owner name instead of owner id
                ownerName.setText(current_list.getOwner());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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

                current_list.setTitleName(listName.getText().toString());
                current_list.setPriority(priority);
                new FirebaseDBadapter().updateList(list_id, current_list, new FirebaseDBadapter.DataStatus() {
                    @Override
                    public void DataIsLoaded(List<ListOfItems> lists, List<String> keys) {

                    }

                    @Override
                    public void DataIsInserted() {

                    }

                    @Override
                    public void DataIsUpdated() {
                        //TODO
                    }

                    @Override
                    public void DataIsDeleted() {

                    }
                });

            }
        });

    }

    public void priorityLowPressed(View v){
        setPriority("Low");
    }

    public void priorityNormalPressed(View v){
        setPriority("Normal");
    }

    public void priorityHighPressed(View v){
        setPriority("High");
    }

    private void setPriority(String prio) {
        priority = prio;

        int not_chosen = 0xffffffff;
        int chosen = 0x88888888;
        greenBtn.setBackgroundColor(not_chosen);
        yellowBtn.setBackgroundColor(not_chosen);
        redBtn.setBackgroundColor(not_chosen);

        if (priority.equals("Low")) {
           greenBtn.setBackgroundColor(chosen);
        } else if (priority.equals("Normal")) {
            yellowBtn.setBackgroundColor(chosen);
        } else if (priority.equals("High")) {
            redBtn.setBackgroundColor(chosen);
        }
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
