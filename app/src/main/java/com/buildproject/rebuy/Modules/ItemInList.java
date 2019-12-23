package com.buildproject.rebuy.Modules;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class ItemInList {

    public enum Priority {
        LOW, MID, HIGH;


    }

    public ItemInList(){

    }

    private String barcode = "";
    private String itemName;
    private User addedBy;
    private int quantity = 1;
    private Priority priority;
    private String notes;
    private Date addedTime;
    private boolean isBought = false;
    private ListOfItems listOfItems;


    public ItemInList(String itemName, User addedBy, ListOfItems listOfItem) {
        this.itemName = itemName;
        this.addedBy = addedBy;
        this.addedTime = new Date();
        this.priority = Priority.LOW;
        this.listOfItems = listOfItem;
        putOnDb();

    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setAddedBy(User addedBy) {
        this.addedBy = addedBy;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setAddedTime(Date addedTime) {
        this.addedTime = addedTime;
    }

    public void setBought(boolean bought) {
        isBought = bought;
    }

    public void setListOfItems(ListOfItems listOfItems) {
        this.listOfItems = listOfItems;
    }

    private void putOnDb() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("lists/"+listOfItems.getListId());
        myRef.setValue(this);
    }


    public String getBarcode() {
        return barcode;
    }

    public String getItemName() {
        return itemName;
    }

    public User getAddedBy() {
        return addedBy;
    }

    public int getQuantity() {
        return quantity;
    }

    public Priority getPriority() {
        return priority;
    }

    public String getNotes() {
        return notes;
    }

    public Date getAddedTime() {
        return addedTime;
    }

    public boolean isBought() {
        return isBought;
    }

    public ListOfItems getListOfItems() {
        return listOfItems;
    }

    /*
 ######    Constructor for barcode

    public ItemInList(Barcode barcode, User addedBy) {
        this.barcode = barcode;
        this.addedBy = addedBy;
        this.addedTime = new Date();
        //getnamefromDB
    }
*/

}
