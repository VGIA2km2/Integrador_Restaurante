package com.example.integrador_restaurante;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class OrdenesActivity extends AppCompatActivity {
    private LinearLayout ordenesContainer;
    private ArrayList<String> ordenesList = new ArrayList<>();
    private Button btnSalir, btnVolverOrdenar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordenes);

        // Obtener referencias
        ordenesContainer = findViewById(R.id.ordenesContainer);
        btnSalir = findViewById(R.id.btnSalir);
        btnVolverOrdenar = findViewById(R.id.btnVolverOrdenar);

        // Recibir las órdenes desde la actividad anterior
        ordenesList = getIntent().getStringArrayListExtra("ordenes");

        if (ordenesList != null && !ordenesList.isEmpty()) {
            // Mostrar las órdenes en la vista
            for (String orden : ordenesList) {
                addOrdenToView(orden);
            }
        } else {
            // Si no hay órdenes, mostrar un mensaje
            Toast.makeText(this, "No hay órdenes para mostrar", Toast.LENGTH_SHORT).show();
        }

        btnSalir.setOnClickListener(v -> finish());

        btnVolverOrdenar.setOnClickListener(v -> {
            // Regresa a la actividad de comida, enviando las órdenes actualizadas
            Intent intent = new Intent(OrdenesActivity.this, ComidaActivity.class);
            intent.putStringArrayListExtra("ordenes", ordenesList); // Enviar las órdenes actualizadas
            startActivity(intent);
        });
    }

    private void addOrdenToView(String orden) {
        // Crear un nuevo layout para cada orden
        LinearLayout ordenLayout = new LinearLayout(this);
        ordenLayout.setOrientation(LinearLayout.HORIZONTAL);
        ordenLayout.setPadding(16, 16, 16, 16);

        // Mostrar el número de orden
        TextView ordenText = new TextView(this);
        ordenText.setText(orden);
        ordenText.setTextColor(getResources().getColor(android.R.color.black));
        ordenText.setTextSize(18);

        // Botón para cancelar la orden
        Button cancelButton = new Button(this);
        cancelButton.setText("X");
        cancelButton.setTextColor(getResources().getColor(android.R.color.white));
        cancelButton.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));

        // Eliminar la orden
        cancelButton.setOnClickListener(v -> {
            // Eliminar la orden de la lista
            ordenesList.remove(orden);

            // Eliminar la orden de la vista
            ordenesContainer.removeView(ordenLayout);

            // Mostrar un mensaje de confirmación
            Toast.makeText(this, "Orden cancelada", Toast.LENGTH_SHORT).show();
        });

        // Añadir la orden y el botón al layout
        ordenLayout.addView(ordenText);
        ordenLayout.addView(cancelButton);

        // Añadir el layout de la orden al contenedor de órdenes
        ordenesContainer.addView(ordenLayout);
    }
}
