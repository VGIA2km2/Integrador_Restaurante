package com.example.integrador_restaurante;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // Diseño del login

        Button btnContinuar = findViewById(R.id.btnContinuar);

        btnContinuar.setOnClickListener(v -> {
            // Navegar a ComidaActivity
            Intent intent = new Intent(LoginActivity.this, ComidaActivity.class);
            startActivity(intent);
        });
    }
}

