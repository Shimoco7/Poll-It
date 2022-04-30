package com.example.appproject.poll;

import com.google.gson.annotations.SerializedName;

public enum PollQuestionType{
    @SerializedName("Multi Choice")
    Multi_Choice,
    @SerializedName("Image Answers")
    Image_Answers,
    @SerializedName("Image Question")
    Image_Question
}
