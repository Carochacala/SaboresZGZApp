package com.comenendez.saboreszgz.activities;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.comenendez.saboreszgz.R;
import com.comenendez.saboreszgz.data.FirebaseRepository;
import com.comenendez.saboreszgz.model.Restaurante;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RuletaActivity extends AppCompatActivity {

    private TextView tvResultado;
    private Button btnGirar;
    private ImageView ivRuleta;
    private LinearLayout layoutFiltros;
    private FirebaseRepository repository;
    private List<Restaurante> todosRestaurantes = new ArrayList<>();
    private List<Restaurante> restaurantesFiltrados = new ArrayList<>();
    private String tipoSeleccionado = "Todos";
    private List<String> tipos = new ArrayList<>();
    private boolean girando = false;

    // Sonidos
    private MediaPlayer mediaPlayerGiro;
    private MediaPlayer mediaPlayerCampana;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ruleta);

        tvResultado = findViewById(R.id.tvResultado);
        btnGirar = findViewById(R.id.btnGirar);
        ivRuleta = findViewById(R.id.ivRuleta);
        layoutFiltros = findViewById(R.id.layoutFiltros);

        cargarSonidos();
        repository = FirebaseRepository.getInstance();

        repository.getAllRestaurantes((value, error) -> {
            if (error != null || value == null) return;

            todosRestaurantes.clear();
            tipos.clear();
            tipos.add("Todos");

            for (QueryDocumentSnapshot doc : value) {
                Restaurante r = doc.toObject(Restaurante.class);
                if (r != null) {
                    r.setId(doc.getId());
                    todosRestaurantes.add(r);
                    String tipo = r.getTipoCocinaPais();
                    if (!tipos.contains(tipo)) {
                        tipos.add(tipo);
                    }
                }
            }
            restaurantesFiltrados = new ArrayList<>(todosRestaurantes);
            configurarFiltros();
        });

        btnGirar.setOnClickListener(v -> {
            if (!girando) {
                girarRuleta();
            }
        });
    }

    private void cargarSonidos() {
        try {
            mediaPlayerGiro = MediaPlayer.create(this, R.raw.ruleta_giro);
            mediaPlayerCampana = MediaPlayer.create(this, R.raw.campana);
        } catch (Exception e) {
            // Sonidos opcionales
        }
    }

    private void configurarFiltros() {
        layoutFiltros.removeAllViews();

        for (String tipo : tipos) {
            Button btnFiltro = new Button(this);
            btnFiltro.setText(tipo);
            btnFiltro.setPadding(24, 12, 24, 12);
            btnFiltro.setBackgroundTintList(getColorStateList(android.R.color.darker_gray));

            if (tipo.equals(tipoSeleccionado)) {
                btnFiltro.setBackgroundTintList(getColorStateList(android.R.color.holo_orange_dark));
            }

            btnFiltro.setOnClickListener(v -> {
                tipoSeleccionado = tipo;
                actualizarColoresFiltros();

                if (tipo.equals("Todos")) {
                    restaurantesFiltrados = new ArrayList<>(todosRestaurantes);
                } else {
                    restaurantesFiltrados.clear();
                    for (Restaurante r : todosRestaurantes) {
                        if (r.getTipoCocinaPais().equals(tipo)) {
                            restaurantesFiltrados.add(r);
                        }
                    }
                }
                Toast.makeText(this, "Filtro: " + tipo, Toast.LENGTH_SHORT).show();
            });

            layoutFiltros.addView(btnFiltro);
        }
    }

    private void actualizarColoresFiltros() {
        for (int i = 0; i < layoutFiltros.getChildCount(); i++) {
            Button b = (Button) layoutFiltros.getChildAt(i);
            if (b.getText().equals(tipoSeleccionado)) {
                b.setBackgroundTintList(getColorStateList(android.R.color.holo_orange_dark));
            } else {
                b.setBackgroundTintList(getColorStateList(android.R.color.darker_gray));
            }
        }
    }

    private void girarRuleta() {
        if (restaurantesFiltrados.isEmpty()) {
            Toast.makeText(this, "No hay restaurantes en esta categoría", Toast.LENGTH_SHORT).show();
            return;
        }

        girando = true;

        // Detener sonido anterior si existe
        if (mediaPlayerGiro != null) {
            if (mediaPlayerGiro.isPlaying()) {
                mediaPlayerGiro.pause();
            }
            mediaPlayerGiro.seekTo(3000);  // Saltar silencio inicial (3 segundos)
            mediaPlayerGiro.start();

            // Detener el sonido después de 3 segundos de reproducción real
            new Handler().postDelayed(() -> {
                if (mediaPlayerGiro != null && mediaPlayerGiro.isPlaying()) {
                    mediaPlayerGiro.pause();
                    mediaPlayerGiro.seekTo(3000);
                }
            }, 3000);
        }

        // Animación: 6 vueltas en 3 segundos
        RotateAnimation rotate = new RotateAnimation(
                0, 2160,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );
        rotate.setDuration(3000);
        rotate.setFillAfter(true);
        ivRuleta.startAnimation(rotate);

        // Campana y resultado al final
        new Handler().postDelayed(() -> {
            // Campana
            if (mediaPlayerCampana != null) {
                mediaPlayerCampana.seekTo(0);
                mediaPlayerCampana.start();
            }

            // Resultado aleatorio
            Random random = new Random();
            int index = random.nextInt(restaurantesFiltrados.size());
            Restaurante elegido = restaurantesFiltrados.get(index);

            String mensaje = "🍽️ " + elegido.getNombre() + "\n" +
                    "📍 " + elegido.getDireccion() + "\n" +
                    "⭐ " + elegido.getValoracionMedia() + "/5\n\n" +
                    "👆 Toca para ver detalles";
            tvResultado.setText(mensaje);
            girando = false;

            tvResultado.setOnClickListener(v -> {
                Intent intent = new Intent(RuletaActivity.this, DetalleRestauranteActivity.class);
                intent.putExtra("restaurante_id", elegido.getId());
                startActivity(intent);
            });
        }, 3000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayerGiro != null) {
            mediaPlayerGiro.release();
            mediaPlayerGiro = null;
        }
        if (mediaPlayerCampana != null) {
            mediaPlayerCampana.release();
            mediaPlayerCampana = null;
        }
    }
}