package com.example.appproject.model;

import androidx.room.TypeConverter;

import com.example.appproject.model.reward.Order;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Converters {
    @TypeConverter
    public static ArrayList<String> fromString(String value) {
        Type listType = new TypeToken<ArrayList<String>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromList(List<String> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }

    @TypeConverter
    public static List<Order> storeStringToOrders(String data){
        Gson gson = new Gson();
        if(data == null){
            return Collections.emptyList();
        }
        Type listType = new TypeToken<List<Order>>() {}.getType();
        return gson.fromJson(data,listType);
    }

    @TypeConverter
    public static String ordersToStoredString(List<Order> orders) {
        Gson gson = new Gson();
        return gson.toJson(orders);
    }
}
