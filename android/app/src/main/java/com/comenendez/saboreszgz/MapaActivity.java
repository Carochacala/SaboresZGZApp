package com.comenendez.saboreszgz;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.comenendez.saboreszgz.data.FirebaseRepository;
import com.comenendez.saboreszgz.model.Restaurante;
import com.google.firebase.firestore.QueryDocumentSnapshot;


import java.util.ArrayList;
import java.util.List;

public class MapaActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FirebaseRepository repository;
    private List<Restaurante> restaurantes = new ArrayList<>();
    private String tipoCocina;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_restaurantes);

        tipoCocina = getIntent().getStringExtra("tipo_cocina");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapa);
        mapFragment.getMapAsync(this);

        repository = FirebaseRepository.getInstance();
        cargarRestaurantes();
    }

    private void cargarRestaurantes() {
        if (tipoCocina != null && !tipoCocina.isEmpty()) {
            repository.getRestaurantesByTipo(tipoCocina, (value, error) -> {
                if (error != null || value == null) return;

                restaurantes.clear();
                for (QueryDocumentSnapshot doc : value) {
                    Restaurante r = doc.toObject(Restaurante.class);
                    r.setId(doc.getId());
                    restaurantes.add(r);
                }

                if (mMap != null) {
                    agregarMarcadores();
                }
            });
        } else {
            repository.getAllRestaurantes((value, error) -> {
                if (error != null || value == null) return;

                restaurantes.clear();
                for (QueryDocumentSnapshot doc : value) {
                    Restaurante r = doc.toObject(Restaurante.class);
                    r.setId(doc.getId());
                    restaurantes.add(r);
                }

                if (mMap != null) {
                    agregarMarcadores();
                }
            });
        }
    }

    private void agregarMarcadores() {
        mMap.clear();

        for (Restaurante restaurante : restaurantes) {
            if (restaurante.getUbicacion() != null) {
                LatLng ubicacion = new LatLng(
                        restaurante.getUbicacion().getLatitude(),
                        restaurante.getUbicacion().getLongitude()
                );

                mMap.addMarker(new MarkerOptions()
                        .position(ubicacion)
                        .title(restaurante.getNombre())
                        .snippet(restaurante.getDireccion()));
            }
        }

        // Centrar el mapa en el primer restaurante si existe
        if (!restaurantes.isEmpty() && restaurantes.get(0).getUbicacion() != null) {
            LatLng centro = new LatLng(
                    restaurantes.get(0).getUbicacion().getLatitude(),
                    restaurantes.get(0).getUbicacion().getLongitude()
            );
            mMap.moveCamera(com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom(centro, 12));
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        agregarMarcadores();
    }
}