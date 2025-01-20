package com.example.integrador_restaurante;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class IngredientesActivity extends AppCompatActivity {

    private CheckBox queso, lechuga, tomate, pepinillos, bacon, cebolla;
    private Button btnCosto, btnContinuar;
    private int cantidad; // Cantidad recibida

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredientes);

        queso = findViewById(R.id.checkbox_queso);
        lechuga = findViewById(R.id.checkbox_lechuga);
        tomate = findViewById(R.id.checkbox_tomate);
        pepinillos = findViewById(R.id.checkbox_pepinillos);
        bacon = findViewById(R.id.checkbox_bacon);
        cebolla = findViewById(R.id.checkbox_cebolla);


        btnCosto = findViewById(R.id.btn_costo);
        btnContinuar = findViewById(R.id.btn_continuar);


        cantidad = getIntent().getIntExtra("cantidad", 1); // Valor por defecto: 1


        btnCosto.setOnClickListener(v -> {
            int costoTotal = calcularCosto(); // Calcula y actualiza el costo
            btnCosto.setText("Costo: $" + costoTotal); // Actualiza el texto del botón
        });


        btnContinuar.setOnClickListener(v -> {
            Intent intent = new Intent(IngredientesActivity.this, OrdenesActivity.class);


            intent.putStringArrayListExtra("ordenes", ComidaActivity.ordenesList);


            StringBuilder ingredientesSeleccionados = new StringBuilder();
            if (queso.isChecked()) ingredientesSeleccionados.append("Queso, ");
            if (lechuga.isChecked()) ingredientesSeleccionados.append("Lechuga, ");
            if (tomate.isChecked()) ingredientesSeleccionados.append("Tomate, ");
            if (pepinillos.isChecked()) ingredientesSeleccionados.append("Pepinillos, ");
            if (bacon.isChecked()) ingredientesSeleccionados.append("Bacon, ");
            if (cebolla.isChecked()) ingredientesSeleccionados.append("Cebolla, ");
            // Eliminar la última coma y espacio
            if (ingredientesSeleccionados.length() > 0) {
                ingredientesSeleccionados.setLength(ingredientesSeleccionados.length() - 2);
            }
            intent.putExtra("ingredientes", ingredientesSeleccionados.toString());

            // Enviar cantidad y costo
            intent.putExtra("cantidad", cantidad);
            intent.putExtra("costo", calcularCosto());

            startActivity(intent);
        });
    }

    private int calcularCosto() {
        int costoBase = 125 * cantidad;
        int costoExtra = 0;

        // Sumar costo
        if (queso.isChecked()) costoExtra += 10;
        if (lechuga.isChecked()) costoExtra += 5;
        if (tomate.isChecked()) costoExtra += 5;
        if (pepinillos.isChecked()) costoExtra += 7;
        if (bacon.isChecked()) costoExtra += 15;
        if (cebolla.isChecked()) costoExtra += 5;

        return costoBase + costoExtra;
    }
}
