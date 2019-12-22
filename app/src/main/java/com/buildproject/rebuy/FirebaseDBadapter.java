package com.buildproject.rebuy;

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

public class FirebaseDBadapter {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReferenceLists;
    private List<ListOfItems> lists = new ArrayList<>();

    public FirebaseDBadapter() {
        mDatabase = FirebaseDatabase.getInstance();
        mReferenceLists = mDatabase.getReference("lists");
    }
    public interface DataStatus{
        void DataIsLoaded(List<ListOfItems> lists, List<String> keys);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();

    }

    public void readLists(final DataStatus dataStatus){
        mReferenceLists.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lists.clear();
                List<String> keys = new ArrayList<>();
                for(DataSnapshot keyNode: dataSnapshot.getChildren()){
                    keys.add(keyNode.getKey());
                    ListOfItems listOfItems = keyNode.getValue(ListOfItems.class);
                    lists.add(listOfItems);
                }
                dataStatus.DataIsLoaded(lists, keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void addList(ListOfItems listOfItems,final DataStatus dataStatus){

        String key = mReferenceLists.push().getKey();
        mReferenceLists.child(key).setValue(listOfItems)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dataStatus.DataIsInserted();
                    }
                });
    }
}
