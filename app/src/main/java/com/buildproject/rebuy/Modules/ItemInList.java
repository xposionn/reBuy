package com.buildproject.rebuy.Modules;

import com.google.firebase.Timestamp;

import java.io.Serializable;

public class ItemInList implements Serializable {

    public enum Priority {
        LOW, MID, HIGH;
    }

    public ItemInList(){
    }

    public ItemInList(String itemName,Priority priority) {
        this.itemName = itemName;
        this.priority = priority;
    }
    private String addedTime;
    private String barcode = "";
    private String itemName;
    private String userId;
    private int quantity = 1;
    private Priority priority = Priority.LOW;
    private String notes = "NO NOTES";
    private String addedBy;
    private boolean isBought = false;
    private String listId;

    public Priority getPriority() {
        return priority;
    }

    public String getAddedTime() {
        return addedTime;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public void setAddedTime(String addedTime) {
        this.addedTime = addedTime;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public boolean isBought() {
        return isBought;
    }

    public void setBought(boolean bought) {
        isBought = bought;
    }

    public String getListId() {
        return listId;
    }

    public void setListId(String listId) {
        this.listId = listId;
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
