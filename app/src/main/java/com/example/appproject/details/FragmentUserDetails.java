package com.example.appproject.details;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.appproject.MyApplication;
import com.example.appproject.R;
import com.example.appproject.model.General;
import com.example.appproject.model.Model;
import com.example.appproject.model.detail.Detail;
import com.example.appproject.model.question.Question;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FragmentUserDetails extends Fragment {
    Button nextBtn;
    EditText nameEt;
    TextInputLayout nameTi;
    EditText addressEt;
    TextInputLayout addressTi;
    DetailsViewModel detailsViewModel;
    DetailsAdapter detailsAdapter;
    ProgressBar detailsProgressBar;
    Boolean isNameEmpty=true,isAddressEmpty=true;
    ViewGroup container;
    RecyclerView list;


    public FragmentUserDetails() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        detailsViewModel = new ViewModelProvider(this).get(DetailsViewModel.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if(menu.findItem(R.id.main_menu_settings)!=null) {
            menu.findItem(R.id.main_menu_settings).setVisible(false);
            super.onPrepareOptionsMenu(menu);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_details, container, false);
        this.container = container;
        list = view.findViewById(R.id.details_list_rv);
        nameEt = view.findViewById(R.id.details_name_et);
        nameTi = view.findViewById(R.id.details_ti);
        addressEt = view.findViewById(R.id.details_address_et);
        addressTi = view.findViewById(R.id.details_address_ti);
        if (!MyApplication.getUserAddress().equals(""))
            addressEt.setText(MyApplication.getUserAddress());
        if (!MyApplication.getUserName().equals(""))
            nameEt.setText(MyApplication.getUserName());
        nextBtn = view.findViewById(R.id.userDetails_next_btn);
        detailsProgressBar = view.findViewById(R.id.details_progress_bar);
        nameEt.setVisibility(View.GONE);
        nameTi.setVisibility(View.GONE);
        addressEt.setVisibility(View.GONE);
        addressTi.setVisibility(View.GONE);
        nextBtn.setVisibility(View.GONE);
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        detailsAdapter = new DetailsAdapter(detailsViewModel, getLayoutInflater());
        list.setAdapter(detailsAdapter);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        setInputListeners();
        nextBtn.setOnClickListener(v -> {
            if(!allDetailsFilled()){ return; };
            uploadDetailsToDB();
            Navigation.findNavController(nextBtn).navigate(R.id.action_fragmentUserDetails_to_userImage);
        });

        detailsViewModel.getQuestions().observe(getViewLifecycleOwner(), questionsList->refresh());
        General.progressBarOn(getActivity(),container,detailsProgressBar);
        Model.instance.refreshQuestions();
        return view;
    }

    @SuppressLint("RestrictedApi")
    private void setInputListeners(){

        nameEt.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                nameTi.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                nameTi.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
                nameTi.setErrorIconDrawable(null);
                if (s.toString().length() == 0) {
                    nameTi.setError(null);
                    isNameEmpty =true;
                }
                String regex= "^[a-zA-Z\\s]+.{4,20}";

                if(!s.toString().matches(regex)||s.toString().length()>15){
                    nameTi.setError("Invalid Name");
                }
                if(s.toString().length()==0){
                    nameTi.setError(null);
                    nameTi.setHelperTextEnabled(true);
                }


            }
        });

        addressEt.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                addressTi.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                addressTi.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
                addressTi.setErrorIconDrawable(null);
                if (s.toString().length() == 0) {
                    addressTi.setError(null);
                    isAddressEmpty = true; }


                }


        });


    }

    @SuppressLint("NotifyDataSetChanged")
    private void refresh() {
        detailsAdapter.notifyDataSetChanged();
        nameEt.setVisibility(View.VISIBLE);
        nameTi.setVisibility(View.VISIBLE);
        addressEt.setVisibility(View.VISIBLE);
        addressTi.setVisibility(View.VISIBLE);
        nextBtn.setVisibility(View.VISIBLE);
        General.progressBarOff(getActivity(),container,detailsProgressBar);
    }

    @SuppressLint("NotifyDataSetChanged")
    public boolean allDetailsFilled(){

        ArrayList<String> errors = new ArrayList<>();
        if(!Model.instance.validateName(nameEt.getText().toString().trim())){
            errors.add(getString(R.string.invalid_name));
            if (isNameEmpty) {
                nameTi.setError(getString(R.string.missing_name));
            } else {
                nameTi.setError(getString(R.string.invalid_name));
            }
            nameTi.setErrorIconDrawable(null);

        }

        for(String question: detailsViewModel.getAnswersMap().keySet()){
            if(Objects.equals(detailsViewModel.getAnswersMap().get(question), "")){
                detailsViewModel.getAnswersMap().put(question, "Empty");
            }
            if(Objects.equals(detailsViewModel.getAnswersMap().get(question), "Empty")){
                errors.add(question+" is Required");
            }
        }


        if(!Model.instance.validateAddress(addressEt.getText().toString().trim())){
            errors.add(getString(R.string.invalid_address));
            if (isAddressEmpty) addressTi.setError(getString(R.string.missing_address));
            else addressTi.setError(getString(R.string.invalid_address));
            addressTi.setErrorIconDrawable(null);
        }
        if(errors.size()>0){
            detailsAdapter.notifyDataSetChanged();
            return false;
        }
        return true;

    }


    public void uploadDetailsToDB(){
        Model.instance.updateUser(MyApplication.getUserKey(),"name",nameEt.getText().toString().trim(), ()->{
            MyApplication.setUserName(nameEt.getText().toString().trim());
        });
        Model.instance.updateUser(MyApplication.getUserKey(),"address",addressEt.getText().toString().trim(), ()->{
            MyApplication.setUserAddress(addressEt.getText().toString().trim());
        });
    }

}