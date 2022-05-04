package com.example.appproject.poll;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.android.material.button.MaterialButton;

import java.util.Objects;


public class FragmentPollQuestionImageAnswers extends Fragment {

    PollQuestionImageAnswersViewModel viewModel;
    PollQuestionImageAnswersAdapter adapter;
    RecyclerView options;
    String pollId,pollQuestionId;
    TextView questionTitle;
    TextView page;
    MaterialButton nextBtn, prevBtn;
    ProgressBar progressBar;
    ViewGroup container;

    public FragmentPollQuestionImageAnswers() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        menu.findItem(R.id.main_menu_settings).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_poll_question_image, container, false);
        this.container = container;
        nextBtn = view.findViewById(R.id.pollImage_btn_right);
        prevBtn = view.findViewById(R.id.pollImage_btn_left);
        progressBar = view.findViewById(R.id.pollImage_question_progress_bar);
        questionTitle = view.findViewById(R.id.pollImage_question_title);
        page = view.findViewById(R.id.pollImage_txt_qnumber);
        options = view.findViewById(R.id.pollImage_rv);

        viewModel = new ViewModelProvider(this).get(PollQuestionImageAnswersViewModel.class);
        pollId = FragmentPollQuestionImageAnswersArgs.fromBundle(getArguments()).getPollId();
        pollQuestionId = FragmentPollQuestionImageAnswersArgs.fromBundle(getArguments()).getPollQuestionId();
        Model.instance.getPollQuestionWithAnswer(pollQuestionId, pollQuestionWithAnswer ->{
            viewModel.setPollQuestionWithAnswer(pollQuestionWithAnswer);
            if(pollQuestionWithAnswer.pollQuestion.getQuestionNumber().equals(1)){
                prevBtn.setVisibility(View.GONE);
            }
            Model.instance.getPollNumberOfQuestions(pollId,numOfQuestions->{
                viewModel.setTotalPollNumberOfQuestions(numOfQuestions);
                questionTitle.setText(Objects.requireNonNull(viewModel.getPollQuestionWithAnswer().getValue()).pollQuestion.getPollQuestion());
                String pageFormat = viewModel.getPollQuestionWithAnswer().getValue().pollQuestion.getQuestionNumber()+"/"+ viewModel.getTotalPollNumberOfQuestions();
                page.setText(pageFormat);

                if(!numOfQuestions.equals(viewModel.getPollQuestionWithAnswer().getValue().pollQuestion.questionNumber)){
                    Model.instance.getPollQuestionByNumber(pollId,viewModel.getPollQuestionWithAnswer().getValue().pollQuestion.questionNumber+1,pollQuestion -> {
                        viewModel.setNextPollQuestion(pollQuestion);
                        General.progressBarOff(getActivity(),container,progressBar,true);
                    });
                }
                else{
                    nextBtn.setText(getString(R.string.finish)); //TODO - make sure it is visible
                    General.progressBarOff(getActivity(),container,progressBar,true);
                }
            });
        });

        setRvAndAdapter();
        setListeners();
        viewModel.getPollQuestionWithAnswer().observe(getViewLifecycleOwner(),questionWithAnswer -> refresh());

        return view;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void refresh() {
        adapter.notifyDataSetChanged();
    }

    private void setAnswer(String answer) {
        for(int i = 0 ; i < adapter.getItemCount() ; i++){
            PollQuestionImageAnswersViewHolder holder = (PollQuestionImageAnswersViewHolder) options.findViewHolderForAdapterPosition(i);
            assert holder != null;
            if(holder.url.equals(answer)){
                holder.option.setAlpha((float)1.0);
            }
            else{
                holder.option.setAlpha((float)0.25);
            }
        }
    }

    private void setRvAndAdapter() {
        options.setLayoutManager(new GridLayoutManager(getActivity(),2,LinearLayoutManager.VERTICAL,false){
            @Override
            public void onLayoutCompleted(RecyclerView.State state) {
                super.onLayoutCompleted(state);
                if(viewModel.getPollQuestionWithAnswer() != null
                        && viewModel.getPollQuestionWithAnswer().getValue() != null
                        && viewModel.getPollQuestionWithAnswer().getValue().answer != null){
                    setAnswer(viewModel.getPollQuestionWithAnswer().getValue().answer.getAnswer());
                }
            }
        });
        options.setHasFixedSize(true);
        adapter = new PollQuestionImageAnswersAdapter(viewModel,getLayoutInflater());
        options.setAdapter(adapter);
        adapter.setOnItemClickListener((v,pos)->{
            String chosenAnswer = Objects.requireNonNull(Objects.requireNonNull(viewModel.getPollQuestionWithAnswer().getValue()).pollQuestion.getChoices().get(pos));
            for(int i = 0 ; i < adapter.getItemCount() ; i++){
                if(i!=pos){
                    PollQuestionImageAnswersViewHolder holder = (PollQuestionImageAnswersViewHolder) options.findViewHolderForAdapterPosition(i);
                    assert holder != null;
                    holder.option.setAlpha((float)0.25);
                }
            }
            if(viewModel.getPollQuestionWithAnswer().getValue().answer == null){
                Answer answer = new Answer(MyApplication.getUserKey(),pollId,pollQuestionId,chosenAnswer);
                viewModel.getPollQuestionWithAnswer().getValue().answer = answer;
                Model.instance.saveAnswerOnLocalDb(answer);
            }
            else{
                Model.instance.updateAnswerOnLocalDb(viewModel.getPollQuestionWithAnswer().getValue().answer.getAnswerId(),chosenAnswer);
            }
        });
    }

    private void setListeners() {
        nextBtn.setOnClickListener(v -> {
            if(Objects.requireNonNull(viewModel.getPollQuestionWithAnswer().getValue()).pollQuestion.getQuestionNumber().equals(viewModel.getTotalPollNumberOfQuestions())){
                Model.instance.savePollAnswersToRemoteDb(MyApplication.getUserKey(),pollId,()->
                        Navigation.findNavController(this.container).navigate(FragmentPollQuestionImageAnswersDirections.actionGlobalFragmentHomeScreen()));

            }
            else{
                switch (viewModel.getNextPollQuestion().getPollQuestionType()){
                    case Multi_Choice:{
                        Navigation.findNavController(v).navigate((FragmentPollQuestionImageAnswersDirections
                                .actionFragmentPollQuestionImageAnswersToFragmentPollQuestionMultiChoice(pollId,viewModel.getNextPollQuestion().getPollQuestionId(),false)));
                        break;
                    }
                    case Image_Question:{
                        Navigation.findNavController(v).navigate((FragmentPollQuestionImageAnswersDirections
                                .actionFragmentPollQuestionImageAnswersToFragmentPollQuestionMultiChoice(pollId,viewModel.getNextPollQuestion().getPollQuestionId(),true)));
                        break;
                    }
                    case Image_Answers:{
                        Navigation.findNavController(v).navigate((FragmentPollQuestionImageAnswersDirections
                                .actionFragmentPollQuestionImageAnswersSelf(pollId,viewModel.getNextPollQuestion().getPollQuestionId())));
                        break;
                    }
                }
            }
        });
        prevBtn.setOnClickListener(v -> {
            Navigation.findNavController(v).navigateUp();
        });
    }
}