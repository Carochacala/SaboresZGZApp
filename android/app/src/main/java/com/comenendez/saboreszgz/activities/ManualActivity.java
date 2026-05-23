package com.comenendez.saboreszgz.activities;

import android.os.Bundle;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.webkit.WebViewAssetLoader;
import androidx.webkit.WebViewClientCompat;
import com.comenendez.saboreszgz.R;

public class ManualActivity extends AppCompatActivity {

    private static final String TAG = "ManualActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual);

        // Configurar la Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // 1. Configurar WebViewAssetLoader
        final WebViewAssetLoader assetLoader = new WebViewAssetLoader.Builder()
                .addPathHandler("/assets/", new WebViewAssetLoader.AssetsPathHandler(this))
                .build();

        // 2. Configurar el WebView
        WebView webView = findViewById(R.id.webViewManual);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClientCompat() {
            @Override
            @RequiresApi(21)
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                return assetLoader.shouldInterceptRequest(request.getUrl());
            }

            @Override
            @SuppressWarnings("deprecation")
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                return assetLoader.shouldInterceptRequest(android.net.Uri.parse(url));
            }
        });

        // 3. Cargar el manual correspondiente
        // Recibimos el nombre del manual desde el Intent
        String manualName = getIntent().getStringExtra("MANUAL_NAME");
        // Ruta dentro de assets, por ejemplo: "manuales/manual_usuario.html"
        String manualPath = "manuales/" + manualName;
        // La URL mágica que usa el WebViewAssetLoader
        String url = "https://appassets.androidplatform.net/assets/" + manualPath;
        webView.loadUrl(url);
    }

    // Manejar la flecha de la toolbar para volver atrás
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}