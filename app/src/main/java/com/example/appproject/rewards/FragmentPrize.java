package com.example.appproject.rewards;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.menu.MenuAdapter;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.appproject.MyApplication;
import com.example.appproject.R;
import com.example.appproject.model.General;
import com.example.appproject.model.Model;
import com.example.appproject.model.listeners.OnItemClickListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;

import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;


public class FragmentPrize extends Fragment {

    String rewardId;
    Integer cost;
    Integer userCoins;
    MaterialTextView totalCoins;
    MaterialTextView description;
    MaterialTextView price;
    MaterialTextView totalPriceTxt;
    ShapeableImageView prizeImage, supplierImage;
    ProgressBar progressBar;
    TextInputLayout dropMenuTextInputLayout;
    AutoCompleteTextView autoCompleteTextView;
    Integer qnt =1;

    public FragmentPrize() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_prize, container, false);
        progressBar = view.findViewById(R.id.prize_progress_bar);
        totalCoins = view.findViewById(R.id.rewardCenter_text_coinsNumber);
        prizeImage = view.findViewById(R.id.prize_img_thePrize);
        totalPriceTxt = view.findViewById(R.id.prize_txt_total_coins);
        supplierImage = view.findViewById(R.id.prize_img_company);
        description = view.findViewById(R.id.prize_txt_details);
        dropMenuTextInputLayout = view.findViewById(R.id.prize_text_input);
        price = view.findViewById(R.id.prize_txt_price);
        Button collectBtn = view.findViewById(R.id.prize_btn_collect);
        autoCompleteTextView = view.findViewById(R.id.prize_autoC);

        rewardId = FragmentPrizeArgs.fromBundle(getArguments()).getRewardId();
        General.progressBarOn(getActivity(), container, progressBar, false);


        //DropDown Menu

        String[] menuItems = {"1", "2", "3", "4"};
        ArrayAdapter adapter = new ArrayAdapter<>(requireContext(), R.layout.prize_menu_item, menuItems);
        autoCompleteTextView.post(() -> autoCompleteTextView.setAdapter(adapter));
        autoCompleteTextView.setOnItemClickListener((adapterView, viewb, i, l) -> {
            qnt = Integer.parseInt(adapterView.getItemAtPosition(i).toString());
            Integer ans = qnt * cost;
            totalPriceTxt.setText(String.valueOf(ans));
            if (ans > this.userCoins) {
                dropMenuTextInputLayout.setError("Not enough coins");
                collectBtn.setAlpha((float) 0.25);
                collectBtn.setClickable(false);
                collectBtn.setFocusable(false);
            } else {
                dropMenuTextInputLayout.setError(null);
                collectBtn.setAlpha((float) 1);
                collectBtn.setClickable(true);
                collectBtn.setFocusable(true);
            }


        });


        Model.instance.getRewardFromLocalDb(rewardId, reward -> {
            Model.instance.getUserRankAndCoins(MyApplication.getUserKey(), map -> {
                Integer coins = (Integer) map.get(getString(R.string.user_coins));
                totalCoins.setText(String.valueOf(coins));
                this.userCoins = coins;
            });
            description.setText(reward.getDescription());
            cost = reward.getPrice();
            price.setText(String.valueOf(cost));
            totalPriceTxt.setText(String.valueOf(cost));
            if (reward.getImage() != null) {
                General.loadImage(reward.getImage(), prizeImage, R.drawable.loadimagebig);
            } else {
                prizeImage.setImageResource(R.drawable.giftbox);
            }
            if (reward.getSupplierImage() != null) {
                General.loadImage(reward.getSupplierImage(), supplierImage, R.drawable.loadimagebig);
            } else {
                supplierImage.setImageResource(R.drawable.default_poll_image);
            }
            General.progressBarOff(getActivity(), container, progressBar, true);
        });
        collectBtn.setOnClickListener((v -> {
            popup(container);
        }));
        return view;
    }

    private void popup(ViewGroup container) {
        AlertDialog.Builder alert = new AlertDialog.Builder(requireContext());
        alert.setMessage("Are You Sure?")
                .setPositiveButton("BUY", (dialog, which) -> {
                    General.progressBarOn(requireActivity(), container, progressBar, false);
                    Model.instance.redeemReward(rewardId, user -> {
                        if (user != null) {

                            Navigation.findNavController(price).navigate(FragmentPrizeDirections.actionGlobalFragmentHomeScreen());
                        } else {
                            General.progressBarOff(requireActivity(), container, progressBar, true);
                            Snackbar.make(requireView(), getString(R.string.server_is_off), Snackbar.LENGTH_INDEFINITE).setAction("Redeem Failed... Try again later", view -> {
                            }).show();
                        }
                    });
                })
                .setNeutralButton("Cancel", null);
        AlertDialog alert1 = alert.create();
        alert1.show();
    }



}