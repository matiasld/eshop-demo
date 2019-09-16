package com.example.eshop.fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eshop.R;
import com.example.eshop.adapter.BestsellerAdapterRecyclerView;
import com.example.eshop.adapter.CategoryAdapterRecyclerView;
import com.example.eshop.adapter.FeatureViewPagerAdapter;
import com.example.eshop.model.Deals;
import com.example.eshop.model.Instrument;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    private static final String DEALS_NODE = "deals";
    private static final String INSTRUMENTS_NODE = "instruments";
    private DatabaseReference databaseReference;
    private ViewPager viewPager;
    private RecyclerView categoriesRecyclerView, bestsellerRecyclerView;
    private BestsellerAdapterRecyclerView bestsellerAdapterRecyclerView;
    private CategoryAdapterRecyclerView categoriesAdapterRecyclerView;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);

        bestsellerRecyclerView = v.findViewById(R.id.vendidos_recyclerview);
        categoriesRecyclerView = v.findViewById(R.id.categories_recyclerview);
        viewPager = v.findViewById(R.id.featured_viewpager);

        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);         // Mantiene la persistencia de datos por mas que no tenga conexion a internet.
        databaseReference = FirebaseDatabase.getInstance().getReference();  //dispositivos-tp2    : Nodo principal


        setupViewPager();

        setRecyclerViews();


        return v;
    }


    private void setRecyclerViews () {
        // RecyclerView: Categorias
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        categoriesRecyclerView.setLayoutManager(linearLayoutManager);
        categoriesRecyclerView.setHasFixedSize(true);
        categoriesAdapterRecyclerView = new CategoryAdapterRecyclerView(R.layout.cardview_category, getActivity());
        categoriesRecyclerView.setAdapter(categoriesAdapterRecyclerView);

        // RecyclerView: Mas Vendidos
        final ArrayList<Instrument> masVendidos = new ArrayList<>();
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getContext());
        linearLayoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
        bestsellerRecyclerView.setLayoutManager(linearLayoutManager2);
        bestsellerRecyclerView.setHasFixedSize(true);
        databaseReference.child(INSTRUMENTS_NODE).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Instrument instrument = snapshot.getValue(Instrument.class);

                    masVendidos.add(instrument);
                }
                if(masVendidos.size() > 0) {
                    bestsellerAdapterRecyclerView = new BestsellerAdapterRecyclerView(masVendidos, R.layout.cardview_product, getActivity());
                    bestsellerRecyclerView.setAdapter(bestsellerAdapterRecyclerView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    private void setupViewPager() {
        final ArrayList<String> imageUrls = new ArrayList<>();

        databaseReference.child(DEALS_NODE).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Deals myDeal = snapshot.getValue(Deals.class);

                    imageUrls.add(myDeal.getUrlimg());
                }

                if(imageUrls.size() > 0) {
                    FeatureViewPagerAdapter adapter = new FeatureViewPagerAdapter(getContext(), imageUrls);
                    viewPager.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }



    private class AsyncTaskDownloader extends AsyncTask<String, String, Boolean> {

        @Override
        protected void onPreExecute() {

        }


        @Override
        protected Boolean doInBackground(String... strings) {

            return true;
        }

        @Override
        protected void onPostExecute(Boolean bool) {


        }

    }


}
