package com.example.xchangebarter.Item;

public class Item {
    String itemID, title, description, tags, image, user, tradeID;

    public Item(){}

    public Item(String itemID, String image,String title,String description,String tags, String user){
        this.itemID = itemID;
        this.image = image;
        this.title = title;
        this.description = description;
        this.tags = tags;
        this.user = user;
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

    public String getTradeID() {
        return tradeID;
    }

    public void setTradeID(String tradeID) {
        this.tradeID = tradeID;
    }
}
