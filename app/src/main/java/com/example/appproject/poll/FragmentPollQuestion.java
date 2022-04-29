package com.example.appproject.poll;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.appproject.MyApplication;
import com.example.appproject.R;
import com.example.appproject.model.General;
import com.example.appproject.model.Model;
import com.example.appproject.model.poll.Answer;
import com.example.appproject.model.poll.PollQuestion;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import java.util.Objects;
import java.util.UUID;


public class FragmentPollQuestion extends Fragment {

    PollQuestionViewModel viewModel;
    String pollId;
    int numOfQuestions;
    TextView questionTitle;
    TextView page;
    MaterialButton nextBtn, prevBtn;
    ProgressBar progressBar;
    ViewGroup container;


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

    public FragmentPollQuestion() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(PollQuestionViewModel.class);
        pollId = FragmentActivePollArgs.fromBundle(getArguments()).getPollId();
        Model.instance.getPollQuestionsFromLocalDb(pollId,list->{
            viewModel.setPollQuestions(list);
            numOfQuestions = list.size();
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_poll_question, container, false);
        this.container = container;
        pollId = FragmentActivePollArgs.fromBundle(getArguments()).getPollId();
        nextBtn= view.findViewById(R.id.poll_btn_right);
        prevBtn=view.findViewById(R.id.poll_btn_left);
        progressBar = view.findViewById(R.id.poll_question_progress_bar);
        questionTitle = view.findViewById(R.id.poll_question_title);
        page= view.findViewById(R.id.poll_txt_qnumber);

        progressBar.setVisibility(View.GONE);
        setListeners();
        General.progressBarOn(getActivity(),container,progressBar,false);
        //Get only poll question and answers that are related to the user without the image question and its answer
        Model.instance.getAllAnswersByUserAndPollIds(MyApplication.getUserKey(),pollId,map->{
            if(map != null){
                if(!map.isEmpty()){
                    viewModel.setPollMap(map);
                    Model.instance.getMainThread().post(()->{
                        General.progressBarOff(getActivity(),container,progressBar,true);
                        setPoll();
                        setButtonsColor();
                    });
                }
                else{
                    setPoll();
                    setButtonsColor();
                    Model.instance.getMainThread().post(()->{
                        General.progressBarOff(getActivity(),container,progressBar,true);
                    });
                }
            }
            else{
                setPoll();
                setButtonsColor();
                Model.instance.getMainThread().post(()->{
                    General.progressBarOff(getActivity(),container,progressBar,true);
                });
            }
        });
        setPoll();
        return view;
    }

    public void setPoll(){
        PollQuestion pollQuestion = viewModel.getPollQuestions().get(viewModel.index);
        page.setText((viewModel.index+1)+"/"+(numOfQuestions+1));
        questionTitle.setText(pollQuestion.getPollQuestion());

    }

    public boolean isAnswerSelected(){
        return viewModel.pollMap.containsKey(viewModel.getPollQuestions().get(viewModel.index).getPollQuestionId());
    }


    public void setButtonsColor(){
        if(!viewModel.pollMap.containsKey(viewModel.getPollQuestions().get(viewModel.index).getPollQuestionId())){

        }
        else{
            String ans = Objects.requireNonNull(viewModel.pollMap.get(viewModel.getPollQuestions().get(viewModel.index).getPollQuestionId())).answer;

        }

    }


    public void getNextPollQuestion(){

        viewModel.index++;
        PollQuestion pollQuestion = viewModel.getPollQuestions().get(viewModel.index);
        page.setText((viewModel.index+1)+"/"+(numOfQuestions+1));
        questionTitle.setText(pollQuestion.getPollQuestion());

    }

    public void getPrevPollQuestion(){
        viewModel.index--;
        PollQuestion pollQuestion = viewModel.getPollQuestions().get(viewModel.index);
        page.setText((viewModel.index+1)+"/"+(numOfQuestions+1));
        questionTitle.setText(pollQuestion.getPollQuestion());

    }

    public void setListeners(){
        nextBtn.setOnClickListener(v -> {
            if(viewModel.index==numOfQuestions)
                return;
            if(isAnswerSelected()) {
                if(viewModel.index==numOfQuestions-1) {
                    General.progressBarOn(getActivity(),container,progressBar,false);
                    Model.instance.savePollAnswersOnLocalDb(viewModel.pollMap,()->{
                        Model.instance.getMainThread().post(()->{

                        });
                    });
                }
                else{
                    getNextPollQuestion();
                    setButtonsColor();}
            }
            else {
                Snackbar.make(getView(),"Please Select An Answer",Snackbar.LENGTH_LONG).show();
            }
        });
        prevBtn.setOnClickListener(v -> {
            if(viewModel.index==0)
                return;

            getPrevPollQuestion();
            setButtonsColor();
        });
    }

    private void setAnswerListener(MaterialButton answer) {
        answer.setOnClickListener(v -> {
            PollQuestion pollQuestion = viewModel.getPollQuestions().get(viewModel.index);
            if(viewModel.pollMap.containsKey(pollQuestion.getPollQuestionId())){
                Answer ans = viewModel.pollMap.get(pollQuestion.getPollQuestionId());
                assert ans != null;
                ans.setAnswer(answer.getText().toString());
                viewModel.pollMap.put(pollQuestion.getPollQuestionId(),ans);
            }
            else{
                viewModel.pollMap.put(pollQuestion.getPollQuestionId(),new Answer(UUID.randomUUID().toString(),MyApplication.getUserKey(),pollId,pollQuestion.pollQuestionId,answer.getText().toString(),false));
            }
            setButtonsColor();
        });
    }
}

