package com.buildproject.rebuy.Modules;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ListOfItems {
    private String listId;
    private String titleName;
    private String owner; //id of owner, as
    private String priority;
    private List<String> editors;
    private List<String> viewers;
    private HashMap<String,ItemInList> items;


    public void setListId(String listId) {
        this.listId = listId;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public ListOfItems(){ //used to init from firebase db
        this.editors = new ArrayList<>();
        this.viewers = new ArrayList<>();
        this.items = new HashMap<>();
    }

    //Add viewer
    public void addViewer(String viewer){
    this.viewers.add(viewer);
    }

    //TODO:REMOVE THIS

    public void setOwner(String ownerName){
        owner = ownerName;
    }


    //Add editor
    public void addEditor(String editor){
        this.editors.add(editor);
    }

    public void addItem(String key,ItemInList item){
        this.items.put(key,item);
    }




    public List<String> getEditors() {
        return editors;
    }

    public List<String> getViewers() {
        return viewers;
    }

    public void setEditors(List<String> editors) {
        this.editors = editors;
    }

    public void setViewers(List<String> viewers) {
        this.viewers = viewers;
    }

    public HashMap<String, ItemInList> getItems() {
        return items;
    }

    public void setItems(HashMap<String, ItemInList> items) {
        this.items = items;
    }

    public String getOwner() {
        return owner;
    }

    public String getTitleName() {
        return titleName;
    }

    public String getPriority() {
        return priority;
    }

    public String getListId() {
        return listId;
    }
}
