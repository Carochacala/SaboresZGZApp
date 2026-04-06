package com.comenendez.sabores_zgz;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TipoActivity extends AppCompatActivity {

    RecyclerView recyclerTipos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tipo_comida);

        // RecyclerView


        RecyclerView recycler = findViewById(R.id.recyclerTipos);
        recycler.setLayoutManager(new GridLayoutManager(this, 2)); // 2 columnas
        recycler.setHasFixedSize(true);


        // Lista de tipos de comida
        List<TipoComida> listaTipos = new ArrayList<>();
        listaTipos.add(new TipoComida("Italia", R.drawable.bandera_italia, R.drawable.plato_italiano));
        listaTipos.add(new TipoComida("Japón", R.drawable.bandera_japon, R.drawable.plato_japones));
        listaTipos.add(new TipoComida("México", R.drawable.bandera_mexico, R.drawable.plato_mexicano));
        listaTipos.add(new TipoComida("Estados Unidos", R.drawable.bandera_estados_unidos, R.drawable.plato_americano));
        listaTipos.add(new TipoComida("Francia", R.drawable.bandera_francia, R.drawable.plato_frances));
        listaTipos.add(new TipoComida("Colombia", R.drawable.bandera_colombia, R.drawable.plato_colombiano));
        listaTipos.add(new TipoComida("India", R.drawable.bandera_india, R.drawable.plato_indu));
        listaTipos.add(new TipoComida("China/Asiática", R.drawable.bandera_china, R.drawable.plato_chino));
        listaTipos.add(new TipoComida("Turquía", R.drawable.bandera_turquia, R.drawable.plato_turco));
        listaTipos.add(new TipoComida("Venezuela", R.drawable.bandera_venezuela, R.drawable.plato_venezolano));
        listaTipos.add(new TipoComida("Grecia", R.drawable.bandera_grecia, R.drawable.plato_griego));

        // Adapter
        TipoAdapter adapter = new TipoAdapter(listaTipos, tipo -> {
            // Acción al clickear un tipo de comida
            Toast.makeText(this, "Seleccionaste: " + tipo.getNombre(), Toast.LENGTH_SHORT).show();
            // Aquí puedes abrir RestauranteActivity
        });


        recycler.setAdapter(adapter);
    }
}