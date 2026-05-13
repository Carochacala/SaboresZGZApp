package com.comenendez.saboreszgz;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.comenendez.saboreszgz.data.FirebaseRepository;
import com.comenendez.saboreszgz.model.Restaurante;
import com.comenendez.saboreszgz.model.Valoracion;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class DetalleRestauranteActivity extends AppCompatActivity implements OnMapReadyCallback {

    private TextView tvNombre, tvDireccion, tvHorario, tvPlatoDestacado, tvValoracion;
    private ImageView imgRestaurante;
    private Button btnFavorito, btnEnviarValoracion, btnRuta, btnCompartir;
    private RatingBar ratingBar;
    private EditText etComentario;
    private RecyclerView rvValoraciones;
    private FirebaseRepository repository;
    private String restauranteId;
    private Restaurante restaurante;
    private GoogleMap mMap;
    private ValoracionAdapter valoracionAdapter;
    private List<Valoracion> listaValoraciones = new ArrayList<>();
    private String valoracionIdActual;
    private boolean modoEdicion = false;

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
        btnRuta = findViewById(R.id.btnRuta);
        btnCompartir = findViewById(R.id.btnCompartir);

        repository = FirebaseRepository.getInstance();
        restauranteId = getIntent().getStringExtra("restaurante_id");
        modoEdicion = getIntent().getBooleanExtra("modo_edicion", false);

        btnRuta.setOnClickListener(v -> abrirRutaEnGoogleMaps());
        btnCompartir.setOnClickListener(v -> compartirRestaurante());

        // Configurar RecyclerView de valoraciones
        rvValoraciones.setLayoutManager(new LinearLayoutManager(this));
        valoracionAdapter = new ValoracionAdapter(listaValoraciones);
        rvValoraciones.setAdapter(valoracionAdapter);

        // ========== MODO EDICIÓN ==========
        // ========== FORMULARIO DE VALORACIÓN ==========
        if (modoEdicion) {
            // MODO EDICIÓN - Mostrar formulario para editar
            ratingBar.setVisibility(View.VISIBLE);
            etComentario.setVisibility(View.VISIBLE);
            btnEnviarValoracion.setVisibility(View.VISIBLE);

            // Cargar la valoración existente
            repository.getUserValoracion(restauranteId, task -> {
                if (task.isSuccessful() && !task.getResult().isEmpty()) {
                    DocumentSnapshot doc = task.getResult().getDocuments().get(0);
                    valoracionIdActual = doc.getId();
                    int puntuacion = doc.getLong("puntuacion").intValue();
                    String comentario = doc.getString("comentario");
                    ratingBar.setRating(puntuacion);
                    etComentario.setText(comentario);
                    btnEnviarValoracion.setText("Guardar cambios");

                    // Configurar botón para guardar
                    btnEnviarValoracion.setOnClickListener(v -> guardarCambios());
                }
            });
        } else if (repository.isUserLoggedIn()) {
            // MODO NORMAL - Usuario logueado
            // Verificar si el usuario ya tiene valoración
            repository.getUserValoracion(restauranteId, task -> {
                if (task.isSuccessful() && !task.getResult().isEmpty()) {
                    // YA VALORÓ - Ocultar formulario
                    ratingBar.setVisibility(View.GONE);
                    etComentario.setVisibility(View.GONE);
                    btnEnviarValoracion.setVisibility(View.GONE);

                    DocumentSnapshot doc = task.getResult().getDocuments().get(0);
                    valoracionIdActual = doc.getId();
                } else {
                    // NO HA VALORADO - Mostrar formulario
                    ratingBar.setVisibility(View.VISIBLE);
                    etComentario.setVisibility(View.VISIBLE);
                    btnEnviarValoracion.setVisibility(View.VISIBLE);

                    ratingBar.setRating(0);
                    etComentario.setText("");
                    btnEnviarValoracion.setText("Enviar valoración");
                    valoracionIdActual = null;

                    btnEnviarValoracion.setOnClickListener(v -> {
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

                        repository.addValoracion(restauranteId, puntuacion, comentario, taskAdd -> {
                            Toast.makeText(this, "Valoración enviada", Toast.LENGTH_SHORT).show();
                            ratingBar.setVisibility(View.GONE);
                            etComentario.setVisibility(View.GONE);
                            btnEnviarValoracion.setVisibility(View.GONE);
                            cargarValoraciones();
                        });
                    });
                }
            });
        } else {
            // INVITADO - Ocultar formulario y mostrar botón registrarse
            ratingBar.setVisibility(View.GONE);
            etComentario.setVisibility(View.GONE);
            btnEnviarValoracion.setVisibility(View.GONE);

            Button btnRegistrate = new Button(this);
            btnRegistrate.setText("📝 Regístrate para dejar tu valoración");
            btnRegistrate.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_dark));
            btnRegistrate.setOnClickListener(v -> {
                Intent intent = new Intent(DetalleRestauranteActivity.this, RegistroActivity.class);
                startActivity(intent);
            });

            ViewGroup parent = (ViewGroup) ratingBar.getParent();
            int index = parent.indexOfChild(ratingBar);
            parent.addView(btnRegistrate, index);

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
                new AlertDialog.Builder(this)
                        .setTitle("Regístrate")
                        .setMessage("Regístrate o inicia sesión para guardar restaurantes en favoritos")
                        .setPositiveButton("Registrarse", (dialog, which) -> {
                            startActivity(new Intent(DetalleRestauranteActivity.this, RegistroActivity.class));
                        })
                        .setNegativeButton("Cancelar", null)
                        .show();
            }
        });
    }

    private void cargarMiValoracion() {
        repository.getUserValoracion(restauranteId, task -> {
            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                DocumentSnapshot doc = task.getResult().getDocuments().get(0);
                valoracionIdActual = doc.getId();
                int puntuacion = doc.getLong("puntuacion").intValue();
                String comentario = doc.getString("comentario");
                ratingBar.setRating(puntuacion);
                etComentario.setText(comentario);
                btnEnviarValoracion.setText("Guardar cambios");
            }
        });
    }

    private void guardarCambios() {
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

        repository.updateValoracion(valoracionIdActual, puntuacion, comentario, task -> {
            Toast.makeText(this, "Valoración actualizada", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Recargar valoraciones al volver de la edición
        if (restauranteId != null) {
            cargarValoraciones();
        }
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

        // ========== IMAGEN ==========
        if (restaurante.getFotoUrl() != null && !restaurante.getFotoUrl().isEmpty()) {
            Glide.with(this)
                    .load(restaurante.getFotoUrl())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.ic_menu_gallery)
                    .into(imgRestaurante);
        } else {
            imgRestaurante.setImageResource(android.R.drawable.ic_menu_gallery);
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
    private void abrirRutaEnGoogleMaps() {
        if (restaurante == null || restaurante.getUbicacion() == null) {
            Toast.makeText(this, "No hay ubicación disponible", Toast.LENGTH_SHORT).show();
            return;
        }

        double lat = restaurante.getUbicacion().getLatitude();
        double lng = restaurante.getUbicacion().getLongitude();
        String nombre = restaurante.getNombre();

        // Construir URI para abrir Google Maps con ruta
        Uri uri = Uri.parse("google.navigation:q=" + lat + "," + lng + "&mode=d");

        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setPackage("com.google.android.apps.maps");

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            // Si no tiene Google Maps, abrir en navegador
            Uri webUri = Uri.parse("https://maps.google.com/maps?q=" + lat + "," + lng);
            Intent webIntent = new Intent(Intent.ACTION_VIEW, webUri);
            startActivity(webIntent);
        }
    }
    private void compartirRestaurante() {
        if (restaurante == null) return;

        String mensaje = "🍽️ " + restaurante.getNombre() + "\n" +
                "📍 " + restaurante.getDireccion() + "\n";

        if (restaurante.getUbicacion() != null) {
            double lat = restaurante.getUbicacion().getLatitude();
            double lng = restaurante.getUbicacion().getLongitude();
            mensaje += "🗺️ https://maps.google.com/?q=" + lat + "," + lng + "\n";
        }

        mensaje += "\n📱 Descubre más restaurantes en Sabores Zaragoza";

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, mensaje);
        startActivity(Intent.createChooser(intent, "Compartir restaurante"));
    }

    private void cargarValoraciones() {
        repository.getValoracionesByRestaurante(restauranteId, (value, error) -> {
            if (error != null) return;
            if (value == null) return;

            listaValoraciones.clear();
            for (QueryDocumentSnapshot doc : value) {
                Valoracion v = doc.toObject(Valoracion.class);
                if (v != null) {
                    v.setId(doc.getId());
                    listaValoraciones.add(v);
                }
            }
            valoracionAdapter.updateList(listaValoraciones);

            if (valoracionAdapter != null) {
                valoracionAdapter.updateUsuarioId();
            }

            // Si no hay valoraciones y estamos en modo normal (no edición), limpiar el formulario
            if (!modoEdicion && listaValoraciones.isEmpty()) {
                // Verificar si el usuario actual tiene valoración
                boolean tieneMiValoracion = false;
                String userId = repository.getCurrentUserId();
                for (Valoracion v : listaValoraciones) {
                    if (v.getUsuarioId().equals(userId)) {
                        tieneMiValoracion = true;
                        break;
                    }
                }

                if (!tieneMiValoracion && btnEnviarValoracion != null) {
                    btnEnviarValoracion.setText("Enviar valoración");
                    ratingBar.setRating(0);
                    etComentario.setText("");
                    valoracionIdActual = null;
                }
            }
        });
    }

    public void onValoracionEliminada() {
        // Resetear el ID
        valoracionIdActual = null;

        // Mostrar formulario para nueva valoración
        ratingBar.setVisibility(View.VISIBLE);
        etComentario.setVisibility(View.VISIBLE);
        btnEnviarValoracion.setVisibility(View.VISIBLE);
        ratingBar.setRating(0);
        etComentario.setText("");
        btnEnviarValoracion.setText("Enviar valoración");

        // Configurar botón para enviar
        btnEnviarValoracion.setOnClickListener(v -> {
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
                ratingBar.setVisibility(View.GONE);
                etComentario.setVisibility(View.GONE);
                btnEnviarValoracion.setVisibility(View.GONE);
                // Recargar valoraciones para que aparezca la nueva tarjeta
                cargarValoraciones();
            });
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

            // ✅ AÑADIR .anchor(0.5f, 1.0f) para que la punta toque el suelo
            mMap.addMarker(new MarkerOptions()
                    .anchor(0.5f, 1.0f)  // ← ESTO ES CLAVE
                    .position(ubicacion)
                    .title(restaurante.getNombre())
                    .snippet(restaurante.getDireccion()));

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacion, 15f));
        } else {
            LatLng zaragoza = new LatLng(41.6569, -0.8783);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(zaragoza, 12f));
        }
    }
}