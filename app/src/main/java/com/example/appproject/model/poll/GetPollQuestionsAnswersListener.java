package com.example.appproject.model.poll;

import java.util.List;

public interface GetPollQuestionsAnswersListener {
    void onComplete(List<Answer> list);
}
