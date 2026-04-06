import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.comenendez.sabores_zgz.R;

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
        TextView txtNombre;

        public ViewHolder(View itemView) {
            super(itemView);
            imgBandera = itemView.findViewById(R.id.imgBandera);
            txtNombre = itemView.findViewById(R.id.txtNombre);
        }

        public void bind(final TipoComida tipo, final OnItemClickListener listener) {
            txtNombre.setText(tipo.getNombre());
            imgBandera.setImageResource(tipo.getBanderaResId());
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
        holder.bind(listaTipos.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return listaTipos.size();
    }
}