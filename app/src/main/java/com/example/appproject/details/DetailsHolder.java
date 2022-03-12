package com.example.appproject.details;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appproject.R;
import com.example.appproject.model.detail.Detail;
import com.google.android.material.textfield.TextInputLayout;

public class DetailsHolder extends RecyclerView.ViewHolder {

    TextInputLayout questionTv;

    AutoCompleteTextView answersAc;

    public DetailsHolder(@NonNull View itemView) {
        super(itemView);
        questionTv = itemView.findViewById(R.id.details_list_tv);
        answersAc = itemView.findViewById(R.id.details_list_ac);

    }

    public void bind(Detail detail) {

        questionTv.setHint(detail.getQuestion());
        String[] array = detail.getAnswers().toArray(new String[0]);
        ArrayAdapter adapter = new ArrayAdapter<>(answersAc.getContext(), R.layout.drop_down, array);
        answersAc.setAdapter(adapter);
//        answersAc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                String answer=(String)adapterView.getItemAtPosition(i);
//                detail.setFinalAnswer(answer);
//                Model.instance.saveDetailOnDb(detail,()->{ });
//            }
//        });
    }
}
