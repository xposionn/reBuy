package com.buildproject.rebuy;

import android.widget.TextView;

import androidx.annotation.NonNull;

import com.buildproject.rebuy.Modules.ItemInList;
import com.buildproject.rebuy.Modules.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseDBadapterUsers {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReferenceItems;
    private List<String> users = new ArrayList<>();
    String list_id;

    public FirebaseDBadapterUsers() {
        mDatabase = FirebaseDatabase.getInstance();
    }

    public FirebaseDBadapterUsers(String list_id) {
        mDatabase = FirebaseDatabase.getInstance();
        this.list_id = list_id;
        mReferenceItems = mDatabase.getReference("lists").child(list_id);
    }



public interface DataStatus {
    void DataIsLoaded(List<String> lists, List<String> keys);

    void DataIsInserted();

    void DataIsUpdated();

    void DataIsDeleted();

}


    public void deleteItem(String path, String key, final DataStatus dataStatus) {
        mReferenceItems.child(path).child(key).setValue(null)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dataStatus.DataIsDeleted();
                    }
                });
    }

    public void addItem(ItemInList itemInList, final DataStatus dataStatus) {
        mReferenceItems = mReferenceItems.child("users");
        String key = mReferenceItems.push().getKey();
        mReferenceItems.child(key).setValue(itemInList)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dataStatus.DataIsInserted();
                    }
                });
    }


    public void readUsers(String path, final DataStatus dataStatus) {
        mReferenceItems.child(path).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                users.clear();
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    String user = keyNode.getValue(String.class);

                    users.add(user);
                }
                dataStatus.DataIsLoaded (users, keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void setNameByUesrID (String user_id, TextView textView) {
        DatabaseReference mReferenceUser = mDatabase.getReference("users").child(user_id);
        class UserListener implements ValueEventListener {

            private TextView textView;

            UserListener(TextView textView) {
                this.textView = textView;
            }

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                textView.setText(user.getFirstName() + " " + user.getLastName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        }
        mReferenceUser.addListenerForSingleValueEvent(new UserListener(textView));
    }
}
