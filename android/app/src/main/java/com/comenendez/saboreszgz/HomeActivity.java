package com.comenendez.saboreszgz;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        // 🔥 FIREBASE
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        TextView txtUser = findViewById(R.id.txtUser);
        Button btn = findViewById(R.id.btnEntrar);

        if (user == null) {
            txtUser.setText("Modo invitado 🍽️");
        } else {
            txtUser.setText("Bienvenido " + user.getEmail());
        }

        // 🔘 BOTÓN
        btn.setOnClickListener(v -> {
            Intent intent = new Intent(this, TiposComidaActivity.class);
            startActivity(intent);
        });
    }
}