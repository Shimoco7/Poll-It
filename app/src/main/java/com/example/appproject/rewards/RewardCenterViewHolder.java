package com.example.appproject.rewards;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appproject.R;
import com.example.appproject.model.General;
import com.example.appproject.model.listeners.OnItemClickListener;
import com.example.appproject.model.reward.Reward;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

public class RewardCenterViewHolder extends RecyclerView.ViewHolder {

    ShapeableImageView supplierImage;
    ShapeableImageView clickImage;
    MaterialTextView prizeName;
    MaterialTextView price;

    public RewardCenterViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
        super(itemView);
        supplierImage = itemView.findViewById(R.id.prizerow_image);
        clickImage = itemView.findViewById(R.id.prizerow_click_image);
        prizeName = itemView.findViewById(R.id.prizerow_txt_prizename);
        price = itemView.findViewById(R.id.prizerow_txt_price);
        clickImage.setOnClickListener(v->{
            int pos = getAdapterPosition();
            onItemClickListener.onItemClick(v,pos);
        });
    }

    public void bind(Reward reward) {
        price.setText(String.valueOf(reward.getPrice()));
        prizeName.setText(reward.getTitle());
        if(reward.getSupplierImage() != null){
            General.loadImage(reward.getSupplierImage(), supplierImage,R.drawable.loadimagebig);
        }
    }
}
