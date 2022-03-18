package com.example.appproject.poll;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.example.appproject.R;
import com.example.appproject.model.Model;
import com.example.appproject.model.poll.Answer;
import com.example.appproject.model.poll.PollQuestion;
import com.google.android.material.button.MaterialButton;
import java.util.Objects;


public class FragmentPollQuestion extends Fragment {

    PollQuestionViewModel viewModel;
    String pollId;
    int numOfQuestions;
    TextView questionTitle;
    TextView page;
    MaterialButton answer1,answer2,answer3,answer4;
    MaterialButton nextBtn, prevBtn;


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
        pollId = FragmentActivePollArgs.fromBundle(getArguments()).getPollId();
        nextBtn= view.findViewById(R.id.poll_btn_right);
        prevBtn=view.findViewById(R.id.poll_btn_left);

        questionTitle = view.findViewById(R.id.poll_question_title);
        answer1 = view.findViewById(R.id.poll_btn_op1);
        answer2 = view.findViewById(R.id.poll_btn_op2);
        answer3 = view.findViewById(R.id.poll_btn_op3);
        answer4 = view.findViewById(R.id.poll_btn_op4);

        page= view.findViewById(R.id.poll_txt_qnumber);

        setListeners();
        setPoll();
        setButtonsColor();
        return view;
    }

    public void setPoll(){
        PollQuestion pollQuestion = viewModel.getPollQuestions().get(viewModel.index);
        page.setText((viewModel.index+1)+"/"+(numOfQuestions+1));
        questionTitle.setText(pollQuestion.getPollQuestion());
        answer1.setText(pollQuestion.getChoices().get(0));
        answer2.setText(pollQuestion.getChoices().get(1));
        answer3.setText(pollQuestion.getChoices().get(2));
        answer4.setText(pollQuestion.getChoices().get(3));
    }

    public boolean isAnswerSelected(){
        return viewModel.pollMap.containsKey(viewModel.getPollQuestions().get(viewModel.index));
    }


    public void setButtonsColor(){
        if(!viewModel.pollMap.containsKey(viewModel.getPollQuestions().get(viewModel.index))){
            answer1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.primeGray)));
            answer1.setAlpha(1);
            answer2.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.primeGray)));
            answer2.setAlpha(1);
            answer3.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.primeGray)));
            answer3.setAlpha(1);
            answer4.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.primeGray)));
            answer4.setAlpha(1);
        }
        else{
            String ans = Objects.requireNonNull(viewModel.pollMap.get(viewModel.getPollQuestions().get(viewModel.index))).answer;
            if(answer1.getText().toString().equals(ans)) {
                answer1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#00FF00")));
                answer1.setAlpha(1);
                answer2.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.primeGray)));
                answer2.setAlpha((float)0.25);
                answer3.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.primeGray)));
                answer3.setAlpha((float)0.25);
                answer4.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.primeGray)));
                answer4.setAlpha((float)0.25);
                return;
            }
            if(answer2.getText().toString().equals(ans)) {
                answer1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.primeGray)));
                answer1.setAlpha((float)0.25);
                answer2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#00FF00")));
                answer2.setAlpha(1);
                answer3.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.primeGray)));
                answer3.setAlpha((float)0.25);
                answer4.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.primeGray)));
                answer4.setAlpha((float)0.25);
                return;
            }
            if(answer3.getText().toString().equals(ans)) {
                answer1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.primeGray)));
                answer1.setAlpha((float)0.25);
                answer2.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.primeGray)));
                answer2.setAlpha((float)0.25);
                answer3.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#00FF00")));
                answer3.setAlpha(1);
                answer4.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.primeGray)));
                answer4.setAlpha((float)0.25);
                return;
            }
            if(answer4.getText().toString().equals(ans)) {
                answer1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.primeGray)));
                answer1.setAlpha((float)0.25);
                answer2.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.primeGray)));
                answer2.setAlpha((float)0.25);
                answer3.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.primeGray)));
                answer3.setAlpha((float)0.25);
                answer4.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#00FF00")));
                answer4.setAlpha(1);
            }
        }

    }


    public void getNextPollQuestion(){

        viewModel.index++;
        PollQuestion pollQuestion = viewModel.getPollQuestions().get(viewModel.index);
        page.setText((viewModel.index+1)+"/"+(numOfQuestions+1));
        questionTitle.setText(pollQuestion.getPollQuestion());
        answer1.setText(pollQuestion.getChoices().get(0));
        answer2.setText(pollQuestion.getChoices().get(1));
        answer3.setText(pollQuestion.getChoices().get(2));
        answer4.setText(pollQuestion.getChoices().get(3));
    }

    public void getPrevPollQuestion(){
        viewModel.index--;
        PollQuestion pollQuestion = viewModel.getPollQuestions().get(viewModel.index);
        page.setText((viewModel.index+1)+"/"+(numOfQuestions+1));
        questionTitle.setText(pollQuestion.getPollQuestion());
        answer1.setText(pollQuestion.getChoices().get(0));
        answer2.setText(pollQuestion.getChoices().get(1));
        answer3.setText(pollQuestion.getChoices().get(2));
        answer4.setText(pollQuestion.getChoices().get(3));
    }

    public void setListeners(){
        answer1.setOnClickListener(v -> {
            PollQuestion pollQuestion = viewModel.getPollQuestions().get(viewModel.index);
            viewModel.pollMap.put(pollQuestion,new Answer(pollQuestion.pollQuestionId,answer1.getText().toString()));
            setButtonsColor();
        });
        answer2.setOnClickListener(v -> {
            PollQuestion pollQuestion = viewModel.getPollQuestions().get(viewModel.index);
            viewModel.pollMap.put(pollQuestion,new Answer(pollQuestion.pollQuestionId,answer2.getText().toString()));
            setButtonsColor();
        });
        answer3.setOnClickListener(v -> {
            PollQuestion pollQuestion = viewModel.getPollQuestions().get(viewModel.index);
            viewModel.pollMap.put(pollQuestion,new Answer(pollQuestion.pollQuestionId,answer3.getText().toString()));
            setButtonsColor();

        });
        answer4.setOnClickListener(v -> {
            PollQuestion pollQuestion = viewModel.getPollQuestions().get(viewModel.index);
            viewModel.pollMap.put(pollQuestion,new Answer(pollQuestion.pollQuestionId,answer4.getText().toString()));
            setButtonsColor();

        });
        nextBtn.setOnClickListener(v -> {
            if(viewModel.index==numOfQuestions)
                return;
            if(isAnswerSelected()) {
                if(viewModel.index==numOfQuestions-1) {
                    Navigation.findNavController(nextBtn).navigate(R.id.action_fragmentPollQuestion_to_fragmentPollImage);
                    FragmentPollQuestionDirections.actionFragmentPollQuestionToFragmentPollImage();
                }
                else{
                    getNextPollQuestion();
                    setButtonsColor();}
            }
            else {
                Toast.makeText(getActivity(),"Please Select An Answer", Toast.LENGTH_LONG).show();
            }
        });
        prevBtn.setOnClickListener(v -> {
            if(viewModel.index==0)
                return;

            getPrevPollQuestion();
            setButtonsColor();
        });
    }
}

