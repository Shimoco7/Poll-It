package com.example.appproject;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.appproject.model.General;
import com.example.appproject.model.Model;
import com.example.appproject.model.poll.PollQuestion;
import com.example.appproject.poll.FragmentActivePollArgs;
import com.example.appproject.poll.PollQuestionViewModel;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


public class FragmentOtherUserPoll extends Fragment {

    MaterialTextView pollName, question1Tv, ans1Tv, question2Tv, ans2Tv, question3Tv, ans3Tv, imageTv;
    ShapeableImageView pollImage;
    String pollId, userId;
    ProgressBar progressBar;

    public FragmentOtherUserPoll() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_other_user_poll, container, false);
        pollName = view.findViewById(R.id.otherUserPoll_txt_pollName);
        question1Tv = view.findViewById(R.id.otherUserPoll_txt_Q1);
        question2Tv = view.findViewById(R.id.otherUserPoll_txt_Q2);
        question3Tv = view.findViewById(R.id.otherUserPoll_txt_Q3);
        ans1Tv = view.findViewById(R.id.otherUserPoll_txt_ans1);
        ans2Tv = view.findViewById(R.id.otherUserPoll_txt_ans2);
        ans3Tv = view.findViewById(R.id.otherUserPoll_txt_ans3);
        imageTv = view.findViewById(R.id.otherUserPoll_txt_imgQ);
        pollImage = view.findViewById(R.id.otheruser_img_factimg);
        progressBar = view.findViewById(R.id.otherUserPoll_progress_bar);
        progressBar.setVisibility(View.GONE);
        pollId = FragmentOtherUserPollArgs.fromBundle(getArguments()).getPollId();
        userId = FragmentOtherUserPollArgs.fromBundle(getArguments()).getUserId();

        Model.instance.getPollByPollId(pollId, poll -> {
            Model.instance.getMainThread().post(() -> pollName.setText(poll.getPollName()));
        });


        Model.instance.getAllAnswersByUserAndPollIds(MyApplication.getUserKey(), pollId, map -> {
            if (map != null) {
                if (!map.isEmpty()) {


                    Model.instance.getPollQuestionsFromLocalDb(pollId, list -> {
                        Model.instance.getMainThread().post(() -> {
                            for (PollQuestion pq : list) {
                                if (pq.getChoices() == null) {
                                    progressBar.setVisibility(View.VISIBLE);
                                    Picasso.get().load(map.get(pq.getPollQuestionId()).getAnswer()).into(pollImage, new Callback() {
                                        @Override
                                        public void onSuccess() {
                                            progressBar.setVisibility(View.GONE);
                                        }

                                        @Override
                                        public void onError(Exception e) {
                                            progressBar.setVisibility(View.GONE);
                                        }
                                    });
                                    imageTv.setText(pq.getPollQuestion());
                                    list.remove(pq);
                                    break;
                                }
                            }


                            ans1Tv.setText(map.get(list.get(0).getPollQuestionId()).getAnswer());
                            ans2Tv.setText(map.get(list.get(1).getPollQuestionId()).getAnswer());
                            ans3Tv.setText(map.get(list.get(2).getPollQuestionId()).getAnswer());
                            question1Tv.setText(list.get(0).getPollQuestion());
                            question2Tv.setText(list.get(1).getPollQuestion());
                            question3Tv.setText(list.get(2).getPollQuestion());
                        });
                    });


                }
            }
        });
        return view;
    }
}