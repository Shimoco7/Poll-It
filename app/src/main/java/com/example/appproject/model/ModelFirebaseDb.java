package com.example.appproject.model;

import com.example.appproject.MyApplication;
import com.example.appproject.R;
import com.example.appproject.model.detail.Detail;
import com.example.appproject.model.detail.GetDetailsListener;
import com.example.appproject.model.detail.GetLocationsListener;
import com.example.appproject.model.detail.GetUserLocationListener;
import com.example.appproject.model.poll.Answer;
import com.example.appproject.model.poll.GetAnswersListener;
import com.example.appproject.model.poll.GetPollQuestionsListener;
import com.example.appproject.model.poll.GetPollsListener;
import com.example.appproject.model.poll.Poll;
import com.example.appproject.model.poll.PollQuestion;
import com.example.appproject.model.poll.SavePollAnswerListener;
import com.example.appproject.model.user.GetUsersListener;
import com.example.appproject.model.user.SaveUserListener;
import com.example.appproject.model.user.User;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
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


    public void getUsers(GetUsersListener listener, Long lastUpdateDate){
        db.collection(MyApplication.getContext().getString(R.string.users_collection))
                .whereGreaterThanOrEqualTo("update_date",new Timestamp(lastUpdateDate,0))
                .get()
                .addOnCompleteListener(task -> {
                   List<User> list = new ArrayList<>();
                   if(task.isSuccessful()){
                       for(QueryDocumentSnapshot doc : task.getResult()){
                           User user = User.create(doc.getData());
                           list.add(user);
                       }
                   }
                   listener.onComplete(list);
                });
    }



    public void getDetails(GetDetailsListener listener){
        db.collection(MyApplication.getContext().getString(R.string.details_collection))
                .whereEqualTo("uid", MyApplication.getUserKey())
                .get()
                .addOnCompleteListener(task -> {
                    List<Detail> list = new ArrayList<>();
                    if(task.isSuccessful()){
                        for(QueryDocumentSnapshot doc : task.getResult()){
                            Detail detail = Detail.create(doc.getData());
                            list.add(detail);
                        }
                    }
                    listener.onComplete(list);
                });
    }


    public void updateUpdateDateUser(String userId, SaveUserListener saveUserListener) {
        DocumentReference docRef = db.collection(MyApplication.getContext().getString(R.string.users_collection)).document(userId);
        docRef.update("update_date", FieldValue.serverTimestamp()).addOnCompleteListener(l->{
            saveUserListener.onComplete();
        });
    }

    public void getLocations(GetLocationsListener listener){
        db.collection(MyApplication.getContext().getString(R.string.users_collection))
                .whereNotEqualTo("uid", MyApplication.getUserKey())
                .get()
                .addOnCompleteListener(task -> {
                    List<String> list = new ArrayList<>();
                    if(task.isSuccessful()){
                        for(QueryDocumentSnapshot doc : task.getResult()){
                            String location = doc.getData().get("address").toString();
                            list.add(location);
                        }
                    }
                    listener.onComplete(list);
                });
    }

    public void getUserLocation(GetUserLocationListener listener){
        db.collection(MyApplication.getContext().getString(R.string.users_collection))
                .whereEqualTo("uid", MyApplication.getUserKey())
                .get()
                .addOnCompleteListener(task -> {
                    String userLocation = "";
                    if(task.isSuccessful()){
                        for(QueryDocumentSnapshot doc : task.getResult()){
                            userLocation = doc.getData().get("address").toString();
                        }
                    }
                    listener.onComplete(userLocation);
                });
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

    public void savePollAnswersOnDb(Map<String, Answer> pollMap, SavePollAnswerListener listener) {
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

    public void getUsersWithDetails(List<String> listUserIds, GetDetailsListener listener) {
        if(!listUserIds.isEmpty()){
            db.collection(MyApplication.getContext().getString(R.string.details_collection))
                    .whereIn("uid",listUserIds)
                    .get()
                    .addOnCompleteListener(task->{
                        List<Detail> details = new ArrayList<>();
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot doc : task.getResult()){
                                Detail detail = Detail.create(doc.getData());
                                details.add(detail);
                            }
                            listener.onComplete(details);
                        }
                    });
        }
    }

    public void getPollQuestionsAnswers(List<String> listUserIds, GetAnswersListener listener) {
        if(!listUserIds.isEmpty()){
            db.collection(MyApplication.getContext().getString(R.string.answers_collection))
                    .whereIn("user_id",listUserIds)
                    .get()
                    .addOnCompleteListener(task->{
                        List<Answer> answers = new ArrayList<>();
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot doc : task.getResult()){
                                Answer answer = Answer.create(doc.getData());
                                answers.add(answer);
                            }
                            listener.onComplete(answers);
                        }
                    });
        }
    }

}
