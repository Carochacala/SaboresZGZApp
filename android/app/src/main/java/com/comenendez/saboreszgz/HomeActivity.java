package com.comenendez.saboreszgz;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import androidx.appcompat.app.AlertDialog;

public class HomeActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.nav_view);

        // Configurar Toolbar
        setSupportActionBar(toolbar);

        // Configurar menú hamburguesa
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Actualizar header del menú
        View headerView = navigationView.getHeaderView(0);
        TextView tvNombre = headerView.findViewById(R.id.tvNombreUsuario);
        TextView tvEmail = headerView.findViewById(R.id.tvEmailUsuario);
        Button btnIniciarSesion = headerView.findViewById(R.id.btnIniciarSesion);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            tvNombre.setText(currentUser.getDisplayName() != null ? currentUser.getDisplayName() : "Usuario");
            tvEmail.setText(currentUser.getEmail());
            btnIniciarSesion.setVisibility(View.GONE);
        } else {
            tvNombre.setText("Invitado");
            tvEmail.setText("Inicia sesión para más opciones");
            btnIniciarSesion.setVisibility(View.VISIBLE);
            btnIniciarSesion.setOnClickListener(v -> {
                startActivity(new Intent(this, MainActivity.class));
                finish();
            });
        }

        // Configurar clics del menú
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_inicio) {
                // Ya estamos en Home
                drawerLayout.closeDrawers();

            } else if (id == R.id.nav_tipos_comida) {
                startActivity(new Intent(this, TiposComidaActivity.class));
                drawerLayout.closeDrawers();

            } else if (id == R.id.nav_favoritos) {
                if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                    Toast.makeText(this, "Inicia sesión para ver tus favoritos", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(this, FavoritosActivity.class));
                }
                drawerLayout.closeDrawers();

            } else if (id == R.id.nav_mis_valoraciones) {
                if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                    Toast.makeText(this, "Inicia sesión para ver tus valoraciones", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Próximamente: Mis Valoraciones", Toast.LENGTH_SHORT).show();
                }
                drawerLayout.closeDrawers();

            } else if (id == R.id.nav_mapa) {
                Intent intent = new Intent(this, MapaActivity.class);
                intent.putExtra("tipo_cocina", "TODOS");
                startActivity(intent);
                drawerLayout.closeDrawers();

            } else if (id == R.id.nav_cerca_de_mi) {
                Toast.makeText(this, "Próximamente: Restaurantes cerca de ti", Toast.LENGTH_SHORT).show();
                drawerLayout.closeDrawers();

            } else if (id == R.id.nav_perfil) {
                if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                    Toast.makeText(this, "Inicia sesión para ver tu perfil", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Próximamente: Mi Perfil", Toast.LENGTH_SHORT).show();
                }
                drawerLayout.closeDrawers();

            } else if (id == R.id.nav_configuracion) {
                Toast.makeText(this, "Próximamente: Configuración", Toast.LENGTH_SHORT).show();
                drawerLayout.closeDrawers();

            } else if (id == R.id.nav_about) {
                new AlertDialog.Builder(this)
                        .setTitle("Sabores Zaragoza")
                        .setMessage("Versión 1.0\n\nDescubre los mejores restaurantes internacionales en Zaragoza.\n\nDesarrollado con ❤️ para los amantes de la buena comida.")
                        .setPositiveButton("Cerrar", null)
                        .show();
                drawerLayout.closeDrawers();

            } else if (id == R.id.nav_logout) {
                new AlertDialog.Builder(this)
                        .setTitle("Cerrar Sesión")
                        .setMessage("¿Seguro que quieres cerrar sesión?")
                        .setPositiveButton("Sí", (dialog, which) -> {
                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(this, MainActivity.class));
                            finish();
                        })
                        .setNegativeButton("No", null)
                        .show();
                drawerLayout.closeDrawers();
            }

            return true;
        });

        // Actualizar datos del usuario en el header
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        TextView txtUser = findViewById(R.id.txtUser);
        Button btnEntrar = findViewById(R.id.btnEntrar);

        if (user == null) {
            txtUser.setText("Modo invitado 🍽️");
        } else {
            txtUser.setText("Bienvenido " + user.getEmail());
        }

        btnEntrar.setOnClickListener(v -> {
            startActivity(new Intent(this, TiposComidaActivity.class));
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}