package com.example.eshop.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.eshop.R;
import com.example.eshop.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateAccountFragment extends Fragment {

    private static final String TAG = "CreateAccountFragment";
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private ProgressBar progressBar;

    private Button btnRegistrarse;
    private EditText edtName, edtUsername, edtEmail, edtPass1, edtPass2;

    public CreateAccountFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_create_account, container, false);


        progressBar = v.findViewById(R.id.progressBar2);
        edtEmail = v.findViewById(R.id.create_acc_email);
        edtName = v.findViewById(R.id.create_acc_nombre);
        edtUsername = v.findViewById(R.id.create_acc_usuario);
        edtPass1 = v.findViewById(R.id.create_acc_pass1);
        edtPass2 = v.findViewById(R.id.create_acc_pass2);

        btnRegistrarse = v.findViewById(R.id.create_acc_btn);

        // Firebase
        initFirebase();

        btnRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                createAccount();
            }
        });


        return v;
    }

    //Necesito manejar mi AuthState en onCreate y onStart, asi como removerlo en onStop
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



    private void createAccount() {

        final String email = edtEmail.getText().toString();
        final String name = edtName.getText().toString();
        final String username = edtUsername.getText().toString();
        final String password = edtPass1.getText().toString();
        final String password2 = edtPass2.getText().toString();

        try {
            if(!email.isEmpty() && !password.isEmpty() && !name.isEmpty() && !username.isEmpty() && !password2.isEmpty()) {
                if(password.equals(password2)) {

                    disableInputs();
                    progressBar.setVisibility(View.VISIBLE);
                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    enableInputs();
                                    progressBar.setVisibility(View.INVISIBLE);
                                    if(task.isSuccessful()) {

                                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                                        if(firebaseUser != null) {
                                            DatabaseReference userRef = ref.child("users");        // Referencia de la database en nodo 'users'

                                            // Creo el HashMap de mi usuario nuevo. El mismo lo almacenaré en mi RT-DB
                                            Map<String, User> users = new HashMap<>();
                                            User myUser = new User(email, name, "", username);
                                            users.put(firebaseUser.getUid(), myUser);

                                            userRef.child(firebaseUser.getUid()).setValue(myUser);

                                        }



                                        Toast.makeText(getContext(), "Cuenta Creada Exitosamente", Toast.LENGTH_SHORT).show();
                                        goLogin();
                                    }
                                    else {
                                        Toast.makeText(getContext(), "Ocurrió un error al crear la cuenta", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else
                    Toast.makeText(getContext(), "Los Password no cooinciden", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getContext(), "Formulario Incompleto", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e) {
            Toast.makeText(getContext(), "Error 404", Toast.LENGTH_SHORT).show();
        }
    }

    private void initFirebase() {
        try {
            database = FirebaseDatabase.getInstance();
            ref = database.getReference();
            firebaseAuth = FirebaseAuth.getInstance();

            authStateListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                    if(firebaseUser != null) {
                        Log.w(TAG, "Usuario logeado " + firebaseUser.getEmail());
                    }
                    else {
                        Log.w(TAG, "Usuario no logeado");
                    }
                }
            };

        }
        catch(Exception e) {

        }
    }

    private void goLogin() {
        LoginFragment loginFragment = new LoginFragment();
        getFragmentManager().beginTransaction().replace(R.id.container_login, loginFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .addToBackStack(null).commit();
    }

    public void enableInputs() {
        edtUsername.setEnabled(true);
        edtPass1.setEnabled(true);
        btnRegistrarse.setEnabled(true);
        edtPass2.setEnabled(true);
        edtName.setEnabled(true);
        edtEmail.setEnabled(true);
    }

    public void disableInputs() {
        edtUsername.setEnabled(false);
        edtPass1.setEnabled(false);
        btnRegistrarse.setEnabled(false);
        edtPass2.setEnabled(false);
        edtName.setEnabled(false);
        edtEmail.setEnabled(false);
    }
}
