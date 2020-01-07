package com.buildproject.rebuy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        list_id = Objects.requireNonNull(getIntent().getExtras()).getString("list_id");
        permission = ListOfItems.string2Permission(Objects.requireNonNull(getIntent().getExtras()).getString("permission"));
        setTitle(Objects.requireNonNull(getIntent().getExtras()).getString("list_title"));

        setContentView(R.layout.activity_items);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycleview_items);
        new FirebaseDBadapterItems(list_id).readItems(new FirebaseDBadapterItems.DataStatus() {
            @Override
            public void DataIsLoaded(List<ItemInList> items, List<String> keys) {
                new ItemsRecyclerView_Config().setConfig(mRecyclerView, ItemsActivity.this, items, keys, list_id);
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
            startActivity(i);
        }
    }
}
