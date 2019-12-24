package com.buildproject.rebuy.Modules;

public class ItemInList {

    public enum Priority {
        LOW, MID, HIGH;


    }

    public ItemInList(){

    }

    public ItemInList(String itemName,String priority) {
        this.itemName = itemName;
        this.priority = priority;
    }

    private String barcode = "";
    private String itemName;
    private String useId;
    private int quantity = 1;
    private String priority = "HAS NON";
    private String notes = "NO NOTES";
//    private Date addedTime;
    private boolean isBought = false;
    private String listId;


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

    public String getUseId() {
        return useId;
    }

    public void setUseId(String useId) {
        this.useId = useId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
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
