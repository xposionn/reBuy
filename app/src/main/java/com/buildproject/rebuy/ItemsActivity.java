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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String list_id= Objects.requireNonNull(getIntent().getExtras()).getString("list_id");

        setContentView(R.layout.activity_items);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycleview_items);
        new FirebaseDBadapterItems(list_id).readItems(new FirebaseDBadapterItems.DataStatus() {
            @Override
            public void DataIsLoaded(List<ItemInList> items, List<String> keys) {
                new ItemsRecyclerView_Config().setConfig(mRecyclerView, ItemsActivity.this, items, keys);
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
        //Creates new Item and insert into DB
      /*  ItemInList item = new ItemInList();
        item.setItemName("itemname");
        item.setBought(false);

        new FirebaseDBadapterItems(list_id).addItem(item, new FirebaseDBadapterItems.DataStatus() {
            @Override
            public void DataIsLoaded(List<ItemInList> lists, List<String> keys) {

            }

            @Override
            public void DataIsInserted() {
                Toast.makeText(getApplicationContext(), "Item Added!", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void DataIsUpdated() {

            }

            @Override
            public void DataIsDeleted() {

            }
        });*/


    }


    public void moveToAddItemActivity(View v) {
        Intent i = new Intent(getApplicationContext(), EditItemActivity.class);
//        i.putExtra("list_id",)
        startActivity(i);
    }
}
