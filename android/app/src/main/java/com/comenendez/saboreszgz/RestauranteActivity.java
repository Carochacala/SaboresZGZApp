package com.comenendez.sabores_zgz;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.LinearLayout;

public class RestauranteActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Layout mínimo sin XML (solo para que compile)
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        TextView txt = new TextView(this);
        txt.setText("Pantalla de Restaurantes");
        txt.setTextSize(20);
        layout.addView(txt);

        setContentView(layout);

        // Recibir tipo seleccionado (opcional)
        String tipoSeleccionado = getIntent().getStringExtra("tipoSeleccionado");
        if(tipoSeleccionado != null){
            txt.setText("Restaurantes de tipo: " + tipoSeleccionado);
            txt.setTextSize(20);

            layout.addView(txt);
            setContentView(layout);

        }
    }

}
