package com.example.xchangebarter.Item;
/**
this class serves as a blueprint for creating Items with various properties and provides
methods to retrieve and update those properties.
 */
public class Item {
    String itemID, title, description, tags, image, user;
    boolean available, complete;

    public Item(){
        complete = false;
    }

    public Item(String itemID, String image,String title,String description,String tags, String user){
        this.itemID = itemID;
        this.image = image;
        this.title = title;
        this.description = description;
        this.tags = tags;
        this.user = user;
        available = true;
        complete = false;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUser(){ return user; }

    public void setUser(String user) { this.user = user; }

}
