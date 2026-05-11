package com.comenendez.saboreszgz.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.comenendez.saboreszgz.modelo.Pais;
import com.comenendez.saboreszgz.R;
import com.comenendez.saboreszgz.UI.SeleccionRestaurantes;

import java.util.List;

public class PaisAdapter extends RecyclerView.Adapter<PaisAdapter.PaisViewHolder> {

    private List<Pais> listaPaises;

    // Constructor que recibe la lista de países


    @NonNull
    @Override
    public PaisViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Aquí "inflamos" (convertimos en código) el diseño XML de la tarjeta
        //elegimos el cardview correspondiente
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pais,
                parent, false);
        return new PaisViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaisViewHolder holder, int position) {
        Pais pais = listaPaises.get(position);
        holder.tvNombrePais.setText(pais.getNombre());

        // Configurar el click en la tarjeta
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), SeleccionRestaurantes.class);
            // "Pasamos" el nombre del país a la siguiente pantalla
            intent.putExtra("PAIS_SELECCIONADO", pais.getNombre());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return listaPaises.size();
    }

    // Esta clase interna busca los elementos de la tarjeta (el TextView)
    public static class PaisViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombrePais;

        public PaisViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombrePais = itemView.findViewById(R.id.tvNombrePais);
        }
    }
    // Añade estas variables arriba en el PaisAdapter
    private List<Pais> listaOriginal;

    // Actualiza el constructor para guardar la lista original
    public PaisAdapter(List<Pais> listaPaises) {
        this.listaPaises = listaPaises;
        this.listaOriginal = new java.util.ArrayList<>(listaPaises);
    }


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
        notifyDataSetChanged(); // Esto refresca la lista en pantalla
    }
}