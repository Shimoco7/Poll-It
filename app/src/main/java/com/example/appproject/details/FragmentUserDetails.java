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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.appproject.MyApplication;
import com.example.appproject.R;
import com.example.appproject.model.General;
import com.example.appproject.model.Model;
import com.example.appproject.model.detail.Detail;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

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

    RecyclerView list;


    public FragmentUserDetails() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        detailsViewModel = new ViewModelProvider(this).get(DetailsViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_details, container, false);
        list = view.findViewById(R.id.details_list_rv);
        nameEt = view.findViewById(R.id.details_name_et);
        nameTi = view.findViewById(R.id.details_ti);
        addressEt = view.findViewById(R.id.details_address_et);
        addressTi = view.findViewById(R.id.details_address_ti);
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



      //  detailsViewModel.getDetails().observe(getViewLifecycleOwner(),detailsList->refresh());
        detailsViewModel.getQuestions().observe(getViewLifecycleOwner(), questionsList->refresh());
       // Model.instance.refreshDetails();
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
        detailsProgressBar.setVisibility(View.GONE);

    }

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
        for (int i = 0; i < list.getChildCount(); i++) {
            DetailsHolder holder = (DetailsHolder) list.findViewHolderForAdapterPosition(i);
            if (holder==null|| holder.multiChoiceAc.getText().toString().trim().equals("")) {
                errors.add("Invalid "+holder.questionTv.getHint().toString());
                holder.questionTv.setError(getString(R.string.missing_answer));
                holder.multiChoiceAc.setOnItemClickListener((parent, view, position, id) -> holder.questionTv.setError(null));

            }


        }
        if(!Model.instance.validateAddress(addressEt.getText().toString().trim())){
            errors.add(getString(R.string.invalid_address));
            if (isAddressEmpty) addressTi.setError(getString(R.string.missing_address));
            else addressTi.setError(getString(R.string.invalid_address));
            addressTi.setErrorIconDrawable(null);
        }
        return errors.size() <= 0;

    }


    public void uploadDetailsToDB(){
        Detail detail;
        Model.instance.updateUser(MyApplication.getUserKey(),"name",nameEt.getText().toString().trim(), ()->{});
        Model.instance.updateUser(MyApplication.getUserKey(),"address",addressEt.getText().toString().trim(), ()->{});
        for (int i = 0; i < list.getChildCount(); i++) {
            DetailsHolder holder = (DetailsHolder) list.findViewHolderForAdapterPosition(i);
            detail = new Detail(holder.questionTv.getTag().toString().trim(), holder.questionTv.getHint().toString().trim(),holder.multiChoiceAc.getText().toString().trim());
            Model.instance.saveDetailOnDb(detail,()->{ });
        }
    }

}