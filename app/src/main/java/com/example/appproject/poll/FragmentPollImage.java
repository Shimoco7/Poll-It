package com.example.appproject.poll;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.appproject.R;
import com.google.android.material.button.MaterialButton;


public class FragmentPollImage extends Fragment {

    MaterialButton backBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.main_menu_settings).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_poll_image, container, false);
        backBtn = view.findViewById(R.id.imgpoll_btn_back);
        backBtn.setOnClickListener(v->{
            Navigation.findNavController(v).navigateUp();
        });
        return view;
    }
}