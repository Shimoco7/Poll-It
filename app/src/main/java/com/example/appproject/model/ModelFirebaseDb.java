package com.example.appproject.model;

import com.example.appproject.MyApplication;
import com.example.appproject.R;
import com.example.appproject.model.detail.Detail;
import com.example.appproject.model.detail.GetDetailsListener;
import com.example.appproject.model.detail.GetLocationsListener;
import com.example.appproject.model.detail.GetUserLocationListener;
import com.example.appproject.model.poll.GetPollQuestionsListener;
import com.example.appproject.model.poll.GetPollsListener;
import com.example.appproject.model.poll.Poll;
import com.example.appproject.model.poll.PollQuestion;
import com.example.appproject.model.question.GetQuestionsListener;
import com.example.appproject.model.detail.SaveDetailListener;
import com.example.appproject.model.question.Question;
import com.example.appproject.model.user.FinishRegistrationListener;
import com.example.appproject.model.user.GetUsersListener;
import com.example.appproject.model.user.SaveUserListener;
import com.example.appproject.model.user.User;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;

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

    //TODO: addOnFailureListener
    public void SaveUserOnDb(User user, SaveUserListener saveUserListener) {
        Map<String,Object> json = user.toJson();
        db.collection(MyApplication.getContext().getString(R.string.users_collection))
            .document(user.getUid())
            .set(json)
            .addOnSuccessListener(task->saveUserListener.onComplete());
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

    public void getUserById(String uid,GetUsersListener listener){
        db.collection(MyApplication.getContext().getString(R.string.users_collection))
                .whereEqualTo("uid",uid)
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



    public void SaveDetailOnDb(Detail detail, SaveDetailListener saveDetailListener) {
        Map<String, Object> json = detail.toJson();
        db.collection(MyApplication.getContext().getString(R.string.details_collection))
                .document(detail.getDetailId())
                .set(json)
                .addOnSuccessListener(task->saveDetailListener.onComplete());
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


    public void getQuestions(GetQuestionsListener listener){

        db.collection(MyApplication.getContext().getString(R.string.questions_collection))
                .get()
                .addOnCompleteListener(task -> {
                    List<Question> list = new ArrayList<>();
                    if(task.isSuccessful()){
                        for(QueryDocumentSnapshot doc : task.getResult()){
                            Question question = Question.create(doc.getData());
                            list.add(question);
                        }
                    }
                    listener.onComplete(list);
                });
    }

    public void updateUser(String userId, String key, String value, SaveUserListener saveUserListener) {
        DocumentReference docRef = db.collection(MyApplication.getContext().getString(R.string.users_collection)).document(userId);
        docRef.update(key,value)
                .addOnCompleteListener(l->saveUserListener.onComplete());
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

    public void isFinishedRegistration(FinishRegistrationListener listener) {
        db.collection(MyApplication.getContext().getString(R.string.details_collection))
                .whereEqualTo("uid",MyApplication.getUserKey())
                .get()
                .addOnCompleteListener(task->{
                    if(task.isSuccessful()){
                        listener.onComplete(!task.getResult().isEmpty());
                    }
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

}
