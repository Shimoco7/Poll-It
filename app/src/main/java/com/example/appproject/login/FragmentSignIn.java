package com.example.appproject.login;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.renderscript.ScriptGroup;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.TextKeyListener;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ThemedSpinnerAdapter;
import android.widget.Toast;

import com.example.appproject.MainActivity;
import com.example.appproject.R;
import com.example.appproject.model.General;
import com.example.appproject.model.Model;
import com.google.android.material.internal.TextWatcherAdapter;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.apache.commons.validator.routines.EmailValidator;

import java.util.Objects;

public class FragmentSignIn extends Fragment {

    Button signInBtn;
    EditText emailAddress;
    EditText password;
    TextInputLayout emaillayout, passwordlayout;
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
        emaillayout=view.findViewById(R.id.sign_lout_email);
        passwordlayout=view.findViewById(R.id.sign_lout_password);
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
                emaillayout.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                emaillayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
                emaillayout.setErrorIconDrawable(null);
                if (s.toString().length() == 0) emaillayout.setError(null);
                else {
                    if (!Patterns.EMAIL_ADDRESS.matcher(s.toString()).matches())
                        emaillayout.setError("Invalid Email Address");
                    else if (Patterns.EMAIL_ADDRESS.matcher(s.toString()).matches())
                        emaillayout.setError(null);
                }


            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                passwordlayout.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passwordlayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
                passwordlayout.setErrorIconDrawable(null);
                if(s.toString().length()==0) passwordlayout.setError("Please Enter Password");
                else passwordlayout.setError(null);

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