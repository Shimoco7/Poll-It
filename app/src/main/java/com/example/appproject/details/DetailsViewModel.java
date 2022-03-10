package com.example.appproject.details;

import androidx.lifecycle.ViewModel;

import com.example.appproject.model.detail.Detail;
import com.example.appproject.model.Model;

import java.util.ArrayList;

public class DetailsViewModel extends ViewModel {
    ArrayList<Detail> details;

    public DetailsViewModel() {
        details = Model.instance.getDetails();

    }

    public ArrayList<Detail> getDetails() {
        return details;
    }

}
