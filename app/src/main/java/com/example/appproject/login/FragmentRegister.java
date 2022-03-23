package com.example.appproject.login;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.appproject.R;
import com.example.appproject.model.General;
import com.example.appproject.model.Model;
import com.example.appproject.model.user.User;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Collections;


public class FragmentRegister extends Fragment {

    public FragmentRegister() { }
    Button registerBtn;
    EditText email, password,confirmPassword;
    ProgressBar progressBar;
    TextInputLayout emailLayout,passwordLayout,confirmLayout;
    Boolean isPassEmpty=true,isEmailEmpty=true,isConfirmEmpty=true;
    MenuItem backMenu;
    Boolean failToCreate=false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        registerBtn = view.findViewById(R.id.register_btn);
        email = view.findViewById(R.id.register_input_email);
        password = view.findViewById(R.id.register_input_password);
        confirmPassword = view.findViewById(R.id.register_input_confirmpassword);
        emailLayout= view.findViewById(R.id.register_lout_email);
        passwordLayout=view.findViewById(R.id.register_lout_password);
        confirmLayout= view.findViewById(R.id.register_lout_confirmpass);
        progressBar = view.findViewById(R.id.register_progressbar);
        progressBar.setVisibility(View.GONE);
        setRegisterBtnListener(container);
        //inputCheck//
        setInputListeners();
        return view;
    }

    @SuppressLint("RestrictedApi")
    private void setInputListeners(){
        isPassEmpty=true;
        email.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                emailLayout.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                emailLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
                emailLayout.setErrorIconDrawable(null);
                if (s.toString().length() == 0) {
                    emailLayout.setError(null);
                    isEmailEmpty =true;


                }
                else {
                    if (!Model.instance.validateEmailAddress(email.getText().toString().trim())) {
                        emailLayout.setError("Invalid Email Address");
                        isEmailEmpty = false;
                    }

                    else if (Patterns.EMAIL_ADDRESS.matcher(s.toString()).matches()) {
                        emailLayout.setError(null);
                        isEmailEmpty = false;
                    }
                }


            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                passwordLayout.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passwordLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
                passwordLayout.setErrorIconDrawable(null);
                if(s.toString().length()==0){
                    passwordLayout.setError("Please Enter Password");
                    isPassEmpty=true;
                }
                else{
                    passwordLayout.setError(null);
                    isPassEmpty=false;
                }

            }
        });
        confirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                confirmLayout.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                confirmLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
                confirmLayout.setErrorIconDrawable(null);
                if(s.toString().length()==0){
                    confirmLayout.setError("Please Confirm Password");
                    isConfirmEmpty=true;
                }
                else{
                    passwordLayout.setError(null);
                    isConfirmEmpty=false;
                }

            }
        });
    }


    private void setRegisterBtnListener(ViewGroup container) {
        registerBtn.setOnClickListener(v->{



            if ((isPassEmpty)||(isEmailEmpty)||(isConfirmEmpty)) {
                    if (isEmailEmpty) {
                    emailLayout.setErrorIconDrawable(null);
                    emailLayout.setError("Please Enter Email");
                    password.setText("");
                }
                if(isPassEmpty) {
                    passwordLayout.setErrorIconDrawable(null);
                    passwordLayout.setError("Please Enter Password");
                }
                if(isConfirmEmpty) {
                    confirmLayout.setErrorIconDrawable(null);
                    confirmLayout.setError("Please Confirm Password");
                }

            }

            else {
                General.progressBarOn(getActivity(), container, progressBar);
                ArrayList<String> errors = new ArrayList<>();
                if (!Model.instance.validateEmailAddress(email.getText().toString().trim())) {
//                    errors.add(getString(R.string.invalid_email_address));
                    Snackbar.make(getView(),getString(R.string.invalid_email_address),Snackbar.LENGTH_LONG).show();
                    failToCreate=true;
                }
                if (password.getText().toString().trim().equals(confirmPassword.getText().toString().trim())) {
                    if (!Model.instance.validatePassword(password.getText().toString().trim())) {
//                        errors.add(getString(R.string.invalid_password));
                        Snackbar.make(getView(),getString(R.string.invalid_password),Snackbar.LENGTH_INDEFINITE).setAction("Close",view->{
                            password.setText("");
                            confirmPassword.setText("");
                        }).show();
                        failToCreate=true;

                    }
                } else {
//                    errors.add(getString(R.string.passwords_do_not_match));
//                    password.setText("");
//                    confirmPassword.setText("");
                    Snackbar.make(getView(),getString(R.string.invalid_password),Snackbar.LENGTH_INDEFINITE).setAction("Close",view->{
                        password.setText("");
                        confirmPassword.setText("");
                    }).show();
                    failToCreate=true;
                }

                if (failToCreate) {
//                    General.showToast(getActivity(), errors);
                    General.progressBarOff(getActivity(), container, progressBar);



                    return;
                }

                Model.instance.createUser(email.getText().toString().trim(), password.getText().toString().trim(), (user, message) -> {
                    if (user != null) {
                        User u = new User(user.getUid(), email.getText().toString().trim());
                        Model.instance.saveUserOnDb(u, this::afterRegisterFlow);
                        General.progressBarOff(getActivity(), container, progressBar);
                    } else {
                        General.progressBarOff(getActivity(), container, progressBar);
//                        General.showToast(getActivity(), new ArrayList<>(Collections.singletonList(message)));
                        Snackbar.make(getView(),message,Snackbar.LENGTH_LONG).show();

                    }
                });
            }
        });
    }

    private void afterRegisterFlow() {
        Navigation.findNavController(registerBtn).navigate(R.id.action_global_fragmentUserDetails);
    }


}