package com.buildproject.rebuy;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.buildproject.rebuy.Modules.ItemInList;
import com.buildproject.rebuy.Modules.ListOfItems;

import java.util.List;

public class ItemsActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //TODO get list id
        String list_id="";

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
    }
}
