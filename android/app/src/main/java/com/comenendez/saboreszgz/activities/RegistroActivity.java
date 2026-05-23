package com.comenendez.saboreszgz.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.comenendez.saboreszgz.R;
import com.comenendez.saboreszgz.helpers.ToastHelper;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FieldValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import com.comenendez.saboreszgz.helpers.ValidacionHelper;  // ← IMPORTAR VALIDACIONHELPER

public class RegistroActivity extends AppCompatActivity {

    EditText etNombre, etEmail, etPassword;
    Button btnRegister;

    FirebaseAuth auth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        etNombre = findViewById(R.id.etNombre);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(v -> registerUser());
    }

    // ============================================================
    // REGISTRO DE USUARIO CON VALIDACIONES
    // ============================================================
    private void registerUser() {

        String nombre = etNombre.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // ============================================================
        // ANTES: if (nombre.isEmpty() || email.isEmpty() || password.isEmpty())
        // AHORA: Usamos ValidacionHelper
        // ============================================================
        if (ValidacionHelper.isCampoVacio(nombre) ||
                ValidacionHelper.isCampoVacio(email) ||
                ValidacionHelper.isCampoVacio(password)) {
            ToastHelper.mostrarError(this, "Completa todos los campos");
            return;
        }

        // ============================================================
        // VALIDACIÓN DE EMAIL (NUEVO)
        // ============================================================
        if (!ValidacionHelper.isEmailValido(email)) {
            ToastHelper.mostrarError(this, "Introduce un email válido");
            return;
        }

        // ============================================================
        // VALIDACIÓN DE CONTRASEÑA (NUEVO)
        // ============================================================
        if (!ValidacionHelper.isPasswordValida(password)) {
            ToastHelper.mostrarError(this, "La contraseña debe tener al menos 6 carácteres");
            return;
        }

        // ============================================================
        // VALIDACIÓN DE NOMBRE (NUEVO)
        // ============================================================
        if (!ValidacionHelper.isNombreValido(nombre)) {
            ToastHelper.mostrarError(this, "El nombre debe tener al menos 2 carácteres");
            return;
        }

        // ============================================================
        // REGISTRO EN FIREBASE
        // ============================================================
        auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {

                    String uid = auth.getCurrentUser().getUid();

                    Map<String, Object> user = new HashMap<>();
                    user.put("nombre", nombre);
                    user.put("email", email);

                    // 👤 SIEMPRE usuario
                    user.put("tipoUsuario", "usuario");

                    // ❤️ favoritos vacíos
                    user.put("favoritos", new ArrayList<>());

                    // 📅 fecha registro
                    user.put("fechaRegistro", FieldValue.serverTimestamp());

                    db.collection("usuarios")
                            .document(uid)
                            .set(user)
                            .addOnSuccessListener(aVoid -> {
                                ToastHelper.mostrarExito(this, "Registro exitoso");
                                startActivity(new Intent(this, HomeActivity.class));
                                finish();
                            });

                })
                .addOnFailureListener(e ->
                        ToastHelper.mostrarError(this, "Error: " + e.getMessage())
                );
    }
}