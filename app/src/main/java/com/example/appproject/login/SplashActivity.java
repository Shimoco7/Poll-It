package com.example.appproject.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.appproject.MainActivity;
import com.example.appproject.R;
import com.example.appproject.model.Model;

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
            if(Model.instance.isSignedIn()){
                Model.instance.isFinishedRegistration(isFinished -> {
                    if(isFinished){
                        Model.instance.getMainThread().post(this::toFeedActivity);
                    }
                    else{
                        Model.instance.getMainThread().post(this::toLoginActivity);
                    }
                });
            }
            else{
                Model.instance.clearCaches();
                Model.instance.getMainThread().post(this::toLoginActivity);
            }
        });
    }

    private void toLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void toFeedActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}