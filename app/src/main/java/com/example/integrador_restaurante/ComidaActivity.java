package com.example.integrador_restaurante;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.ImageButton; // Importa ImageButton

import androidx.appcompat.app.AppCompatActivity;

public class ComidaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comida); // Vista de selección de comida

        // Referencias a los botones de comida
        ImageButton btnComida = findViewById(R.id.imgbtn_comida);      // Usamos ImageButton

        // Acción para el botón Comida
        btnComida.setOnClickListener(v ->
                Toast.makeText(ComidaActivity.this, "Has seleccionado comida", Toast.LENGTH_SHORT).show());
    }
}

