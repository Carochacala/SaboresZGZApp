package com.comenendez.saboreszgz;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RestauranteAdapter extends RecyclerView.Adapter<RestauranteAdapter.RestauranteViewHolder> {
    List <ModeloCajitaRestaurante> listaRestaurantes;

    public RestauranteAdapter(List<ModeloCajitaRestaurante> listaRestaurantes) {
        this.listaRestaurantes = listaRestaurantes;
    }

    @Override
    public RestauranteViewHolder onCreateViewHolder(@NonNull ViewGroup parent,int viewType){
        android.view.View vista = android.view.LayoutInflater.from(parent.getContext()).inflate(R.layout.card_restaurante, parent, false);
        return new RestauranteViewHolder(vista);

    }

    @Override
    public void onBindViewHolder(@NonNull RestauranteViewHolder holder, int pos){
        ModeloCajitaRestaurante restauranteActual = listaRestaurantes.get(pos);
        holder.tvNombreR.setText(restauranteActual.getNombreRestaurante());
        holder.tvdescripcionCortaR.setText(restauranteActual.getDescripcionCortaRestaurante());
        holder.rbestrellasR.setRating(restauranteActual.getEstrellansRestaurante());
        holder.imgRestaurante.setImageResource(restauranteActual.getImagenRestauranteGuardada());
        holder.tvDistanciaR.setText(restauranteActual.getDistanciaRestaurante() + " km");

    }

    @Override
    public int getItemCount(){
        return listaRestaurantes.size();

    }


    public class RestauranteViewHolder extends RecyclerView.ViewHolder {

        TextView tvNombreR,tvdescripcionCortaR,tvDistanciaR;
        RatingBar rbestrellasR;
        Integer imgRLocal;
        String imgRNoLocal;
        ImageView imgRestaurante;


        public RestauranteViewHolder(@NonNull android.view.View itemView) {
            super(itemView);
            tvNombreR=itemView.findViewById(R.id.tv_NombreRestaurante);
            tvdescripcionCortaR=itemView.findViewById(R.id.tvDescripcionCortaRestaurante);
            rbestrellasR=itemView.findViewById(R.id.rb_estrellasRestaurante);
            tvDistanciaR = itemView.findViewById(R.id.tv_distanciaRestaurante);
            imgRestaurante = itemView.findViewById(R.id.imgRestaurante);
        }
    }
}
