package com.example.appproject.details;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.appproject.R;
import com.example.appproject.model.General;
import com.example.appproject.model.Model;
import com.example.appproject.model.detail.Detail;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class FragmentUserDetails extends Fragment {
    Button finishBtn;
    EditText nameEt;
    TextInputLayout nameTi;
    EditText addressEt;
    TextInputLayout addressTi;
    DetailsViewModel detailsViewModel;
    DetailsAdapter detailsAdapter;
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
        finishBtn = view.findViewById(R.id.userDetails_next_btn);
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        detailsAdapter = new DetailsAdapter(detailsViewModel, getLayoutInflater());
        list.setAdapter(detailsAdapter);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        finishBtn.setOnClickListener(v -> {
            if(!allDetailsFilled()){ return; };
            uploadDetailsToDB();
            Navigation.findNavController(finishBtn).navigate(R.id.action_fragmentUserDetails_to_userImage);
        });




        return view;
    }

    public boolean allDetailsFilled(){

        ArrayList<String> errors = new ArrayList<>();
        if(!Model.instance.validateName(nameEt.getText().toString().trim())){
            errors.add(getString(R.string.invalid_name));
        }

        if(!Model.instance.validateAddress(addressEt.getText().toString().trim())){
            errors.add(getString(R.string.invalid_address));
        }
        for (int i = 0; i < list.getChildCount(); i++) {
            DetailsHolder holder = (DetailsHolder) list.findViewHolderForAdapterPosition(i);
            if (holder==null|| holder.answersAc.getText().toString().equals("")) {
                errors.add("Invalid "+holder.questionTv.getHint().toString());
            }

        }
        if(errors.size()>0){
            General.showToast(getActivity(),errors);
            return false;
        }

        return true;

    }


    public void uploadDetailsToDB(){
        Detail detail;
        detail = new Detail(nameTi.getHint().toString().trim(),nameEt.getText().toString().trim());
        Model.instance.saveDetailOnDb(detail,()->{ });
        detail = new Detail(addressTi.getHint().toString().trim(),nameEt.getText().toString().trim());
        Model.instance.saveDetailOnDb(detail,()->{ });
        for (int i = 0; i < list.getChildCount(); i++) {
            DetailsHolder holder = (DetailsHolder) list.findViewHolderForAdapterPosition(i);
            detail = new Detail(holder.questionTv.getHint().toString().trim(),holder.answersAc.getText().toString().trim());
            Model.instance.saveDetailOnDb(detail,()->{ });
        }
    }

}