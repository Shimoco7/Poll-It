package com.example.appproject.model;

import com.example.appproject.MyApplication;
import com.example.appproject.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

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
}
