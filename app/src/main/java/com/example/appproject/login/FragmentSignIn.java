package com.example.appproject.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.appproject.MainActivity;
import com.example.appproject.R;
import com.example.appproject.model.General;
import com.example.appproject.model.Model;

import java.util.Objects;

public class FragmentSignIn extends Fragment {

    Button signInBtn;
    EditText emailAddress;
    EditText password;
    ProgressBar progressBar;

    public FragmentSignIn() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signin, container, false);

        signInBtn = view.findViewById(R.id.sign_in_btn);
        emailAddress = view.findViewById(R.id.login_input_email);
        password = view.findViewById(R.id.login_input_password);
        progressBar = view.findViewById(R.id.sign_progressbar);
        progressBar.setVisibility(View.GONE);
        setSignInBtnListener(container);

        return view;
    }

    private void setSignInBtnListener(ViewGroup container) {
        signInBtn.setOnClickListener(v -> {
            General.progressBarOn(getActivity(),container,progressBar);
            Model.instance.signIn(emailAddress.getText().toString().trim(), password.getText().toString().trim(), (user, message) -> {
                if (user != null) {
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    startActivity(intent);
                    Objects.requireNonNull(getActivity()).finish();
                } else {
                    General.progressBarOff(getActivity(),container,progressBar);
                    Toast.makeText(getContext(), getString(R.string.email_or_password_is_incorrect), Toast.LENGTH_LONG).show();
                }
            });
        });
    }

}