package com.comenendez.saboreszgz.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.comenendez.saboreszgz.R;
import com.comenendez.saboreszgz.UI.RestauranteElegido;
import com.comenendez.saboreszgz.modelo.Restaurante;
import com.comenendez.saboreszgz.UI.SeleccionRestaurantes;

import java.util.List;

public class RestauranteAdapter extends RecyclerView.Adapter<RestauranteAdapter.RestauranteViewHolder> {

    private List<Restaurante> listaRestaurantes;

    public RestauranteAdapter(List<Restaurante> listaRestaurantes) {
        this.listaRestaurantes = listaRestaurantes;
    }

    @NonNull
    @Override
    public RestauranteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //aqui elegimos el cardview correspondiente
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_restaurante,
                parent, false);
        return new RestauranteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestauranteViewHolder holder, int position) {
        Restaurante rest = listaRestaurantes.get(position);
        //configuramos el clicked la tarjeta del restaurante elegido
        holder.tvNombre.setText(rest.getNombre());
        holder.tvValoracion.setText("⭐ " + rest.getValoracionMedia());

        // Por ahora cargamos una imagen por defecto, luego usaremos la URL de Firebase
        holder.imgRest.setImageResource(R.drawable.canva_final);

        // Configurar el click en la tarjeta
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), RestauranteElegido.class);
            // "Pasamos" el nombre del país a la siguiente pantalla
            intent.putExtra("RESTAURANTE_ID", rest.getId());
            v.getContext().startActivity(intent);
        });
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