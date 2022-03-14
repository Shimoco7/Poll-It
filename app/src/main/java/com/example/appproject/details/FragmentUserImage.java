package com.example.appproject.details;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.example.appproject.MainActivity;
import com.example.appproject.MyApplication;
import com.example.appproject.R;
import com.example.appproject.model.General;
import com.example.appproject.model.Model;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class FragmentUserImage extends Fragment {

    Button finishBtn;
    ImageButton camBtn;
    MaterialButton galleryBtn;
    ProgressBar progressBar;
    ActivityResultLauncher<Void> cameraActivityResultLauncher;
    ActivityResultLauncher<String> galleryActivityResultLauncher;
    ShapeableImageView userAvatar;
    Bitmap bitMap;

    public FragmentUserImage() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_image, container, false);
        finishBtn = view.findViewById(R.id.userImg_finish_btn);
        camBtn = view.findViewById(R.id.userImg_btn_upload_cam);
        galleryBtn = view.findViewById(R.id.userImg_btn_upload_gallery);
        userAvatar = view.findViewById(R.id.userImg_img_user);
        setUserAvatar();
        progressBar = view.findViewById(R.id.userImg_progress_bar);
        progressBar.setVisibility(View.GONE);
        setListeners(container);

        return view;
    }

    //Todo - set user's avatar based on their gender
    private void setUserAvatar() {

    }

    private void setListeners(ViewGroup container) {
        camBtn.setOnClickListener(v -> {
            openCam();
        });

        cameraActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.TakePicturePreview(),
                result -> {
                    if(result!=null){
                        bitMap = result;
                        userAvatar.setImageBitmap(bitMap);
                    }
                }
        );

        galleryActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                result -> {
                    try {
                        bitMap = MediaStore.Images.Media.getBitmap(this.getContext().getContentResolver(),result);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    userAvatar.setImageBitmap(bitMap);
                }
        );

        galleryBtn.setOnClickListener(v -> {
            openGallery();
        });

        finishBtn.setOnClickListener(v->{
            finish(container);
        });
    }

    //Todo - Save to storage and Url to DB
    private void finish(ViewGroup container) {
        General.progressBarOn(getActivity(),container,progressBar);
        if(bitMap == null){
            toMainActivity();
        }
        else{
            Model.instance.saveImage(bitMap,MyApplication.getUserKey()+".jpg",url->{
                if(url == null){
                    General.showToast(getActivity(),new ArrayList<>(Collections.singletonList(getString(R.string.image_upload_failed))));
                    General.progressBarOff(getActivity(),container,progressBar);
                }
                else{
                    Model.instance.updateUser(MyApplication.getUserKey(),"profile_pic_url",url, this::toMainActivity);
                }
            });
        }
    }

    private void openCam() {
        cameraActivityResultLauncher.launch(null);
    }

    private void openGallery() {
        galleryActivityResultLauncher.launch("image/*");
    }

    private void toMainActivity(){
        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}