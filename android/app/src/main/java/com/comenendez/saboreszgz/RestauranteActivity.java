package com.comenendez.saboreszgz;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.LinearLayout;

public class RestauranteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        TextView txt = new TextView(this);
        txt.setTextSize(20);

        // Recibir dato del Intent
        String tipoSeleccionado = getIntent().getStringExtra("tipoSeleccionado");

        if (tipoSeleccionado != null) {
            txt.setText("Restaurantes de tipo: " + tipoSeleccionado);
        } else {
            txt.setText("Pantalla de Restaurantes");
        }
        String tipo = getIntent().getStringExtra("tipo");

        layout.addView(txt);

        setContentView(layout);
    }
}