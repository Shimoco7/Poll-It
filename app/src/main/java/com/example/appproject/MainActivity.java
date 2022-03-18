package com.example.appproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavHost;
import androidx.navigation.ui.NavigationUI;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.appproject.login.LoginActivity;
import com.example.appproject.model.Model;

public class MainActivity extends AppCompatActivity {
    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NavHost navHost = (NavHost) getSupportFragmentManager().findFragmentById(R.id.main_navhost);
        assert navHost != null;
        navController = navHost.getNavController();
        NavigationUI.setupActionBarWithNavController(this,navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(!super.onOptionsItemSelected(item)){
            switch (item.getItemId()){
                case android.R.id.home:
                    navController.navigateUp();
                    return true;
                case R.id.main_menu_logout:
                    showPopup();
                    return true;
                default:
                    NavigationUI.onNavDestinationSelected(item,navController);
            }
        }
        return false;
    }

    private void showPopup() {
        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        alert.setMessage("Are you sure ?")
                .setPositiveButton("Logout", (dialog, which) -> {
                    Model.instance.signOut(); // Last step. Logout function
                    toLoginActivity();
                }).setNegativeButton("Cancel", null);
        AlertDialog alert1 = alert.create();
        alert1.show();
    }


    private void toLoginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}