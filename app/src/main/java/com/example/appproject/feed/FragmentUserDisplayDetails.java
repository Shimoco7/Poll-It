package com.example.appproject.feed;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.appproject.R;
import com.example.appproject.model.Model;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

public class FragmentUserDisplayDetails extends Fragment {

    TextView userName;
    ShapeableImageView profilePic;

    public FragmentUserDisplayDetails() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_display_details, container, false);
        String userId = FragmentUserDisplayDetailsArgs.fromBundle(getArguments()).getUserUid();
        profilePic = view.findViewById(R.id.user_display_details_img_main);
        userName = view.findViewById(R.id.user_display_details_txt_username);
        Button backToFeedBtn = view.findViewById(R.id.feed_back_btn);

        Model.instance.getUserById(userId,user->{
            userName.setText(user.getName());
            if(user.getProfilePicUrl() != null){
                Model.instance.getMainThread().post(()->{
                    Picasso.get().load(user.getProfilePicUrl()).placeholder(R.drawable.avatar).into(profilePic);
                });
            }
            else{
                if(user.getGender()!=null){
                    if(user.getGender().equals("Female")){
                        profilePic.setImageResource(R.drawable.female_avatar);
                    }
                    else{
                        profilePic.setImageResource(R.drawable.avatar);
                    }
                }
                else{
                    profilePic.setImageResource(R.drawable.avatar);
                }
            }
        });
        backToFeedBtn.setOnClickListener(v->{
            Navigation.findNavController(v).navigateUp();
        });

        return view;
    }
}