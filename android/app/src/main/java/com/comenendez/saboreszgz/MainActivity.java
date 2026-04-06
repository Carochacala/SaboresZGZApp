package com.comenendez.sabores_zgz;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Botón para ir a la pantalla de tipos de comida
        Button btnEntrar = findViewById(R.id.btnEntrar);
        btnEntrar.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, TipoActivity.class);
            startActivity(intent);
        });
    }
}
