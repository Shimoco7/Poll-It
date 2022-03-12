package com.example.appproject.details;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavAction;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.appproject.BuildConfig;
import com.example.appproject.MainActivity;
import com.example.appproject.MyApplication;
import com.example.appproject.R;
import com.example.appproject.model.General;
import com.example.appproject.model.Model;
import com.example.appproject.model.detail.Detail;
import com.example.appproject.model.user.User;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

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
            if(!informationwasfilled()){ return; };
            if (!detailsValidations()) {
                return;
            }
            uploadDetailsToDB();
            Navigation.findNavController(finishBtn).navigate(R.id.action_fragmentUserDetails_to_userImage);
        });




        return view;
    }

    public boolean informationwasfilled(){

        ArrayList<String> error = new ArrayList<>();
        for (int i = 0; i < list.getChildCount(); i++) {
            DetailsHolder holder = (DetailsHolder) list.findViewHolderForAdapterPosition(i);
            if (holder==null|| holder.answersAc.getText().toString().equals("")) {
                error.add("You forgot to fill in your "+holder.questionTv.getHint().toString());
            }

        }
        if(nameEt.getText().toString().trim().equals("") || nameEt.getText()==null){
            error.add("You forgot to fill in your name");
        }

        if(addressEt.getText().toString().trim().equals("") || addressEt.getText()==null){
            error.add("You forgot to fill in your address");
        }

        if(error.size()>0){
            General.showToast(getActivity(),error);
            return false;
        }

        return true;

    }

    public boolean detailsValidations(){
        ArrayList<String> error = new ArrayList<>();
        if(!nameEt.getText().toString().matches("^[a-zA-Z\\s]+")){
            error.add("You have to choose a valid name");
        }

        if(!addressValidation()) {
            error.add("Your address is not according to the format");
        }


        if(error.size()>0){
            General.showToast(getActivity(),error);
            return false;
        }



        return true;

    }


    public boolean addressValidation(){
        String[] addressList = addressEt.getText().toString().trim().split(",");
        if(addressList.length!=4) {
            return false;
        }
        for(int i=0; i<3; i++){
            if(!addressList[i].matches("^[a-zA-Z\\s]+")){
               return false;
            }
        }

        if(!addressList[3].matches("-?\\d+(\\.\\d+)?")){
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