package com.example.appproject.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.appproject.MainActivity;
import com.example.appproject.MyApplication;
import com.example.appproject.R;
import com.example.appproject.model.Model;
import com.google.android.material.snackbar.Snackbar;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Model.instance.getExecutor().execute(()->{
            try {
                Thread.sleep(3*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Model.instance.isSignedIn(signedIn->{
                if(signedIn){
                    if(MyApplication.getGender() != null && !MyApplication.getGender().equals("")){
                        Model.instance.getMainThread().post(this::toFeedActivity);
                    }
                    else{
                        Model.instance.getMainThread().post(()->{
                            toLoginActivity(true);
                        });
                    }
                }
                else{
                    Model.instance.signOut(()->{
                        Model.instance.getMainThread().post(()->{
                            toLoginActivity(false);
                        });
                    });
                }
            });
        });
    }

    private void toLoginActivity(Boolean isSignedIn) {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra(getString(R.string.is_signed_in),isSignedIn);
        startActivity(intent);
        finish();
    }

    private void toFeedActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}