package com.example.appproject.details;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.util.Log;
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
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class FragmentUserImage extends Fragment {

    Button finishBtn, backBtn;
    MaterialButton galleryBtn, camBtn;
    ProgressBar progressBar;
    ActivityResultLauncher<Uri> cameraActivityResultLauncher;
    ActivityResultLauncher<String> galleryActivityResultLauncher;
    ShapeableImageView userAvatar;
    Bitmap bitMap;
    Uri imageUri;

    public FragmentUserImage() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if(menu.findItem(R.id.main_menu_settings)!=null) {
            menu.findItem(R.id.main_menu_settings).setVisible(false);
            super.onPrepareOptionsMenu(menu);
        }
    }

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
                    Model.instance.getMainThread().post(()->userAvatar.setImageResource(R.drawable.female_avatar));
                } else {
                    Model.instance.getMainThread().post(()->userAvatar.setImageResource(R.drawable.avatar));
                }
            });
        } else {
            progressBar.setVisibility(View.VISIBLE);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(chain -> {
                Request newRequest = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer " + MyApplication.getAccessToken())
                        .build();
                return chain.proceed(newRequest);
            }).build();
            Picasso picasso = new Picasso.Builder(requireContext()).downloader(new OkHttp3Downloader(client)).build();
            picasso.load(MyApplication.getUserProfilePicUrl())
                    .placeholder(R.drawable.loadimagebig)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .into(userAvatar, new Callback() {
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
                new ActivityResultContracts.TakePicture(),
                result -> {
                    if(result){
                        try {
                            bitMap = MediaStore.Images.Media.getBitmap(this.getContext().getContentResolver(), imageUri);
                            userAvatar.setImageBitmap(bitMap);
                        } catch (IOException e) {
                            setUserAvatar();
                            Snackbar.make(requireView(),getString(R.string.storage_issue),Snackbar.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        setUserAvatar();
                    }
                }
        );

        galleryActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                result -> {
                    try {
                        if(result!=null){
                            bitMap = MediaStore.Images.Media.getBitmap(this.getContext().getContentResolver(), result);
                            userAvatar.setImageBitmap(bitMap);
                        }
                        else{
                            setUserAvatar();
                        }
                    } catch (IOException e) {
                        setUserAvatar();
                        Snackbar.make(requireView(),getString(R.string.storage_issue),Snackbar.LENGTH_SHORT).show();
                    }
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
        General.progressBarOn(getActivity(), container, progressBar,false);
        Model.instance.getAllDetails(MyApplication.getUserKey(), list -> {
            for (Detail d : list) {
                if(d.getQuestion().equals("Gender")){
                    Map<String,Object> map = new HashMap<>();
                    map.put("gender",d.getAnswer());
                    Model.instance.updateUser(MyApplication.getUserKey(), map, (user,message)->{
                        MyApplication.setGender(d.getAnswer());
                    });
                }
                Model.instance.saveDetailToRemoteDb(d, () -> {});
            }
            if (bitMap == null) {
                toNextScreen();
            } else {
                Model.instance.convertBitmapToFile(bitMap, file->{
                    if (file == null) {
                        Snackbar.make(requireView(),getString(R.string.image_upload_failed),Snackbar.LENGTH_SHORT).show();
                        General.progressBarOff(getActivity(), container, progressBar,true);
                    }
                    else{
                        Model.instance.saveImage(file,url->{
                            if(url == null){
                                Snackbar.make(requireView(),getString(R.string.image_upload_failed),Snackbar.LENGTH_INDEFINITE).setAction("Try Again Later",v->{
                                    progressBar.setVisibility(View.GONE);
                                    toNextScreen();
                                }).show();
                            }
                            else{
                                toNextScreen();
                            }
                        });
                    }
                });
            }
        });
    }

    private void openCam() {
        File imagePath = new File(getContext().getFilesDir(),".");
        File newFile = new File(imagePath, MyApplication.getUserKey()+".jpg");
        Log.d("TAG", newFile.getAbsolutePath());
        imageUri = FileProvider.getUriForFile(
                getContext(),
                "com.example.appproject.fileprovider",
                newFile);
        cameraActivityResultLauncher.launch(imageUri);
    }

    private void openGallery() {
        galleryActivityResultLauncher.launch("image/*");
    }

    private void toNextScreen() {
        if(requireActivity().getClass().getSimpleName().equals(MainActivity.class.getSimpleName())){
            Model.instance.setIsDetailsChanged(true);
            Navigation.findNavController(finishBtn).navigate(FragmentUserDetailsDirections.actionGlobalFragmentUserDisplayDetails());
        }
        else{
            Intent intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
    }
}