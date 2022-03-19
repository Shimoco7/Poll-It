package com.example.appproject.model.poll;

import java.util.Map;

public interface GetPollQuestionsWithAnswersListener {
    void onComplete(Map<String,Answer> map);
}
