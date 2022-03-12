package com.example.appproject.details;

import androidx.lifecycle.ViewModel;

import com.example.appproject.model.detail.Detail;
import com.example.appproject.model.Model;

import java.util.ArrayList;
import java.util.List;

public class DetailsViewModel extends ViewModel {
    List<Detail> details;

    public DetailsViewModel() {
        Model.instance.getDetails((list)->{
            details = list;
        });

    }

    public List<Detail> getDetails() {
        return details;
    }

    public List<Detail> getMultiChoiceQuestions() {
        return Model.instance.getMultiChoiceQuestions();
    }

}
