package com.algonquincollege.lelesandroidlabs;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Entity
public class ChatMessage {
    @ColumnInfo(name="message")
    private String message;

    @ColumnInfo(name="TimeSent")
    private String timeSent;

    @ColumnInfo(name="SendOrReceive")
    private boolean SendOrReceive;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    public int id;

    public ChatMessage(String m, String t, boolean sent) {
        message = m;
        timeSent = t;
        SendOrReceive = sent;
    }

    // getter methods
    public String getMessage() {
        return message;
    }

    public String getTimeSent() {
        return timeSent;
    }

    public boolean SendOrReceive() {
        return SendOrReceive;
    }

}


