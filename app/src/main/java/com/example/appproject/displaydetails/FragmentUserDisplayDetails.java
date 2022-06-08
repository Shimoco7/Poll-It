package com.example.appproject.displaydetails;

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
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

public class FragmentUserDisplayDetails extends Fragment {

    TextView userName,email,education,gender,address;
    ShapeableImageView profilePic;
    UserDisplayDetailsViewModel viewModel;
    UserDisplayDetailsAdapter adapter;
    ProgressBar progressBar;
    Button editBtn;

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
        editBtn = view.findViewById(R.id.user_display_details_editDetails_btn);
        Button editPassBtn=view.findViewById(R.id.user_display_details_editPassword_btn);




        if(MyApplication.getFacebookId() != null && MyApplication.getFacebookId().length() > 0) {
            editPassBtn.setVisibility(View.GONE);
        }

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
            General.loadImage(MyApplication.getUserProfilePicUrl(),profilePic,R.drawable.loadimagebig);
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


        viewModel.isPassChanged.observe(getViewLifecycleOwner(),isPassChanged->{
            if(isPassChanged){
                passwordChangedSnackBar();
                Model.instance.setIsPassChanged(false);
            }
        });

        if(viewModel.getIsPassChanged().getValue() != null){
            if(viewModel.getIsPassChanged().getValue()){
                passwordChangedSnackBar();
            }
        }

        return view;
    }

    private void passwordChangedSnackBar(){
        Model.instance.getMainThread().post(()->{
            Snackbar.make(requireView(), "Password changed", 2000)
                    .setBackgroundTint(requireContext().getColor(R.color.primeGreen))
                    .setTextColor(requireContext().getColor(R.color.white))
                    .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_FADE)
//                    .setAnchorView(address)
                    .show();

        });
    }
}