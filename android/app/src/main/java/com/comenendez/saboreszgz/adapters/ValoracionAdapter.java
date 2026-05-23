package com.comenendez.saboreszgz.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.comenendez.saboreszgz.R;
import com.comenendez.saboreszgz.activities.DetalleRestauranteActivity;
import com.comenendez.saboreszgz.data.FirebaseRepository;
import com.comenendez.saboreszgz.model.Valoracion;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ValoracionAdapter extends RecyclerView.Adapter<ValoracionAdapter.ValoracionViewHolder> {

    private List<Valoracion> listaValoraciones;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
    private String usuarioActualId;
    private FirebaseRepository repository;

    public ValoracionAdapter(List<Valoracion> listaValoraciones) {
        this.listaValoraciones = listaValoraciones;
        this.repository = FirebaseRepository.getInstance();
        this.usuarioActualId = repository.getCurrentUserId();
    }

    @NonNull
    @Override
    public ValoracionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_valoracion, parent, false);
        return new ValoracionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ValoracionViewHolder holder, int position) {
        Valoracion valoracion = listaValoraciones.get(position);

        holder.tvNombreUsuario.setText(valoracion.getNombreUsuario());
        holder.tvPuntuacion.setText("⭐ " + valoracion.getPuntuacion());
        holder.tvComentario.setText(valoracion.getComentario());

        if (valoracion.getFecha() != null) {
            holder.tvFecha.setText(dateFormat.format(valoracion.getFecha().toDate()));
        }

        // ========== NUEVO: Mostrar menú solo si es mi valoración ==========
        boolean esMiValoracion = usuarioActualId != null &&
                usuarioActualId.equals(valoracion.getUsuarioId());

        if (esMiValoracion) {
            holder.btnMenu.setVisibility(View.VISIBLE);
            holder.btnMenu.setOnClickListener(v -> mostrarMenuOpciones(v.getContext(), valoracion, position));
        } else {
            holder.btnMenu.setVisibility(View.GONE);
        }
    }

    // ========== NUEVO: Menú de opciones ==========
    private void mostrarMenuOpciones(android.content.Context context, Valoracion valoracion, int position) {
        String[] opciones = {"✏️ Editar valoración", "🗑️ Eliminar valoración"};

        new AlertDialog.Builder(context)
                .setTitle("Opciones")
                .setItems(opciones, (dialog, which) -> {
                    if (which == 0) {
                        // Editar - abre detalle en modo edición
                        Intent intent = new Intent(context, DetalleRestauranteActivity.class);
                        intent.putExtra("restaurante_id", valoracion.getRestauranteId());
                        intent.putExtra("modo_edicion", true);
                        context.startActivity(intent);
                    } else if (which == 1) {
                        // Eliminar
                        eliminarValoracion(context, valoracion, position);
                    }
                })
                .show();
    }

    // ========== NUEVO: Eliminar valoración ==========
    private void eliminarValoracion(android.content.Context context, Valoracion valoracion, int position) {
        if (valoracion.getId() == null || valoracion.getId().isEmpty()) {
            Toast.makeText(context, "Error: No se puede eliminar", Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(context)
                .setTitle("Eliminar valoración")
                .setMessage("¿Seguro que quieres eliminar tu valoración?")
                .setPositiveButton("Eliminar", (dialog, which) -> {

                    repository.deleteValoracion(valoracion.getId(), task -> {
                        if (task != null && task.isSuccessful()) {  // ← AÑADE null check
                            // Eliminar de la lista local
                            listaValoraciones.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, listaValoraciones.size());

                            if (context instanceof DetalleRestauranteActivity) {
                                ((DetalleRestauranteActivity) context).onValoracionEliminada();
                            }

                            Toast.makeText(context, "Valoración eliminada", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Error al eliminar", Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
    public void updateUsuarioId() {
        this.usuarioActualId = repository.getCurrentUserId();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return listaValoraciones.size();
    }

    public void updateList(List<Valoracion> nuevaLista) {
        this.listaValoraciones = nuevaLista;
        notifyDataSetChanged();
    }

    static class ValoracionViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombreUsuario, tvPuntuacion, tvComentario, tvFecha;
        TextView btnMenu;

        ValoracionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombreUsuario = itemView.findViewById(R.id.tvNombreUsuario);
            tvPuntuacion = itemView.findViewById(R.id.tvPuntuacion);
            tvComentario = itemView.findViewById(R.id.tvComentario);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            btnMenu = itemView.findViewById(R.id.btnMenu);
        }
    }
}