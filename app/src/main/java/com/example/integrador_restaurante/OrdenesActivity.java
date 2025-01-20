package com.example.integrador_restaurante;

import android.content.Intent;
import android.os.Bundle;
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

        // referencias
        ordenesContainer = findViewById(R.id.ordenesContainer);
        btnSalir = findViewById(R.id.btnSalir);
        btnVolverOrdenar = findViewById(R.id.btnVolverOrdenar);

        // Recibir las órdenes
        ordenesList = getIntent().getStringArrayListExtra("ordenes");
        String ingredientesSeleccionados = getIntent().getStringExtra("ingredientes");
        int cantidad = getIntent().getIntExtra("cantidad", 1);
        int costo = getIntent().getIntExtra("costo", 125); // Costo predeterminado

        if (ordenesList != null && !ordenesList.isEmpty()) {
            // Mostrar las órdenes
            for (String orden : ordenesList) {
                addOrdenToView(orden, ingredientesSeleccionados, cantidad, costo);
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

    private void addOrdenToView(String orden, String ingredientes, int cantidad, int costo) {

        LinearLayout ordenLayout = new LinearLayout(this);
        ordenLayout.setOrientation(LinearLayout.VERTICAL);
        ordenLayout.setPadding(16, 16, 16, 16);


        TextView ordenText = new TextView(this);
        ordenText.setText(
                orden +
                        "\nCantidad: " + cantidad +
                        "\nIngredientes: " + ingredientes +
                        "\nCosto: $" + costo
        );
        ordenText.setTextColor(getResources().getColor(android.R.color.black));
        ordenText.setTextSize(18);

        // Botón para cancelar la orden
        Button cancelButton = new Button(this);
        cancelButton.setText("Cancelar");
        cancelButton.setTextColor(getResources().getColor(android.R.color.white));
        cancelButton.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));

        // Eliminar la orden
        cancelButton.setOnClickListener(v -> {
            // Eliminar la orden de la lista
            ordenesList.remove(orden);

            // Eliminar la orden de la vista
            ordenesContainer.removeView(ordenLayout);

            //mensaje de confirmación
            Toast.makeText(this, "Orden cancelada", Toast.LENGTH_SHORT).show();
        });


        ordenLayout.addView(ordenText);
        ordenLayout.addView(cancelButton);


        ordenesContainer.addView(ordenLayout);
    }
}
