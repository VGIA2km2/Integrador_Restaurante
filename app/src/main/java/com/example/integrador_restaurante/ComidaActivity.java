package com.example.integrador_restaurante;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ComidaActivity extends AppCompatActivity {

    private Spinner spinnerCantidad;
    private ImageButton imgBtnComida;
    public static ArrayList<String> ordenesList = new ArrayList<>(); // Lista estática

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comida);


        spinnerCantidad = findViewById(R.id.spinner_direccion); // Reutilizando spinner para cantidad
        imgBtnComida = findViewById(R.id.imgbtn_comida);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.direcciones_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCantidad.setAdapter(adapter);


        imgBtnComida.setOnClickListener(v -> {
            String cantidadSeleccionada = spinnerCantidad.getSelectedItem().toString();
            if (cantidadSeleccionada == null || cantidadSeleccionada.isEmpty()) {
                Toast.makeText(ComidaActivity.this, "Por favor selecciona una cantidad", Toast.LENGTH_SHORT).show();
                return;
            }

            // Agregar la orden a la lista
            String nuevaOrden = "Comida #" + (ordenesList.size() + 1) + " - Cantidad: " + cantidadSeleccionada;
            ordenesList.add(nuevaOrden);

            // Mostrar un mensaje de confirmación
            Toast.makeText(ComidaActivity.this, "Has seleccionado comida: Cantidad " + cantidadSeleccionada, Toast.LENGTH_SHORT).show();


            Intent intent = new Intent(ComidaActivity.this, IngredientesActivity.class);
            intent.putExtra("cantidad", Integer.parseInt(cantidadSeleccionada)); // Pasar la cantidad como entero
            intent.putStringArrayListExtra("ordenes", ordenesList);
            startActivity(intent);
        });
    }
}
