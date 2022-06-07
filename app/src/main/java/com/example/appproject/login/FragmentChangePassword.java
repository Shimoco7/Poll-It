package com.example.appproject.login;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.appproject.MyApplication;
import com.example.appproject.R;
import com.example.appproject.model.General;
import com.example.appproject.model.Model;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;


public class FragmentChangePassword extends Fragment {


    public FragmentChangePassword() {}
    Button btnChangePassword;
    EditText oldPassText,newPassText,confirmPassText;
    TextInputLayout oldPassLayout,newPassLayout,confirmPassLayout;
    Boolean isOldPassEmpty=true,isNewPassEmpty=true,isConfirmPassEmpty=true;
    ProgressBar progressBar;
    Boolean failToCreate=false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_change_password, container, false);
        btnChangePassword=view.findViewById(R.id.changePassword_btn_changePassword);
        oldPassText=view.findViewById(R.id.changePassword_input_old_password);
        newPassText=view.findViewById(R.id.changePassword_input_new_password);
        confirmPassText=view.findViewById(R.id.changePassword_input_confirm_new_password);
        oldPassLayout=view.findViewById(R.id.changePassword_lout_old_password);
        newPassLayout=view.findViewById(R.id.changePassword_lout_new_password);
        confirmPassLayout=view.findViewById(R.id.changePassword_lout_new_confirmpass);
        progressBar=view.findViewById(R.id.changePassword_progressbar);
        progressBar.setVisibility(View.GONE);
        setChangeBtnListener(container);
        setInputListeners();

        return view;
    }

    @SuppressLint("RestrictedApi")
    private void setInputListeners() {
        isConfirmPassEmpty=true;
        isNewPassEmpty=true;
        isOldPassEmpty=true;

        oldPassText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                oldPassLayout.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                oldPassLayout.setError(null);

            }

            @Override
            public void afterTextChanged(Editable s) {
                oldPassLayout.setErrorIconDrawable(null);
                if (s.toString().length() == 0) {
                    oldPassLayout.setError("Please Enter Old Password");
                    isOldPassEmpty = true;
                } else {
                    oldPassLayout.setError(null);
                    isOldPassEmpty = false;

                }

            }
        });

        newPassText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                newPassLayout.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                newPassLayout.setError(null);

            }

            @Override
            public void afterTextChanged(Editable s) {
                newPassLayout.setErrorIconDrawable(null);
                if (s.toString().length() == 0) {
                    newPassLayout.setError("Please Enter New Password");
                    isNewPassEmpty = true;
                } else {
                    newPassLayout.setError(null);
                    isNewPassEmpty = false;

                }

            }
        });

        confirmPassText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                confirmPassLayout.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                confirmPassLayout.setError(null);

            }

            @Override
            public void afterTextChanged(Editable s) {
                confirmPassLayout.setErrorIconDrawable(null);
                if (s.toString().length() == 0) {
                    confirmPassLayout.setError("Please Confirm New Password");
                    isConfirmPassEmpty = true;
                } else {
                    confirmPassLayout.setError(null);
                    isConfirmPassEmpty = false;

                }

            }
        });
    }

    private void setChangeBtnListener(ViewGroup container) {
        btnChangePassword.setOnClickListener(v -> {
            failToCreate = false;
            if ((isOldPassEmpty)||(isNewPassEmpty)||(isConfirmPassEmpty)){
                if (isOldPassEmpty){
                    oldPassLayout.setErrorIconDrawable(null);
                    oldPassLayout.setError("Please Enter Old Password");
                    newPassText.setText("");
                    confirmPassText.setText("");
                }

                if (isNewPassEmpty){
                    newPassLayout.setErrorIconDrawable(null);
                    newPassLayout.setError("Please Enter New Password");
                }
                if (isConfirmPassEmpty){
                    confirmPassLayout.setErrorIconDrawable(null);
                    confirmPassLayout.setError("Please Confirm New Password");
                }
            }
            else{
                General.progressBarOn(getActivity(),container,progressBar,false);
                if (newPassText.getText().toString().trim().equals(confirmPassText.getText().toString().trim())) {
                    if(newPassText.getText().toString().trim().equals(oldPassText.getText().toString().trim())){
                        Snackbar.make(requireView(),"You Have Entered Same Passwords",Snackbar.LENGTH_INDEFINITE).setAction("Close",view->{
                            oldPassText.setText("");
                            newPassText.setText("");
                            confirmPassText.setText("");
                        }).show();
                        failToCreate=true;
                    }
                    else if (!Model.instance.validatePassword(newPassText.getText().toString().trim())){
                        Snackbar.make(requireView(),"Invalid Password",Snackbar.LENGTH_INDEFINITE).setAction("Close",view->{
                            newPassText.setText("");
                            confirmPassText.setText("");
                        }).show();
                        failToCreate=true;
                    }
                } else {
                    Snackbar.make(requireView(),"Invalid Password",Snackbar.LENGTH_INDEFINITE).setAction("close",view->{
                        newPassText.setText("");
                        confirmPassText.setText("");
                    }).show();
                    failToCreate=true;
                }
                if(failToCreate){
                    General.progressBarOff(getActivity(),container,progressBar,true);
                    return;
                }

                Model.instance.updatePassword(oldPassText.getText().toString().trim(),newPassText.getText().toString().trim(),isSuccessful->{
                    if(isSuccessful){
                        Model.instance.setIsPassChanged(true);
                        Navigation.findNavController(v).navigate(FragmentChangePasswordDirections.actionFragmentChangePasswordToFragmentUserDisplayDetails());
                    }
                    else{
                        Snackbar.make(requireView(),"Update Password Failed - You Might Have Entered A Wrong Password",Snackbar.LENGTH_INDEFINITE).setAction("Try again later", view->{
                            General.progressBarOff(getActivity(),container,progressBar,true);
                            Navigation.findNavController(v).navigate(FragmentChangePasswordDirections.actionGlobalFragmentHomeScreen());
                        }).show();
                    }
                });
            }
        });

    }
}

