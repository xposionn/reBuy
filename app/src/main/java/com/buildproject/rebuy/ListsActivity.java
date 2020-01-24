package com.buildproject.rebuy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.buildproject.rebuy.Configs.RecyclerView_Config;
import com.buildproject.rebuy.Modules.ListOfItems;
import com.buildproject.rebuy.Services.FirebaseDBadapter;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.List;

public class ListsActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    GoogleSignInAccount account;
    GoogleSignInClient mGoogleSignInClient;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        setContentView(R.layout.activity_lists);
        account = getIntent().getParcelableExtra("account");
        mRecyclerView = (RecyclerView) findViewById(R.id.recycleview_lists);
        new FirebaseDBadapter().readLists(account.getId(),new FirebaseDBadapter.DataStatus() {
            @Override
            public void DataIsLoaded(List<ListOfItems> lists, List<String> keys) {
                new RecyclerView_Config().setConfig(mRecyclerView, ListsActivity.this, lists,keys, account.getDisplayName());
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


    public void moveToNewListActivity(View v) {
        Intent i = new Intent(getApplicationContext(), NewListActivity.class);
        i.putExtra("account", account);
        startActivity(i);
    }

    public void logout_btn(View view) {
        signOut();
        Toast.makeText(this, "logout successfully.", Toast.LENGTH_SHORT).show();
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);

                    }
                });
    }
}
