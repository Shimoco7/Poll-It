package com.example.appproject.poll;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
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
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.snackbar.Snackbar;
import com.google.common.base.Stopwatch;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


public class FragmentPollQuestionMultiChoice extends Fragment {

    PollQuestionMultiChoiceViewModel viewModel;
    PollQuestionMultiChoiceAdapter adapter;
    RecyclerView options;
    Boolean isWithImageQuestion;
    String pollId,pollQuestionId;
    ShapeableImageView questionImage;
    TextView questionTitle;
    TextView page;
    MaterialButton nextBtn, prevBtn;
    ProgressBar progressBar;
    ViewGroup container;
    Stopwatch stopwatch;

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
        questionImage= view.findViewById(R.id.poll_QuestionImage);
        options = view.findViewById(R.id.poll_question_multi_choice_rv);
        General.progressBarOn(getActivity(),container,progressBar,false);

        viewModel = new ViewModelProvider(this).get(PollQuestionMultiChoiceViewModel.class);
        pollId = FragmentPollQuestionMultiChoiceArgs.fromBundle(getArguments()).getPollId();
        pollQuestionId = FragmentPollQuestionMultiChoiceArgs.fromBundle(getArguments()).getPollQuestionId();
        isWithImageQuestion = FragmentPollQuestionMultiChoiceArgs.fromBundle(getArguments()).getIsWithImageQuestion();
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

                if(isWithImageQuestion){
                    questionImage.setMinimumHeight(500);
                    questionImage.setMinimumWidth(600);
                    setImageQuestion();
                }

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
        PollQuestionMultiChoiceViewHolder holder;
        for(int i = 0 ; i < adapter.getItemCount() ; i++){
            holder = (PollQuestionMultiChoiceViewHolder) options.findViewHolderForAdapterPosition(i);
            if(holder != null){
                if(holder.option.getText().equals(answer)){
                    holder.option.setBackgroundTintList(ColorStateList.valueOf(requireContext().getColor(R.color.primeGreen)));
                    holder.option.setAlpha(1);
                }
                else{
                    holder.option.setBackgroundTintList(ColorStateList.valueOf(requireContext().getColor(R.color.primeGray)));
                    holder.option.setAlpha((float)0.25);
                }
            }
        }
        if(viewModel.getPollQuestionWithAnswer().getValue() != null){
            options.smoothScrollToPosition(viewModel.getPollQuestionWithAnswer().getValue().answer.getPosition());
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
                    viewModel.setAnswered(true);
                }
                else{
                    viewModel.setAnswered(false);
                }
            }
        });
        options.setHasFixedSize(true);
        adapter = new PollQuestionMultiChoiceAdapter(viewModel,getLayoutInflater());
        options.setAdapter(adapter);
        adapter.setOnItemClickListener((v,pos)->{
            viewModel.setAnswered(true);
            String chosenAnswer = Objects.requireNonNull(Objects.requireNonNull(viewModel.getPollQuestionWithAnswer().getValue()).pollQuestion.getChoices().get(pos));
            for(int i = 0 ; i < adapter.getItemCount() ; i++){
                if(i!=pos){
                    PollQuestionMultiChoiceViewHolder holder = (PollQuestionMultiChoiceViewHolder) options.findViewHolderForAdapterPosition(i);
                    if(holder != null){
                        holder.option.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.primeGray)));
                        holder.option.setAlpha((float)0.25);
                    }
                }
            }
            if(viewModel.getPollQuestionWithAnswer().getValue().answer == null){
                Answer answer = new Answer(MyApplication.getUserKey(),pollId,pollQuestionId,chosenAnswer,pos,viewModel.getPollQuestionWithAnswer().getValue().pollQuestion.getChoices().size());
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

    private void setListeners(){
        nextBtn.setOnClickListener(v -> {
            if(!viewModel.getAnswered()){
                Snackbar.make(requireView(),getString(R.string.select_an_answer),Snackbar.LENGTH_SHORT).show();
                return;
            }

            General.progressBarOn(getActivity(),container,progressBar,false);
            stopwatch.stop();
            if(viewModel.getPollQuestionWithAnswer().getValue() != null){
                Double currentTime = viewModel.getPollQuestionWithAnswer().getValue().answer.getTimeInSeconds();
                viewModel.getPollQuestionWithAnswer().getValue().answer.setTimeInSeconds(stopwatch.elapsed(TimeUnit.MILLISECONDS)/1000.0 + currentTime);
                Model.instance.saveAnswerOnLocalDb(viewModel.getPollQuestionWithAnswer().getValue().answer);
            }

            if(Objects.requireNonNull(viewModel.getPollQuestionWithAnswer().getValue()).pollQuestion.getQuestionNumber().equals(viewModel.getTotalPollNumberOfQuestions())){
                Model.instance.savePollAnswersToRemoteDb(MyApplication.getUserKey(),pollId,(timeForAllAnswers,ansIndicesArray)-> Model.instance.getPollByPollId(pollId, poll->{
                    Log.d("TAG", "time average for all answers: " + timeForAllAnswers/poll.getTotalNumberOfQuestions());
                    Double score = Model.instance.checkReliability(timeForAllAnswers, ansIndicesArray);

                    Model.instance.getUserRankAndCoins(MyApplication.getUserKey(),coinsAndRank-> {
                        Map<String, Object> map = new HashMap<>();
                        Integer coins = (Integer) coinsAndRank.get(getString(R.string.user_coins));
                        Double rank = (Double) coinsAndRank.get(getString(R.string.user_rank));
                        Integer updatedCoins = coins + poll.getCoins();
                        Double updateRank = rank + score;
                        map.put("coins",updatedCoins);
                        map.put("rank",updateRank);
                        Model.instance.updateUser(MyApplication.getUserKey(),map,(user,message)->{
                            if(user != null && message.equals(getString(R.string.success))){
                                Navigation.findNavController(this.container).navigate(FragmentPollQuestionMultiChoiceDirections.actionGlobalFragmentHomeScreen());
                            }
                            else{
                                General.progressBarOff(getActivity(),container,progressBar,true);
                                Snackbar.make(requireView(),"An error has occurred... Please try again",Snackbar.LENGTH_INDEFINITE).setAction("Close",view->{ }).show();
                            }
                        });
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

    private void setImageQuestion() {
        General.loadImage(Objects.requireNonNull(viewModel.getPollQuestionWithAnswer().getValue()).pollQuestion.getPollQuestionImage(),questionImage,R.drawable.loadimage100x100);
    }

    private void navigateToPollQuestion(PollQuestion pollQuestion){
        switch (pollQuestion.getPollQuestionType()){
            case Multi_Choice:{
                Navigation.findNavController(nextBtn).navigate((FragmentPollQuestionMultiChoiceDirections
                        .actionFragmentPollQuestionSelf(pollId,pollQuestion.getPollQuestionId(),false)));
                break;
            }
            case Image_Question:{
                Navigation.findNavController(nextBtn).navigate((FragmentPollQuestionMultiChoiceDirections
                        .actionFragmentPollQuestionSelf(pollId,pollQuestion.getPollQuestionId(),true)));
                break;
            }
            case Image_Answers:{
                Navigation.findNavController(nextBtn).navigate((FragmentPollQuestionMultiChoiceDirections
                        .actionFragmentPollQuestionMultiChoiceToFragmentPollQuestionImageAnswers(pollId,pollQuestion.getPollQuestionId())));
                break;
            }
        }
    }
}

