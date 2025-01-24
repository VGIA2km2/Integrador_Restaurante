package com.example.integrador_restaurante;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class OrdenesActivity extends AppCompatActivity {

    private LinearLayout ordenesContainer;
    private ArrayList<String> ordenesList = new ArrayList<>();
    private Button btnSalir, btnVolverOrdenar;

    private int ordenId; // ID de la orden actual
    private static final String URL_ORDEN = "http://10.0.2.2/orden.php";
    private final OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordenes);

        // Referencias a las vistas
        ordenesContainer = findViewById(R.id.ordenesContainer);
        btnSalir = findViewById(R.id.btnSalir);
        btnVolverOrdenar = findViewById(R.id.btnVolverOrdenar);

        // Obtener datos de la Intent
        ordenesList = getIntent().getStringArrayListExtra("ordenes");
        ordenId = getIntent().getIntExtra("orden_id", -1);

        if (ordenId <= 0) {
            Toast.makeText(this, "Error: Orden no válida", Toast.LENGTH_SHORT).show();
            finish(); // Finaliza si no hay una orden válida
            return;
        }

        // Mostrar las órdenes en la interfaz
        if (ordenesList != null && !ordenesList.isEmpty()) {
            for (String orden : ordenesList) {
                addOrdenToView(orden);
            }
        } else {
            Toast.makeText(this, "No hay órdenes para mostrar", Toast.LENGTH_SHORT).show();
        }

        // Botón para salir
        btnSalir.setOnClickListener(v -> finish());

        // Botón para volver a ComidaActivity
        btnVolverOrdenar.setOnClickListener(v -> {
            Intent intent = new Intent(OrdenesActivity.this, ComidaActivity.class);
            intent.putStringArrayListExtra("ordenes", ordenesList); // Enviar las órdenes actualizadas
            startActivity(intent);
        });
    }

    private void addOrdenToView(String orden) {
        LinearLayout ordenLayout = new LinearLayout(this);
        ordenLayout.setOrientation(LinearLayout.VERTICAL);
        ordenLayout.setPadding(16, 16, 16, 16);

        TextView ordenText = new TextView(this);
        ordenText.setText(orden);
        ordenText.setTextColor(getResources().getColor(android.R.color.black));
        ordenText.setTextSize(18);

        // Botón para cancelar la orden
        Button cancelButton = new Button(this);
        cancelButton.setText("Cancelar");
        cancelButton.setTextColor(getResources().getColor(android.R.color.white));
        cancelButton.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));

        // Acción al hacer clic en "Cancelar"
        cancelButton.setOnClickListener(v -> cancelarOrden());

        ordenLayout.addView(ordenText);
        ordenLayout.addView(cancelButton);

        ordenesContainer.addView(ordenLayout);
    }

    // Método para cancelar la orden
    private void cancelarOrden() {
        JSONObject json = new JSONObject();
        try {
            json.put("action", "cancelar_orden");
            json.put("orden_id", ordenId);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        RequestBody body = RequestBody.create(
                json.toString(),
                MediaType.parse("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url(URL_ORDEN)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> {
                    Toast.makeText(OrdenesActivity.this,
                            "Error al cancelar la orden: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    runOnUiThread(() -> {
                        Toast.makeText(OrdenesActivity.this,
                                "Orden cancelada con éxito",
                                Toast.LENGTH_SHORT).show();
                        finish(); // Finaliza la actividad
                    });
                } else {
                    runOnUiThread(() -> {
                        Toast.makeText(OrdenesActivity.this,
                                "Error HTTP al cancelar la orden: " + response.code(),
                                Toast.LENGTH_SHORT).show();
                    });
                }
            }
        });
    }
}
