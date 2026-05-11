package com.comenendez.saboreszgz.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.comenendez.saboreszgz.modelo.Pais;
import com.comenendez.saboreszgz.R;
import com.comenendez.saboreszgz.UI.SeleccionRestaurantes;

import java.util.ArrayList;
import java.util.List;

public class PaisAdapter extends RecyclerView.Adapter<PaisAdapter.PaisViewHolder> {

    // 1. Declaración de variables
    private List<Pais> listaPaises;
    private List<Pais> listaOriginal;

    //  Constructor
    public PaisAdapter(List<Pais> listaPaises) {
        this.listaPaises = listaPaises;
        this.listaOriginal = new ArrayList<>(listaPaises);
    }

    @NonNull
    @Override
    public PaisViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflamos el diseño XML de la tarjeta
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pais, parent, false);
        return new PaisViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaisViewHolder holder, int position) {
        Pais pais = listaPaises.get(position);

        // Ponemos el nombre
        holder.tvNombrePais.setText(pais.getNombre());

        // --- LA MAGIA DE LAS BANDERAS sin TILDES
        // 1. Quitamos mayúsculas y espacios
        String nombreLimpio = pais.getNombre().toLowerCase().replace(" ", "");

        // 2. Quitamos las tildes usando el Normalizador de Java
        nombreLimpio = java.text.Normalizer.normalize(nombreLimpio, java.text.Normalizer.Form.NFD);
        nombreLimpio = nombreLimpio.replaceAll("\\p{M}", ""); // Esto borra cualquier acento

        // 3. Fabricamos el nombre final (ej: "México" -> "mexico" -> "b_mexico")
        String nombreArchivo = "b_" + nombreLimpio;

        // Buscamos la imagen en la carpeta drawable
        int idImagen = holder.itemView.getContext().getResources().getIdentifier(
                nombreArchivo,
                "drawable",
                holder.itemView.getContext().getPackageName()
        );

        // Si la encuentra (idImagen es distinto de 0), la pone. Si no, pone fondo_cielo.
        if (idImagen != 0) {
            holder.imgBandera.setImageResource(idImagen);
        } else {
            holder.imgBandera.setImageResource(R.drawable.fondo_cielo);
        }
        // --------------------------------

        // Configurar el click en la tarjeta para viajar a la otra pantalla
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), SeleccionRestaurantes.class);
            intent.putExtra("PAIS_SELECCIONADO", pais.getNombre());
            v.getContext().startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return listaPaises.size();
    }

    // Método para el buscador
    public void filtrar(String texto) {
        listaPaises.clear();
        if (texto.isEmpty()) {
            listaPaises.addAll(listaOriginal);
        } else {
            for (Pais p : listaOriginal) {
                if (p.getNombre().toLowerCase().contains(texto.toLowerCase())) {
                    listaPaises.add(p);
                }
            }
        }
        notifyDataSetChanged(); // Refresca la lista en pantalla
    }

    //
    public static class PaisViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombrePais;
        ImageView imgBandera;

        public PaisViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombrePais = itemView.findViewById(R.id.tvNombrePais);
            //
            imgBandera = itemView.findViewById(R.id.imgBanderaPais);
        }
    }
}