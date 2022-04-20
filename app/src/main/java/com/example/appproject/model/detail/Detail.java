package com.example.appproject.model.detail;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.appproject.MyApplication;
import com.google.gson.annotations.SerializedName;

import org.bson.types.ObjectId;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Entity
public class Detail {

    @PrimaryKey
    @NonNull
    @SerializedName("_id")
    public String detailId;
    @SerializedName("accountId")
    @NonNull
    public String userUid;
    @NonNull
    public String questionId;
    public String question;
    public String answer;

    public Detail() { }

    @Ignore
    public Detail(@NonNull String userUid,@NonNull String questionId,@NonNull String question, String answer) {
        this.userUid = userUid;
        this.detailId = new ObjectId().toString();
        this.questionId = questionId;
        this.question = question;
        this.answer = answer;
    }

    /**
     * Factory
     *
     */
    //TODO - remove
    public static Detail create(Map<String, Object> data) {
        String userId = (String)data.get("uid");
        String detailId = (String)data.get("detail_id");
        String questionId = (String)data.get("question_id");
        String question = (String)data.get("question");
        String answer = (String)data.get("answer");

        assert userId != null;
        assert answer != null;
        assert questionId != null;
        assert question != null;
        assert detailId != null;
        Detail detail = new Detail(userId,questionId, question,answer);
        detail.setDetailId(detailId);
        return detail;
    }

    public Map<String,String> toJson(){
        Map<String,String> json = new HashMap<>();
        json.put("_id", detailId);
        json.put("accountId", userUid);
        json.put("questionId", questionId);
        json.put("question",question);
        json.put("answer", answer);
        return json;
    }

    @NonNull
    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(@NonNull String userUid) {
        this.userUid = userUid;
    }
    @NonNull
    public String getDetailId() {
        return detailId;
    }

    public void setDetailId(@NonNull String detailId) {
        this.detailId = detailId;
    }
    @NonNull
    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }
    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

}
