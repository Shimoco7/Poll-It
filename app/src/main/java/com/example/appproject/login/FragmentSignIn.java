package com.example.appproject.login;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import com.example.appproject.MainActivity;
import com.example.appproject.R;
import com.example.appproject.model.General;
import com.example.appproject.model.Model;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import java.util.Objects;

public class FragmentSignIn extends Fragment {

    Button signInBtn;
    EditText emailAddress;
    EditText password;
    TextInputLayout emailLayout, passwordLayout;
    Boolean isPassEmpty=true, isEmailEmpty =true;
    ProgressBar progressBar;

    public FragmentSignIn() { }

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
        isPassEmpty=true;
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
                if (s.toString().length() == 0) {
                    emailLayout.setError(null);
                    isEmailEmpty =true;
                } else {

                    if (!Patterns.EMAIL_ADDRESS.matcher(s.toString()).matches()) {
                        emailLayout.setError("Invalid Email Address");
                        isEmailEmpty = false;
                    } else if (Patterns.EMAIL_ADDRESS.matcher(s.toString()).matches()) {
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
                } else {
                    passwordLayout.setError(null);
                    isPassEmpty=false;
                }
            }
        });
    }

    @SuppressLint("ResourceAsColor")
    private void setSignInBtnListener(ViewGroup container) {
        signInBtn.setOnClickListener(v -> {
            if ((isPassEmpty)||(isEmailEmpty)||(emailLayout.getError()!=null)) {
                if (isEmailEmpty) {
                    emailLayout.setErrorIconDrawable(null);
                    emailLayout.setError("Please Enter Email");
                    password.setText("");
                }
                if(isPassEmpty) {
                    passwordLayout.setError("Please Enter Password");
                }
            } else {
                General.progressBarOn(getActivity(), container, progressBar);
                Model.instance.login(emailAddress.getText().toString().trim(), password.getText().toString().trim(), (user, message) -> {
                    if (user != null) {
                        if(message.equals(getString(R.string.success))){
                            Intent intent = new Intent(getContext(), MainActivity.class);
                            startActivity(intent);
                            Objects.requireNonNull(getActivity()).finish();
                        }
                        else if(message.equals(getString(R.string.registration_details_needed))){
                            Navigation.findNavController(v).navigate(R.id.fragmentUserDetails);
                        }
                    }
                    else {
                        General.progressBarOff(getActivity(), container, progressBar);
                        Snackbar.make(getView(),getString(R.string.email_or_password_is_incorrect),Snackbar.LENGTH_INDEFINITE).setAction("Close",view->{
                            password.setText("");
                        }).show();
                    }
                });
            }
        });
    }
}