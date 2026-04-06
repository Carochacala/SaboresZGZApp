package com.comenendez.saboreszgz;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SeleccionRestaurantes extends AppCompatActivity {
    /* clase donde se selecciono el pais y muestra sus restaurantes

     */
    RecyclerView listaVisualRestaurantes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_seleccion_restaurantes);
        //  Conectaremos la variable de Java con el ID del XML
        listaVisualRestaurantes=findViewById(R.id.rvRestaurantes);
        //Le decimos a la lista que ordene las tarjetas de arriba hacia abajo,de preferencia en este caso linearlayout
        listaVisualRestaurantes.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(this));

        //EJEMPLO PRUEBAS MOMENTANEO
        // 1. Preparamos los datos de prueba
        List<ModeloCajitaRestaurante> listaDePrueba = new java.util.ArrayList<>();

        listaDePrueba.add(new ModeloCajitaRestaurante(
                "La Parrilla Vegana",
                "Comida deliciosa sin origen animal",
                4.5f,
                1.2f,
                R.drawable.fondo_cuadrado,
                ""
        ));

        listaDePrueba.add(new ModeloCajitaRestaurante(
                "El Rincón del Café",
                "Desayunos y meriendas especiales",
                5.0f,
                3.5f,
                R.drawable.fondo_cielo,
                ""
        ));
        //le entrego al adaptador mi lista de prueba
       RestauranteAdapter miAdaptadorRestaurante= new RestauranteAdapter(listaDePrueba);
       //y le comunicamos a la lista que ese sera su adaptador
        listaVisualRestaurantes.setAdapter(miAdaptadorRestaurante);



    }
}