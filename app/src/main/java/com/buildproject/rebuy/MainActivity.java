package com.buildproject.rebuy;

import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
}
