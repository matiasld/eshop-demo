package com.example.eshop.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.eshop.R;
import com.example.eshop.adapter.FeatureViewPagerAdapter;
import com.example.eshop.model.Instrument;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {

    private ViewPager viewPager;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private String instrumentoId;
    private Instrument instrument;

    private CheckBox checkBoxLike;
    private String checkBokKey;
    private TextView title, subtitle, description, details, price;

    private boolean init;

    private static final String INSTRUMENTS_NODE = "instruments";
    private static final String FAV_NODE = "users_store";

    public DetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_detail, container, false);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        init = true;

        checkBoxLike = v.findViewById(R.id.like_detalle);
        checkBoxLike.setChecked(false);
        viewPager = v.findViewById(R.id.viewpager_detalle);
        title = v.findViewById(R.id.title_detalle);
        subtitle = v.findViewById(R.id.subtitle_detalle);
        description = v.findViewById(R.id.descripcion_detalle);
        details = v.findViewById(R.id.detalle_detalle);
        price = v.findViewById(R.id.price_detalle);

        instrumentoId = getArguments().getString("instrument");

        setupViewPager();
        manageFavorites();

        checkBoxLike.setOnClickListener(new View.OnClickListener() {
            // Se realizó un checkeo inicial previamente que define el estado del checkbox segun si estaba agendado como favorito
            @Override
            public void onClick(View v) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                if(!checkBoxLike.isChecked()) {
                    // Si esta Likeado, paso a eliminar de la base de datos el favorito. Estado Siguiente: dislike
                    if(!checkBokKey.isEmpty()) {
                        ref.child(FAV_NODE).child(firebaseUser.getUid()).child(checkBokKey).removeValue();
                        checkBokKey = "";
                    }

                } else {
                    // Viceversa. Paso a agregarlo en la base de datos.

                    checkBokKey = ref.child(FAV_NODE).child(firebaseUser.getUid()).push().getKey();
                    ref.child(FAV_NODE).child(firebaseUser.getUid()).child(checkBokKey).setValue(instrumentoId);
                }


            }
        });

        return v;
    }



    private void setupViewPager() {
        final ArrayList<String> imageUrls = new ArrayList<>();

        databaseReference.child(INSTRUMENTS_NODE).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                try {

                    for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Instrument myInstrument = snapshot.getValue(Instrument.class);

                        if(myInstrument.getInstrumentid().equals(instrumentoId)) {
                            instrument = myInstrument;

                            setFragment(myInstrument);

                            imageUrls.add(myInstrument.getUrlimg1());
                            imageUrls.add(myInstrument.getUrlimg2());
                            imageUrls.add(myInstrument.getUrlimg3());
                        }
                    }

                    if(imageUrls.size() > 0) {
                        FeatureViewPagerAdapter adapter = new FeatureViewPagerAdapter(getContext(), imageUrls);
                        viewPager.setAdapter(adapter);
                    }
                }
                catch(Exception e) {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void manageFavorites() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        final ArrayList<String> favs = new ArrayList<>();
        final ArrayList<String> favsId = new ArrayList<>();

        final ArrayList<Instrument> instruments = new ArrayList<>();

        databaseReference.child(FAV_NODE).child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (init) {
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String fav = snapshot.getValue(String.class);
                        favs.add(fav);
                        favsId.add(snapshot.getKey());

                    }
                    if(favs.size() > 0) {
                        for(int i = 0; favs.size() > i; i++) {
                            if(favs.get(i).equals(instrumentoId)) {     // Si el elemento que estoy viendo esta dentro de los likeados:
                                checkBoxLike.setChecked(true);
                                checkBokKey = favsId.get(i);
                            }
                        }
                    }
                    else {
                        checkBokKey = "";
                        checkBoxLike.setChecked(false);
                    }

                    init = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setFragment (Instrument instrument) {
        title.setText(instrument.getModel());
        subtitle.setText(instrument.getBrand());
        description.setText(instrument.getDescription());
        details.setText(instrument.getDetails());
        String precio = "$" + instrument.getPrice();
        price.setText(precio);
    }



}
