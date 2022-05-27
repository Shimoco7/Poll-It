package com.example.appproject.home;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

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
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textview.MaterialTextView;


public class FragmentPrize extends Fragment {

    String rewardId;
    Integer cost;
    MaterialTextView totalCoins;
    MaterialTextView description;
    MaterialTextView price;
    ShapeableImageView prizeImage, supplierImage;
    ProgressBar progressBar;

    public FragmentPrize() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_prize, container, false);
        progressBar = view.findViewById(R.id.prize_progress_bar);
        totalCoins = view.findViewById(R.id.rewardCenter_text_coinsNumber);
        prizeImage = view.findViewById(R.id.prize_img_thePrize);
        supplierImage = view.findViewById(R.id.prize_img_company);
        description = view.findViewById(R.id.prize_txt_details);
        price = view.findViewById(R.id.prize_txt_price);
        Button collectBtn=view.findViewById(R.id.prize_btn_collect);

        rewardId = FragmentPrizeArgs.fromBundle(getArguments()).getRewardId();
        General.progressBarOn(getActivity(),container,progressBar,false);

        Model.instance.getRewardFromLocalDb(rewardId,reward->{
            totalCoins.setText(MyApplication.getUserCoins());
            description.setText(reward.getDescription());
            cost = reward.getPrice();
            price.setText(String.valueOf(cost));
            if(reward.getImage() != null){
                General.loadImage(reward.getImage(), prizeImage,R.drawable.loadimagebig);
            }
            else{
                prizeImage.setImageResource(R.drawable.giftbox);
            }
            if(reward.getSupplierImage() != null){
                General.loadImage(reward.getSupplierImage(), supplierImage,R.drawable.loadimagebig);
            }
            else{
                supplierImage.setImageResource(R.drawable.default_poll_image);
            }
            General.progressBarOff(getActivity(),container,progressBar,true);
        });
        collectBtn.setOnClickListener((v -> {
            popup(container);
        }));
        return view;
    }

    private void popup(ViewGroup container){
        AlertDialog.Builder alert = new AlertDialog.Builder(requireContext());
        alert.setMessage("Are You Sure?")
                .setPositiveButton("BUY", (dialog, which) -> {
                    General.progressBarOn(requireActivity(),container,progressBar,false);
                    Model.instance.redeemReward(rewardId, user->{
                        if(user != null){
                            Navigation.findNavController(price).navigate(FragmentPrizeDirections.actionGlobalFragmentHomeScreen());
                        }
                        else{
                            General.progressBarOff(requireActivity(),container,progressBar,true);
                            Snackbar.make(requireView(),getString(R.string.server_is_off),Snackbar.LENGTH_INDEFINITE).setAction("Redeem Failed... Try again later", view->{ }).show();
                        }
                    });
                })
                .setNeutralButton("Cancel", null);
        AlertDialog alert1 = alert.create();
        alert1.show();
    }
}