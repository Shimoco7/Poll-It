package com.example.appproject.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
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
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class FragmentSignIn extends Fragment {

    Button signInBtn;
    EditText emailAddress;
    EditText password;
    TextInputLayout emailLayout, passwordLayout;
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
        signInBtn=view.findViewById(R.id.sign_in_btn);
        emailAddress=view.findViewById(R.id.login_input_email);
        password=view.findViewById(R.id.login_input_password);
        emailLayout =view.findViewById(R.id.sign_lout_email);
        passwordLayout =view.findViewById(R.id.sign_lout_password);
        progressBar = view.findViewById(R.id.sign_progressbar);
        progressBar.setVisibility(View.GONE);
        setSignInBtnListener(container);
        //inputCheck//
        setInputListeners();
        return view;
    }

    @SuppressLint("RestrictedApi")
    private void setInputListeners(){
        emailAddress.addTextChangedListener(new TextWatcher() {

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
                if (s.toString().length() == 0) emailLayout.setError(null);
                else {
                    if (!Patterns.EMAIL_ADDRESS.matcher(s.toString()).matches())
                        emailLayout.setError("Invalid Email Address");
                    else if (Patterns.EMAIL_ADDRESS.matcher(s.toString()).matches())
                        emailLayout.setError(null);
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
                if(s.toString().length()==0) passwordLayout.setError("Please Enter Password");
                else passwordLayout.setError(null);

            }
        });
    }

    private void setSignInBtnListener(ViewGroup container) {
        signInBtn.setOnClickListener(v -> {
            General.progressBarOn(getActivity(),container,progressBar);
            Model.instance.signIn(emailAddress.getText().toString().trim(), password.getText().toString().trim(), (user, message) -> {
                if (user != null) {
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    startActivity(intent);
                    Objects.requireNonNull(getActivity()).finish();
                }

                else{
                    General.progressBarOff(getActivity(),container,progressBar);
                    Toast.makeText(getContext(),getString(R.string.email_or_password_is_incorrect), Toast.LENGTH_LONG).show();
                    password.setText("");

                }
            });
        });
    }
}