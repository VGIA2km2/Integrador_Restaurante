package com.example.integrador_restaurante;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList; // Asegúrate de tener esta línea

public class ComidaActivity extends AppCompatActivity {
    // Lista estática para almacenar las órdenes
    public static ArrayList<String> ordenesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comida);

        ImageButton btnComida = findViewById(R.id.imgbtn_comida);

        btnComida.setOnClickListener(v -> {
            Log.d("ComidaActivity", "Botón comida presionado");

            // Agregar la orden a la lista
            String nuevaOrden = "Comida #" + (ordenesList.size() + 1);  // Generar un nombre único para cada orden
            ordenesList.add(nuevaOrden);  // Agregar la orden a la lista

            // Mostrar un mensaje de confirmación
            Toast.makeText(ComidaActivity.this, "Has seleccionado comida", Toast.LENGTH_SHORT).show();

            // Enviar la lista de órdenes a la siguiente actividad
            Intent intent = new Intent(ComidaActivity.this, OrdenesActivity.class);
            intent.putStringArrayListExtra("ordenes", ordenesList);  // Enviar la lista de órdenes
            startActivity(intent);
        });
    }
}
