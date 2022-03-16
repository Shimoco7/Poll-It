package com.example.appproject;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appproject.model.poll.Poll;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.collection.LLRBNode;

import java.util.ArrayList;
import java.util.HashMap;


public class FragmentPollQuestion extends Fragment {
    int index = 0;
    ArrayList<Poll> pollArrayList = new ArrayList<Poll>();
    HashMap<String,String>pollMap = new HashMap<>();
    TextView questionTitle;
    TextView page;
    MaterialButton answer1;
    MaterialButton answer2;
    MaterialButton answer3;
    MaterialButton answer4;
    MaterialButton nextBtn;
    MaterialButton prevBtn;
    Button finishbtn;

    public FragmentPollQuestion() {
    }







    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_poll_question, container, false);
        nextBtn= view.findViewById(R.id.poll_btn_right);
        prevBtn=view.findViewById(R.id.poll_btn_left);
        finishbtn= view.findViewById(R.id.poll_finish_btn);
        questionTitle = view.findViewById(R.id.poll_question_title);
        answer1 = view.findViewById(R.id.poll_btn_op1);
        answer2 = view.findViewById(R.id.poll_btn_op2);
        answer3 = view.findViewById(R.id.poll_btn_op3);
        answer4 = view.findViewById(R.id.poll_btn_op4);
        finishbtn.setVisibility(View.GONE);
        page= view.findViewById(R.id.poll_txt_qnumber);

        finishbtn.setOnClickListener(Navigation.createNavigateOnClickListener(FragmentPollQuestionDirections.actionFragmentPollQuestionToFragmentPollImage()));

        setPoll();

        questionTitle.setText(pollArrayList.get(index).question);
        answer1.setText(pollArrayList.get(index).answer1);
        answer2.setText(pollArrayList.get(index).answer2);
        answer3.setText(pollArrayList.get(index).answer3);
//        getNextPoll();

        answer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pollMap.put(pollArrayList.get(index).question,answer1.getText().toString());
                setButtonsColor();
            }
        });
        answer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pollMap.put(pollArrayList.get(index).question,answer2.getText().toString());
                setButtonsColor();
            }
        });
        answer3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pollMap.put(pollArrayList.get(index).question,answer3.getText().toString());
                setButtonsColor();

            }
        });
        answer4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pollMap.put(pollArrayList.get(index).question,answer4.getText().toString());
                setButtonsColor();

            }
        });
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG","next clicked"+index+pollMap.keySet());
                if(index==3)
                    return;
                if(isAnswerSelected()) {
                    getNextPoll();
                    setButtonsColor();
                }
                else {
                    Toast.makeText(getActivity(),"Wrong", Toast.LENGTH_LONG).show();
                }

            }
        });
        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG","prev clicked" + index);
                if(index==0)
                    return;
                finishbtn.setVisibility(View.GONE);
                getPrevPoll();
                setButtonsColor();
            }
        });
        return view;
    }

    public void setPoll(){
        pollArrayList.add(new Poll("What is your favorite time of day?","Morning","Afternoon","Evening","Test"));
        pollArrayList.add(new Poll("What is your favorite drink?","Coca-Cola","Water","Sprite","Test"));
        pollArrayList.add(new Poll("What is your favorite sport?","Basketball","Football","Tennis","Test"));
    }

    public boolean isAnswerSelected(){
        if(pollMap.containsKey(pollArrayList.get(index).question))
            return true;
        return false;
    }


    public void setButtonsColor(){
        if(!pollMap.containsKey(pollArrayList.get(index).question)){
            answer1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.primeGray)));
            answer2.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.primeGray)));
            answer3.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.primeGray)));
            answer4.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.primeGray)));
            return;
        }
        else{
            String ans = pollMap.get(pollArrayList.get(index).question);
            if(answer1.getText().toString().equals(ans)) {
                answer1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#00FF00")));


                answer1.setIcon(getResources().getDrawable(R.drawable.ic_check));
                answer1.setPadding(0,0,150,0);
                answer2.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.primeGray)));
                answer1.setAlpha(1);
                answer2.setAlpha((float)0.25);
                answer2.setIcon(null);
                answer2.setPadding(0,0,0,0);
                answer3.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.primeGray)));
                answer3.setAlpha((float)0.25);
                answer3.setIcon(null);
                answer3.setPadding(0,0,0,0);
                answer4.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.primeGray)));
                answer4.setAlpha((float)0.25);
                answer4.setIcon(null);
                answer4.setPadding(0,0,0,0);
                return;
            }
            if(answer2.getText().toString().equals(ans)) {
                answer1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.primeGray)));
                answer1.setAlpha((float)0.25);
                answer1.setIcon(null);
                answer1.setPadding(0,0,0,0);
                answer2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#00FF00")));
                answer2.setPadding(0,0,150,0);
                answer2.setAlpha(1);
                answer2.setIcon(getResources().getDrawable(R.drawable.ic_check));
                answer3.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.primeGray)));
                answer3.setAlpha((float)0.25);
                answer3.setIcon(null);
                answer3.setPadding(0,0,0,0);
                answer4.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.primeGray)));
                answer4.setIcon(null);
                answer4.setPadding(0,0,0,0);
                answer4.setAlpha((float)0.25);


                return;
            }
            if(answer3.getText().toString().equals(ans)) {
                answer1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.primeGray)));
                answer1.setIcon(null);
                answer1.setPadding(0,0,0,0);
                answer1.setAlpha((float)0.25);
                answer2.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.primeGray)));
                answer2.setIcon(null);
                answer2.setPadding(0,0,0,0);
                answer2.setAlpha((float)0.25);
                answer3.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#00FF00")));
                answer3.setPadding(0,0,150,0);
                answer3.setAlpha(1);
                answer3.setIcon(getResources().getDrawable(R.drawable.ic_check));
                answer4.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.primeGray)));
                answer4.setIcon(null);
                answer4.setPadding(0,0,0,0);
                answer4.setAlpha((float)0.25);
                return;
            }
            if(answer4.getText().toString().equals(ans)) {
                answer1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.primeGray)));
                answer1.setIcon(null);
                answer1.setPadding(0,0,0,0);
                answer1.setAlpha((float)0.25);
                answer2.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.primeGray)));
                answer2.setIcon(null);
                answer2.setPadding(0,0,0,0);
                answer2.setAlpha((float)0.25);
                answer3.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.primeGray)));
                answer3.setIcon(null);
                answer3.setPadding(0,0,0,0);
                answer3.setAlpha((float)0.25);
                answer4.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#00FF00")));
                answer4.setIcon(getResources().getDrawable(R.drawable.ic_check));
                answer4.setPadding(0,0,150,0);
                answer4.setAlpha(1);
                return;
            }
        }

    }

    public void getNextPoll(){
        if(index==pollArrayList.size()-1) {
            finishbtn.setVisibility(View.VISIBLE);
            return;
        }
        index++;
        Poll current = pollArrayList.get(index);
        page.setText((index+1)+"/"+(pollArrayList.size()+1));
        questionTitle.setText(current.question);
        answer1.setText(current.answer1);
        answer2.setText(current.answer2);
        answer3.setText(current.answer3);
    }

    public void getPrevPoll(){
        index--;
        Poll current = pollArrayList.get(index);
        page.setText((index+1)+"/"+(pollArrayList.size()+1));
        questionTitle.setText(current.question);
        answer1.setText(current.answer1);
        answer2.setText(current.answer2);
        answer3.setText(current.answer3);
    }

}

