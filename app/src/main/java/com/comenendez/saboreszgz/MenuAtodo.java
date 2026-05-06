package com.comenendez.saboreszgz;

import android.content.Intent;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;

public class MenuAtodo {
//esta clase es para el menu lateral,una clase extra
    public static void configurarMenu(AppCompatActivity actividad, DrawerLayout drawerLayout, Toolbar toolbar) {
        // 1. Configuramos la barra
        actividad.setSupportActionBar(toolbar);
        if (actividad.getSupportActionBar() != null) {
            actividad.getSupportActionBar().setTitle("Menú");
        }

        // 2. Configuramos las 3 lineas
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                actividad, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // 3. Configuramos los clics de las opciones
        NavigationView navigationView = actividad.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_perfil) {
                // Intent para ir a Perfil en el futuro
            } else if (id == R.id.nav_favoritos) {
                //  Intent para ir a Favoritos
            } else if (id == R.id.nav_logout) {
                actividad.finishAffinity(); // Cierra toda la app
            }

            drawerLayout.closeDrawers();
            return true;
        });
    }
}