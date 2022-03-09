package com.example.appproject.feed;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appproject.MyApplication;
import com.example.appproject.R;
import com.example.appproject.model.User;

import java.util.Objects;

public class FeedAdapter extends RecyclerView.Adapter<FeedViewHolder>{

    FeedViewModel feedViewModel;
    LayoutInflater layoutInflater;

    public FeedAdapter(FeedViewModel feedViewModel,LayoutInflater layoutInflater) {
        this.feedViewModel = feedViewModel;
        this.layoutInflater = layoutInflater;
    }


    @NonNull
    @Override
    public FeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) MyApplication.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.feed_row,parent,false);
        return new FeedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedViewHolder holder, int position) {
        User user = Objects.requireNonNull(feedViewModel.getUsers().getValue()).get(position);
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        if(feedViewModel.getUsers().getValue() == null){
            return 0;
        }
        Log.d("TAG",String.valueOf(feedViewModel.getUsers().getValue().size()));
        return feedViewModel.getUsers().getValue().size();
    }
}
