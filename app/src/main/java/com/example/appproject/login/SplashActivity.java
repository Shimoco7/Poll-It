package com.example.appproject.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.appproject.MainActivity;
import com.example.appproject.MyApplication;
import com.example.appproject.R;
import com.example.appproject.model.Model;
import com.example.appproject.model.listeners.BooleanListener;

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
                    Model.instance.getUsersFromLocalDb(isEmpty->{
                        if(isEmpty){
                            Model.instance.clearCaches();
                            toLoginActivity(false);
                        }
                        else{
                            if(MyApplication.getGender() != null && !MyApplication.getGender().equals("")){
                                Model.instance.getMainThread().post(this::toMainActivity);
                            }
                            else{
                                Model.instance.getMainThread().post(()-> toLoginActivity(true));
                            }
                        }
                    });
                }
                else{
                    Model.instance.getMainThread().post(()->{
                        Model.instance.clearCaches();
                        toLoginActivity(false);
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

    private void toMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}