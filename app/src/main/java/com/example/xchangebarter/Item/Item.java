package com.example.xchangebarter.Item;

public class Item {
    String ID, name, description, tags, imgUrl, user, tradeID;

    public Item(){}

    public Item(String ID, String imgUrl,String name,String description,String tags, String user){
        this.ID = ID;
        this.imgUrl = imgUrl;
        this.name = name;
        this.description = description;
        this.tags = tags;
        this.user = user;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
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
