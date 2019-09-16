package com.example.eshop.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eshop.R;
import com.example.eshop.adapter.BestsellerAdapterRecyclerView;
import com.example.eshop.model.Categories;
import com.example.eshop.model.Instrument;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment {

    private TextView title;
    private ImageView imageView;
    private DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private BestsellerAdapterRecyclerView bestsellerAdapterRecyclerView;

    private static final String CATEGORY_NODE = "categories";
    private static final String INSTRUMENTS_NODE = "instruments";

    public CategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_category, container, false);

        title = v.findViewById(R.id.category_title);
        imageView = v.findViewById(R.id.category_img);
        recyclerView = v.findViewById(R.id.category_recyclerview);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        initUI();

        setRecyclerView();



        return v;
    }

    private void setRecyclerView() {
        // RecyclerView: Categorias
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        final String categoria = getArguments().getString("category");
        final String c = categoryFirebaseString(categoria);
        final ArrayList<Instrument> misInstrumentos = new ArrayList<>();
        databaseReference.child(INSTRUMENTS_NODE).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Instrument instrument = snapshot.getValue(Instrument.class);

                    if(c.equals(instrument.getCategory())) {

                        misInstrumentos.add(instrument);
                    }
                }
                if(misInstrumentos.size() > 0) {
                    bestsellerAdapterRecyclerView = new BestsellerAdapterRecyclerView(misInstrumentos, R.layout.cardview_product, getActivity());
                    recyclerView.setAdapter(bestsellerAdapterRecyclerView);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void initUI() {
        final String categoria = getArguments().getString("category");

        if(!categoria.isEmpty())
            title.setText(categoria);

        final String c = categoryFirebaseString(categoria);

        databaseReference.child(CATEGORY_NODE).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Categories categories = snapshot.getValue(Categories.class);

                    if(c.equals(snapshot.getKey())) {
                        String urlimg = categories.getUrlimg();

                        Picasso.get()
                                .load(urlimg)
                                .fit()
                                //.resize(450, 200)
                                .centerCrop()
                                .into(imageView);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    private String categoryFirebaseString(String category) {
        String c = "";
        switch (category) {
            case "Accesorios":
                c = "accesories";
                break;
            case "Acustica":
                c = "acoustic";
                break;
            case "Amplific.":
                c = "amp";
                break;
            case "Bajos":
                c = "bass";
                break;
            case "Baterias":
                c = "drums";
                break;
            case "Guitarras":
                c = "guitar";
                break;
            default:
                c = "guitar";

        }

        return c;
    }



}
