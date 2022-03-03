package com.example.appproject.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavHost;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.appproject.R;

public class LoginActivity extends AppCompatActivity {

    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        NavHost navHost = (NavHost) getSupportFragmentManager().findFragmentById(R.id.base_login_navhost);
        assert navHost != null;
        navController = navHost.getNavController();
        NavigationUI.setupActionBarWithNavController(this,navController);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(!super.onOptionsItemSelected(item)){
            switch (item.getItemId()){
                case android.R.id.home:
                    navController.navigateUp();
                    return true;
                default:
                    NavigationUI.onNavDestinationSelected(item,navController);
            }
        }
        return false;
    }
}