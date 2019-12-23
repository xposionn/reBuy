package com.buildproject.rebuy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.buildproject.rebuy.Modules.ListOfItems;

import java.util.List;

public class ListsActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lists);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycleview_lists);
        new FirebaseDBadapter().readLists(new FirebaseDBadapter.DataStatus() {
            @Override
            public void DataIsLoaded(List<ListOfItems> lists, List<String> keys) {
                new RecyclerView_Config().setConfig(mRecyclerView, ListsActivity.this, lists,keys);
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
