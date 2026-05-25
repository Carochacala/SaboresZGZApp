package com.comenendez.saboreszgz.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.comenendez.saboreszgz.R;
import com.comenendez.saboreszgz.helpers.ImageHelper;  // ← IMPORTAR IMAGEHELPER
import com.comenendez.saboreszgz.model.Restaurante;
import java.util.List;

public class RestauranteAdapter extends RecyclerView.Adapter<RestauranteAdapter.RestauranteViewHolder> {

    private List<Restaurante> listaRestaurantes;
    private OnRestauranteClickListener listener;
    //para filtro de busqueda
    private List<Restaurante> listaOriginal;
    private List<String> nombresFavoritos = new java.util.ArrayList<>();

    // ============================================================
    // INTERFAZ PARA MANEJAR CLICS
    // ============================================================
    public interface OnRestauranteClickListener {
        void onRestauranteClick(Restaurante restaurante);
    }

    // ============================================================
    // CONSTRUCTOR modificado (filtro busqueda)
    // ============================================================
    public RestauranteAdapter(List<Restaurante> listaRestaurantes, OnRestauranteClickListener listener) {
        this.listaRestaurantes = listaRestaurantes;
        this.listener = listener;
        this.listaOriginal = new java.util.ArrayList<>();
        if (listaRestaurantes != null) {
            this.listaOriginal.addAll(listaRestaurantes);
        }
    }

    // ============================================================
    // CREAR VISTA (ViewHolder)
    // ============================================================
    @Override
    public RestauranteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_restaurante, parent, false);
        return new RestauranteViewHolder(vista);
    }

    // ============================================================
    // ENLAZAR DATOS CON LA VISTA
    // ============================================================
    @Override
    public void onBindViewHolder(@NonNull RestauranteViewHolder holder, int position) {
        Restaurante restauranteActual = listaRestaurantes.get(position);

        // --- NOMBRE DEL RESTAURANTE ---
        holder.tvNombreR.setText(restauranteActual.getNombre());

        // --- PLATO DESTACADO ---
        // Si platoDestacado es una lista, muestra el primer elemento
        if (restauranteActual.getPlatoDestacado() != null && !restauranteActual.getPlatoDestacado().isEmpty()) {
            holder.tvdescripcionCortaR.setText(restauranteActual.getPlatoDestacado().get(0));
        } else {
            holder.tvdescripcionCortaR.setText("Plato destacado");
        }

        // --- VALORACIÓN MEDIA (ESTRELLAS) ---
        holder.rbestrellasR.setRating((float) restauranteActual.getValoracionMedia());

        // --- DISTANCIA (OCULTA, NO SE USA) ---
        holder.tvDistanciaR.setVisibility(View.GONE);

        // ============================================================
        // CARGA DE IMAGEN CON IMAGEHELPER (SIMPLIFICADO)
        // ============================================================
        // Antes: 10 líneas de Glide
        // Ahora: 1 línea
        ImageHelper.cargarImagen(holder.itemView.getContext(),
                restauranteActual.getFotoUrl(),
                holder.imgRestaurante);
        // ============================================================

        // --- MANEJAR CLIC EN EL ITEM ---
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onRestauranteClick(restauranteActual);
            }
        });
    }

    // ============================================================
    // MÉTODO OPCIONAL (YA NO SE USA PORQUE IMAGEHELPER LO HACE)
    // Pero lo dejamos comentado por si lo necesitas
    // ============================================================
    /*
    private void cargarImagen(String url, ImageView imageView) {
        if (url == null || url.isEmpty()) {
            imageView.setImageResource(android.R.drawable.ic_menu_gallery);
            return;
        }
        Glide.with(imageView.getContext())
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_gallery)
                .centerCrop()
                .into(imageView);
    }
    */

    // ============================================================
    // NÚMERO DE ITEMS
    // ============================================================
    @Override
    public int getItemCount() {
        return listaRestaurantes != null ? listaRestaurantes.size() : 0;
    }

    // ============================================================
    // ACTUALIZAR LISTA
    // ============================================================
    public void updateList(List<Restaurante> nuevaLista) {
        this.listaRestaurantes = nuevaLista;
        this.listaOriginal = new java.util.ArrayList<>();
        if (nuevaLista != null) {
            this.listaOriginal.addAll(nuevaLista); // Actualizamos la copia de seguridad también
        }
        notifyDataSetChanged();
    }

    // ============================================================
    // VIEWHOLDER (CONTENEDOR DE VISTAS)
    // ============================================================
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
//para las busqueda de filtrad usamos este metodo
    public void setNombresFavoritos(List<String> favoritos) {
        this.nombresFavoritos = favoritos;
    }
    public void filtrar(String textoBuscado, int idChipSeleccionado) {
        listaRestaurantes.clear();
        String texto = textoBuscado.toLowerCase();

        for (Restaurante restaurante : listaOriginal) {
            // 1. Filtro de texto (Buscador)
            boolean coincideTexto = false;
            if (restaurante.getNombre() != null) {
                coincideTexto = restaurante.getNombre().toLowerCase().contains(texto);
            }

            boolean pasaFiltroChip = true;

            // 2. Filtros de los Chips
            if (idChipSeleccionado == R.id.chipValorados) {
                if (restaurante.getValoracionMedia() < 4.5) {
                    pasaFiltroChip = false;
                }
            } else if (idChipSeleccionado == R.id.chipFavoritos) {
                // Comprobamos si la lista descargada de Firebase contiene el ID o el Nombre del restaurante
                if (nombresFavoritos == null ||
                        (!nombresFavoritos.contains(restaurante.getId()) && !nombresFavoritos.contains(restaurante.getNombre()))) {
                    pasaFiltroChip = false;
                }
            }

            // 3. Aplicar resultados
            if (coincideTexto && pasaFiltroChip) {
                listaRestaurantes.add(restaurante);
            }
        }

        // Avisamos al adaptador que los datos han cambiado
        notifyDataSetChanged();

    }
}