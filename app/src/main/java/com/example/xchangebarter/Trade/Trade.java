package com.example.xchangebarter.Trade;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Trade implements Parcelable{

    String tradeID;       // Unique ID of trade
    String receiveItem;   // ID of Item current user will receive in trade
    String giveItem;      // ID of Item current user will choose to give to other user.
    String currentUser;
    String otherUser;

    String place;       // Landmark for CSUSM to complete trade

    boolean fresh;   // Trade was just created by current user
    boolean incoming;   // Trade was sent to the current user by another user

    boolean accepted;   // Set to help send trade to completed trades
    boolean rejected;   // Set to help erase trade from trade block
    boolean countered;  // not sure if we need this yet, here just in case

    // Constructor leaving out item to be traded to other user, since it is unknown at start of trade
    public Trade(String received, String currentUser, String otherUser, boolean fresh, boolean incoming){
        setTradeID();
        receiveItem = received;
        this.currentUser = currentUser;
        this.otherUser = otherUser;
        this.fresh = fresh;
        this.incoming = incoming;
    }

    // Constructor for using with Parcel
    public Trade(Parcel in){
        tradeID = in.readString();
        receiveItem = in.readString();
        currentUser = in.readString();
        otherUser = in.readString();
        fresh = in.readInt() == 1;
        incoming = in.readInt() == 1;
        accepted = in.readInt() == 1;
        rejected = in.readInt() == 1;
        countered = in.readInt() == 1;
    }

    public String getTradeID() {
        return tradeID;
    }

    public void setTradeID() {
        Calendar calendar = Calendar.getInstance();

        //check date
        SimpleDateFormat currDate = new SimpleDateFormat("ddMMyyyy");
        String currentDate = currDate.format(calendar.getTime());

        //check time
        SimpleDateFormat currTime = new SimpleDateFormat("HHmmss");
        String currentTime = currTime.format(calendar.getTime());

        //primary key from date + time
        tradeID = currentDate + currentTime;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
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

    public String getReceiveItem() {
        return receiveItem;
    }

    public void setReceiveItem(String receiveItem) {
        this.receiveItem = receiveItem;
    }

    public String getGiveItem() {
        return giveItem;
    }

    public void setGiveItem(String giveItem) {
        this.giveItem = giveItem;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }

    public String getOtherUser() {
        return otherUser;
    }

    public void setOtherUser(String otherUser) {
        this.otherUser = otherUser;
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
        parcel.writeString(tradeID);
        parcel.writeString(receiveItem);
        parcel.writeString(currentUser);
        parcel.writeString(otherUser);
        parcel.writeInt(isFresh()? 1 : 0);
        parcel.writeInt(isIncoming()? 1 : 0);
        parcel.writeInt(isAccepted()? 1 : 0);
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
