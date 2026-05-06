package com.comenendez.saboreszgz;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class SeleccionRestaurantes extends AppCompatActivity {

    private FirebaseFirestore db;
    private List<Restaurante> listaRestaurantes;
    private RecyclerView rvRestaurantes;

    private RestauranteAdapter adaptador;

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

        // 2. Si por algún error llega vacío, ponemos Bolivia por defecto
        if (paisBuscado == null) {
            paisBuscado = "Bolivia";
        }

        // 3. Mostramos el nombre en el título y cargamos los datos
        tvPais.setText(paisBuscado.toUpperCase());
        cargarDatosDesdeFirebase(paisBuscado);
    }


    // Dentro de SeleccionRestaurantes.java

    private void cargarDatosDesdeFirebase(String pais) {
        db.collection("restaurantes")
                .whereEqualTo("tipoCocinaPais", pais)
                .get()
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {
                        listaRestaurantes.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Restaurante r = document.toObject(Restaurante.class);
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