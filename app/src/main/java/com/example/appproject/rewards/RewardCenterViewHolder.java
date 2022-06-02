package com.example.appproject.rewards;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appproject.MyApplication;
import com.example.appproject.R;
import com.example.appproject.model.General;
import com.example.appproject.model.Model;
import com.example.appproject.model.listeners.OnItemClickListener;
import com.example.appproject.model.reward.Reward;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

public class RewardCenterViewHolder extends RecyclerView.ViewHolder {

    ShapeableImageView supplierImage;
    ShapeableImageView clickImage;
    MaterialTextView prizeName;
    MaterialTextView price;
    MaterialCardView card;
    Integer coins;

    public RewardCenterViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
        super(itemView);
        supplierImage = itemView.findViewById(R.id.prizerow_image);
        clickImage = itemView.findViewById(R.id.prizerow_click_image);
        prizeName = itemView.findViewById(R.id.prizerow_txt_prizename);
        price = itemView.findViewById(R.id.prizerow_txt_price);
        card = itemView.findViewById(R.id.prizerow_cardView);
        clickImage.setOnClickListener(v->{
            int pos = getAdapterPosition();
            onItemClickListener.onItemClick(v,pos);
        });
    }

    public void bind(Reward reward) {
        Model.instance.getUserRankAndCoins(MyApplication.getUserKey(), map->{
            coins = (Integer) map.get(MyApplication.getContext().getString(R.string.user_coins));
            price.setText(String.valueOf(reward.getPrice()));
            prizeName.setText(reward.getTitle());
            if (coins<reward.getPrice()){
                card.setAlpha((float)0.25);
                card.setClickable(false);
                clickImage.setClickable(false);
            }
            if(reward.getSupplierImage() != null){
                General.loadImage(reward.getSupplierImage(), supplierImage,R.drawable.loadimagesmall);
            }
        });
    }
}
