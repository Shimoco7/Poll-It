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

public class FragmentUserDisplayDetails extends Fragment {

    TextView userName;
    public FragmentUserDisplayDetails() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_display_details, container, false);
        String userId = FragmentUserDisplayDetailsArgs.fromBundle(getArguments()).getUserUid();

        userName = view.findViewById(R.id.user_display_details_txt_username);
        Button backToFeedBtn = view.findViewById(R.id.user_display_back_btn);

        Model.instance.getUserById(userId,user->{
            userName.setText(user.getEmail());
        });
        backToFeedBtn.setOnClickListener(v->{
            Navigation.findNavController(v).navigateUp();
        });

        return view;
    }
}