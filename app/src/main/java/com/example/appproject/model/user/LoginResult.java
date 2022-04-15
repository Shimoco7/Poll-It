package com.example.appproject.model.user;

public class LoginResult {
    private String accessToken;
    private String refreshToken;
    private Boolean detailsFilled;
    private User account;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public User getAccount() {
        return account;
    }

    public void setAccount(User account) {
        this.account = account;
    }

    public Boolean getDetailsFilled() {
        return detailsFilled;
    }

    public void setDetailsFilled(Boolean detailsFilled) {
        this.detailsFilled = detailsFilled;
    }
}
