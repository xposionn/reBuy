package com.buildproject.rebuy.Services;

import androidx.annotation.NonNull;

import com.buildproject.rebuy.Modules.ItemInList;
import com.buildproject.rebuy.Modules.ListOfItems;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseDBadapterItems {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReferenceItems;
    private List<ItemInList> items = new ArrayList<>();
    String list_id;

    public FirebaseDBadapterItems(String list_id) {
        mDatabase = FirebaseDatabase.getInstance();
        this.list_id = list_id;
        mReferenceItems = mDatabase.getReference("lists").child(list_id);
    }

    public void editItem(String itemKey, ItemInList current_item, final DataStatus dataStatus) {
        mReferenceItems = mReferenceItems.child("items").child(itemKey);
        mReferenceItems.setValue(current_item)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dataStatus.DataIsUpdated();
                    }});
    }


public interface DataStatus {
    void DataIsLoaded(List<ItemInList> lists, List<String> keys);

    void DataIsInserted();

    void DataIsUpdated();

    void DataIsDeleted();

}

    public void updateItem(String itemId, String attribute, Object value, final DataStatus dataStatus) {
        mReferenceItems.child("items").child(itemId).child(attribute).setValue(value)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dataStatus.DataIsUpdated();
                    }
                });
    }

    public void deleteItem(String key, final DataStatus dataStatus) {
        mReferenceItems.child("items").child(key).setValue(null)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dataStatus.DataIsDeleted();
                    }
                });
    }

    public void addItem(ItemInList itemInList, final DataStatus dataStatus) {
        mReferenceItems = mReferenceItems.child("items");
        String key = mReferenceItems.push().getKey();
        mReferenceItems.child(key).setValue(itemInList)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dataStatus.DataIsInserted();
                    }
                });
    }


    public void readItems(final DataStatus dataStatus) {
        mReferenceItems.child("items").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                items.clear();
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    ItemInList itemInList = keyNode.getValue(ItemInList.class);
                    items.add(itemInList);
                }
                dataStatus.DataIsLoaded(items, keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
