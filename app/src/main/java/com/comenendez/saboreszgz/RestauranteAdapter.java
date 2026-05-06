package com.comenendez.saboreszgz;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RestauranteAdapter extends RecyclerView.Adapter<RestauranteAdapter.RestauranteViewHolder> {

    private List<Restaurante> listaRestaurantes;

    public RestauranteAdapter(List<Restaurante> listaRestaurantes) {
        this.listaRestaurantes = listaRestaurantes;
    }

    @NonNull
    @Override
    public RestauranteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_restaurante, parent, false);
        return new RestauranteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestauranteViewHolder holder, int position) {
        Restaurante rest = listaRestaurantes.get(position);
        holder.tvNombre.setText(rest.getNombre());
        holder.tvValoracion.setText("⭐ " + rest.getValoracionMedia());

        // Por ahora cargamos una imagen por defecto, luego usaremos la URL de Firebase
        holder.imgRest.setImageResource(R.drawable.canva_final);
    }

    @Override
    public int getItemCount() {
        return listaRestaurantes.size();
    }

    public static class RestauranteViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvValoracion, tvEslogan;
        ImageView imgRest;

        public RestauranteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombreRestauranteItem);
            tvValoracion = itemView.findViewById(R.id.tvValoracionItem);
            tvEslogan = itemView.findViewById(R.id.tvEsloganItem);
            imgRest = itemView.findViewById(R.id.imgRestauranteItem);
        }
    }
}