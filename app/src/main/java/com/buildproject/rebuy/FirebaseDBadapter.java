package com.buildproject.rebuy;

import android.util.Log;

import androidx.annotation.NonNull;

import com.buildproject.rebuy.Modules.ItemInList;
import com.buildproject.rebuy.Modules.ListOfItems;
import com.buildproject.rebuy.Modules.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class FirebaseDBadapter {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReferenceLists;
    private DatabaseReference mReferenceUsers;
    private List<ListOfItems> lists = new ArrayList<>();

    public FirebaseDBadapter() {
        mDatabase = FirebaseDatabase.getInstance();
        mReferenceLists = mDatabase.getReference("lists");
        mReferenceUsers = mDatabase.getReference("users");
    }

    public interface DataStatus {
        void DataIsLoaded(List<ListOfItems> lists, List<String> keys);

        void DataIsInserted();

        void DataIsUpdated();

        void DataIsDeleted();
    }

    public void readLists(String id, final DataStatus dataStatus){
        class ValueEventListener_WithID implements ValueEventListener {
            String id;

            ValueEventListener_WithID(String _id) {
                super();
                id = _id;
            }

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lists.clear();
                List<String> keys = new ArrayList<>();
                for(DataSnapshot keyNode: dataSnapshot.getChildren()){
                    ListOfItems listOfItems = keyNode.getValue(ListOfItems.class);

                    boolean is_my_list = false;
                    assert listOfItems != null;
                    if (listOfItems.getOwner().equals(id)) {
                        is_my_list = true;
                        listOfItems.setPermission(ListOfItems.Permission.OWNER);
                    }
                    else if (listOfItems.getEditors().contains(id)) {
                        is_my_list = true;
                        listOfItems.setPermission(ListOfItems.Permission.EDITOR);
                    }
                    else if (listOfItems.getViewers().contains(id)) {
                        is_my_list = true;
                        listOfItems.setPermission(ListOfItems.Permission.VIEWER);
                    }
                    if (is_my_list) {
                        keys.add(keyNode.getKey());
                        lists.add(listOfItems);
                    }
                }
                dataStatus.DataIsLoaded(lists, keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        }

        mReferenceLists.addValueEventListener(new ValueEventListener_WithID(id));
    }



    public void addList(ListOfItems listOfItems, final DataStatus dataStatus) {

        String key = mReferenceLists.push().getKey();
        mReferenceLists.child(key).setValue(listOfItems)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dataStatus.DataIsInserted();
                    }
                });
    }
/*     //TODO: get User from DB by his ID
    public User getUser(String userId,User user){
        User user1;
        mReferenceUsers.child(userId);
        mReferenceUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.i(TAG, "onDataChange: this is the user: " + dataSnapshot.getValue(User.class));
                user1 = dataSnapshot.getValue(User.class)
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


 }*/
}
