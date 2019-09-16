
package com.example.eshop.activity;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.eshop.R;
import com.example.eshop.fragment.FavFragment;
import com.example.eshop.fragment.MainFragment;
import com.example.eshop.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    private Toolbar toolbar;
    private FloatingActionButton fab;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private View hView;
    private TextView txtUsername, txtEmail;
    private static final String USERS_NODE = "users";

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        hView =  navigationView.getHeaderView(0);   // View referenciada a mi vista de usuario en Navigation drawer

        txtEmail = hView.findViewById(R.id.drawer_mail);
        txtUsername = hView.findViewById(R.id.drawer_name);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        initFirebase();


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        loadMainFragment();
        welcomeSnackbar();


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendWhatsApp();
            }
        });

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


    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(fm.getBackStackEntryCount() > 1) {
                fm.popBackStack();
            }
            else
                moveTaskToBack(true);
        }
    }


    //Toolbar----------------------------------------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }
    //----------------------------------------------------------------------Toolbar

    //NavDrawer----------------------------------------------------------------------
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_favorites:
                loadFavoriteFragment();
                break;
            case R.id.nav_home:
                goHome();
                break;
            case R.id.nav_log_out:
                showLogoutPopup();
                break;
            case R.id.nav_configuration:
                Intent i = new Intent(this, PreferencesActivity.class);
                startActivity(i);
            default:
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    //----------------------------------------------------------------------NavDrawer

    private void initFirebase() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        try {
            // Seteo los datos de usuario en la UI
            databaseReference.child(USERS_NODE).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        User user = snapshot.getValue(User.class);

                        if(snapshot.getKey().equals(firebaseUser.getUid())) {

                            txtUsername.setText(user.getUsername());
                            txtEmail.setText(user.getEmail());
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        catch (Exception e) {

        }

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if(firebaseUser != null) {

                }
                else {
                    goLoginActivity();
                }
            }
        };
    }

    private void goHome() {
        FragmentManager fm = getSupportFragmentManager();

        fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        loadMainFragment();

    }

    private void showLogoutPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cerrar Sesión");
        builder.setMessage("Esta seguro que desea cerrar sesión?");

        builder.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                logOut();
            }
        });
        builder.setNegativeButton("Cancelar", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void logOut() {
        firebaseAuth.signOut();
    }

    private void goLoginActivity() {
        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    private void loadMainFragment() {
        MainFragment mainFragment = new MainFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.container_home, mainFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .addToBackStack(null).commit();

    }

    private void loadFavoriteFragment() {
        FavFragment favFragment = new FavFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.container_home, favFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .addToBackStack(null).commit();

    }

    private void welcomeSnackbar() {
        try {

            Snackbar.make(findViewById(R.id.container_home), "Bienvenido " + firebaseAuth.getCurrentUser().getEmail(), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
        catch(Exception e) {}
    }

    private void sendWhatsApp() {
        try {
            String number = "541132706365";

            Intent sendIntent = new Intent("android.intent.action.MAIN");
            sendIntent.setComponent(new ComponentName("com.whatsapp","com.whatsapp.Conversation"));
            sendIntent.putExtra("jid", PhoneNumberUtils.stripSeparators(number)+"@s.whatsapp.net");
            startActivity(sendIntent);

        } catch(Exception e) {
            Log.e("whatsapp", "ERROR_OPEN_MESSANGER"+e.toString());
        }
    }



}


