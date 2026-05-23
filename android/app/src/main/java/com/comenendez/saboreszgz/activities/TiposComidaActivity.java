package com.comenendez.saboreszgz.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.comenendez.saboreszgz.R;
import com.comenendez.saboreszgz.adapters.TipoComida;
import com.comenendez.saboreszgz.adapters.TipoAdapter;
import com.google.android.material.navigation.NavigationView;
import com.comenendez.saboreszgz.helpers.MenuHelper;
import java.util.ArrayList;
import java.util.List;

public class TiposComidaActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tipo_comida);

        // ========== CONFIGURAR MENÚ (NUEVO) ==========
        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.nav_view);
        MenuHelper.setupMenu(this, drawerLayout, toolbar, navigationView);
        // =============================================

        RecyclerView recycler = findViewById(R.id.recyclerTipos);
        recycler.setLayoutManager(new GridLayoutManager(this, 2));
        recycler.setHasFixedSize(true);

        List<TipoComida> listaTipos = new ArrayList<>();
        listaTipos.add(new TipoComida("Italia", R.drawable.bandera_italia, R.drawable.plato_italiano));
        listaTipos.add(new TipoComida("Japón", R.drawable.bandera_japon, R.drawable.plato_japones));
        listaTipos.add(new TipoComida("México", R.drawable.bandera_mexico, R.drawable.plato_mexicano));
        listaTipos.add(new TipoComida("Estados Unidos", R.drawable.bandera_estados_unidos, R.drawable.plato_americano));
        listaTipos.add(new TipoComida("Francia", R.drawable.bandera_francia, R.drawable.plato_frances));
        listaTipos.add(new TipoComida("Colombia", R.drawable.bandera_colombia, R.drawable.plato_colombiano));
        listaTipos.add(new TipoComida("India", R.drawable.bandera_india, R.drawable.plato_indu));
        listaTipos.add(new TipoComida("China", R.drawable.bandera_china, R.drawable.plato_chino));
        listaTipos.add(new TipoComida("Turquía", R.drawable.bandera_turquia, R.drawable.plato_turco));
        listaTipos.add(new TipoComida("Venezuela", R.drawable.bandera_venezuela, R.drawable.plato_venezolano));
        listaTipos.add(new TipoComida("Grecia", R.drawable.bandera_grecia, R.drawable.plato_griego));
        listaTipos.add(new TipoComida("Argentina", R.drawable.bandera_argentina, R.drawable.plato_argentino));
        listaTipos.add(new TipoComida("Perú", R.drawable.bandera_peru, R.drawable.plato_peruano));
        listaTipos.add(new TipoComida("Tailandia", R.drawable.bandera_tailandia, R.drawable.plato_tailandes));
        listaTipos.add(new TipoComida("Corea del Sur", R.drawable.bandera_corea, R.drawable.plato_coreano));
        listaTipos.add(new TipoComida("Alemania", R.drawable.bandera_alemania, R.drawable.plato_aleman));
        listaTipos.add(new TipoComida("Brasil", R.drawable.bandera_brasil, R.drawable.plato_brasileno));
        listaTipos.add(new TipoComida("Cuba", R.drawable.bandera_cuba, R.drawable.plato_cubano));
        listaTipos.add(new TipoComida("Marruecos", R.drawable.bandera_marruecos, R.drawable.plato_marroqui));

        TipoAdapter adapter = new TipoAdapter(listaTipos, tipo -> {
            Intent intent = new Intent(TiposComidaActivity.this, RestauranteActivity.class);
            intent.putExtra("tipo_cocina", tipo.getNombre());
            startActivity(intent);
        });

        recycler.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        MenuHelper.handleBackPressed(this, drawerLayout);
    }
}