package com.example.integrador_restaurante;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
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

public class ComidaActivity extends AppCompatActivity {

    private Spinner spinnerCantidad;
    private ImageButton imgBtnComida;
    public static ArrayList<String> ordenesList = new ArrayList<>();

    private static final String URL_ORDEN = "http://10.0.2.2/orden.php";
    private static final int USUARIO_ID = 1;
    private final OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comida);

        spinnerCantidad = findViewById(R.id.spinner_direccion);
        imgBtnComida = findViewById(R.id.imgbtn_comida);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.direcciones_array,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCantidad.setAdapter(adapter);

        imgBtnComida.setOnClickListener(v -> {
            String cantidadSeleccionada = spinnerCantidad.getSelectedItem().toString();
            if (cantidadSeleccionada == null || cantidadSeleccionada.isEmpty()) {
                Toast.makeText(ComidaActivity.this, "Por favor selecciona una cantidad", Toast.LENGTH_SHORT).show();
                return;
            }

            int cantidad = Integer.parseInt(cantidadSeleccionada);
            crearOrdenEnBD(USUARIO_ID, cantidad);
        });
    }

    private void crearOrdenEnBD(int usuarioId, int cantidad) {
        JSONObject json = new JSONObject();
        try {
            json.put("action", "crear_orden");
            json.put("usuario_id", usuarioId);
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
                runOnUiThread(() -> {
                    Toast.makeText(ComidaActivity.this, "Error al crear orden: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        JSONObject resp = new JSONObject(response.body().string());
                        boolean success = resp.optBoolean("success", false);
                        if (success) {
                            int ordenId = resp.optInt("orden_id", -1);
                            agregarMenuEnBD(ordenId, cantidad);
                        } else {
                            String msg = resp.optString("message", "Fallo desconocido");
                            runOnUiThread(() -> Toast.makeText(ComidaActivity.this, msg, Toast.LENGTH_SHORT).show());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    runOnUiThread(() -> Toast.makeText(ComidaActivity.this, "Error HTTP al crear orden", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }

    private void agregarMenuEnBD(int ordenId, int cantidad) {
        int precioBase = 125;

        JSONObject json = new JSONObject();
        try {
            json.put("action", "agregar_menu");
            json.put("orden_id", ordenId);
            json.put("nombre_menu", "Hamburguesa");
            json.put("cantidad", cantidad);
            json.put("precio_base", precioBase);
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
                runOnUiThread(() -> {
                    Toast.makeText(ComidaActivity.this, "Error al agregar menú: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        JSONObject resp = new JSONObject(response.body().string());
                        boolean success = resp.optBoolean("success", false);
                        if (success) {
                            int menuId = resp.optInt("menu_id", -1);

                            String nuevaOrden = "Orden " + ordenId + " -> Menú Hamburguesa x " + cantidad;
                            ordenesList.add(nuevaOrden);

                            runOnUiThread(() -> {
                                Toast.makeText(ComidaActivity.this, "Menú agregado correctamente (orden " + ordenId + ")", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ComidaActivity.this, IngredientesActivity.class);
                                intent.putExtra("orden_id", ordenId);
                                intent.putExtra("menu_id", menuId);
                                startActivity(intent);
                            });

                        } else {
                            String msg = resp.optString("message", "Fallo desconocido al agregar menú");
                            runOnUiThread(() -> Toast.makeText(ComidaActivity.this, msg, Toast.LENGTH_SHORT).show());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    runOnUiThread(() -> Toast.makeText(ComidaActivity.this, "Error HTTP al agregar menú", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }
}
