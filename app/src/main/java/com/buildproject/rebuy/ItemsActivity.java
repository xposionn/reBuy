package com.buildproject.rebuy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.buildproject.rebuy.Modules.ItemInList;
import com.buildproject.rebuy.Modules.ListOfItems;

import java.util.List;
import java.util.Objects;

public class ItemsActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private String list_id;
    private ListOfItems.Permission permission;
    private String displayName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        list_id = Objects.requireNonNull(getIntent().getExtras()).getString("list_id");
        permission = (ListOfItems.Permission) getIntent().getExtras().getSerializable("permission");
        displayName = getIntent().getExtras().getString("display_name");
        setTitle(Objects.requireNonNull(getIntent().getExtras()).getString("list_title"));

        setContentView(R.layout.activity_items);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycleview_items);
        new FirebaseDBadapterItems(list_id).readItems(new FirebaseDBadapterItems.DataStatus() {
            @Override
            public void DataIsLoaded(List<ItemInList> items, List<String> keys) {
                new ItemsRecyclerView_Config().setConfig(mRecyclerView, ItemsActivity.this, items, keys, list_id,permission);
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
    
    public void moveToAddItemActivity(View v) {
        if (permission == ListOfItems.Permission.EDITOR || permission == ListOfItems.Permission.OWNER) {
            Intent i = new Intent(getApplicationContext(), AddItemActivity.class);
            i.putExtra("list_id", list_id);
            i.putExtra("display_name",displayName);
            startActivity(i);
        }else{
            Toast.makeText(this, "You have no permission to add a new Item", Toast.LENGTH_SHORT).show();
        }
    }

    public void editList(View view) {
        if (permission == ListOfItems.Permission.OWNER) {
            Intent i = new Intent(getApplicationContext(), EditListActivity.class);
            i.putExtra("list_id", list_id);
            startActivity(i);
        }else{
            Toast.makeText(this, "You have no permission to edit this list", Toast.LENGTH_SHORT).show();
        }
    }
}
