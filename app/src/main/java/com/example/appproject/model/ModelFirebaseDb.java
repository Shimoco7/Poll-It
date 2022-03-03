package com.example.appproject.model;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

public class ModelFirebaseDb {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public ModelFirebaseDb() {
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build();
        db.setFirestoreSettings(settings);
    }

}
