package com.comenendez.saboreszgz;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.comenendez.saboreszgz.model.Restaurante;
import com.squareup.picasso.Picasso;
import java.util.List;

public class RestauranteAdapter extends RecyclerView.Adapter<RestauranteAdapter.RestauranteViewHolder> {

    private List<Restaurante> listaRestaurantes;
    private OnRestauranteClickListener listener;

    // Interfaz para manejar clics
    public interface OnRestauranteClickListener {
        void onRestauranteClick(Restaurante restaurante);
    }

    public RestauranteAdapter(List<Restaurante> listaRestaurantes, OnRestauranteClickListener listener) {
        this.listaRestaurantes = listaRestaurantes;
        this.listener = listener;
    }

    @Override
    public RestauranteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_restaurante, parent, false);
        return new RestauranteViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull RestauranteViewHolder holder, int position) {
        Restaurante restauranteActual = listaRestaurantes.get(position);

        holder.tvNombreR.setText(restauranteActual.getNombre());

        // Si platoDestacado es una lista, muestra el primer elemento
        if (restauranteActual.getPlatoDestacado() != null && !restauranteActual.getPlatoDestacado().isEmpty()) {
            holder.tvdescripcionCortaR.setText(restauranteActual.getPlatoDestacado().get(0));
        } else {
            holder.tvdescripcionCortaR.setText("Plato destacado");
        }

        holder.rbestrellasR.setRating((float) restauranteActual.getValoracionMedia());

        // Distancia - si no tienes este campo en Firestore, puedes ocultarlo o calcularlo
        // Por ahora lo dejamos con valor predeterminado
        holder.tvDistanciaR.setVisibility(View.GONE); // Ocultar si no lo usas

        // Cargar imagen desde URL (fotoUrl)
        if (restauranteActual.getFotoUrl() != null && !restauranteActual.getFotoUrl().isEmpty()) {
            Picasso.get()
                    .load(restauranteActual.getFotoUrl())
                    .placeholder(R.drawable.plato_chino) // Imagen por defecto
                    .error(android.R.drawable.ic_delete)
                    .into(holder.imgRestaurante);
        }

        // Manejar clic
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onRestauranteClick(restauranteActual);
            }
        });


    }

    @Override
    public int getItemCount() {
        return listaRestaurantes != null ? listaRestaurantes.size() : 0;
    }

    // Método para actualizar la lista
    public void updateList(List<Restaurante> nuevaLista) {
        this.listaRestaurantes = nuevaLista;
        notifyDataSetChanged();
    }

    public static class RestauranteViewHolder extends RecyclerView.ViewHolder {

        TextView tvNombreR, tvdescripcionCortaR, tvDistanciaR;
        RatingBar rbestrellasR;
        ImageView imgRestaurante;

        public RestauranteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombreR = itemView.findViewById(R.id.tv_NombreRestaurante);
            tvdescripcionCortaR = itemView.findViewById(R.id.tvDescripcionCortaRestaurante);
            rbestrellasR = itemView.findViewById(R.id.rb_estrellasRestaurante);
            tvDistanciaR = itemView.findViewById(R.id.tv_distanciaRestaurante);
            imgRestaurante = itemView.findViewById(R.id.imgRestaurante);
        }
    }
}