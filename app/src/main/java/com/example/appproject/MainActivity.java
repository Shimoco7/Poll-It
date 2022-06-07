package com.example.appproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
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

import com.example.appproject.feed.FragmentUserDisplayDetails;
import com.example.appproject.login.LoginActivity;
import com.example.appproject.model.General;
import com.example.appproject.model.Model;
import com.example.appproject.poll.FragmentPollQuestionImageAnswers;
import com.example.appproject.poll.FragmentPollQuestionMultiChoice;

public class MainActivity extends AppCompatActivity {
    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NavHost navHost = (NavHost) getSupportFragmentManager().findFragmentById(R.id.main_navhost);
        assert navHost != null;
        navController = navHost.getNavController();
        NavigationUI.setupActionBarWithNavController(this, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (!super.onOptionsItemSelected(item)) {
            switch (item.getItemId()) {
                case android.R.id.home:
                    Fragment navHostFragment = getSupportFragmentManager().findFragmentById(R.id.main_navhost);
                    Fragment f;
                    if(navHostFragment != null){
                        f = navHostFragment.getChildFragmentManager().getFragments().get(0);
                        if(f instanceof FragmentPollQuestionImageAnswers
                                || f instanceof FragmentPollQuestionMultiChoice
                                    || f instanceof FragmentUserDisplayDetails){
                            navController.navigate(R.id.action_global_fragmentHomeScreen);
                        }
                        else{
                            navController.navigateUp();
                        }
                        return true;
                    }
                    else{
                        navController.navigateUp();
                    }
                    return true;
                case R.id.main_menu_logout:
                    showPopup();
                    return true;
                case R.id.main_menu_settings:
                    navController.navigate(R.id.action_global_fragmentUserDisplayDetails);
                default:
                    NavigationUI.onNavDestinationSelected(item, navController);
            }
        }
        return false;
    }

    private void showPopup() {
        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        alert.setMessage("Are you sure ?")
                .setPositiveButton("Logout", (dialog, which) -> {
                    Model.instance.signOut(()->{
                       Model.instance.getMainThread().post(()->{
                           toLoginActivity(false);
                       });
                    });
                }).setNegativeButton("Cancel", null);
        AlertDialog alert1 = alert.create();
        alert1.show();
    }

    private void toLoginActivity(Boolean isSignedIn) {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra(getString(R.string.is_signed_in),isSignedIn);
        startActivity(intent);
        finish();
    }
}