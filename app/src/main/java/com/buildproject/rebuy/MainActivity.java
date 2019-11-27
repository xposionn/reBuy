package com.buildproject.rebuy;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class MainActivity extends AppCompatActivity {
    GoogleSignInAccount account;
    TextView dispName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dispName = findViewById(R.id.dispName);
        account = getIntent().getParcelableExtra("account");

    }


    @Override
    protected void onStart() {
        super.onStart();
        dispName.setText(account.getDisplayName());
        }
}
