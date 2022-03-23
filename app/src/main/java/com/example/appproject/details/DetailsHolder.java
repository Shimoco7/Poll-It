package com.example.appproject.details;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appproject.MyApplication;
import com.example.appproject.R;
import com.example.appproject.model.Model;
import com.example.appproject.model.detail.Detail;
import com.example.appproject.model.question.Question;
import com.google.android.material.textfield.TextInputLayout;

public class DetailsHolder extends RecyclerView.ViewHolder {

    TextInputLayout questionTv;
    AutoCompleteTextView multiChoiceAc;
    DetailsViewModel detailsViewModel;

    public DetailsHolder(@NonNull View itemView,DetailsViewModel detailsViewModel) {
        super(itemView);
        this.detailsViewModel = detailsViewModel;
        questionTv = itemView.findViewById(R.id.details_list_tv);
        multiChoiceAc = itemView.findViewById(R.id.details_list_ac);

    }

    public void bind(Question question) {
        questionTv.setError(null);
        if(!detailsViewModel.getAnswersMap().containsKey(question.getQuestion())){
            detailsViewModel.answersMap.put(question.getQuestion(), "");
        }
        questionTv.setHint(question.getQuestion());
        questionTv.setTag(question.getQuestionId());
        Model.instance.getUserDetailById(MyApplication.getUserKey(), question.getQuestion(), returnedDetail -> {
            if (returnedDetail == null) {
                String[] array = question.getMultiChoice().toArray(new String[0]);
                ArrayAdapter adapter = new ArrayAdapter<>(MyApplication.getContext(), R.layout.drop_down, array);
                multiChoiceAc.post(() -> multiChoiceAc.setAdapter(adapter));

            } else {
                multiChoiceAc.post(() -> multiChoiceAc.setText(returnedDetail.getAnswer()));
                detailsViewModel.getAnswersMap().put(question.getQuestion(), returnedDetail.getAnswer());
                String[] array = question.getMultiChoice().toArray(new String[0]);
                ArrayAdapter adapter = new ArrayAdapter<>(MyApplication.getContext(), R.layout.drop_down, array);
                multiChoiceAc.post(() -> multiChoiceAc.setAdapter(adapter));
            }
        });

        multiChoiceAc.setOnItemClickListener((adapterView, view, i, l) -> {
            String answer = (String) adapterView.getItemAtPosition(i);
            Detail detail = new Detail(MyApplication.getUserKey(),questionTv.getTag().toString().trim(), questionTv.getHint().toString().trim(), multiChoiceAc.getText().toString().trim());
            detail.setAnswer(answer);
            detailsViewModel.answersMap.put(question.getQuestion(), answer);
            questionTv.setError(null);

            Model.instance.getUserDetailById(MyApplication.getUserKey(), question.getQuestion(), returnedDetail -> {
                if (returnedDetail == null) {
                    Model.instance.saveDetailOnLocalDb(detail);
                } else if (!answer.equals(returnedDetail.getAnswer())) {
                    Model.instance.updateAnswerByDetailId(returnedDetail.getDetailId(), answer, () -> {
                    });

                }
            });


        });

        if(detailsViewModel.getAnswersMap().get(question.getQuestion()).equals("Empty")){
            questionTv.setError(question.getQuestion() +" is Required");
        }
    }
}
