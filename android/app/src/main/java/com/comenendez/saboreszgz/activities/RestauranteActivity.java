package com.comenendez.saboreszgz.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.comenendez.saboreszgz.R;
import com.comenendez.saboreszgz.adapters.RestauranteAdapter;
import com.google.android.material.navigation.NavigationView;
import com.comenendez.saboreszgz.helpers.MenuHelper;
import com.comenendez.saboreszgz.data.FirebaseRepository;
import com.comenendez.saboreszgz.model.Restaurante;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class RestauranteActivity extends AppCompatActivity {

    private RecyclerView rvRestaurantes;
    private TextView tvTitulo;
    private RestauranteAdapter adapter;
    private List<Restaurante> restaurantes = new ArrayList<>();
    private FirebaseRepository repository;
    private String tipoCocina;

    // Menú
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;

    //Busqueda filtro
    private String textoBuscadoActual = "";
    private int chipSeleccionadoActual = android.view.View.NO_ID; // Significa que no hay ningún filtro pulsado al inicio

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurante);

        // ========== CONFIGURAR MENÚ ==========
        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.nav_view);
        MenuHelper.setupMenu(this, drawerLayout, toolbar, navigationView);
        // ====================================

        // Botón para abrir el mapa
        Button btnMapa = findViewById(R.id.btnMapa);

        rvRestaurantes = findViewById(R.id.rvRestaurantes);
        tvTitulo = findViewById(R.id.tvTitulo);

        tipoCocina = getIntent().getStringExtra("tipo_cocina");

        android.util.Log.d("PRUEBA", "tipo_cocina recibido: " + tipoCocina);

        if (tipoCocina == null) {
            tipoCocina = "Restaurantes";
        }

        tvTitulo.setText(tipoCocina);

        rvRestaurantes.setLayoutManager(new LinearLayoutManager(this));

        adapter = new RestauranteAdapter(restaurantes, restaurante -> {
            Intent intent = new Intent(RestauranteActivity.this, DetalleRestauranteActivity.class);
            intent.putExtra("restaurante_id", restaurante.getId());
            startActivity(intent);
        });

        rvRestaurantes.setAdapter(adapter);

        repository = FirebaseRepository.getInstance();
        cargarRestaurantes();

        btnMapa.setOnClickListener(v -> {
            Intent intent = new Intent(RestauranteActivity.this, MapaActivity.class);
            intent.putExtra("tipo_cocina", tipoCocina);
            startActivity(intent);
        });

        //para filtro y busqueda

        // Enlazar los componentes visuales
        androidx.appcompat.widget.SearchView searchView = findViewById(R.id.searchViewRestaurantes);
        com.google.android.material.chip.ChipGroup chipGroup = findViewById(R.id.chipGroupFiltros);

        // 1. Escuchar el texto del buscador
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { return false; }

            @Override
            public boolean onQueryTextChange(String newText) {
                textoBuscadoActual = newText;
                // Enviamos AMBAS cosas al adaptador
                adapter.filtrar(textoBuscadoActual, chipSeleccionadoActual);
                return true;
            }
        });

        // 2. Escuchar los clics en los botones de filtro (Chips)
        chipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            chipSeleccionadoActual = checkedId;
            // Enviamos AMBAS cosas al adaptador
            adapter.filtrar(textoBuscadoActual, chipSeleccionadoActual);
        });

        // Usamos el repositorio que creó tu compañera para obtener los IDs
        com.comenendez.saboreszgz.data.FirebaseRepository repository = com.comenendez.saboreszgz.data.FirebaseRepository.getInstance();
        repository.loadCurrentUser();

        // Le damos medio segundo para que descargue los datos, igual que hace ella en su pantalla
        new android.os.Handler().postDelayed(() -> {
            java.util.List<String> misFavoritosIds = repository.getFavoriteIds();

            // Si encontró favoritos, se los pasamos a tu adaptador
            if (misFavoritosIds != null && adapter != null) {
                adapter.setNombresFavoritos(misFavoritosIds);
            }
        }, 500);
    }

    private void cargarRestaurantes() {
        android.util.Log.d("PRUEBA", "Buscando restaurantes con tipo: " + tipoCocina);

        repository.getRestaurantesByTipo(tipoCocina, (value, error) -> {
            if (error != null) {
                android.util.Log.d("PRUEBA", "Error: " + error.getMessage());
                return;
            }

            android.util.Log.d("PRUEBA", "Resultados encontrados: " + (value != null ? value.size() : 0));

            restaurantes.clear();
            if (value != null) {
                for (QueryDocumentSnapshot doc : value) {
                    Restaurante r = doc.toObject(Restaurante.class);
                    r.setId(doc.getId());
                    restaurantes.add(r);
                    android.util.Log.d("PRUEBA", "Restaurante cargado: " + r.getNombre() + " - Tipo: " + r.getTipoCocinaPais());
                }
            }
            adapter.updateList(restaurantes);
        });
    }

    @Override
    public void onBackPressed() {
        MenuHelper.handleBackPressed(this, drawerLayout);
    }
}