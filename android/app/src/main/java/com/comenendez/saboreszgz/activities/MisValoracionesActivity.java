package com.comenendez.saboreszgz.activities;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.comenendez.saboreszgz.R;
import com.comenendez.saboreszgz.adapters.ValoracionAdapter;
import com.comenendez.saboreszgz.data.FirebaseRepository;
import com.comenendez.saboreszgz.model.Valoracion;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class MisValoracionesActivity extends AppCompatActivity {

    private RecyclerView rvMisValoraciones;
    private ValoracionAdapter adapter;
    private List<Valoracion> listaValoraciones = new ArrayList<>();
    private FirebaseRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_valoraciones);

        rvMisValoraciones = findViewById(R.id.rvMisValoraciones);
        rvMisValoraciones.setLayoutManager(new LinearLayoutManager(this));

        repository = FirebaseRepository.getInstance();

        adapter = new ValoracionAdapter(listaValoraciones);
        rvMisValoraciones.setAdapter(adapter);

        cargarMisValoraciones();
    }

    private void cargarMisValoraciones() {
        if (!repository.isUserLoggedIn()) {
            Toast.makeText(this, "Inicia sesión para ver tus valoraciones", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        String userId = repository.getCurrentUserId();

        repository.getMisValoraciones(userId, (value, error) -> {
            if (error != null) {
                android.util.Log.d("MIS_VAL", "Error: " + error.getMessage());  // ← Solo log, no Toast
                //Toast.makeText(this, "Error al cargar valoraciones", Toast.LENGTH_SHORT).show();
                return;
            }

            listaValoraciones.clear();
            if (value != null) {
                for (QueryDocumentSnapshot doc : value) {
                    Valoracion v = doc.toObject(Valoracion.class);
                    if (v != null) {
                        v.setId(doc.getId());
                        listaValoraciones.add(v);
                    }
                }
            }
            adapter.updateList(listaValoraciones);

            if (listaValoraciones.isEmpty()) {
                Toast.makeText(this, "No has escrito ninguna valoración", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarMisValoraciones();
    }
}