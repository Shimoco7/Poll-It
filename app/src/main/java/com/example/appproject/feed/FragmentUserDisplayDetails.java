package com.example.appproject.feed;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.appproject.MyApplication;
import com.example.appproject.R;
import com.example.appproject.model.General;
import com.example.appproject.model.Model;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class FragmentUserDisplayDetails extends Fragment {

    TextView userName,email,education,gender,address;
    ShapeableImageView profilePic;
    UserDisplayDetailsViewModel viewModel;
    UserDisplayDetailsAdapter adapter;
        ProgressBar progressBar;

    public FragmentUserDisplayDetails() { }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(UserDisplayDetailsViewModel.class);
    }

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

        View view = inflater.inflate(R.layout.fragment_user_display_details, container, false);
        String userId = MyApplication.getUserKey();
        viewModel.setUserId(userId);
        profilePic = view.findViewById(R.id.user_display_details_img_main);
        userName = view.findViewById(R.id.user_display_details_txt_username);
        email=view.findViewById(R.id.user_display_details_txt_email);
        education=view.findViewById(R.id.user_display_details_txt_education);
        gender=view.findViewById(R.id.user_display_details_txt_gender);
        address=view.findViewById(R.id.user_display_details_txt_address);
        progressBar=view.findViewById(R.id.user_display_details_progress_bar);
        Button backBtn = view.findViewById(R.id.user_display_details_back_btn);
        Button editBtn = view.findViewById(R.id.user_display_details_editDetails_btn);
        Button editPassBtn=view.findViewById(R.id.user_display_details_editPassword_btn);


        adapter = new UserDisplayDetailsAdapter(viewModel,getLayoutInflater());
        General.progressBarOn(getActivity(),container,progressBar,false);
        userName.setText(MyApplication.getUserName());
        email.setText(MyApplication.getUserEmail());
        address.setText(MyApplication.getUserAddress());
        gender.setText(MyApplication.getGender());
        Model.instance.getUserDetailById(MyApplication.getUserKey(),"Education Level",edu->{
            education.setText(edu.getAnswer());
            General.progressBarOff(getActivity(),container,progressBar,true);
        });

        if(MyApplication.getUserProfilePicUrl() != null && MyApplication.getUserProfilePicUrl().length() >0){
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(chain -> {
                Request newRequest = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer " + MyApplication.getAccessToken())
                        .build();
                return chain.proceed(newRequest);
            }).build();
            Picasso picasso = new Picasso.Builder(MyApplication.getContext()).downloader(new OkHttp3Downloader(client)).build();
            picasso.load(MyApplication.getUserProfilePicUrl())
                    .placeholder(R.drawable.avatar)
                    .into(profilePic);
        }
        else{
            if(MyApplication.getGender()!=null){
                if(MyApplication.getGender().equals("Female")){
                    Model.instance.getMainThread().post(()->{
                        profilePic.setImageResource(R.drawable.female_avatar);
                    });
                }
                else{
                    Model.instance.getMainThread().post(()->{
                        profilePic.setImageResource(R.drawable.avatar);
                    });
                }
            }
            else{
                Model.instance.getMainThread().post(()->{
                    profilePic.setImageResource(R.drawable.avatar);
                });
            }
        }


        editBtn.setOnClickListener(Navigation.createNavigateOnClickListener(FragmentUserDisplayDetailsDirections.actionFragmentUserDisplayDetailsToFragmentUserDetails()));
        editPassBtn.setOnClickListener(Navigation.createNavigateOnClickListener(FragmentUserDisplayDetailsDirections.actionFragmentUserDisplayDetailsToFragmentChangePassword()));
        backBtn.setOnClickListener(v->{
            Navigation.findNavController(v).navigateUp();
        });

        return view;
    }
}