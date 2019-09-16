package com.example.eshop.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eshop.R;
import com.example.eshop.activity.HomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    /**
     * FirebaseAuth: Instancia de firebase
     * FirebaseUser: Si se logueó correctamente, contiene los datos de mi usuario
     */
    private static final String TAG = "LoginAccountFragment";
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    private TextView txtCrearCuenta, txtRecuperarPass;
    private EditText edtUsername, edtPassword;
    private Button btnLogin;
    private ProgressBar progressBar;


    public LoginFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        progressBar = v.findViewById(R.id.progressBar);
        txtCrearCuenta = v.findViewById(R.id.log_in_txt_creacuenta);
        txtRecuperarPass = v.findViewById(R.id.log_in_txt_forgot_pass);
        edtUsername = v.findViewById(R.id.log_in_username);
        edtPassword = v.findViewById(R.id.log_in_password);
        btnLogin = v.findViewById(R.id.log_in_btn);

        // Firebase
        initFirebase();


        txtCrearCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goCreateAccount();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn(edtUsername.getText().toString(), edtPassword.getText().toString());
            }
        });


        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }



    private void initFirebase() {
        firebaseAuth = FirebaseAuth.getInstance();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if(firebaseUser != null) {
                    //Si ya hay un  usuario logueado lo envio a la Main Activity
                    Log.w(TAG, "Usuario logeado " + firebaseUser.getEmail());
                    goMainActivity();
                }
                else {
                    Log.w(TAG, "Usuario no logeado");
                }
            }
        };
    }


    private void signIn(String username, String password) {

        try {
            if(!username.isEmpty() && !password.isEmpty()) {

                disableInputs();
                progressBar.setVisibility(View.VISIBLE);

                firebaseAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        enableInputs();
                        progressBar.setVisibility(View.INVISIBLE);
                        if(task.isSuccessful()) {
                            goMainActivity();
                        }
                        else {
                            Toast.makeText(getContext(), "Usuario y/o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            else {
                Toast.makeText(getContext(), "Formulario Incompleto", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e) {
            Toast.makeText(getContext(), "Error 404", Toast.LENGTH_SHORT).show();
        }
    }

    private void goCreateAccount() {
        CreateAccountFragment createAccountFragment = new CreateAccountFragment();
        getFragmentManager().beginTransaction().replace(R.id.container_login, createAccountFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .addToBackStack(null).commit();
    }

    private void goMainActivity() {
        Intent i = new Intent(getContext(), HomeActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    public void enableInputs() {
        edtUsername.setEnabled(true);
        edtPassword.setEnabled(true);
        btnLogin.setEnabled(true);
    }

    public void disableInputs() {
        edtUsername.setEnabled(false);
        edtPassword.setEnabled(false);
        btnLogin.setEnabled(false);
    }

}
