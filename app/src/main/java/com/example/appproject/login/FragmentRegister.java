package com.example.appproject.login;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.appproject.R;
import com.example.appproject.model.Model;
import com.example.appproject.model.user.User;

import java.util.ArrayList;
import java.util.Collections;


public class FragmentRegister extends Fragment {

    public FragmentRegister() { }
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
                errors.add(getString(R.string.invalid_email_address));
            }
            if(password.getText().toString().trim().equals(confirmPassword.getText().toString().trim())){
                if(!Model.instance.validatePassword(password.getText().toString().trim())){
                    errors.add(getString(R.string.invalid_password));
                }
            }
            else{
                errors.add(getString(R.string.passwords_do_not_match));
            }

            if(!errors.isEmpty()){
                progressBarOff();
                showToast(errors);
                return;
            }

            Model.instance.createUser(email.getText().toString().trim(),password.getText().toString().trim(), (user, message)->{
                if(user!=null){
                    User u = new User(user.getUid());
                    Model.instance.saveUserOnDb(u, this::afterRegisterFlow);
                }
                else{
                    progressBarOff();
                    showToast(new ArrayList<>(Collections.singletonList(message)));
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