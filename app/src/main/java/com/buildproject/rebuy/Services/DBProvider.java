package com.buildproject.rebuy.Services;

import androidx.annotation.NonNull;

import com.buildproject.rebuy.Modules.ItemInList;
import com.buildproject.rebuy.Modules.ListOfItems;
import com.buildproject.rebuy.Modules.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class DBProvider {

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private ListOfItems listOfItems;
    private User user;
    private static DBProvider dbProvider;

    public static DBProvider getInstance() {
        if (dbProvider==null)
            dbProvider = new DBProvider();
        return dbProvider;
    }

    private DBProvider(){
        mDatabase = FirebaseDatabase.getInstance();
        mReference=mDatabase.getReference();
    }

    public ListOfItems getListByID(String list_id){
        mReference.child("lists").child(list_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ListOfItems list = dataSnapshot.getValue(ListOfItems.class);
                System.out.println("loaded getlistbyid");
                listOfItems = list;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return listOfItems;

    }

}
