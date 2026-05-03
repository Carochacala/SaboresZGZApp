package com.comenendez.saboreszgz;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class TiposComidaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tipo_comida);

        RecyclerView recycler = findViewById(R.id.recyclerTipos);
        recycler.setLayoutManager(new GridLayoutManager(this, 2));
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
            android.util.Log.d("PRUEBA", "1. Click en: " + tipo.getNombre());

            Intent intent = new Intent(TiposComidaActivity.this, RestauranteActivity.class);
            intent.putExtra("tipo_cocina", tipo.getNombre());
            android.util.Log.d("PRUEBA", "2. Enviando tipo: " + tipo.getNombre());

            startActivity(intent);
        });

        recycler.setAdapter(adapter);
    }
}