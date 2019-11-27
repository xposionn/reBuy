package com.buildproject.rebuy;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {
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


    }
}
