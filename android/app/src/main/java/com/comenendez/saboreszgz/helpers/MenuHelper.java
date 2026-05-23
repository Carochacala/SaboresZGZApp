package com.comenendez.saboreszgz.helpers;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.comenendez.saboreszgz.activities.ManualActivity;
import com.comenendez.saboreszgz.activities.MisValoracionesActivity;
import com.comenendez.saboreszgz.activities.RuletaActivity;
import com.google.android.material.navigation.NavigationView;
import com.comenendez.saboreszgz.activities.FavoritosActivity;
import com.comenendez.saboreszgz.activities.HomeActivity;
import com.comenendez.saboreszgz.activities.MainActivity;
import com.comenendez.saboreszgz.activities.MapaActivity;
import com.comenendez.saboreszgz.activities.TiposComidaActivity;
import com.comenendez.saboreszgz.R;
import com.google.firebase.auth.FirebaseUser;

public class MenuHelper {

    // ============================================================
    // CONFIGURAR MENÚ LATERAL
    // ============================================================
    public static void setupMenu(AppCompatActivity activity, DrawerLayout drawerLayout,
                                 Toolbar toolbar, NavigationView navigationView) {

        // ============================================================
        // CONFIGURAR TOOLBAR
        // ============================================================
        activity.setSupportActionBar(toolbar);

        // ============================================================
        // BOTÓN HAMBURGUESA (☰)
        // ============================================================
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                activity, drawerLayout, toolbar,
                R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // ============================================================
        // CLICS DEL MENÚ
        // ============================================================
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_inicio) {
                activity.startActivity(new Intent(activity, HomeActivity.class));
                drawerLayout.closeDrawers();

            } else if (id == R.id.nav_tipos_comida) {
                activity.startActivity(new Intent(activity, TiposComidaActivity.class));
                drawerLayout.closeDrawers();

            } else if (id == R.id.nav_favoritos) {
                // ANTES: FirebaseAuth.getInstance().getCurrentUser() == null
                // AHORA: Usamos FirebaseHelper
                if (!FirebaseHelper.isUserLoggedIn()) {
                    Toast.makeText(activity, "Inicia sesión para ver tus favoritos", Toast.LENGTH_SHORT).show();
                } else {
                    activity.startActivity(new Intent(activity, FavoritosActivity.class));
                }
                drawerLayout.closeDrawers();

            } else if (id == R.id.nav_mapa) {
                Intent intent = new Intent(activity, MapaActivity.class);
                intent.putExtra("tipo_cocina", "TODOS");
                activity.startActivity(intent);
                drawerLayout.closeDrawers();

            } else if (id == R.id.nav_ruleta) {
                activity.startActivity(new Intent(activity, RuletaActivity.class));
                drawerLayout.closeDrawers();

            } else if (id == R.id.nav_about) {
                new AlertDialog.Builder(activity)
                        .setTitle("Sabores Zaragoza")
                        .setMessage("Versión 1.0\n\nDescubre los mejores restaurantes internacionales en Zaragoza.")
                        .setPositiveButton("Cerrar", null)
                        .show();
                drawerLayout.closeDrawers();

            } else if (id == R.id.nav_mis_valoraciones) {
                // ANTES: FirebaseAuth.getInstance().getCurrentUser() == null
                // AHORA: Usamos FirebaseHelper
                if (!FirebaseHelper.isUserLoggedIn()) {
                    Toast.makeText(activity, "Inicia sesión para ver tus valoraciones", Toast.LENGTH_SHORT).show();
                } else {
                    activity.startActivity(new Intent(activity, MisValoracionesActivity.class));
                }
                drawerLayout.closeDrawers();

            } else if (id == R.id.nav_logout) {
                new AlertDialog.Builder(activity)
                        .setTitle("Cerrar Sesión")
                        .setMessage("¿Seguro que quieres cerrar sesión?")
                        .setPositiveButton("Sí", (dialog, which) -> {
                            // ANTES: FirebaseAuth.getInstance().signOut()
                            // AHORA: Usamos FirebaseHelper.logout()
                            FirebaseHelper.logout();
                            activity.startActivity(new Intent(activity, MainActivity.class));
                            activity.finish();
                        })
                        .setNegativeButton("No", null)
                        .show();
                drawerLayout.closeDrawers();
            } else if (id == R.id.nav_manual_usuario) {
                Intent intent = new Intent(activity, ManualActivity.class);
                intent.putExtra("MANUAL_NAME", "manual_usuario.html");
                activity.startActivity(intent);
                drawerLayout.closeDrawers();
            } else if (id == R.id.nav_manual_instalacion) {
                Intent intent = new Intent(activity, ManualActivity.class);
                intent.putExtra("MANUAL_NAME", "manual_instalacion.html");
                activity.startActivity(intent);
                drawerLayout.closeDrawers();
            }

            return true;
        });

        // ============================================================
        // ACTUALIZAR HEADER DEL MENÚ (FOTO, NOMBRE, EMAIL)
        // ============================================================
        actualizarHeader(activity, navigationView);
    }

    // ============================================================
    // ACTUALIZAR HEADER DEL MENÚ SEGÚN USUARIO LOGUEADO
    // ============================================================
    private static void actualizarHeader(AppCompatActivity activity, NavigationView navigationView) {
        View headerView = navigationView.getHeaderView(0);
        TextView tvNombre = headerView.findViewById(R.id.tvNombreUsuario);
        TextView tvEmail = headerView.findViewById(R.id.tvEmailUsuario);
        Button btnIniciarSesion = headerView.findViewById(R.id.btnIniciarSesion);

        // ANTES: FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        // AHORA: Usamos FirebaseHelper.getCurrentUser()
        FirebaseUser user = FirebaseHelper.getCurrentUser();

        if (user != null) {
            tvNombre.setText(user.getEmail().split("@")[0]);
            tvEmail.setText(user.getEmail());
            btnIniciarSesion.setVisibility(View.GONE);
        } else {
            tvNombre.setText("Invitado");
            tvEmail.setText("Inicia sesión para más opciones");
            btnIniciarSesion.setVisibility(View.VISIBLE);
            btnIniciarSesion.setOnClickListener(v -> {
                activity.startActivity(new Intent(activity, MainActivity.class));
                activity.finish();
            });
        }
    }

    // ============================================================
    // MANEJAR BOTÓN ATRÁS
    // ============================================================
    public static void handleBackPressed(AppCompatActivity activity, DrawerLayout drawerLayout) {
        if (drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            // Si es HomeActivity, ir al login
            if (activity instanceof HomeActivity) {
                Intent intent = new Intent(activity, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                activity.startActivity(intent);
                activity.finish();
            } else {
                // Para el resto, solo cerrar actividad actual
                activity.finish();
            }
        }
    }
}