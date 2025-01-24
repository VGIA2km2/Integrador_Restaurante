package com.example.integrador_restaurante;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
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

public class ResumenActivity extends AppCompatActivity {

    private TextView txtResumen;
    private Button btnCancelar, btnConfirmar;
    private int ordenId;

    private static final String URL_ORDEN = "http://10.0.2.2/orden.php";
    private final OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resumen);

        txtResumen = findViewById(R.id.txtResumen);
        btnCancelar = findViewById(R.id.btnCancelar);
        btnConfirmar = findViewById(R.id.btnConfirmar);

        ordenId = getIntent().getIntExtra("orden_id", -1);

        if (ordenId == -1) {
            Toast.makeText(this, "Error: Orden no válida", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        btnCancelar.setOnClickListener(v -> cancelarOrden());
        btnConfirmar.setOnClickListener(v -> confirmarOrden());
    }

    private void cancelarOrden() {
        JSONObject json = new JSONObject();
        try {
            json.put("action", "cancelar_orden");
            json.put("orden_id", ordenId);
        } catch (JSONException e) {
            e.printStackTrace();
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
                runOnUiThread(() -> Toast.makeText(ResumenActivity.this, "Error al cancelar orden", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    runOnUiThread(() -> {
                        Toast.makeText(ResumenActivity.this, "Orden cancelada", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ResumenActivity.this, ComidaActivity.class);
                        startActivity(intent);
                        finish();
                    });
                } else {
                    runOnUiThread(() -> Toast.makeText(ResumenActivity.this, "Error al cancelar", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }

    private void confirmarOrden() {
        JSONObject json = new JSONObject();
        try {
            json.put("action", "confirmar_orden");
            json.put("orden_id", ordenId);
        } catch (JSONException e) {
            e.printStackTrace();
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
                runOnUiThread(() -> Toast.makeText(ResumenActivity.this, "Error al confirmar orden: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    runOnUiThread(() -> {
                        Toast.makeText(ResumenActivity.this, "Orden confirmada correctamente", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ResumenActivity.this, ComidaActivity.class);
                        startActivity(intent);
                        finish();
                    });
                } else {
                    runOnUiThread(() -> {
                        Toast.makeText(ResumenActivity.this, "Error al confirmar orden. Código: " + response.code(), Toast.LENGTH_SHORT).show();
                    });
                }
            }
        });
    }

}
