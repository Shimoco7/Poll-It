package com.example.appproject.details;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.FileProvider;
import androidx.exifinterface.media.ExifInterface;
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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

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
            OkHttpClient client;
            if(MyApplication.getUserProfilePicUrl().contains("poll-it.cs.colman.ac.il") || MyApplication.getUserProfilePicUrl().contains("10.10.248.124")){
                client = General.getOkClientWithAuth();
            }
            else{
                client = General.getOkHttpClient();
            }
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
                    Model.instance.getUserDetailById(MyApplication.getUserKey(), "Gender", gender -> {
                        if (gender.getAnswer().equals("Female")) {
                            Model.instance.getMainThread().post(()->userAvatar.setImageResource(R.drawable.female_avatar));
                        } else {
                            Model.instance.getMainThread().post(()->userAvatar.setImageResource(R.drawable.avatar));
                        }
                    });
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
                            ExifInterface ei = new ExifInterface(requireContext().getContentResolver().openInputStream(imageUri));
                            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                                    ExifInterface.ORIENTATION_UNDEFINED);
                            Bitmap rotatedBitMap = getRotatedBitMap(bitMap,orientation);
                            if(rotatedBitMap != null){
                                userAvatar.setImageBitmap(rotatedBitMap);
                            }
                            else{
                                setUserAvatar();
                            }
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
                            ExifInterface ei = new ExifInterface(requireContext().getContentResolver().openInputStream(result));
                            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                                    ExifInterface.ORIENTATION_UNDEFINED);
                            Bitmap rotatedBitMap = getRotatedBitMap(bitMap,orientation);
                            if(rotatedBitMap != null){
                                userAvatar.setImageBitmap(rotatedBitMap);
                            }
                            else{
                                setUserAvatar();
                            }
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
        AtomicBoolean isChangedDetails = new AtomicBoolean(false);
        General.progressBarOn(getActivity(), container, progressBar,false);
        Model.instance.getAllDetailsFromLocalDb(MyApplication.getUserKey(), list -> {
            Set<String> details = new HashSet<>();
            for (Detail d : list) {
                details.add(d.getAnswer());
            }
            Model.instance.getAllDetailsFromRemoteDb(MyApplication.getUserKey(), remoteDbDetails->{
                for(Detail det : remoteDbDetails){
                    details.remove(det.getAnswer());
                }
                if(!details.isEmpty()){
                    isChangedDetails.set(true);
                    for (Detail d : list){
                        if(d.getQuestion().equals("Gender")){
                            Map<String,Object> map = new HashMap<>();
                            map.put("gender",d.getAnswer());
                            Model.instance.updateUser(MyApplication.getUserKey(), map, (user,message)->{
                                MyApplication.setGender(d.getAnswer());
                            });
                        }
                        Model.instance.saveDetailToRemoteDb(d, () -> {});
                    }
                }
                if (bitMap == null) {
                    toNextScreen(isChangedDetails.get());
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
                                        toNextScreen(isChangedDetails.get());
                                    }).show();
                                }
                                else{
                                    toNextScreen(isChangedDetails.get());
                                }
                            });
                        }
                    });
                }
            });
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

    private void toNextScreen(boolean isChangedDetails) {
        if(requireActivity().getClass().getSimpleName().equals(MainActivity.class.getSimpleName())){
            Model.instance.setIsDetailsChanged(isChangedDetails);
            Navigation.findNavController(finishBtn).navigate(FragmentUserDetailsDirections.actionGlobalFragmentUserDisplayDetails());
        }
        else{
            Intent intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
    }

    private Bitmap getRotatedBitMap(Bitmap bitmap, int orientation){
        Bitmap rotatedBitmap = null;
        switch(orientation) {

            case ExifInterface.ORIENTATION_ROTATE_90:
                rotatedBitmap = rotateImage(bitmap, 90);
                break;

            case ExifInterface.ORIENTATION_ROTATE_180:
                rotatedBitmap = rotateImage(bitmap, 180);
                break;

            case ExifInterface.ORIENTATION_ROTATE_270:
                rotatedBitmap = rotateImage(bitmap, 270);
                break;

            case ExifInterface.ORIENTATION_NORMAL:
            default:
                rotatedBitmap = bitmap;
        }
        return rotatedBitmap;
    }

    private Bitmap rotateImage(Bitmap bitmap, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(),
                matrix, true);
    }
}