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
import com.example.appproject.feed.FragmentUserDisplayDetailsArgs;

public class FragmentActivePoll extends Fragment {

    public FragmentActivePoll() {
    }
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
        View view=inflater.inflate(R.layout.fragment_active_poll,container,false);
        String pollId = FragmentActivePollArgs.fromBundle(getArguments()).getPollId();
        Button startBtn=view.findViewById(R.id.activePoll_start_btn);

//        startBtn.setOnClickListener(Navigation.createNavigateOnClickListener(FragmentActivePollDirections.actionFragmentActivePollToFragmentPollQuestion(pollId)));
        return view;
    }
}