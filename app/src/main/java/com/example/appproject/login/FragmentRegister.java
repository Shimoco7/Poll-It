package com.example.appproject.login;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.appproject.R;
import com.example.appproject.model.Model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


public class FragmentRegister extends Fragment {

    public FragmentRegister() { }

    final String INVALID_EMAIL_ADDRESS = "Invalid Email Address";
    final String INVALID_PASSWORD = "Invalid Password";
    final String PASSWORDS_DO_NOT_MATCH = "Passwords do not match";
    Button registerBtn;
    EditText email, password,confirmPassword;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        registerBtn = view.findViewById(R.id.register_btn);
        email = view.findViewById(R.id.register_input_email);
        password = view.findViewById(R.id.register_input_password);
        confirmPassword = view.findViewById(R.id.register_input_confirmpassword);
        progressBar = view.findViewById(R.id.register_progressbar);
        progressBar.setVisibility(View.GONE);
        setRegisterBtnListener();

        return view;
    }


    private void setRegisterBtnListener() {
        registerBtn.setOnClickListener(v->{
            progressBarOn();
            ArrayList<String> errors = new ArrayList<>();
            if(!Model.instance.validateEmailAddress(email.getText().toString().trim())){
                errors.add(INVALID_EMAIL_ADDRESS);
            }
            if(password.getText().toString().trim().equals(confirmPassword.getText().toString().trim())){
                if(!Model.instance.validatePassword(password.getText().toString().trim())){
                    errors.add(INVALID_PASSWORD);
                }
            }
            else{
                errors.add(PASSWORDS_DO_NOT_MATCH);
            }

            if(!errors.isEmpty()){
                progressBarOff();
                showToast(errors);
                return;
            }

            //Todo : handle error in create user
            Model.instance.createUser(email.getText().toString().trim(),password.getText().toString().trim(),user->{
                if(user!=null){
                    Log.d("TAG","Registration succeeded");
                    afterRegisterFlow();
                }
                else{
                    progressBarOff();
                    showToast(new ArrayList<>(Collections.singletonList("Registration Failed")));
                }
            });
        });
    }

    private void afterRegisterFlow() {
        Navigation.findNavController(registerBtn).navigate(R.id.action_global_fragmetnUserDetails);
    }

    private void showToast(ArrayList<String> errors) {
        StringBuilder message = new StringBuilder();
        for(String error : errors){
            message.append(error);
            message.append("\n");
        }
        Toast.makeText(getActivity(), message.toString().trim(),
                Toast.LENGTH_LONG).show();
    }

    private void progressBarOn(){
        progressBar.setVisibility(View.VISIBLE);
        registerBtn.setClickable(false);
    }

    private void progressBarOff(){
        progressBar.setVisibility(View.GONE);
        registerBtn.setClickable(true);
    }

}