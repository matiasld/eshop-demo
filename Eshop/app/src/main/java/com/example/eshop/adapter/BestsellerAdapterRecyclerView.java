package com.example.eshop.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eshop.R;
import com.example.eshop.fragment.DetailFragment;
import com.example.eshop.model.Instrument;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BestsellerAdapterRecyclerView extends RecyclerView.Adapter<BestsellerAdapterRecyclerView.BestsellerViewHolder> {

    private ArrayList<Instrument> instruments;
    private int resource;
    private Activity activity;


    public BestsellerAdapterRecyclerView(ArrayList<Instrument> instruments, int resource, Activity activity) {
        this.instruments = instruments;
        this.resource = resource;
        this.activity = activity;
    }

    @NonNull
    @Override
    public BestsellerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);

        return new BestsellerAdapterRecyclerView.BestsellerViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final BestsellerAdapterRecyclerView.BestsellerViewHolder holder, final int position) {

        Instrument instrument = instruments.get(position);

        holder.check.setVisibility(View.INVISIBLE);
        holder.model.setText(instrument.getModel());
        holder.brand.setText(instrument.getBrand());

        String precio = "$" + instrument.getPrice();
        holder.price.setText(precio);

        Picasso.get()
                .load(instrument.getUrlimg1())
                .fit()
                //.resize(450, 200)
                .centerCrop()
                .into(holder.img);

        holder.constrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putString("instrument", instruments.get(holder.getAdapterPosition()).getInstrumentid());
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                DetailFragment myFragment = new DetailFragment();
                myFragment.setArguments(bundle);

                activity.getSupportFragmentManager().beginTransaction().replace(R.id.container_home, myFragment).addToBackStack(null).commit();

            }
        });
    }

    @Override
    public int getItemCount() {
        return instruments.size();
    }


    public Instrument obtenerElemento(int posicion) {
        return instruments.get(posicion);
    }

    public void removeItem(int position) {
        instruments.remove(position);
        notifyItemRemoved(position);

    }

    public void restoreItem(Instrument item, int position) {
        instruments.add(position, item);
        notifyItemInserted(position);
    }

    public ArrayList<Instrument> getData() {
        return instruments;
    }


    public class BestsellerViewHolder extends RecyclerView.ViewHolder {

        private ImageView img;
        private TextView model, brand, price;
        private CheckBox check;
        private ConstraintLayout constrait;

        public BestsellerViewHolder(View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.product_img);
            model = itemView.findViewById(R.id.product_model);
            brand = itemView.findViewById(R.id.product_brand);
            price = itemView.findViewById(R.id.product_price);
            check = itemView.findViewById(R.id.product_like);
            constrait = itemView.findViewById(R.id.product_constrait);

        }
    }

}


