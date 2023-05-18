package com.example.xchangebarter.Trade;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Trade implements Parcelable{

    String tradeID;       // Unique ID of trade
    String receiverItem;   // ID of Item initiator user will receive in trade
    String receiverItemTitle;   // Name of item being received
    String initiatorItem;      // ID of Item intitiator user will choose to give.
    String initiatorItemTitle;  // Name of item being sent
    String initiator;       // User email without @csusm of initiating user
    String receiver;        // User email without @csusm of receiving user
    String place;       // Landmark for CSUSM to complete trade
    String status;      // Display message so user knows what is happening with trade
    String iCompletion;   // Set to help complete a trade. Initiator has accepted.
    String rCompletion;   // Set to help complete a trade. Receiver has accepted
    boolean fresh;   // Trade was just created by current user
    boolean incoming;   // Trade was sent to the current user by another user
    boolean rejected;   // Set to help erase trade from trade block
    boolean countered;  // not sure if we need this yet, here just in case

    // Constructor leaving out item to be traded to other user, since it is unknown at start of trade
    public Trade(String receiverItem, String receiverItemTitle, String initiator, String receiver, boolean fresh, boolean incoming){
        this.receiverItem = receiverItem;
        this.receiverItemTitle = receiverItemTitle;
        this.initiator = initiator;
        this.receiver = receiver;
        place = "";
        status = "";
        iCompletion = "ongoing";
        rCompletion = "ongoing";
        this.fresh = fresh;
        this.incoming = incoming;
        rejected = false;
        countered = false;
    }

    // Default Constructor to set each field when retrieving from database
    public Trade(){
        tradeID = "";
        receiverItem = "";
        receiverItemTitle = "";
        initiatorItem = "";
        initiatorItemTitle = "";
        initiator = "";
        receiver = "";
        place = "";
        status = "";
        iCompletion = "ongoing";
        rCompletion = "ongoing";
        fresh = false;
        incoming = false;
        rejected = false;
        countered = false;
    }

    // Constructor for using with Parcel
    public Trade(Parcel in){
        tradeID = in.readString();
        receiverItem = in.readString();
        receiverItemTitle = in.readString();
        initiatorItem = in.readString();
        initiatorItemTitle = in.readString();
        initiator = in.readString();
        receiver = in.readString();
        place = in.readString();
        status = in.readString();
        iCompletion = in.readString();
        rCompletion = in.readString();
        fresh = in.readInt() == 1;
        incoming = in.readInt() == 1;
        rejected = in.readInt() == 1;
        countered = in.readInt() == 1;
    }

    public String getReceiverItemTitle() {
        return receiverItemTitle;
    }

    public void setReceiverItemTitle(String receiverItemTitle) {
        this.receiverItemTitle = receiverItemTitle;
    }

    public String getInitiatorItemTitle() {
        return initiatorItemTitle;
    }

    public void setInitiatorItemTitle(String initiatorItemTitle) {
        this.initiatorItemTitle = initiatorItemTitle;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTradeID(String tradeID){
        this.tradeID = tradeID;
    }
    public void setTradeID() {
        tradeID = receiverItem + initiatorItem;
    }
    public String getTradeID() {
        return tradeID;
    }

    public String getiCompletion() {
        return iCompletion;
    }

    public void setiCompletion(String iCompletion) {
        this.iCompletion = iCompletion;
    }

    public String getrCompletion() {
        return rCompletion;
    }

    public void setrCompletion(String rCompletion) {
        this.rCompletion = rCompletion;
    }

    public boolean isRejected() {
        return rejected;
    }

    public void setRejected(boolean rejected) {
        this.rejected = rejected;
    }

    public boolean isCountered() {
        return countered;
    }

    public void setCountered(boolean countered) {
        this.countered = countered;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getReceiverItem() {
        return receiverItem;
    }

    public void setReceiverItem(String receiverItem) {
        this.receiverItem = receiverItem;
    }

    public String getInitiatorItem() {
        return initiatorItem;
    }

    public void setInitiatorItem(String initiatorItem) {
        this.initiatorItem = initiatorItem;
    }

    public String getInitiator() {
        return initiator;
    }

    public void setInitiator(String initiator) {
        this.initiator = initiator;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public boolean isFresh() {
        return fresh;
    }

    public void setFresh(boolean fresh) {
        this.fresh = fresh;
    }

    public boolean isIncoming() {
        return incoming;
    }

    public void setIncoming(boolean incoming) {
        this.incoming = incoming;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(getTradeID());
        parcel.writeString(receiverItem);
        parcel.writeString(receiverItemTitle);
        parcel.writeString(initiatorItem);
        parcel.writeString(initiatorItemTitle);
        parcel.writeString(initiator);
        parcel.writeString(receiver);
        parcel.writeString(place);
        parcel.writeString(status);
        parcel.writeString(iCompletion);
        parcel.writeString(rCompletion);
        parcel.writeInt(isFresh()? 1 : 0);
        parcel.writeInt(isIncoming()? 1 : 0);
        parcel.writeInt(isRejected()? 1 : 0);
        parcel.writeInt(isCountered()? 1 : 0);
    }

    public static final Parcelable.Creator<Trade> CREATOR = new Parcelable.Creator<Trade>() {

        public Trade createFromParcel(Parcel in) {
            return new Trade(in);
        }

        public Trade[] newArray(int size) {
            return new Trade[size];
        }
    };
}
