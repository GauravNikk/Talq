package com.talq.calls;

public class CallerData {
String name,ava,channel_id,id,my_id;
boolean out;

    public boolean isOut() {
        return out;
    }

    public void setOut(boolean out) {
        this.out = out;
    }

    public CallerData() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAva() {
        return ava;
    }

    public void setAva(String ava) {
        this.ava = ava;
    }

    public String getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(String channel_id) {
        this.channel_id = channel_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMy_id() {
        return my_id;
    }

    public void setMy_id(String my_id) {
        this.my_id = my_id;
    }
}
