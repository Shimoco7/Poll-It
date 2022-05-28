package com.example.appproject.rewards;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appproject.R;
import com.example.appproject.model.General;
import com.example.appproject.model.reward.Order;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

import java.text.DateFormat;
import java.util.Date;

public class UserOrdersViewHolder extends RecyclerView.ViewHolder {

    MaterialTextView orderName;
    MaterialTextView orderCode;
    MaterialTextView orderExpiryDate;
    ShapeableImageView supplierImage;

    public UserOrdersViewHolder(@NonNull View itemView) {
        super(itemView);
        supplierImage = itemView.findViewById(R.id.userPrizeRow_image);
        orderExpiryDate = itemView.findViewById(R.id.userPrizeRow_txt_date);
        orderCode = itemView.findViewById(R.id.userPrizeRow_txt_code);
        orderName = itemView.findViewById(R.id.userPrizeRow_txt_prizename);
    }

    public void bind(Order order) {
//        orderName.setText(order.get);
        orderCode.setText(order.getId().substring(14));
        Date d = new Date(order.getExpirationDate() * 1000);
        String dateAfterFormat  = DateFormat.getDateInstance().format(d);
        //TODO - if expiration date passed - set alpha to 0.25
        orderExpiryDate.setText(dateAfterFormat);
        if(order.getSupplierImage() != null){
            General.loadImage(order.getSupplierImage(),supplierImage,R.drawable.loadimagesmall);
        }
    }
}
