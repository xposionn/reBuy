package com.buildproject.rebuy;

import android.app.Notification;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.buildproject.rebuy.Modules.ItemInList;
import com.buildproject.rebuy.Modules.ListOfItems;
import com.buildproject.rebuy.Modules.User;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();

    User user1;
    GoogleSignInAccount account;

    TextView dispName;
    ImageView profilePic;
    Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dispName = findViewById(R.id.dispName);
        account = getIntent().getParcelableExtra("account");
        imageUri = account.getPhotoUrl();
        profilePic = findViewById(R.id.profilePic);
    }


    @Override
    protected void onStart() {
        super.onStart();
        dispName.setText(account.getDisplayName());
        Picasso.get().load(imageUri).into(profilePic);


//        ListOfItems list1 = new ListOfItems(user);
//        list1.addEditor(user);
//        list1.addViewer(user);


        // Read from the database
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference userReference = mDatabase.getReference()
                .child("users").child(account.getId());
        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                System.out.println("my user"+user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
//                Toast.makeText(ThreadActivity.this, R.string.error_loading_user, Toast.LENGTH_SHORT).show();
//                finish();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.help:
                System.out.println("clicked on help");
        }
        return super.onOptionsItemSelected(item);
    }

    public void moveToNewListActivity(View v) {
        Intent i = new Intent(getApplicationContext(), NewListActivity.class);
        i.putExtra("account", account);
        startActivity(i);
    }

    public void moveToList(View v) {
        Intent i = new Intent(getApplicationContext(), ListsActivity.class);
        i.putExtra("account", account);
        startActivity(i);
    }

    public void hardCodedList(View view) {
        ListOfItems listOfItems = new ListOfItems();
        listOfItems.setOwner(account.getId());
        listOfItems.setTitleName("this is hard coded list");
        listOfItems.addEditor("id of editor");
        listOfItems.addViewer("id of viewer");
        new FirebaseDBadapter().addList(listOfItems, new FirebaseDBadapter.DataStatus() {
            @Override
            public void DataIsLoaded(List<ListOfItems> lists, List<String> keys) {

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
    }
}
