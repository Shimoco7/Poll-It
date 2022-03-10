package com.example.appproject.model.detail;

import com.example.appproject.model.user.User;

import java.util.List;

public interface GetDetailsListener {
    void onComplete(List<Detail> list);
}
