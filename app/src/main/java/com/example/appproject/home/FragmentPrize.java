package com.example.appproject.home;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.appproject.MyApplication;
import com.example.appproject.R;
import com.example.appproject.model.Model;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentPrize#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentPrize extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentPrize() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment prize.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentPrize newInstance(String param1, String param2) {
        FragmentPrize fragment = new FragmentPrize();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_prize, container, false);
        Button collectBtn=view.findViewById(R.id.prize_btn_collect);
        collectBtn.setOnClickListener((v -> {
            popup();
        }));
        return view;
    }

    private void popup(){
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setMessage("Are You Sure?")
                .setPositiveButton("BUY", (dialog, which) -> {
                    //Navigation.findNavController(v).navigate(FragmentHomeScreenDirections.actionFragmentHomeScreenToFragmentActivePoll(pollId));
                })
                .setNeutralButton("Cancel", null);
        AlertDialog alert1 = alert.create();
        alert1.show();
    }
}