package com.comenendez.saboreszgz.adapters;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.comenendez.saboreszgz.R;

import java.util.List;

public class TipoAdapter extends RecyclerView.Adapter<TipoAdapter.ViewHolder> {

    private List<TipoComida> listaTipos;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(TipoComida tipo);
    }

    public TipoAdapter(List<TipoComida> listaTipos, OnItemClickListener listener) {
        this.listaTipos = listaTipos;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgBandera;
        ImageView imgPlato;
        TextView txtNombre;

        public ViewHolder(View itemView) {
            super(itemView);
            imgBandera = itemView.findViewById(R.id.imgBandera);
            imgPlato = itemView.findViewById(R.id.imgPlato);
            txtNombre = itemView.findViewById(R.id.txtNombre);
        }

        public void bind(final TipoComida tipo, final OnItemClickListener listener) {
            txtNombre.setText(tipo.getNombre());
            imgBandera.setImageResource(tipo.getBanderaResId());
            imgPlato.setImageResource(tipo.getPlatoResId());
            itemView.setOnClickListener(v -> listener.onItemClick(tipo));
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_tipo_comida, parent, false);
        return new ViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TipoComida tipo = listaTipos.get(position);
        holder.bind(tipo, listener);

        // Efecto de "flotante" al tocar
        holder.itemView.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    v.setElevation(16f);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    v.setElevation(8f);
                    break;
            }
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return listaTipos.size();
    }
}