package com.example.appproject.model.poll;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Map;
import java.util.UUID;

@Entity
public class Poll {

    @PrimaryKey
    @NonNull
    public String pollId;
    public String pollName;


    public Poll() { }

    @Ignore
    public Poll(String pollName) {
        this.pollId = UUID.randomUUID().toString();
        this.pollName = pollName;
    }

    @Ignore
    public Poll(@NonNull String pollId, String pollName) {
        this.pollId = pollId;
        this.pollName = pollName;
    }

    public static Poll create(Map<String, Object> data){
        String pollId = (String) data.get("poll_id");
        String pollName = (String) data.get("poll_name");
        assert pollId != null;
        assert pollName != null;

        return new Poll(pollId,pollName);
    }

    @NonNull
    public String getPollId() {
        return pollId;
    }

    public void setPollId(@NonNull String pollId) {
        this.pollId = pollId;
    }

    public String getPollName() {
        return pollName;
    }

    public void setPollName(String pollName) {
        this.pollName = pollName;
    }
}

