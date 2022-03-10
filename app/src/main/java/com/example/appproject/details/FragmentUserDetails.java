package com.example.appproject.details;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.appproject.MainActivity;
import com.example.appproject.R;
import com.example.appproject.feed.FeedViewModel;
import com.example.appproject.model.Detail;

import java.util.ArrayList;
import java.util.LinkedHashMap;

///**
// * A simple {@link Fragment} subclass.
// * Use the {@link FragmentUserDetails#newInstance} factory method to
// * create an instance of this fragment.
// */
public class FragmentUserDetails extends Fragment {
    Button finishBtn;
    LinkedHashMap<String, ArrayList<String>> detailsQuestions; //for tests
    DetailsViewModel detailsViewModel;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        detailsViewModel = new ViewModelProvider(this).get(DetailsViewModel.class);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_details, container, false);
        detailsQuestions =getQuestions();
        Log.d("TAG", "size: "+detailsQuestions.size());

        RecyclerView list = view.findViewById(R.id.details_list_rv);
        list.setHasFixedSize(true);

        list.setLayoutManager(new LinearLayoutManager(getContext()));
        DetailsAdapter detailsAdapter = new DetailsAdapter(this);
        list.setAdapter(detailsAdapter);
       // detailsAdapter.setOnItemClickListener(position -> Log.d("TAG", "row was clicked "+position));
        finishBtn = view.findViewById(R.id.userDetails_next_btn);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        finishBtn.setOnClickListener(v -> {
            Log.d("TAG", ""+list.getChildCount());
            for (int i = 0; i < list.getChildCount(); i++) {
                DetailsHolder holder = (DetailsHolder) list.findViewHolderForAdapterPosition(i);
                if (holder!=null) {
                    String question = (String) detailsQuestions.keySet().toArray()[i];
                    Log.d("TAG", "The answer for " +question+"is :"+ holder.questionSp.getSelectedItem().toString());
                }
            }
            Intent intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);
            getActivity().finish();
        });
        return view;
    }

    interface OnItemClickListener {
        void onItemClick(int position);
    }

    public LinkedHashMap<String, ArrayList<String>> getQuestions() {
        ArrayList<Detail> arrayList = detailsViewModel.getDetails();
        detailsQuestions = new LinkedHashMap<>();
        for (Detail detail : arrayList) {
            detailsQuestions.put(detail.getQuestion(), detail.getAnswers());
        }
        return detailsQuestions;
    }

}