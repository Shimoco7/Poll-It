package com.example.appproject.details;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import android.widget.Toast;

import com.example.appproject.BuildConfig;
import com.example.appproject.MainActivity;
import com.example.appproject.R;
import com.example.appproject.model.detail.Detail;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.ArrayList;
import java.util.Arrays;

public class FragmentUserDetails extends Fragment {
    Button finishBtn;
    DetailsViewModel detailsViewModel;
    DetailsAdapter detailsAdapter;
    RecyclerView list;
    PlacesClient placesClient;


    public FragmentUserDetails() { }

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

        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        detailsAdapter = new DetailsAdapter(detailsViewModel,getLayoutInflater());
        list.setAdapter(detailsAdapter);
        detailsAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Log.d("TAG","row was clicked " + position);
                String id = detailsViewModel.getDetails().get(position).getQuestion();

            }
        });
        finishBtn = view.findViewById(R.id.userDetails_next_btn);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        finishBtn.setOnClickListener(v -> {
            ArrayList<String> error = new ArrayList<>();
            for (Detail detail: detailsViewModel.getDetails()){
                if(detail.getFinalAnswer()==""||detail.getFinalAnswer()==null ){
                    error.add("Please fill all the details first");
                    showToast(error);
                    return;
                }

            }
            Intent intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);
            getActivity().finish();
        });
        initPlaces();
        return view;
    }


    private void showToast(ArrayList<String> errors) {
        StringBuilder message = new StringBuilder();
        for(String error : errors){
            message.append(error);
            message.append("\n");
        }
        Toast.makeText(getActivity(), message.toString().trim(),
                Toast.LENGTH_LONG).show();
    }

    private void initPlaces(){
        Places.initialize(getActivity().getApplicationContext(), BuildConfig.MAPS_API_KEY);
        placesClient = Places.createClient(this.getContext());
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onError(@NonNull Status status) {
                Log.i("TAG", "An error occurred: " + status);
            }

            @Override
            public void onPlaceSelected(@NonNull Place place) {
                Log.i("TAG", "Place: " + place.getName() + ", " + place.getId());
            }
        });
    }



}