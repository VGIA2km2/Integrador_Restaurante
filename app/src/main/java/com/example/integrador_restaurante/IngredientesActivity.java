package com.example.integrador_restaurante;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class IngredientesActivity extends AppCompatActivity {

    private CheckBox queso, lechuga, tomate, pepinillos, bacon, cebolla;
    private Button btnCosto, btnContinuar;
    private int ordenId, menuId;

    private static final String URL_ORDEN = "http://10.0.2.2/orden.php";
    private final OkHttpClient client = new OkHttpClient();

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

        ordenId = getIntent().getIntExtra("orden_id", -1);
        menuId = getIntent().getIntExtra("menu_id", -1);

        if (ordenId == -1 || menuId == -1) {
            Toast.makeText(this, "Error: Datos faltantes", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        btnCosto.setOnClickListener(v -> {
            int costoTotal = calcularCosto();
            btnCosto.setText("Costo: $" + costoTotal);
        });

        btnContinuar.setOnClickListener(v -> {
            enviarIngredientesAlServidor();
        });
    }

    private int calcularCosto() {
        int costoExtra = 0;

        if (queso.isChecked()) costoExtra += 10;
        if (lechuga.isChecked()) costoExtra += 5;
        if (tomate.isChecked()) costoExtra += 5;
        if (pepinillos.isChecked()) costoExtra += 7;
        if (bacon.isChecked()) costoExtra += 15;
        if (cebolla.isChecked()) costoExtra += 5;

        return costoExtra;
    }

    private void enviarIngredientesAlServidor() {
        JSONObject json = new JSONObject();
        try {
            json.put("action", "agregar_ingrediente");
            json.put("menu_id", menuId);

            JSONArray ingredientesArray = new JSONArray();
            if (queso.isChecked()) ingredientesArray.put(new JSONObject().put("nombre", "Queso").put("precio_extra", 10));
            if (lechuga.isChecked()) ingredientesArray.put(new JSONObject().put("nombre", "Lechuga").put("precio_extra", 5));
            if (tomate.isChecked()) ingredientesArray.put(new JSONObject().put("nombre", "Tomate").put("precio_extra", 5));
            if (pepinillos.isChecked()) ingredientesArray.put(new JSONObject().put("nombre", "Pepinillos").put("precio_extra", 7));
            if (bacon.isChecked()) ingredientesArray.put(new JSONObject().put("nombre", "Bacon").put("precio_extra", 15));
            if (cebolla.isChecked()) ingredientesArray.put(new JSONObject().put("nombre", "Cebolla").put("precio_extra", 5));

            json.put("ingredientes", ingredientesArray);
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
                runOnUiThread(() -> Toast.makeText(IngredientesActivity.this, "Error al guardar ingredientes: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    runOnUiThread(() -> {
                        Toast.makeText(IngredientesActivity.this, "Ingredientes guardados correctamente", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(IngredientesActivity.this, ResumenActivity.class);
                        intent.putExtra("orden_id", ordenId);
                        startActivity(intent);
                    });
                } else {
                    runOnUiThread(() -> Toast.makeText(IngredientesActivity.this, "Error al guardar ingredientes", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }
}
