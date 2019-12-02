package com.buildproject.rebuy;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class ListsActivity extends AppCompatActivity {
    private static final String TAG = ListsActivity.class.getName();
    GoogleSignInAccount account;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lists);

        account = getIntent().getParcelableExtra("account");


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
}
