package com.example.appproject.model;

import com.example.appproject.MyApplication;
import com.example.appproject.R;
import com.example.appproject.model.listeners.VoidListener;
import com.example.appproject.model.poll.Answer;
import com.example.appproject.model.listeners.GetPollQuestionsListener;
import com.example.appproject.model.listeners.GetPollsListener;
import com.example.appproject.model.poll.Poll;
import com.example.appproject.model.poll.PollQuestion;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ModelFirebaseDb {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public ModelFirebaseDb() {
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build();
        db.setFirestoreSettings(settings);
    }


    public void getPolls(GetPollsListener listener) {
        db.collection(MyApplication.getContext().getString(R.string.polls_collection))
                .get()
                .addOnCompleteListener(task -> {
                    List<Poll> list = new ArrayList<>();
                    if(task.isSuccessful()){
                        for(QueryDocumentSnapshot doc : task.getResult()){
                            Poll poll = Poll.create(doc.getData());
                            list.add(poll);
                        }
                    }
                    listener.onComplete(list);
                });
    }

    public void getPollQuestionsById(String pollId, GetPollQuestionsListener listener) {
        db.collection(MyApplication.getContext().getString(R.string.polls_questions_collection))
                .get()
                .addOnCompleteListener(task -> {
                    List<PollQuestion> list = new ArrayList<>();
                    if(task.isSuccessful()){
                        for(QueryDocumentSnapshot doc : task.getResult()){
                            PollQuestion pollQuestion = PollQuestion.create(doc.getData());
                            list.add(pollQuestion);
                        }
                    }
                    listener.onComplete(list);
                });
    }

    public void savePollAnswersOnDb(Map<String, Answer> pollMap, VoidListener listener) {
        WriteBatch batch = db.batch();
        for(Map.Entry<String,Answer> entry : pollMap.entrySet()){
            Answer answer = entry.getValue();
            DocumentReference ansRef = db.collection(MyApplication.getContext().getString(R.string.answers_collection)).document(answer.getAnswerId());
            batch.set(ansRef,answer.toJson());
        }
        batch.commit().addOnCompleteListener(task -> {
           listener.onComplete();
        });
    }


}
