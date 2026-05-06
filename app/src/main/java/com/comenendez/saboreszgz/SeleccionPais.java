package com.comenendez.saboreszgz;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.navigation.NavigationView;
import java.util.ArrayList;
import java.util.List;

public class SeleccionPais extends AppCompatActivity {

    // Declaramos el toggle aquí fuera para que todos los métodos lo vean
    private ActionBarDrawerToggle toggle;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccion_pais);

        // 1. Configurar RecyclerView y Adaptador
        RecyclerView rvPaises = findViewById(R.id.rvPaises);
        rvPaises.setLayoutManager(new GridLayoutManager(this, 2));

        List<Pais> listaDePrueba = new ArrayList<>();
        listaDePrueba.add(new Pais("Argentina"));
        listaDePrueba.add(new Pais("Bolivia"));
        listaDePrueba.add(new Pais("Chile"));
        listaDePrueba.add(new Pais("Uruguay"));
        listaDePrueba.add(new Pais("México"));
        listaDePrueba.add(new Pais("Marruecos"));
        listaDePrueba.add(new Pais("Francia"));
        listaDePrueba.add(new Pais("Colombia"));
        listaDePrueba.add(new Pais("Venezuela"));
        listaDePrueba.add(new Pais("Ecuador"));
        listaDePrueba.add(new Pais("El Salvador"));

        PaisAdapter adaptador = new PaisAdapter(listaDePrueba);
        rvPaises.setAdapter(adaptador);

        // 2. Buscador
        EditText buscador = findViewById(R.id.etBuscadorPaises);
        buscador.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adaptador.filtrar(s.toString());
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });

        // 3. Configurar Menú Lateral (Drawer)
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

    }

    // para el menu 3 lineas
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}