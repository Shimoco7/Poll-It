package com.example.appproject.model;

import android.graphics.Bitmap;
import android.net.Uri;

import com.example.appproject.model.user.SaveImageListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class ModelFirebaseStorage {
    private final FirebaseStorage storage = FirebaseStorage.getInstance();

    public void saveImage(Bitmap bitMap, String imageName, SaveImageListener saveImageListener) {
        StorageReference storageRef = storage.getReference();
        StorageReference imgRef = storageRef.child("user_avatars/" + imageName);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitMap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imgRef.putBytes(data);
        uploadTask.addOnFailureListener(exception -> saveImageListener.onComplete(null))
                .addOnSuccessListener(taskSnapshot -> imgRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    Uri downloadUrl = uri;
                    saveImageListener.onComplete(downloadUrl.toString());
                }));
    }
}
