package com.example.appproject.login;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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

import java.util.ArrayList;
import java.util.Collections;


public class FragmentRegister extends Fragment {

    public FragmentRegister() { }
    Button registerBtn;
    EditText email, password,confirmPassword;
    ProgressBar progressBar;
    MenuItem backMenu;

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
        setRegisterBtnListener(container);
        return view;
    }


    private void setRegisterBtnListener(ViewGroup container) {
        registerBtn.setOnClickListener(v->{
            General.progressBarOn(getActivity(),container,progressBar);
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
                General.progressBarOff(getActivity(),container,progressBar);
                General.showToast(getActivity(),errors);
                return;
            }

            Model.instance.createUser(email.getText().toString().trim(),password.getText().toString().trim(), (user, message)->{
                if(user!=null){
                    User u = new User(user.getUid(),email.getText().toString().trim());
                    Model.instance.saveUserOnDb(u, this::afterRegisterFlow);
                }
                else{
                    General.progressBarOff(getActivity(),container,progressBar);
                    General.showToast(getActivity(),new ArrayList<>(Collections.singletonList(message)));
                }
            });
        });
    }

    private void afterRegisterFlow() {
        Navigation.findNavController(registerBtn).navigate(R.id.action_global_fragmetnUserDetails);
    }


}