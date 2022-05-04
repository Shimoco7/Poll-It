package com.example.appproject.poll;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.appproject.R;
import com.example.appproject.feed.FragmentUserDisplayDetailsArgs;
import com.example.appproject.model.Model;
import com.example.appproject.model.poll.PollQuestion;

public class FragmentActivePoll extends Fragment {

    Button startBtn;
    public FragmentActivePoll() {
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
        View view=inflater.inflate(R.layout.fragment_active_poll,container,false);
        startBtn=view.findViewById(R.id.activePoll_start_btn);
        String pollId = FragmentActivePollArgs.fromBundle(getArguments()).getPollId();
        Model.instance.getPollQuestionByNumber(pollId,1, pollQuestion->
                setStartBtnListenerByPollQuestionType(pollId,pollQuestion));
        return view;
    }

    private void setStartBtnListenerByPollQuestionType(String pollId, PollQuestion pollQuestion) {
        switch (pollQuestion.getPollQuestionType()){
            case Multi_Choice:{
                startBtn.setOnClickListener
                        (Navigation.createNavigateOnClickListener
                                (FragmentActivePollDirections.actionFragmentActivePollToFragmentPollQuestion
                                        (pollId,pollQuestion.getPollQuestionId(),false)));
                break;  
            }
            case Image_Question:{
                startBtn.setOnClickListener
                        (Navigation.createNavigateOnClickListener
                                (FragmentActivePollDirections.actionFragmentActivePollToFragmentPollQuestion
                                        (pollId,pollQuestion.getPollQuestionId(),true)));
                break;
            }
            case Image_Answers:{
                startBtn.setOnClickListener
                        (Navigation.createNavigateOnClickListener
                                (FragmentActivePollDirections.actionFragmentActivePollToFragmentPollQuestionImageAnswers
                                        (pollId,pollQuestion.getPollQuestionId())));
                break;
            }
            default:
                throw new IllegalStateException("Unexpected value: " + pollQuestion.getPollQuestionType());
        }
    }
}