package com.example.appproject.poll;

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

import com.example.appproject.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;

import java.io.IOException;


public class FragmentPollImage extends Fragment {

    MaterialButton backBtn;
    MaterialButton uploadBtn;
    ActivityResultLauncher<String> galleryActivityResultLauncher;
    ShapeableImageView image;
    Bitmap bitMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.main_menu_settings).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_poll_image, container, false);
        backBtn = view.findViewById(R.id.imgpoll_btn_back);
        backBtn.setOnClickListener(v->{
            Navigation.findNavController(v).navigateUp();
        });
        image=view.findViewById(R.id.imgpoll_image);
        galleryActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                result -> {
                    try {
                        if (result!=null)
                            bitMap = MediaStore.Images.Media.getBitmap(this.getContext().getContentResolver(), result);
                    } catch (IOException ignored) {
                    }
                    if(result!=null)
                        image.setImageBitmap(bitMap);
                }
        );
        uploadBtn=view.findViewById(R.id.imgpoll_btn_upload);
        uploadBtn.setOnClickListener(v->{
            openGallery();
        });
        return view;
    }


    private void openGallery() {
        galleryActivityResultLauncher.launch("image/*");
    }
}