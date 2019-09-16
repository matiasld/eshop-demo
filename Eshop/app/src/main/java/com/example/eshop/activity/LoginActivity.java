package com.example.eshop.activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.eshop.R;
import com.example.eshop.fragment.LoginFragment;

public class LoginActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        LoginFragment loginFragment = new LoginFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.container_login, loginFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .addToBackStack(null).commit();


    }


    // Sobrecarga el botón de Back para que no me cierre el Fragment visible
    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();

        if(fm.getBackStackEntryCount() > 1) {
            fm.popBackStack();
        }
        else {
            moveTaskToBack(true);
        }
    }


}
