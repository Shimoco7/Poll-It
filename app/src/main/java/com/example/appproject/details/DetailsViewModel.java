package com.example.appproject.details;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class DetailsViewModel extends ViewModel {
    LinkedHashMap<String, ArrayList<String>> detailsQuestions; //for tests

public DetailsViewModel() {
    detailsQuestions = new LinkedHashMap<>();
    detailsQuestions.put("your name:", new ArrayList<>());
    detailsQuestions.get("your name:").add("shimi");
    detailsQuestions.get("your name:").add("emil");
    detailsQuestions.get("your name:").add("lee");
    detailsQuestions.put("your age:", new ArrayList<>());
    detailsQuestions.get("your age:").add("4");
    detailsQuestions.get("your age:").add("5");
    detailsQuestions.get("your age:").add("6");
    detailsQuestions.put("your ...:", new ArrayList<>());
    detailsQuestions.get("your ...:").add("test1");
    detailsQuestions.get("your ...:").add("test2");
    detailsQuestions.get("your ...:").add("test3");


        }

public LinkedHashMap<String, ArrayList<String>> getQuestions() {
        return detailsQuestions;
        }
}
