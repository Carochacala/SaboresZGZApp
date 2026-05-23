package com.comenendez.saboreszgz.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.comenendez.saboreszgz.R;
import com.comenendez.saboreszgz.adapters.RestauranteAdapter;
import com.comenendez.saboreszgz.data.FirebaseRepository;
import com.comenendez.saboreszgz.helpers.FirebaseHelper;  // ← IMPORTAR FIREBASEHELPER
import com.comenendez.saboreszgz.model.Restaurante;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class FavoritosActivity extends AppCompatActivity {

    private RecyclerView rvFavoritos;
    private RestauranteAdapter adapter;
    private List<Restaurante> listaFavoritos = new ArrayList<>();
    private FirebaseRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritos);

        rvFavoritos = findViewById(R.id.rvFavoritos);
        rvFavoritos.setLayoutManager(new LinearLayoutManager(this));

        repository = FirebaseRepository.getInstance();

        adapter = new RestauranteAdapter(listaFavoritos, restaurante -> {
            Intent intent = new Intent(FavoritosActivity.this, DetalleRestauranteActivity.class);
            intent.putExtra("restaurante_id", restaurante.getId());
            startActivity(intent);
        });

        rvFavoritos.setAdapter(adapter);

        cargarFavoritos();
    }

    // ============================================================
    // CARGAR FAVORITOS
    // ============================================================
    private void cargarFavoritos() {
        // ============================================================
        // ANTES: if (!repository.isUserLoggedIn())
        // AHORA: Usamos FirebaseHelper
        // ============================================================
        if (!FirebaseHelper.isUserLoggedIn()) {
            Toast.makeText(this, "Inicia sesión para ver tus favoritos", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // Recargar usuario (esto sigue igual porque es del repository)
        repository.loadCurrentUser();

        new android.os.Handler().postDelayed(() -> {
            List<String> favoriteIds = repository.getFavoriteIds();

            if (favoriteIds == null || favoriteIds.isEmpty()) {
                listaFavoritos.clear();
                adapter.updateList(listaFavoritos);
                return;
            }

            repository.getFavoritosRestaurantes(favoriteIds, (value, error) -> {
                if (error != null) return;

                listaFavoritos.clear();
                if (value != null) {
                    for (QueryDocumentSnapshot doc : value) {
                        Restaurante r = doc.toObject(Restaurante.class);
                        r.setId(doc.getId());
                        listaFavoritos.add(r);
                    }
                }
                adapter.updateList(listaFavoritos);
            });
        }, 500);
    }

    // ============================================================
    // AL VOLVER A LA ACTIVIDAD, RECARGAR
    // ============================================================
    @Override
    protected void onResume() {
        super.onResume();
        cargarFavoritos();
    }
}