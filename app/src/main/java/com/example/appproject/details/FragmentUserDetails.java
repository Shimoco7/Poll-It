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

import android.util.Log;
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
    Button nextBtn;
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
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        detailsAdapter = new DetailsAdapter(detailsViewModel, getLayoutInflater());
        list.setAdapter(detailsAdapter);
        detailsAdapter.setOnItemClickListener(position -> {
            Log.d("TAG", "row was clicked " + position);
            String id = detailsViewModel.getDetails().get(position).getQuestion();

        });
        nextBtn = view.findViewById(R.id.userDetails_next_btn);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        nextBtn.setOnClickListener(v -> {
            ArrayList<String> error = new ArrayList<>();
            for (Detail detail : detailsViewModel.getDetails()) {
                if (detail.getFinalAnswer().equals("") || detail.getFinalAnswer() == null) {
                    error.add("Please fill all the details first");
                    General.showToast(getActivity(),error);
                    return;
                }

            }
            if (nameEt.getText().toString().trim().equals("") || nameEt.getText() == null) {
                error.add("You forgot to fill in your name");
                General.showToast(getActivity(),error);
                return;
            }

            if (addressEt.getText().toString().trim().equals("") || addressEt.getText() == null) {
                error.add("You forgot to fill in your address");
                General.showToast(getActivity(),error);
                return;
            }
            Detail detailName = new Detail(nameTi.getHint().toString().trim(), nameEt.getText().toString().trim());
            Model.instance.saveDetailOnDb(detailName, () -> {
            });
            Detail detailAddress = new Detail(addressTi.getHint().toString().trim(), nameEt.getText().toString().trim());
            Model.instance.saveDetailOnDb(detailAddress, () -> {
            });

            Navigation.findNavController(nextBtn).navigate(R.id.action_fragmentUserDetails_to_userImage);
        });




        return view;
    }



}