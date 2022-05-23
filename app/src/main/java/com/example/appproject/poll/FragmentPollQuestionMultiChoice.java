package com.example.appproject.poll;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
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
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Request;


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
//                    nextBtn.setCompoundDrawables(null, ResourcesCompat.getDrawable(getResources(),R.drawable.ic_rightarrow, null),null,null);
                    nextBtn.setCompoundDrawables(null, null,null,null);
                    nextBtn.setText(getString(R.string.finish));
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
            if(!viewModel.getAnswered()){
                Snackbar.make(requireView(),getString(R.string.select_an_answer),Snackbar.LENGTH_SHORT).show();
                return;
            }

            if(Objects.requireNonNull(viewModel.getPollQuestionWithAnswer().getValue()).pollQuestion.getQuestionNumber().equals(viewModel.getTotalPollNumberOfQuestions())){
                Model.instance.savePollAnswersToRemoteDb(MyApplication.getUserKey(),pollId,()-> Model.instance.getPollByPollId(pollId, poll->{
                    Map<String,Object> map = new HashMap<>();
                    Integer updatedCoins = Integer.parseInt(MyApplication.getUserCoins())+poll.getCoins();
                    map.put("coins",updatedCoins);
                    Model.instance.updateUser(MyApplication.getUserKey(),map,(user,message)->{
                        if(user != null && message.equals(getString(R.string.success))){
                            MyApplication.setUserCoins(String.valueOf(updatedCoins));
                            Navigation.findNavController(this.container).navigate(FragmentPollQuestionMultiChoiceDirections.actionGlobalFragmentHomeScreen());
                        }
                        else{
                            Snackbar.make(requireView(),"An error has occurred... Please try again",Snackbar.LENGTH_INDEFINITE).setAction("Close",view->{ }).show();
                        }
                    });
                }));

            }
            else{
                switch (viewModel.getNextPollQuestion().getPollQuestionType()){
                    case Multi_Choice:{
                        Navigation.findNavController(v).navigate((FragmentPollQuestionMultiChoiceDirections
                                .actionFragmentPollQuestionSelf(pollId,viewModel.getNextPollQuestion().getPollQuestionId(),false)));
                        break;
                    }
                    case Image_Question:{
                        Navigation.findNavController(v).navigate((FragmentPollQuestionMultiChoiceDirections
                                .actionFragmentPollQuestionSelf(pollId,viewModel.getNextPollQuestion().getPollQuestionId(),true)));
                        break;
                    }
                    case Image_Answers:{
                        Navigation.findNavController(v).navigate((FragmentPollQuestionMultiChoiceDirections
                                .actionFragmentPollQuestionMultiChoiceToFragmentPollQuestionImageAnswers(pollId,viewModel.getNextPollQuestion().getPollQuestionId())));
                        break;
                    }
                }
            }
        });
        prevBtn.setOnClickListener(v -> {
            Navigation.findNavController(v).navigateUp();
        });
    }

    private void setImageQuestion() {
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(chain -> {
            Request newRequest = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer " + MyApplication.getAccessToken())
                    .build();
            return chain.proceed(newRequest);
        }).build();
        Picasso picasso = new Picasso.Builder(MyApplication.getContext()).downloader(new OkHttp3Downloader(client)).build();
        picasso.load(Objects.requireNonNull(viewModel.getPollQuestionWithAnswer().getValue()).pollQuestion.getPollQuestionImage())
                .placeholder(R.drawable.loadimagesmall)
                .into(questionImage);

    }

}

