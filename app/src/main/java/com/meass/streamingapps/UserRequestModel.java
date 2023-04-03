package com.meass.streamingapps;

public class UserRequestModel {
    String name,image,chancelname,uid,roomID,time,audience,requestuseruid,status;

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

    public String getRequestuseruid() {
        return requestuseruid;
    }

    public void setRequestuseruid(String requestuseruid) {
        this.requestuseruid = requestuseruid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public UserRequestModel(String name, String image, String chancelname, String uid, String roomID,
                            String time, String audience, String requestuseruid, String status) {
        this.name = name;
        this.image = image;
        this.chancelname = chancelname;
        this.uid = uid;
        this.roomID = roomID;
        this.time = time;
        this.audience = audience;
        this.requestuseruid = requestuseruid;
        this.status = status;
    }

    public UserRequestModel() {
    }
}
