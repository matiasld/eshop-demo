package com.example.eshop.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eshop.R;
import com.example.eshop.fragment.CategoryFragment;
import com.example.eshop.model.Category;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CategoryAdapterRecyclerView extends RecyclerView.Adapter<CategoryAdapterRecyclerView.CategoryViewHolder> {

    private ArrayList<Category> categories;
    private int resource;
    private Activity activity;


    public CategoryAdapterRecyclerView(int resource, Activity activity) {
        this.categories = buildCategoriesArray();   //Creo el array desde la misma clase porque las categorias con 'estaticas'. No cambian con el tiempo.
        this.resource = resource;
        this.activity = activity;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);

        return new CategoryAdapterRecyclerView.CategoryViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final CategoryAdapterRecyclerView.CategoryViewHolder holder, final int position) {

        Category category = categories.get(position);
        holder.title.setText(category.getTitle());

        Picasso.get()
                .load(category.getImageId())
                .fit()
                //.resize(450, 200)
                .centerInside()
                .into(holder.icon);

        holder.icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                Intent intent = new Intent(activity, DetailActivity.class);
                intent.putExtra("Source", holder.getId());
                activity.startActivity(intent);
                */
                Bundle bundle = new Bundle();
                bundle.putString("category", categories.get(holder.getAdapterPosition()).getTitle());
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                CategoryFragment myFragment = new CategoryFragment();
                myFragment.setArguments(bundle);

                activity.getSupportFragmentManager().beginTransaction().replace(R.id.container_home, myFragment).addToBackStack(null).commit();
            }
        });
    }


    @Override
    public int getItemCount() {
        return categories.size();
    }


    public class CategoryViewHolder extends RecyclerView.ViewHolder {

        private ImageView icon;
        private TextView title;

        public CategoryViewHolder(View itemView) {
            super(itemView);

            icon = itemView.findViewById(R.id.category_img);
            title = itemView.findViewById(R.id.category_title);

        }
    }

    private ArrayList<Category> buildCategoriesArray() {
        ArrayList<Category> myCat = new ArrayList<>();
        String accesorio = "https://firebasestorage.googleapis.com/v0/b/dispositivos-tp2.appspot.com/o/accesorios2.png?alt=media&token=c729dbe5-33b1-400d-955b-8cfbf75ae030";
        String guitarra = "https://firebasestorage.googleapis.com/v0/b/dispositivos-tp2.appspot.com/o/guitarra.png?alt=media&token=553aa12b-8c9d-4f31-827d-00aa6bc6294f";
        String bajo = "https://firebasestorage.googleapis.com/v0/b/dispositivos-tp2.appspot.com/o/bass2.png?alt=media&token=a1f68a5d-915d-48a8-9077-4f76dd87ddfc";
        String amplificador = "https://firebasestorage.googleapis.com/v0/b/dispositivos-tp2.appspot.com/o/amplificador.png?alt=media&token=f33b747b-ac49-424c-8da2-2bf9ba65989c";
        String bateria = "https://firebasestorage.googleapis.com/v0/b/dispositivos-tp2.appspot.com/o/bateria.png?alt=media&token=2444c909-b4d4-41a9-ae06-6637733c2d61";
        String acustica = "https://firebasestorage.googleapis.com/v0/b/dispositivos-tp2.appspot.com/o/acustica2.png?alt=media&token=c11a2783-a9fe-4634-ba42-7c5ce00c53fc";

        myCat.add(new Category(accesorio, "Accesorios"));
        myCat.add(new Category(acustica, "Acustica"));
        myCat.add(new Category(amplificador, "Amplific."));
        myCat.add(new Category(bajo, "Bajos"));
        myCat.add(new Category(bateria, "Baterias"));
        myCat.add(new Category(guitarra, "Guitarras"));

        return myCat;
    }
}

