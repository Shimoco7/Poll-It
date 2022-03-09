package com.example.appproject.details;

import android.view.View;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appproject.R;

class DetailsHolder extends RecyclerView.ViewHolder {
    Spinner questionSp;

    public DetailsHolder(@NonNull View itemView, FragmentUserDetails.OnItemClickListener listener) {
        super(itemView);
        questionSp = itemView.findViewById(R.id.listrow_question_sp);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = getAdapterPosition();
                listener.onItemClick(pos);
            }
        });
    }
}
