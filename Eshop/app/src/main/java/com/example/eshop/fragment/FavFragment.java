package com.example.eshop.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eshop.R;
import com.example.eshop.adapter.BestsellerAdapterRecyclerView;
import com.example.eshop.model.Instrument;
import com.example.eshop.model.SwipeToDeleteCallback;
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
public class FavFragment extends Fragment {


    private RecyclerView recyclerView;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private BestsellerAdapterRecyclerView mAdapter;
    private ConstraintLayout layout;

    private boolean initRecyclerView;

    private static final String FAV_NODE = "users_store";
    private static final String INSTRUMENTS_NODE = "instruments";

    final ArrayList<String> favInstrumenstKey = new ArrayList<>();

    public FavFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v  = inflater.inflate(R.layout.fragment_fav, container, false);

        initRecyclerView = true;    // Solo sera valido una vez. De esta manera me aseguro que solo cargue los datos de favoritos al inicializar el fragment

        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        recyclerView = v.findViewById(R.id.fav_recyclerview);
        layout = v.findViewById(R.id.fav_constraitlayout);

        setupRecyclerView();
        enableSwipeToDeleteAndUndo();

        return v;
    }






    private void setupRecyclerView() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        DatabaseReference userFavRef = databaseReference.child("users_store");    // Referencia de la database en nodo 'users_store', favoritos

        final ArrayList<String> favs = new ArrayList<>();
        final ArrayList<Instrument> instruments = new ArrayList<>();
        final ArrayList<Instrument> favInstrumenst = new ArrayList<>();
        favInstrumenstKey.clear();

        databaseReference.child(FAV_NODE).child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {      // Recupero lista de favoritos

                if(initRecyclerView) {
                    initRecyclerView = false;

                    for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String fav = snapshot.getValue(String.class);
                        favs.add(fav);
                        favInstrumenstKey.add(snapshot.getKey());
                    }

                    if(favs.size() > 0) {   // Entonces tengo favoritos

                        databaseReference.child(INSTRUMENTS_NODE).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    Instrument instrument = snapshot.getValue(Instrument.class);
                                    instruments.add(instrument);    // Listo todos mis instrumentos
                                }

                                for(int i = 0; instruments.size() > i; i++) {
                                    for(int j= 0; favs.size() > j; j++) {

                                        if(instruments.get(i).getInstrumentid().equals(favs.get(j))) {
                                            favInstrumenst.add(instruments.get(i));         // Barro mis instrumentos y guardo los que esten como favoritos
                                        }
                                    }
                                }
                                if(favInstrumenst.size() > 0) {
                                    mAdapter = new BestsellerAdapterRecyclerView(favInstrumenst, R.layout.cardview_product, getActivity());
                                    recyclerView.setAdapter(mAdapter);
                                }
                            }

                            /**
                             * favs: almacena todos mis favoritos como String
                             * favInstrumenstKey: guarda todas las key de mis favoritos
                             * favInstrumenst: guarda todos mis favoritos como 'Instrument'
                             */

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }



    private void enableSwipeToDeleteAndUndo() {

        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(getContext()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

                final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                final int position = viewHolder.getAdapterPosition();
                final Instrument item = mAdapter.getData().get(position);

                String key = favInstrumenstKey.get(position);
                favInstrumenstKey.remove(position);
                ref.child(FAV_NODE).child(firebaseUser.getUid()).child(key).removeValue();
                mAdapter.removeItem(position);


/*
                Snackbar snackbar = Snackbar.make(layout, "El item fue removido ", Snackbar.LENGTH_LONG);
                snackbar.setAction("DESHACER", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        String pushKey = ref.child(FAV_NODE).child(firebaseUser.getUid()).push().getKey();
                        ref.child(FAV_NODE).child(firebaseUser.getUid()).child(pushKey).setValue(item.getInstrumentid());

                        mAdapter.restoreItem(item, position);
                        recyclerView.scrollToPosition(position);
                    }
                });

                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();
*/
            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }

}
