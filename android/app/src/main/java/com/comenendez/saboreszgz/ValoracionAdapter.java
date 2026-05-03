package com.comenendez.saboreszgz;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.comenendez.saboreszgz.model.Valoracion;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ValoracionAdapter extends RecyclerView.Adapter<ValoracionAdapter.ValoracionViewHolder> {

    private List<Valoracion> listaValoraciones;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

    public ValoracionAdapter(List<Valoracion> listaValoraciones) {
        this.listaValoraciones = listaValoraciones;
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

        ValoracionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombreUsuario = itemView.findViewById(R.id.tvNombreUsuario);
            tvPuntuacion = itemView.findViewById(R.id.tvPuntuacion);
            tvComentario = itemView.findViewById(R.id.tvComentario);
            tvFecha = itemView.findViewById(R.id.tvFecha);
        }
    }
}