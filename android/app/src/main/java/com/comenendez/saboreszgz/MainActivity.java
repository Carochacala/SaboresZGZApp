package com.comenendez.saboreszgz;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.comenendez.saboreszgz.data.FirebaseRepository;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import android.widget.EditText;
import androidx.appcompat.app.AlertDialog;


public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    private GoogleSignInClient googleSignInClient;
    private static final int RC_SIGN_IN = 9001;  // Código para identificar la respuesta


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // 🔥 Inicializar Firebase
        auth = FirebaseAuth.getInstance();

        // ========== CONFIGURAR GOOGLE SIGN-IN ==========
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("485950113186-nuodp603npg4na8754e81fhejc50ao9v.apps.googleusercontent.com")
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);
        // ===============================================

        // Botón de Google Sign-In
        Button btnGoogleSignIn = findViewById(R.id.btnGoogleSignIn);
        btnGoogleSignIn.setOnClickListener(v -> {
            android.util.Log.d("GOOGLE", "1. Botón clickeado");
            signInWithGoogle();
        });



        // 🔗 Conectar con XML
        EditText etCorreo = findViewById(R.id.etCorreo);
        EditText etPassword = findViewById(R.id.etPassword);
        Button btnLogin = findViewById(R.id.btnIniciarSesion);
        Button btnInvitado = findViewById(R.id.btnInvitado);
        TextView tvRegistrate = findViewById(R.id.tvRegistrate);

        //Registro
        tvRegistrate.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegistroActivity.class);
            startActivity(intent);
        });

        // 🔐 LOGIN
        btnLogin.setOnClickListener(v -> {

            String email = etCorreo.getText().toString();
            String pass = etPassword.getText().toString();

            if (email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Rellena todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            auth.signInWithEmailAndPassword(email, pass)
                    .addOnSuccessListener(authResult -> {
                        startActivity(new Intent(this, HomeActivity.class));
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Error al iniciar sesión", Toast.LENGTH_SHORT).show();
                    });
        });

        // 🔓 INVITADO
        btnInvitado.setOnClickListener(v -> {
            // Cerrar sesión si hay alguien logueado
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                FirebaseAuth.getInstance().signOut();
            }
            startActivity(new Intent(this, HomeActivity.class));
        });

        // 🔲 Ajuste pantalla
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // En MainActivity, dentro del onCreate
        FirebaseRepository repo = FirebaseRepository.getInstance();

        repo.getAllRestaurantes((value, error) -> {
            if (error != null) {
                android.util.Log.e("FIREBASE", "Error: " + error.getMessage());
                return;
            }

            if (value != null) {
                android.util.Log.d("FIREBASE", "Restaurantes encontrados: " + value.size());
            }
        });

        // Recuperar contraseña
        TextView tvOlvidoPass = findViewById(R.id.tvOlvidoPass);
        tvOlvidoPass.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Recuperar contraseña");
            builder.setMessage("Ingresa tu correo electrónico");

            final EditText input = new EditText(this);
            input.setHint("correo@ejemplo.com");
            builder.setView(input);

            builder.setPositiveButton("Enviar", (dialog, which) -> {
                String email = input.getText().toString().trim();
                if (email.isEmpty()) {
                    Toast.makeText(this, "Ingresa un correo válido", Toast.LENGTH_SHORT).show();
                    return;
                }

                auth.sendPasswordResetEmail(email)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(this, "Revisa tu correo para restablecer tu contraseña", Toast.LENGTH_LONG).show();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            });

            builder.setNegativeButton("Cancelar", null);
            builder.show();
        });


    }
    private void signInWithGoogle() {
        android.util.Log.d("GOOGLE", "2. signInWithGoogle llamado");
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
        android.util.Log.d("GOOGLE", "3. Intent iniciado");
    }

    // Manejar el resultado de Google Sign-In
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign-In exitoso
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign-In falló
                Toast.makeText(this, "Error al iniciar con Google: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    // Autenticar con Firebase usando el token de Google
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Login exitoso
                        startActivity(new Intent(MainActivity.this, HomeActivity.class));
                        finish();
                    } else {
                        // Login falló
                        Toast.makeText(MainActivity.this, "Error de autenticación", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}