package com.comenendez.saboreszgz.UI;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.comenendez.saboreszgz.Adapters.RestauranteAdapter;
import com.comenendez.saboreszgz.modelo.Restaurante;
import com.comenendez.saboreszgz.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class SeleccionRestaurantes extends AppCompatActivity {

    private FirebaseFirestore db;
    private List<Restaurante> listaRestaurantes;
    private RecyclerView rvRestaurantes;

    private RestauranteAdapter adaptador;

    //para el menu en esta pantalla
    private ActionBarDrawerToggle toggle;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccion_restaurantes);

        db = FirebaseFirestore.getInstance();
        listaRestaurantes = new ArrayList<>();

        TextView tvPais = findViewById(R.id.tvPaisSeleccionado);
        rvRestaurantes = findViewById(R.id.rvRestaurantes);
        rvRestaurantes.setLayoutManager(new LinearLayoutManager(this));

        // 1. SOLO UNA VEZ: Recuperamos el país que viene de la pantalla anterior
        String paisBuscado = getIntent().getStringExtra("PAIS_SELECCIONADO");
        //para el cuadrado en xml  de seleccionrestaurantes
        TextView tvTitulo = findViewById(R.id.tvPaisSeleccionado);
        ImageView imgHeader = findViewById(R.id.imgBanderaSeleccionada);

        // 2. Si por algún error llega vacío, ponemos Bolivia por defecto
        if (paisBuscado == null) {
            paisBuscado = "Bolivia";
        }

        // 3. Mostramos el nombre en el título y cargamos los datos
        tvPais.setText(paisBuscado.toUpperCase());
        cargarDatosDesdeFirebase(paisBuscado);



        //SECCION CONFIG MENU
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_perfil) {
                // Abrir pantalla perfil
            } else if (id == R.id.nav_favoritos) {
                // Abrir pantalla favoritos
            } else if (id == R.id.nav_logout) {
                finish();
            }
            drawerLayout.closeDrawers();
            return true;
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // CONFIGURACIÓN DEL TOGGLE
        toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar, // hace que funcione el clic
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("SaboresZGZ");
        }
        //mismo metodo para banderas ya aqui
        if (paisBuscado != null) {
            tvTitulo.setText(paisBuscado.toUpperCase());

            // 1. Limpiamos el nombre para buscar la foto (méxico -> b_mexico)
            String nombreLimpio = paisBuscado.toLowerCase().replace(" ", "");
            nombreLimpio = java.text.Normalizer.normalize(nombreLimpio, java.text.Normalizer.Form.NFD);
            nombreLimpio = nombreLimpio.replaceAll("\\p{M}", "");

            String nombreArchivo = "b_" + nombreLimpio;

            // 2. Buscamos el ID del dibujo
            int idImagen = getResources().getIdentifier(nombreArchivo, "drawable", getPackageName());

            // 3. Si existe, la ponemos. Si no, ponemos fondo_cielo.
            if (idImagen != 0) {
                imgHeader.setImageResource(idImagen);
            } else {
                imgHeader.setImageResource(R.drawable.fondo_cielo);
            }
        }


    }
    //para el MENU su metodo click
    // para el menu 3 lineas
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    // Dentro de SeleccionRestaurantes.java

    private void cargarDatosDesdeFirebase(String pais) {
        db.collection("restaurantes")
                .whereEqualTo("tipoCocinaPais", pais)
                .get()
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {
                        listaRestaurantes.clear();
                        //for encargado de leer doc /collection de cada doc que haya en firebase
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Restaurante r = document.toObject(Restaurante.class);
                            r.setId(document.getId());
                            listaRestaurantes.add(r);
                        }


                        RestauranteAdapter adaptador = new RestauranteAdapter(listaRestaurantes);
                        rvRestaurantes.setAdapter(adaptador);
                    }
                });
        // 1. Buscamos el botón por el ID que pusimos en el XML
        android.widget.ImageButton btnAtras = findViewById(R.id.btnAtras);

// 2. Le asignamos la acción de "finalizar" la actividad actual
        btnAtras.setOnClickListener(v -> {
            finish(); // Esto cierra la pantalla de restaurantes y te devuelve a la de países
        });
    }

}