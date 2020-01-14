package com.buildproject.rebuy.Services;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.buildproject.rebuy.AddItemActivity;
import com.buildproject.rebuy.FirebaseDBadapter;
import com.buildproject.rebuy.FirebaseDBadapterItems;
import com.buildproject.rebuy.ItemsActivity;
import com.buildproject.rebuy.LoginActivity;
import com.buildproject.rebuy.Modules.ItemInList;
import com.buildproject.rebuy.Modules.ListOfItems;
import com.buildproject.rebuy.NewListActivity;
import com.buildproject.rebuy.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;

import java.util.List;

public class JoinListActivity extends AppCompatActivity {

    private TextView mJoinText;
    private Button mAccept;
    private Button mReject;

    GoogleSignInAccount account;
    private String list_id;
    private String inviter_id;
    private ListOfItems.Permission permission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_list);

        mJoinText = (TextView) findViewById(R.id.join_text);
        mAccept= (Button) findViewById(R.id.accept_join);
        mReject = (Button) findViewById(R.id.reject_join);

        String permission_id  = "-1";
        Uri uri = getIntent().getData();
        if (uri != null) {
            List<String> params = uri.getPathSegments();
            inviter_id = params.get(params.size()-3);
            list_id = params.get(params.size()-2);
            permission_id = params.get(params.size()-1);
        }
        account = getAccount();

        if (permission_id.equals("0")) {
            permission = ListOfItems.Permission.EDITOR;
            mJoinText.setText(getString(R.string.editor_invite));
        }
        else if (permission_id.equals("1")) {
            permission = ListOfItems.Permission.VIEWER;
            mJoinText.setText(getString(R.string.viewer_invite));
        }
        else {
            Toast.makeText(JoinListActivity.this, "Sorry, something wrong", Toast.LENGTH_SHORT).show();
            endActivity();
        }
    }

    public void click_accept(View view) {
        final View current_view = view;
        new FirebaseDBadapter().addPartner(list_id, account.getId(), permission, new FirebaseDBadapterItems.DataStatus() {
            @Override
            public void DataIsLoaded(List< ItemInList > lists, List< String > keys) {

            }

            @Override
            public void DataIsInserted() {

            }

            @Override
            public void DataIsUpdated() {
                Toast.makeText(JoinListActivity.this, "Welcome to the list", Toast.LENGTH_SHORT).show();

                //TODO get list name
                if (false) {
                    Intent newIntent = new Intent(current_view.getContext(), ItemsActivity.class);
                    newIntent.putExtra("list_id", list_id);
                    //newIntent.putExtra("list_title",mList.get(position).getTitleName());
                    newIntent.putExtra("display_name", account.getDisplayName());
                    newIntent.putExtra("permission", permission);
                    current_view.getContext().startActivity(newIntent);
                }
                else {
                    endActivity();
                }
            }

            @Override
            public void DataIsDeleted() {

            }
        });
    }

    public void endActivity() {
        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
        i.putExtra("account", account);
        startActivity(i);
    }

    public void click_reject(View view) {
        Toast.makeText(JoinListActivity.this, "See you next time!", Toast.LENGTH_SHORT).show();
        endActivity();
    }

    public GoogleSignInAccount getAccount() {
        GoogleSignInAccount my_account = GoogleSignIn.getLastSignedInAccount(this);
        if(my_account == null) {
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);
        }
        return my_account;
    }
}
