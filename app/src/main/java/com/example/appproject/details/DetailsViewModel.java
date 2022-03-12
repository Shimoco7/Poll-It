package com.example.appproject.details;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.appproject.model.detail.Detail;
import com.example.appproject.model.Model;

import java.util.ArrayList;
import java.util.List;

public class DetailsViewModel extends ViewModel {
   // LiveData<List<Detail>> details;
    LiveData<List<Detail>> questions;

    public DetailsViewModel() {
       // details = Model.instance.getDetails();
        questions = Model.instance.getQuestions();

    }

//    public LiveData<List<Detail>> getDetails() {
//        return details;
//    }

    public LiveData<List<Detail>> getMultiChoiceQuestions() {
        return questions;
    }

}
