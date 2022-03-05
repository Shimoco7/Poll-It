package com.example.appproject.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.os.Bundle;

import com.example.appproject.R;
import com.example.appproject.model.Model;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        Model.instance.getExecutor().execute(()->{
            try {
                Thread.sleep(3*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(Model.instance.isSignedIn()){
                Model.instance.getMainThread().post(this::toFeedActivity);
            }
            else{
                Model.instance.getMainThread().post(this::toLoginActivity);
            }
        });
    }

    private void toLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    //Todo: complete Feed Activity
    private void toFeedActivity() {
        Model.instance.signOut();
    }
}