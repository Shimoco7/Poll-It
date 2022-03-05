package com.example.appproject.login;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.appproject.R;
import com.example.appproject.model.Model;

public class FragmentSignIn extends Fragment {

    Button signInBtn;
    EditText emailAddress;
    EditText password;
    public FragmentSignIn() { }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_signin,container,false);

        signInBtn=view.findViewById(R.id.login_sign_in_btn);
        emailAddress=view.findViewById(R.id.login_input_email);
        password=view.findViewById(R.id.login_input_password);
        setSignInBtnListener();

        return view;
    }

    private void setSignInBtnListener() {
        signInBtn.setOnClickListener(v -> {
            Model.instance.signIn(emailAddress.getText().toString().trim(),password.getText().toString().trim(), (user, message)->{
                if(user!=null){
                    Navigation.findNavController(signInBtn).navigate(R.id.action_fragmentSignIn_to_homeScreen);
                }
                else{
                    Toast.makeText(getContext(),"Email or Password is incorrect", Toast.LENGTH_LONG).show();
                }
            });
        });
    }
}