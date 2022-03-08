package com.example.appproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.appproject.model.Model;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

///**
// * A simple {@link Fragment} subclass.
// * Use the {@link FragmentUserDetails#newInstance} factory method to
// * create an instance of this fragment.
// */
public class FragmentUserDetails extends Fragment {
    Button finishBtn;
    LinkedHashMap<String, String> detailsQuestions;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_details, container, false);
        detailsQuestions = new LinkedHashMap<>();
        detailsQuestions.put("question1", "");
        detailsQuestions.put("question2", "");
        detailsQuestions.put("question3", "");
        detailsQuestions.put("question4", "");
        RecyclerView list = view.findViewById(R.id.details_list_rv);
        list.setHasFixedSize(true);

        list.setLayoutManager(new LinearLayoutManager(getContext()));
        MyAdapter adapter = new MyAdapter();
        list.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                Log.d("TAG", "row was clicked " + position);

            }
        });
        finishBtn = view.findViewById(R.id.userDetails_next_btn);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        finishBtn.setOnClickListener(v -> {
            for (int i = 0; i < list.getChildCount(); i++) {
                MyViewHolder holder = (MyViewHolder) list.findViewHolderForAdapterPosition(i);
                if (holder.questionEt.getText().toString().trim().length() == 0) {
                    detailsQuestions.put(holder.questionTl.getHint().toString(), "");
                } else {
                    detailsQuestions.put(holder.questionTl.getHint().toString(), holder.questionEt.getText().toString());
                }
            }
            for (String s: detailsQuestions.keySet()) {
               Log.d("TAG", s+","+detailsQuestions.get(s));
            }

            Intent intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);
            getActivity().finish();
        });
        return view;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        EditText questionEt;
        TextInputLayout questionTl;

        public MyViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            questionEt = itemView.findViewById(R.id.listrow_question_et);
            questionTl = itemView.findViewById(R.id.listrow_question_ti);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    listener.onItemClick(pos);
                }
            });
        }
    }

    interface OnItemClickListener {
        void onItemClick(int position);
    }

    class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        OnItemClickListener listener;

        public void setOnItemClickListener(OnItemClickListener listener) {
            this.listener = listener;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.details_list_row, parent, false);
            MyViewHolder holder = new MyViewHolder(view, listener);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            String detail = (String) detailsQuestions.keySet().toArray()[position];
            holder.questionTl.setHint(detail);
            holder.questionEt.setText(detailsQuestions.get(detail));

        }

        @Override
        public int getItemCount() {
            return detailsQuestions.size();
        }
    }
}