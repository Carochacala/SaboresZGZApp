package com.comenendez.saboreszgz.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.comenendez.saboreszgz.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity {

    // 1. Variables para controlar Firebase y Google
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    // 2. Preparar el "Recibidor" que espera la respuesta de la ventanita de Google
    private final ActivityResultLauncher<Intent> googleSignInLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                    try {
                        // Google confirma quién es el usuario, sacamos su "Token"
                        GoogleSignInAccount account = task.getResult(ApiException.class);
                        // Se lo enviamos a Firebase
                        firebaseAuthWithGoogle(account.getIdToken());
                    } catch (ApiException e) {
                        Toast.makeText(this, "Error al conectar con Google", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Llamamos a nuestro cargador para que suba los datos a Firebase
        //CargadorDatosFirebase.cargarRestaurantesIniciales();
        // ------------------------------------
        // Inicializar Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Configurar las opciones de inicio de sesión de Google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Encontrar los botones por su ID
        Button btnInvitado = findViewById(R.id.btnInvitado);

        View btnGoogle = findViewById(R.id.btnGoogle);

        // Acción para el botón de Invitado
        btnInvitado.setOnClickListener(v -> {
            Intent intencion = new Intent(MainActivity.this, SeleccionPais.class);
            startActivity(intencion);
        });

        // Acción para el botón de Google
        btnGoogle.setOnClickListener(v -> {
            // Abrimos la ventana de selección de cuenta de Google
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            googleSignInLauncher.launch(signInIntent);
        });
    }

    // 3. Método final: Firebase valida la credencial y nos deja entrar
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // El usuario se ha registrado/iniciado sesión
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(MainActivity.this, "¡Bienvenido a SaboresZGZ!", Toast.LENGTH_SHORT).show();

                        // para ir a pantalla de los países
                        Intent intencion = new Intent(MainActivity.this, SeleccionPais.class);
                        startActivity(intencion);

                        // Cerramos esta pantalla (el Login) para que al darle "atrás" en el móvil no vuelva aquí
                        finish();
                    } else {
                        // Si algo falla
                        Toast.makeText(MainActivity.this, "Fallo en la autenticación.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}