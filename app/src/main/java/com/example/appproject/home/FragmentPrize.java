package com.example.appproject.home;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.appproject.MyApplication;
import com.example.appproject.R;
import com.example.appproject.model.General;
import com.example.appproject.model.Model;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import okhttp3.OkHttpClient;
import okhttp3.Request;


public class FragmentPrize extends Fragment {

    String rewardId;
    MaterialTextView totalCoins;
    MaterialTextView description;
    MaterialTextView rewardName;
    MaterialTextView price;
    ShapeableImageView prizeImg;
    ProgressBar progressBar;

    public FragmentPrize() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_prize, container, false);
        progressBar = view.findViewById(R.id.prize_progress_bar);
        totalCoins = view.findViewById(R.id.rewardCenter_text_coinsNumber);
        prizeImg = view.findViewById(R.id.prize_img_thePrize);
        description = view.findViewById(R.id.prize_txt_details);
        rewardName = view.findViewById(R.id.prize_txt_prizename);
        price = view.findViewById(R.id.prize_txt_price);
        Button collectBtn=view.findViewById(R.id.prize_btn_collect);


        rewardId = FragmentPrizeArgs.fromBundle(getArguments()).getRewardId();
        General.progressBarOn(getActivity(),container,progressBar,false);

        Model.instance.getRewardFromLocalDb(rewardId,reward->{
            totalCoins.setText(MyApplication.getUserCoins());
            description.setText(reward.getDescription());
            rewardName.setText(reward.getTitle());
            price.setText(String.valueOf(reward.getPrice()));
            if(reward.getImage() != null){
                OkHttpClient client = new OkHttpClient.Builder().addInterceptor(chain -> {
                    Request newRequest = chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer " + MyApplication.getAccessToken())
                            .build();
                    return chain.proceed(newRequest);
                }).build();
                Picasso picasso = new Picasso.Builder(MyApplication.getContext()).downloader(new OkHttp3Downloader(client)).build();
                picasso.load(reward.getImage())
                        .placeholder(R.drawable.loadimagebig)
                        .into(prizeImg);
            }
            else{
                //TODO- handle default prize image
                prizeImg.setImageResource(R.drawable.giftbox);

            }
            General.progressBarOff(getActivity(),container,progressBar,true);
        });
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