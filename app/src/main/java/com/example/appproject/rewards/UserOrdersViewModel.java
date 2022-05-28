package com.example.appproject.rewards;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.appproject.model.Model;
import com.example.appproject.model.reward.Order;

import java.util.List;

public class UserOrdersViewModel extends ViewModel {
    LiveData<List<Order>> orders;

    public UserOrdersViewModel() {
        orders = Model.instance.getOrders();
    }

    public LiveData<List<Order>> getOrders() {
        return orders;
    }
}
