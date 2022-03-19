package com.example.appproject.details;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.appproject.MainActivity;
import com.example.appproject.MyApplication;
import com.example.appproject.R;
import com.example.appproject.model.General;
import com.example.appproject.model.Model;
import com.example.appproject.model.detail.Detail;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class FragmentUserImage extends Fragment {

    Button finishBtn, backBtn;
    MaterialButton galleryBtn, camBtn;
    ProgressBar progressBar;
    ActivityResultLauncher<Void> cameraActivityResultLauncher;
    ActivityResultLauncher<String> galleryActivityResultLauncher;
    ShapeableImageView userAvatar;
    Bitmap bitMap;

    public FragmentUserImage() {
    }
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setHasOptionsMenu(true);
//    }
//    @Override
//    public void onPrepareOptionsMenu(Menu menu) {
//        menu.findItem(R.id.main_menu_settings).setVisible(false);
//        super.onPrepareOptionsMenu(menu);
//    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_image, container, false);
        finishBtn = view.findViewById(R.id.userImg_finish_btn);
        backBtn = view.findViewById(R.id.userImg_btn_back);
        camBtn = view.findViewById(R.id.userImg_btn_upload_cam);
        galleryBtn = view.findViewById(R.id.userImg_btn_upload_gallery);
        userAvatar = view.findViewById(R.id.userImg_img_user);
        progressBar = view.findViewById(R.id.userImg_progress_bar);
        progressBar.setVisibility(View.GONE);
        setUserAvatar();
        setListeners(container);

        return view;
    }


    private void setUserAvatar() {
        if (MyApplication.getUserProfilePicUrl().equals("")) {
            Model.instance.getUserDetailById(MyApplication.getUserKey(), "Gender", gender -> {
                if (gender.getAnswer().equals("Female")) {
                    userAvatar.setImageResource(R.drawable.female_avatar);
                } else {
                    userAvatar.setImageResource(R.drawable.avatar);
                }
            });
        } else {
            progressBar.setVisibility(View.VISIBLE);
            Picasso.get().load(MyApplication.getUserProfilePicUrl()).into(userAvatar, new Callback() {
                @Override
                public void onSuccess() {
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onError(Exception e) {
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
    }

    private void setListeners(ViewGroup container) {
        camBtn.setOnClickListener(v -> {
            openCam();
        });

        cameraActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.TakePicturePreview(),
                result -> {
                    if (result != null) {
                        bitMap = result;
                        userAvatar.setImageBitmap(bitMap);
                    }
                }
        );

        galleryActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                result -> {
                    try {
                        bitMap = MediaStore.Images.Media.getBitmap(this.getContext().getContentResolver(), result);
                    } catch (IOException ignored) {
                    }
                    userAvatar.setImageBitmap(bitMap);
                }
        );

        galleryBtn.setOnClickListener(v -> {
            openGallery();
        });

        finishBtn.setOnClickListener(v -> {
            finish(container);
        });

        backBtn.setOnClickListener(v -> {
            Navigation.findNavController(v).navigateUp();
        });
    }

    private void finish(ViewGroup container) {
        General.progressBarOn(getActivity(), container, progressBar);
        Model.instance.getAllDetails(MyApplication.getUserKey(), list -> {
            for (Detail d : list) {
                if (d.getQuestion().equals("Gender")) {
                    Model.instance.updateUser(d.getUserUid(), "gender", d.getAnswer(), () -> {
                    });
                }
                Model.instance.saveDetailOnRemoteDb(d, () -> {
                });
            }
            if (bitMap == null) {
                toMainActivity();
            } else {
                Model.instance.saveImage(bitMap, MyApplication.getUserKey() + ".jpg", url -> {
                    if (url == null) {
                        General.showToast(getActivity(), new ArrayList<>(Collections.singletonList(getString(R.string.image_upload_failed))));
                        General.progressBarOff(getActivity(), container, progressBar);
                    } else {
                        Model.instance.updateUser(MyApplication.getUserKey(), "profile_pic_url", url, this::toMainActivity);
                        MyApplication.setUserProfilePicUrl(url);
                    }
                });
            }
        });
    }

    private void openCam() {
        cameraActivityResultLauncher.launch(null);
    }

    private void openGallery() {
        galleryActivityResultLauncher.launch("image/*");
    }

    private void toMainActivity() {
        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}