package com.example.appproject.details;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appproject.R;

class DetailsAdapter extends RecyclerView.Adapter<DetailsHolder> {
    private final FragmentUserDetails fragmentUserDetails;
    FragmentUserDetails.OnItemClickListener listener;

    public DetailsAdapter(FragmentUserDetails fragmentUserDetails) {
        this.fragmentUserDetails = fragmentUserDetails;
    }

    public void setOnItemClickListener(FragmentUserDetails.OnItemClickListener listener) {
        this.listener = listener;
    }


    @NonNull
    @Override
    public DetailsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = fragmentUserDetails.getLayoutInflater().inflate(R.layout.details_list_row, parent, false);
        DetailsHolder detailsHolder = new DetailsHolder(view, listener);
        return detailsHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DetailsHolder detailsHolder, int position) {
        if(fragmentUserDetails.detailsQuestions.size()!=0) {
            String detail = (String) fragmentUserDetails.detailsQuestions.keySet().toArray()[position];
            fragmentUserDetails.detailsQuestions.get(detail).add(0,detail);
            String[] array = fragmentUserDetails.detailsQuestions.get(detail).toArray(new String[0]);
            ArrayAdapter adapter = new ArrayAdapter<String>(fragmentUserDetails.getContext(), R.layout.spinner_item, array){
                @Override
                public boolean isEnabled(int position) {
                    if (position == 0) {
                        return false;
                    } else {
                        return true;
                    }
                }
                @Override
                public View getDropDownView(int position, View convertView,
                                            ViewGroup parent) {
                    View view = super.getDropDownView(position, convertView, parent);
                    TextView tv = (TextView) view;
                    if (position == 0) {
                        // Set the hint text color gray
                        tv.setTextColor(Color.GRAY);
                    } else {
                        tv.setTextColor(Color.WHITE);
                    }
                    return view;
                }};
            detailsHolder.questionSp.setAdapter(adapter);
            adapter.setDropDownViewResource(R.layout.spinner_item);

        }
    }

    @Override
    public int getItemCount() {
        return fragmentUserDetails.detailsQuestions.size();
    }
}
