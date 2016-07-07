package com.testcase.emuchat.model;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by AlexShredder on 06.07.2016.
 */
public class ChatItem implements Serializable{

    private String name, message;
    private long timeStamp;
    private boolean mine;

    public ChatItem(String name, String message, boolean mine) {
        this.name = name;
        this.message = message;
        this.mine = mine;
        timeStamp = Calendar.getInstance().getTimeInMillis();
    }

    public ChatItem(String name, String message, boolean mine, long timeStamp) {
        this.name = name;
        this.message = message;
        this.timeStamp = timeStamp;
        this.mine = mine;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setMine(boolean mine) {
        this.mine = mine;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public boolean isMine() {
        return mine;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChatItem chatItem = (ChatItem) o;

        if (timeStamp != chatItem.timeStamp) return false;
        if (mine != chatItem.mine) return false;
        if (name != null ? !name.equals(chatItem.name) : chatItem.name != null) return false;
        return message != null ? message.equals(chatItem.message) : chatItem.message == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (int) (timeStamp ^ (timeStamp >>> 32));
        result = 31 * result + (mine ? 1 : 0);
        return result;
    }
}
