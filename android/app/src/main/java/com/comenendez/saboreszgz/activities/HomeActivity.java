package com.comenendez.saboreszgz.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;

import com.comenendez.saboreszgz.R;
import com.google.android.material.navigation.NavigationView;
import com.comenendez.saboreszgz.helpers.FirebaseHelper;
import com.comenendez.saboreszgz.helpers.MenuHelper;

public class HomeActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Inicializar vistas del menú
        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.nav_view);

        // Configurar menú (ya usa MenuHelper)
        MenuHelper.setupMenu(this, drawerLayout, toolbar, navigationView);

        // ============================================================
        // ANTES: FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        // AHORA: Usamos FirebaseHelper.getCurrentUser()
        // ============================================================
        TextView txtUser = findViewById(R.id.txtUser);
        Button btnEntrar = findViewById(R.id.btnEntrar);

        if (!FirebaseHelper.isUserLoggedIn()) {
            txtUser.setText("Modo invitado 🍽️");
        } else {
            String email = FirebaseHelper.getUserEmail();
            txtUser.setText("Bienvenido " + email);
        }

        btnEntrar.setOnClickListener(v -> {
            startActivity(new Intent(this, TiposComidaActivity.class));
        });
    }

    @Override
    public void onBackPressed() {
        MenuHelper.handleBackPressed(this, drawerLayout);
    }
}