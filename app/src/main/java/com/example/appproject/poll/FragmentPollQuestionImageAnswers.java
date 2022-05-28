package com.example.appproject.poll;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Rect;
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
import com.example.appproject.model.poll.PollQuestion;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.common.base.Stopwatch;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


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
    Stopwatch stopwatch;

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
        General.progressBarOn(getActivity(),container,progressBar,false);

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
                    nextBtn.setCompoundDrawables(null, null,null,null);
                    nextBtn.setText(getString(R.string.finish));
                    General.progressBarOff(getActivity(),container,progressBar,true);
                }
            });
        });

        setRvAndAdapter();
        setListeners();
        viewModel.getPollQuestionWithAnswer().observe(getViewLifecycleOwner(),questionWithAnswer -> refresh());

        stopwatch = Stopwatch.createStarted();
        return view;
    }


    @SuppressLint("NotifyDataSetChanged")
    private void refresh() {
        adapter.notifyDataSetChanged();
    }

    private void setAnswer(String answer) {
        PollQuestionImageAnswersViewHolder holder;
        for(int i = 0 ; i < adapter.getItemCount() ; i++){
            holder = (PollQuestionImageAnswersViewHolder) options.findViewHolderForAdapterPosition(i);
            if(holder != null){
                if(holder.url.equals(answer)){
                    holder.option.setAlpha((float)1.0);
                }
                else{
                    holder.option.setAlpha((float)0.25);
                }
            }
        }
        if(viewModel.getPollQuestionWithAnswer().getValue() != null){
            options.smoothScrollToPosition(viewModel.getPollQuestionWithAnswer().getValue().answer.getPosition());
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
                    viewModel.setAnswered(true);
                }
                else{
                    viewModel.setAnswered(false);
                }
            }
        });
        options.setHasFixedSize(true);
        adapter = new PollQuestionImageAnswersAdapter(viewModel,getLayoutInflater());
        options.setAdapter(adapter);
        options.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.set(5,20,5,20);
            }
        });

        adapter.setOnItemClickListener((v,pos)->{
            viewModel.setAnswered(true);
            String chosenAnswer = Objects.requireNonNull(Objects.requireNonNull(viewModel.getPollQuestionWithAnswer().getValue()).pollQuestion.getChoices().get(pos));
            for(int i = 0 ; i < adapter.getItemCount() ; i++){
                if(i!=pos){
                    PollQuestionImageAnswersViewHolder holder = (PollQuestionImageAnswersViewHolder) options.findViewHolderForAdapterPosition(i);
                    if(holder != null){
                        holder.option.setAlpha((float)0.25);
                    }
                }
            }
            if(viewModel.getPollQuestionWithAnswer().getValue().answer == null){
                Answer answer = new Answer(MyApplication.getUserKey(),pollId,pollQuestionId,chosenAnswer,pos);
                viewModel.getPollQuestionWithAnswer().getValue().answer = answer;
                Model.instance.saveAnswerOnLocalDb(answer);
            }
            else{
                viewModel.getPollQuestionWithAnswer().getValue().answer.setAnswer(chosenAnswer);
                viewModel.getPollQuestionWithAnswer().getValue().answer.setPosition(pos);
                Model.instance.saveAnswerOnLocalDb(viewModel.getPollQuestionWithAnswer().getValue().answer);
            }
        });
    }

    private void setListeners() {
        nextBtn.setOnClickListener(v -> {
            if(!viewModel.getAnswered()){
                Snackbar.make(requireView(),getString(R.string.select_an_answer),Snackbar.LENGTH_SHORT).show();
                return;
            }

            stopwatch.stop();
            if(viewModel.getPollQuestionWithAnswer().getValue() != null){
                Double currentTime = viewModel.getPollQuestionWithAnswer().getValue().answer.getTimeInSeconds();
                viewModel.getPollQuestionWithAnswer().getValue().answer.setTimeInSeconds(stopwatch.elapsed(TimeUnit.MILLISECONDS)/1000.0 + currentTime);
                Model.instance.saveAnswerOnLocalDb(viewModel.getPollQuestionWithAnswer().getValue().answer);
            }

            if(Objects.requireNonNull(viewModel.getPollQuestionWithAnswer().getValue()).pollQuestion.getQuestionNumber().equals(viewModel.getTotalPollNumberOfQuestions())){
                Model.instance.savePollAnswersToRemoteDb(MyApplication.getUserKey(),pollId,()-> Model.instance.getPollByPollId(pollId, poll->{
                    Map<String,Object> map = new HashMap<>();
                    Integer updatedCoins = Integer.parseInt(MyApplication.getUserCoins())+poll.getCoins();
                    map.put("coins",updatedCoins);
                    Model.instance.updateUser(MyApplication.getUserKey(),map,(user,message)->{
                        if(user != null && message.equals(getString(R.string.success))){
                            MyApplication.setUserCoins(String.valueOf(updatedCoins));
                            Navigation.findNavController(this.container).navigate(FragmentPollQuestionImageAnswersDirections.actionGlobalFragmentHomeScreen());
                        }
                        else{
                            Snackbar.make(requireView(),"An error has occurred... Please try again",Snackbar.LENGTH_INDEFINITE).setAction("Close",view->{ }).show();
                        }
                    });
                }));

            }
            else{
                navigateToPollQuestion(viewModel.getNextPollQuestion());
            }
        });
        prevBtn.setOnClickListener(v -> {
            Model.instance.getPollQuestionByNumber(pollId, Objects.requireNonNull(viewModel.getPollQuestionWithAnswer().getValue()).pollQuestion.getQuestionNumber()-1,pollQuestion -> {
                Model.instance.getMainThread().post(()->navigateToPollQuestion(pollQuestion));
            });
        });
    }

    private void navigateToPollQuestion(PollQuestion pollQuestion) {
        switch (pollQuestion.getPollQuestionType()){
            case Multi_Choice:{
                Navigation.findNavController(nextBtn).navigate((FragmentPollQuestionImageAnswersDirections
                        .actionFragmentPollQuestionImageAnswersToFragmentPollQuestionMultiChoice(pollId,pollQuestion.getPollQuestionId(),false)));
                break;
            }
            case Image_Question:{
                Navigation.findNavController(nextBtn).navigate((FragmentPollQuestionImageAnswersDirections
                        .actionFragmentPollQuestionImageAnswersToFragmentPollQuestionMultiChoice(pollId,pollQuestion.getPollQuestionId(),true)));
                break;
            }
            case Image_Answers:{
                Navigation.findNavController(nextBtn).navigate((FragmentPollQuestionImageAnswersDirections
                        .actionFragmentPollQuestionImageAnswersSelf(pollId,pollQuestion.getPollQuestionId())));
                break;
            }
        }
    }
}