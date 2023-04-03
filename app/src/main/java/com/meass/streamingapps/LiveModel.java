package com.meass.streamingapps;

public class LiveModel {
    String name,image,chancelname,uid,roomID,time,audience;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getChancelname() {
        return chancelname;
    }

    public void setChancelname(String chancelname) {
        this.chancelname = chancelname;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAudience() {
        return audience;
    }

    public void setAudience(String audience) {
        this.audience = audience;
    }

    public LiveModel(String name, String image, String chancelname, String uid, String roomID, String time, String audience) {
        this.name = name;
        this.image = image;
        this.chancelname = chancelname;
        this.uid = uid;
        this.roomID = roomID;
        this.time = time;
        this.audience = audience;
    }

    public LiveModel() {
    }
}
