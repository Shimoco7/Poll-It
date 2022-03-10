package com.example.appproject.model;

import android.content.Context;
import android.util.Log;

import com.example.appproject.MyApplication;
import com.example.appproject.R;
import com.example.appproject.model.user.GetUsersListener;
import com.example.appproject.model.user.SaveUserListener;
import com.example.appproject.model.user.User;
import com.google.firebase.Timestamp;
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
                           if(!user.getUid().equals(MyApplication.getContext().getSharedPreferences("Status", Context.MODE_PRIVATE).getString("firebasekey", ""))){
                               list.add(user);
                           }
                       }
                   }
                   listener.onComplete(list);
                });
    }
}
