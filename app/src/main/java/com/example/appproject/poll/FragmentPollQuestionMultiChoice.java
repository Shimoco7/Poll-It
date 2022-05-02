package com.example.appproject.poll;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
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
import com.example.appproject.model.Model;
import com.example.appproject.model.poll.Answer;
import com.google.android.material.button.MaterialButton;

import java.util.Objects;


public class FragmentPollQuestionMultiChoice extends Fragment {

    PollQuestionMultiChoiceViewModel viewModel;
    PollQuestionMultiChoiceAdapter adapter;
    RecyclerView options;
    String pollId,pollQuestionId;
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

    public FragmentPollQuestionMultiChoice() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_poll_question_multi_choice, container, false);
        this.container = container;
        nextBtn= view.findViewById(R.id.poll_btn_right);
        prevBtn=view.findViewById(R.id.poll_btn_left);
        progressBar = view.findViewById(R.id.poll_question_progress_bar);
        questionTitle = view.findViewById(R.id.poll_question_title);
        page= view.findViewById(R.id.poll_txt_qnumber);
        options = view.findViewById(R.id.poll_question_multi_choice_rv);
        progressBar.setVisibility(View.VISIBLE);

        viewModel = new ViewModelProvider(this).get(PollQuestionMultiChoiceViewModel.class);
        pollId = FragmentPollQuestionMultiChoiceArgs.fromBundle(getArguments()).getPollId();
        pollQuestionId = FragmentPollQuestionMultiChoiceArgs.fromBundle(getArguments()).getPollQuestionId();
        Model.instance.getPollQuestionWithAnswer(pollQuestionId,pollQuestionWithAnswer ->{
            viewModel.setPollQuestionWithAnswer(pollQuestionWithAnswer);
            if(pollQuestionWithAnswer.pollQuestion.getQuestionNumber().equals(1)){
                prevBtn.setVisibility(View.GONE);
            }
            Model.instance.getPollNumberOfQuestions(pollId,numOfQuestions->{
                viewModel.setTotalPollNumberOfQuestions(numOfQuestions);
                questionTitle.setText(Objects.requireNonNull(viewModel.getPollQuestionWithAnswer().getValue()).pollQuestion.getPollQuestion());
                String pageFormat = viewModel.getPollQuestionWithAnswer().getValue().pollQuestion.getQuestionNumber()+"/"+ viewModel.getTotalPollNumberOfQuestions();
                page.setText(pageFormat);
            });
        });
        setRvAndAdapter();
        viewModel.getPollQuestionWithAnswer().observe(getViewLifecycleOwner(),questionWithAnswer -> refresh());
        return view;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void refresh() {
        adapter.notifyDataSetChanged();
    }


    private void setAnswer(String answer) {
        for(int i = 0 ; i < adapter.getItemCount() ; i++){
            PollQuestionMultiChoiceViewHolder holder = (PollQuestionMultiChoiceViewHolder) options.findViewHolderForAdapterPosition(i);
            assert holder != null;
            if(holder.option.getText().equals(answer)){
                holder.option.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.primeGreen)));
                holder.option.setAlpha(1);
            }
            else{
                holder.option.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.primeGray)));
                holder.option.setAlpha((float)0.25);
            }
        }
    }

    private void setRvAndAdapter(){
        options.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false) {
            @Override
            public void onLayoutCompleted(RecyclerView.State state) {
                super.onLayoutCompleted(state);
                if(viewModel.getPollQuestionWithAnswer() != null
                        && viewModel.getPollQuestionWithAnswer().getValue() != null
                        && viewModel.getPollQuestionWithAnswer().getValue().answer != null){
                    setAnswer(viewModel.getPollQuestionWithAnswer().getValue().answer.getAnswer());
                }
                progressBar.setVisibility(View.GONE);
            }
        });
        options.setHasFixedSize(true);
        adapter = new PollQuestionMultiChoiceAdapter(viewModel,getLayoutInflater());
        options.setAdapter(adapter);
        adapter.setOnItemClickListener((v,pos)->{
            String chosenAnswer = Objects.requireNonNull(Objects.requireNonNull(viewModel.getPollQuestionWithAnswer().getValue()).pollQuestion.getChoices().get(pos));
            for(int i = 0 ; i < adapter.getItemCount() ; i++){
                if(i!=pos){
                    PollQuestionMultiChoiceViewHolder holder = (PollQuestionMultiChoiceViewHolder) options.findViewHolderForAdapterPosition(i);
                    assert holder != null;
                    holder.option.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.primeGray)));
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

    private void setListeners(){
        nextBtn.setOnClickListener(v -> {

        });
        prevBtn.setOnClickListener(v -> {

        });
    }

}

