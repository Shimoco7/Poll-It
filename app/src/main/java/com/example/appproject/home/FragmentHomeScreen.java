package com.example.appproject.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.example.appproject.MyApplication;
import com.example.appproject.R;
import com.example.appproject.model.Model;
import com.example.appproject.model.poll.PollQuestion;
import com.example.appproject.poll.FragmentActivePollDirections;
import com.example.appproject.rewards.FragmentUserOrdersArgs;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textview.MaterialTextView;

import java.util.Objects;


public class FragmentHomeScreen extends Fragment {

    HomeViewModel homeViewModel;
    HomeAdapter homeAdapter;
    RecyclerView list;
    SwipeRefreshLayout swipeRefresh;
    MaterialTextView coinBalance,isActivePolls;
    Button line;


    public FragmentHomeScreen() { }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_screen, container, false);
        swipeRefresh = view.findViewById(R.id.home_layout_poll_refresh);
        list = view.findViewById(R.id.home_poll_rv);
        coinBalance = view.findViewById(R.id.homeScr_text_coinsNumber);
        Button btnToRewardCenter = view.findViewById(R.id.homescr_btn_rewardCenter);
        isActivePolls=view.findViewById(R.id.homescr_txt_noOrder);
        line = view.findViewById(R.id.homescr_btn_line2);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        MaterialTextView userName = view.findViewById(R.id.homeScr_text_name);
        userName.setText(MyApplication.getUserName());
        Model.instance.getUserRankAndCoins(MyApplication.getUserKey(),map->{
            Integer coins = (Integer) map.get(getString(R.string.user_coins));
            coinBalance.setText(String.valueOf(coins));
        });

        list.setHasFixedSize(true);
        int numOfColumns = 2;
        list.setLayoutManager(new GridLayoutManager(getContext(), numOfColumns,GridLayoutManager.VERTICAL,false));
        homeAdapter = new HomeAdapter(homeViewModel,getLayoutInflater());
        list.setAdapter(homeAdapter);

        list.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.set(5,20,5,20);
            }
        });

        //Check if there is active polls
        homeViewModel.getPolls().observe(getViewLifecycleOwner(),pollsList->{
            if (pollsList.size()==0) isActivePolls.setVisibility(View.VISIBLE);
            else isActivePolls.setVisibility(View.GONE);
            refresh();

        });
        homeAdapter.setOnItemClickListener((v,pos)->{
            String pollId = Objects.requireNonNull(homeViewModel.getPolls().getValue()).get(pos).getPollId();
            Model.instance.getLastUnansweredPollQuestion(pollId,(pollQuestion,isSingleQuestionAndAnswered) -> {
                if(isSingleQuestionAndAnswered || (pollQuestion != null && !pollQuestion.getQuestionNumber().equals(1))){
                    Model.instance.getMainThread().post(()->navigateToPollQuestion(pollId,pollQuestion));
                }
                else{
                    Model.instance.getMainThread().post(()->Navigation.findNavController(v).navigate(FragmentHomeScreenDirections.actionFragmentHomeScreenToFragmentActivePoll(pollId)));
                }
            });
        });

        boolean isPollFilled = FragmentHomeScreenArgs.fromBundle(getArguments()).getIsPollFilled();
        if (isPollFilled) {
            Model.instance.getMainThread().post(() ->
            {
                Snackbar.make(requireView(), "Congratulations ! The survey has been filled", 2000)
                        .setBackgroundTint(requireContext().getColor(R.color.primeGreen))
                        .setTextColor(requireContext().getColor(R.color.white))
                        .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_FADE)
                        .setAnchorView(line)
                        .show();

            });
        }

        Model.instance.getUserById(MyApplication.getUserKey(),user->{
            if(user.getRank()>=6){
                badRank();
            }
        });

        btnToRewardCenter.setOnClickListener(v->Navigation.findNavController(v).navigate(FragmentHomeScreenDirections.actionFragmentHomeScreenToFragmentRewardCenter()));
        swipeRefresh.setOnRefreshListener(Model.instance::refreshPollsList);
        observePollsLoadingState();
        Model.instance.getMainThread().post(Model.instance::refreshPollsList);
        return view;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void refresh() {
        homeAdapter.notifyDataSetChanged();
    }

    private void observePollsLoadingState() {
        Model.instance.getPollsListLoadingState().observe(getViewLifecycleOwner(),usersListLoadingState ->{
            switch (usersListLoadingState){
                case loading:
                    swipeRefresh.setRefreshing(true);
                    break;
                case loaded:
                    swipeRefresh.setRefreshing(false);
                    break;
            }
        });
    }

    private void navigateToPollQuestion(String pollId, PollQuestion pollQuestion) {
        switch (pollQuestion.getPollQuestionType()){
            case Multi_Choice:{
                Navigation.findNavController(list)
                        .navigate(FragmentHomeScreenDirections
                                .actionFragmentHomeScreenToFragmentPollQuestionMultiChoice(pollId,pollQuestion.getPollQuestionId(),false));
                break;
            }
            case Image_Question:{
                Navigation.findNavController(list)
                        .navigate(FragmentHomeScreenDirections
                                .actionFragmentHomeScreenToFragmentPollQuestionMultiChoice(pollId,pollQuestion.getPollQuestionId(),true));
                break;
            }
            case Image_Answers:{
                Navigation.findNavController(list)
                        .navigate(FragmentHomeScreenDirections
                                .actionFragmentHomeScreenToFragmentPollQuestionImageAnswers(pollId,pollQuestion.getPollQuestionId()));
                break;
            }
            default:
                throw new IllegalStateException("Unexpected value: " + pollQuestion.getPollQuestionType());
        }
    }

    private void badRank(){
        Model.instance.getMainThread().post(() ->
                Snackbar.make(getView(),getString(R.string.rank_is_over_six),Snackbar.LENGTH_INDEFINITE).setAction(" I Understand",view->{
                })
                        .setBackgroundTint(requireContext().getColor(R.color.primeRed))
                        .setTextColor(requireContext().getColor(R.color.white))
                        .show());

    }

}