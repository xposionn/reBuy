package com.buildproject.rebuy.Modules;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ListOfItems extends ArrayList {
    private String listId;
    private User owner;
    private List<String> editors;
    private List<String> viewers;
    private List<ItemInList> items;
    public ListOfItems(User owner) {
        this.listId = UUID.randomUUID().toString();
        this.owner = owner;
        this.editors = new ArrayList<>();
        this.viewers = new ArrayList<>();
        this.items = new ArrayList<>();
        putOnDb();
    }

    //Add viewer
    public void addViewer(User viewer){
    this.viewers.add(viewer.getUserId());
        getReference().child("viewers").setValue(viewer.getUserId());
    }

    //Add editor
    public void addEditor(User editor){
        this.editors.add(editor.getUserId());
        getReference().child("editors").setValue(editor.getUserId());
    }

    private DatabaseReference getReference(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        return database.getReference("lists/"+getListId());
    }


    //Init list on DB.
    private void putOnDb() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("lists/"+getListId());
        myRef.child("owner").setValue(getOwner().getUserId());
    }

    public List<String> getEditors() {
        return editors;
    }

    public List<String> getViewers() {
        return viewers;
    }

    public List<ItemInList> getItems() {
        return items;
    }

    public User getOwner() {
        return owner;
    }


    public String getListId() {
        return listId;
    }
}