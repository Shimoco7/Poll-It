package com.example.appproject.model.poll;

import java.util.List;

public interface GetPollQuestionsListener {
    void onComplete(List<PollQuestion> list);
}
