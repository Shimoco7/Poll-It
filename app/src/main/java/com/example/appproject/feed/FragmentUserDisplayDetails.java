package com.example.appproject.feed;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.appproject.R;
import com.example.appproject.model.General;
import com.example.appproject.model.Model;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class FragmentUserDisplayDetails extends Fragment {

    TextView userName,email,education,gender,address;
    ShapeableImageView profilePic;
    UserDisplayDetailsViewModel viewModel;
    UserDisplayDetailsAdapter adapter;
    RecyclerView list;
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
        String userId = FragmentUserDisplayDetailsArgs.fromBundle(getArguments()).getUserUid();
        viewModel.setUserId(userId);
        profilePic = view.findViewById(R.id.user_display_details_img_main);
        userName = view.findViewById(R.id.user_display_details_txt_username);
        email=view.findViewById(R.id.user_display_details_txt_email);
        education=view.findViewById(R.id.user_display_details_txt_education);
        gender=view.findViewById(R.id.user_display_details_txt_gender);
        address=view.findViewById(R.id.user_display_details_txt_address);
        progressBar=view.findViewById(R.id.user_display_details_progress_bar);
        Button backToFeedBtn = view.findViewById(R.id.feed_back_btn);

        list = view.findViewById(R.id.user_display_details_rv);
        list.setHasFixedSize(true);
        int numOfColumns = 2;
        list.setLayoutManager(new GridLayoutManager(getContext(), numOfColumns,GridLayoutManager.VERTICAL,false));
        adapter = new UserDisplayDetailsAdapter(viewModel,getLayoutInflater());
        list.setAdapter(adapter);
        list.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.set(5,20,5,20);
            }
        });

        General.progressBarOn(getActivity(),container,progressBar);
        Model.instance.getUserWithPolls(userId,userWithPolls->{
            viewModel.setUserFilledPolls(userWithPolls);
            Model.instance.getUserById(userId,user->{
                userName.setText(user.getName());
                email.setText(user.getEmail());
                address.setText(user.getAddress());

                Model.instance.getUserDetailById(user.getUid(),"Education Level",edu->{
                    education.setText(edu.getAnswer());
                });

                gender.setText(user.getGender());

                if(user.getProfilePicUrl() != null){
                    Model.instance.getMainThread().post(()->{
                        Picasso.get().load(user.getProfilePicUrl()).placeholder(R.drawable.avatar).into(profilePic);
                    });
                }
                else{
                    if(user.getGender()!=null){
                        if(user.getGender().equals("Female")){
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
                General.progressBarOff(getActivity(),container,progressBar);
            });

        });

        backToFeedBtn.setOnClickListener(v->{
            Navigation.findNavController(v).navigateUp();
        });

        Model.instance.refreshPollsList();
        Model.instance.refreshList();

        return view;
    }
}