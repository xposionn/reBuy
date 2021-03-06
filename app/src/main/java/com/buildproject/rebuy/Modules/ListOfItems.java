package com.buildproject.rebuy.Modules;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ListOfItems {
    private String listId;
    private String titleName;
    private String owner; //id of owner, as
    private String priority;
    private String addedTime;
    private List<String> editors;
    private List<String> viewers;
    private HashMap<String,ItemInList> items;
    private Permission permission = Permission.NOTHING;

    public enum Permission {
        OWNER, EDITOR, VIEWER, NOTHING;
    }

    public void setListId(String listId) {
        this.listId = listId;
    }

    public String getAddedTime() {
        return addedTime;
    }

    public void setAddedTime(String addedTime) {
        this.addedTime = addedTime;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    public static Permission string2Permission(String str) {
        if (str.equals("OWNER"))
            return Permission.OWNER;
        if (str.equals("EDITOR"))
            return Permission.EDITOR;
        if (str.equals("VIEWER"))
            return Permission.VIEWER;
        return Permission.NOTHING;
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

    public Permission getAccountPermission() { return permission;}

    public boolean isAllBought() {
        for(ItemInList item : items.values() ) {
            if (!item.isBought())
                return false;
        }
        return true;
    }
}
