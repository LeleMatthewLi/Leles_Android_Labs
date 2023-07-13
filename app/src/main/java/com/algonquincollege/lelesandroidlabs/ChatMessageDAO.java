package com.algonquincollege.lelesandroidlabs;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

public interface ChatMessageDAO {

    @Insert
    public long insertMessage(ChatMessage m);

    @Query("Select * from ChatMessage")
    List<ChatMessage> getAllMessages();

    @Delete
    void deleteMessage(ChatMessage m);
}
