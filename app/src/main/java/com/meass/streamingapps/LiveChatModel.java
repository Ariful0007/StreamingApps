package com.meass.streamingapps;

public class LiveChatModel {
    String name,uid,picture,chanelname,time,message,fromwhom,userid;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getChanelname() {
        return chanelname;
    }

    public void setChanelname(String chanelname) {
        this.chanelname = chanelname;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFromwhom() {
        return fromwhom;
    }

    public void setFromwhom(String fromwhom) {
        this.fromwhom = fromwhom;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public LiveChatModel(String name, String uid,
                         String picture, String chanelname, String time, String message, String fromwhom, String userid) {
        this.name = name;
        this.uid = uid;
        this.picture = picture;
        this.chanelname = chanelname;
        this.time = time;
        this.message = message;
        this.fromwhom = fromwhom;
        this.userid = userid;
    }

    public LiveChatModel() {
    }
}
