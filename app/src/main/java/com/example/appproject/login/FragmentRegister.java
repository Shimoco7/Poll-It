package com.example.appproject.login;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.appproject.R;
import com.example.appproject.model.Model;

import java.util.ArrayList;


public class FragmentRegister extends Fragment {

    public FragmentRegister() { }

    final String INVALID_EMAIL_ADDRESS = "Invalid Email Address";
    final String INVALID_PASSWORD = "Invalid Password";
    final String PASSWORDS_DO_NOT_MATCH = "Passwords do not match";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        Button registerBtn = view.findViewById(R.id.register_btn);
        EditText email = view.findViewById(R.id.register_input_email);
        EditText password = view.findViewById(R.id.register_input_password);
        EditText confirmPassword = view.findViewById(R.id.register_input_confirmpassword);

        registerBtn.setOnClickListener(v->{
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
                showToast(errors);
                return;
            }

            Model.instance.createUser(email.getText().toString().trim(),password.toString().trim());
            //TODO - after register
        });

        return view;
    }

    private void showToast(ArrayList<String> errors) {
        StringBuilder message = new StringBuilder("");
        for(String error : errors){
            message.append(error);
            message.append("\n");
        }
        Toast.makeText(getActivity(), message.toString().trim(),
                Toast.LENGTH_LONG).show();
    }

}