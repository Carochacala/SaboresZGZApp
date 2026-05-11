package com.comenendez.saboreszgz.UI;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.comenendez.saboreszgz.R;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.firebase.firestore.FirebaseFirestore;

//para firbase

// Necesitarás una librería para cargar la imagen, como Glide o Picasso
// import com.bumptech.glide.Glide;

public class RestauranteElegido extends AppCompatActivity {
    //clase conectada con el activity_detallerestaurante,donde se ve la descripcion,nombre,estrellas
    //horarios,titulos,mapa,boton

    //declaracion varibales
    TextView nombreRest, direccionRest, horariosRest, tituloPlatosDestacados;
    ImageView imagenRest;
    RatingBar estrellasRest;
    RecyclerView galeriaPlatosDestacados;
    SupportMapFragment mapaRest;
    Button botonComoLlegarRest;

    //variable para la BD en firebase
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurante_elegido);

        //inicializamos bd
        db = FirebaseFirestore.getInstance();

        //conectamos  las variables del  xml
        nombreRest = findViewById(R.id.tvNombreRestaurante);
        direccionRest = findViewById(R.id.tvDireccion);
        horariosRest = findViewById(R.id.tvHorariosRestaurante);
        tituloPlatosDestacados = findViewById(R.id.tvTituloPlatosDestacados);
        imagenRest = findViewById(R.id.imgRestaurante);
        estrellasRest = findViewById(R.id.rb_estrellasRestaurante);
        galeriaPlatosDestacados = findViewById(R.id.rvPlatosDestacadosimg);
        //para el mapa
        mapaRest = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapaRectangulo);
        botonComoLlegarRest = findViewById(R.id.btnComoLlegar);


        //pasamos info de bd con intents
        //atrapamos el id que nos mando el restauranteadapater
        String restauranteId = getIntent().getStringExtra("RESTAURANTE_ID");
        //if por si llega vacio
        if (restauranteId != null) {
            obtenerDatosRestaurante(restauranteId);
        }
    }

    private void obtenerDatosRestaurante(String id) {
        db.collection("restaurantes").document(id).
                get().addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        //1.a convercion del doc directamente a nuestro objeto RESTAURANTE
                        com.comenendez.saboreszgz.modelo.Restaurante rest = documentSnapshot.toObject(com.comenendez.saboreszgz.modelo.Restaurante.class);

                        //para evitar errores
                        if (rest != null) {
                            //extraccion datos,con nombres a raja tabla de variable rest de antes,SETTEAMOS
                            nombreRest.setText(rest.getNombre());//string nombre,etc etc
                            direccionRest.setText(rest.getDireccion());
                            horariosRest.setText(rest.getHorario());
                            estrellasRest.setRating((float) rest.getValoracionMedia());

                        }
                        //Para imagenes(GALIDE)
                        if (rest.getFotoUrl() != null) {
                            com.bumptech.glide.Glide.with(this).load(rest.getFotoUrl()).into(imagenRest);
                        }


                        //PARA la ubicacion
                        // Así lo leeremos más adelante en
                        com.google.firebase.firestore.GeoPoint ubicacion = rest.getUbicacion();
                        if (ubicacion != null) {

                            // --- 1. ACCIÓN DEL BOTÓN "CÓMO LLEGAR" ---
                            botonComoLlegarRest.setOnClickListener(v -> {
                                double lat = ubicacion.getLatitude();
                                double lng = ubicacion.getLongitude();
                                // Creamos la orden para abrir el GPS
                                android.net.Uri gmmIntentUri = android.net.Uri.parse("google.navigation:q=" + lat + "," + lng+ "&mode=w");
                                android.content.Intent mapIntent = new android.content.Intent(android.content.Intent.ACTION_VIEW, gmmIntentUri);
                                mapIntent.setPackage("com.google.android.apps.maps"); // Forzamos Google Maps

                                // Verificamos que el móvil tenga Maps instalado
                                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                                    startActivity(mapIntent);
                                }
                            });

                            // --- 2. DIBUJAR EL MAPA VISUAL ---
                            if (mapaRest != null) {
                                mapaRest.getMapAsync(googleMap -> {
                                    double lat = ubicacion.getLatitude();
                                    double lng = ubicacion.getLongitude();
                                    com.google.android.gms.maps.model.LatLng pos = new com.google.android.gms.maps.model.LatLng(lat, lng);

                                    // Añadimos el marcador rojo
                                    googleMap.addMarker(new com.google.android.gms.maps.model.MarkerOptions()
                                            .position(pos)
                                            .title(rest.getNombre()));

                                    // MUEVE LA CÁMARA (¡Aquí estaba! jaja)
                                    googleMap.moveCamera(com.google.android.gms.maps.CameraUpdateFactory
                                            .newLatLngZoom(pos, 16f));
                                });
                            }
                        }
                    }

                });
    }
}



