package com.comenendez.saboreszgz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.comenendez.saboreszgz.data.FirebaseRepository;
import com.comenendez.saboreszgz.model.Restaurante;
import com.comenendez.saboreszgz.model.Valoracion;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

public class DetalleRestauranteActivity extends AppCompatActivity implements OnMapReadyCallback {

    private TextView tvNombre, tvDireccion, tvHorario, tvPlatoDestacado, tvValoracion;
    private ImageView imgRestaurante;
    private Button btnFavorito, btnEnviarValoracion;
    private RatingBar ratingBar;
    private EditText etComentario;
    private RecyclerView rvValoraciones;
    private FirebaseRepository repository;
    private String restauranteId;
    private Restaurante restaurante;
    private GoogleMap mMap;
    private ValoracionAdapter valoracionAdapter;
    private List<Valoracion> listaValoraciones = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_restaurante);

        // Inicializar views
        tvNombre = findViewById(R.id.tvNombre);
        tvDireccion = findViewById(R.id.tvDireccion);
        tvHorario = findViewById(R.id.tvHorario);
        tvPlatoDestacado = findViewById(R.id.tvPlatoDestacado);
        tvValoracion = findViewById(R.id.tvValoracion);
        imgRestaurante = findViewById(R.id.imgRestaurante);
        btnFavorito = findViewById(R.id.btnFavorito);
        btnEnviarValoracion = findViewById(R.id.btnEnviarValoracion);
        ratingBar = findViewById(R.id.ratingBar);
        etComentario = findViewById(R.id.etComentario);
        rvValoraciones = findViewById(R.id.rvValoraciones);

        repository = FirebaseRepository.getInstance();
        restauranteId = getIntent().getStringExtra("restaurante_id");

        // Configurar RecyclerView de valoraciones
        rvValoraciones.setLayoutManager(new LinearLayoutManager(this));
        valoracionAdapter = new ValoracionAdapter(listaValoraciones);
        rvValoraciones.setAdapter(valoracionAdapter);

        // ========== SI ES INVITADO: OCULTAR FORMULARIO Y MOSTRAR BOTÓN ==========
        if (!repository.isUserLoggedIn()) {
            // Ocultar el formulario de valoración
            ratingBar.setVisibility(View.GONE);
            etComentario.setVisibility(View.GONE);
            btnEnviarValoracion.setVisibility(View.GONE);

            // Crear un botón para registrarse
            Button btnRegistrate = new Button(this);
            btnRegistrate.setText("📝 Regístrate para dejar tu valoración");
            btnRegistrate.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_dark));
            btnRegistrate.setOnClickListener(v -> {
                Intent intent = new Intent(DetalleRestauranteActivity.this, RegistroActivity.class);
                startActivity(intent);
            });

            // Agregar el botón donde estaba el RatingBar
            ViewGroup parent = (ViewGroup) ratingBar.getParent();
            int index = parent.indexOfChild(ratingBar);
            parent.addView(btnRegistrate, index);

            // Agregar un margen al botón
            ViewGroup.LayoutParams params = btnRegistrate.getLayoutParams();
            if (params instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) params;
                marginParams.topMargin = 16;
                marginParams.bottomMargin = 16;
                btnRegistrate.setLayoutParams(marginParams);
            }
        }

        if (restauranteId != null) {
            cargarRestaurante();
            cargarValoraciones();
        }

        // ========== BOTÓN FAVORITO ==========
        btnFavorito.setOnClickListener(v -> {
            if (repository.isUserLoggedIn()) {
                if (repository.isFavorite(restauranteId)) {
                    repository.removeFavorite(restauranteId, task -> {
                        Toast.makeText(this, "Eliminado de favoritos", Toast.LENGTH_SHORT).show();
                        btnFavorito.setText("Guardar en favoritos");
                    });
                } else {
                    repository.addFavorite(restauranteId, task -> {
                        Toast.makeText(this, "Añadido a favoritos", Toast.LENGTH_SHORT).show();
                        btnFavorito.setText("Eliminar de favoritos");
                    });
                }
            } else {
                new androidx.appcompat.app.AlertDialog.Builder(this)
                        .setTitle("Regístrate")
                        .setMessage("Regístrate o inicia sesión para guardar restaurantes en favoritos")
                        .setPositiveButton("Registrarse", (dialog, which) -> {
                            startActivity(new Intent(DetalleRestauranteActivity.this, RegistroActivity.class));
                        })
                        .setNegativeButton("Cancelar", null)
                        .show();
            }
        });

        // ========== BOTÓN ENVIAR VALORACIÓN ==========
        btnEnviarValoracion.setOnClickListener(v -> {
            if (repository.isUserLoggedIn()) {
                int puntuacion = (int) ratingBar.getRating();
                String comentario = etComentario.getText().toString().trim();

                if (puntuacion == 0) {
                    Toast.makeText(this, "Selecciona una puntuación", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (comentario.isEmpty()) {
                    Toast.makeText(this, "Escribe un comentario", Toast.LENGTH_SHORT).show();
                    return;
                }

                repository.addValoracion(restauranteId, puntuacion, comentario, task -> {
                    Toast.makeText(this, "Valoración enviada", Toast.LENGTH_SHORT).show();
                    ratingBar.setRating(0);
                    etComentario.setText("");
                    cargarValoraciones();
                });
            } else {
                // INVITADO: Mostrar diálogo y redirigir al registro
                new androidx.appcompat.app.AlertDialog.Builder(this)
                        .setTitle("Inicia sesión")
                        .setMessage("Para dejar una valoración necesitas registrarte o iniciar sesión. ¿Quieres hacerlo ahora?")
                        .setPositiveButton("Registrarse", (dialog, which) -> {
                            Intent intent = new Intent(DetalleRestauranteActivity.this, RegistroActivity.class);
                            startActivity(intent);
                        })
                        .setNegativeButton("Cancelar", null)
                        .show();
            }
        });
    }
    private void cargarRestaurante() {
        repository.getRestauranteById(restauranteId, (documentSnapshot, error) -> {
            if (error != null || documentSnapshot == null || !documentSnapshot.exists()) {
                Toast.makeText(this, "Error al cargar restaurante", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            restaurante = documentSnapshot.toObject(Restaurante.class);
            if (restaurante != null) {
                restaurante.setId(documentSnapshot.getId());
                mostrarDatos();
                inicializarMapa();
            }
        });
    }

    private void mostrarDatos() {
        tvNombre.setText(restaurante.getNombre());
        tvDireccion.setText("📍 " + restaurante.getDireccion());
        tvHorario.setText("🕐 " + restaurante.getHorario());

        if (restaurante.getPlatoDestacado() != null && !restaurante.getPlatoDestacado().isEmpty()) {
            tvPlatoDestacado.setText("🍽️ Plato destacado: " + restaurante.getPlatoDestacado().get(0));
        }

        tvValoracion.setText("⭐ " + restaurante.getValoracionMedia() + " / 5");

        if (restaurante.getFotoUrl() != null && !restaurante.getFotoUrl().isEmpty()) {
            Picasso.get()
                    .load(restaurante.getFotoUrl())
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.ic_menu_gallery)
                    .into(imgRestaurante);
        }

        if (repository.isUserLoggedIn() && repository.isFavorite(restauranteId)) {
            btnFavorito.setText("Eliminar de favoritos");
        }
    }

    private void inicializarMapa() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapa);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    private void cargarValoraciones() {
        android.util.Log.d("VALORACIONES", "=== CARGANDO VALORACIONES ===");
        android.util.Log.d("VALORACIONES", "Buscando para restauranteId: '" + restauranteId + "'");

        if (restauranteId == null) {
            android.util.Log.d("VALORACIONES", "ERROR: restauranteId es null");
            return;
        }

        repository.getValoracionesByRestaurante(restauranteId, (value, error) -> {
            if (error != null) {
                android.util.Log.d("VALORACIONES", "Error: " + error.getMessage());
                return;
            }

            if (value == null) {
                android.util.Log.d("VALORACIONES", "value es null");
                return;
            }

            android.util.Log.d("VALORACIONES", "Documentos encontrados en Firestore: " + value.size());

            // Mostrar cada valoración encontrada
            for (DocumentSnapshot doc : value.getDocuments()) {
                android.util.Log.d("VALORACIONES", "--- Documento ---");
                android.util.Log.d("VALORACIONES", "ID: " + doc.getId());
                android.util.Log.d("VALORACIONES", "restauranteId: '" + doc.getString("restauranteId") + "'");
                android.util.Log.d("VALORACIONES", "comentario: " + doc.getString("comentario"));
                android.util.Log.d("VALORACIONES", "puntuacion: " + doc.getLong("puntuacion"));
                android.util.Log.d("VALORACIONES", "nombreUsuario: " + doc.getString("nombreUsuario"));
            }

            listaValoraciones.clear();
            for (QueryDocumentSnapshot doc : value) {
                Valoracion v = doc.toObject(Valoracion.class);
                if (v != null) {

                    listaValoraciones.add(v);
                }
            }

            android.util.Log.d("VALORACIONES", "Total valoraciones cargadas en lista: " + listaValoraciones.size());
            valoracionAdapter.updateList(listaValoraciones);
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (restaurante != null && restaurante.getUbicacion() != null) {
            LatLng ubicacion = new LatLng(
                    restaurante.getUbicacion().getLatitude(),
                    restaurante.getUbicacion().getLongitude()
            );

            mMap.addMarker(new MarkerOptions()
                    .position(ubicacion)
                    .title(restaurante.getNombre())
                    .snippet(restaurante.getDireccion()));

            mMap.moveCamera(com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom(ubicacion, 15));
        }
    }
}