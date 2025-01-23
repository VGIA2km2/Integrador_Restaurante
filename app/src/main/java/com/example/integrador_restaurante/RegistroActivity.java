package com.example.integrador_restaurante;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;        // <-- Asegúrate de que sea "okhttp3.Callback"
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegistroActivity extends AppCompatActivity {

    private EditText etNombre, etApellido, etCorreo, etTelefono, etContrasena;
    private Button btnContinuar;

    private final String URL_API = "http://10.0.2.2/registro.php";
    // O "http://10.0.2.2/subcarpeta/registro.php" si lo tienes dentro de una subcarpeta

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        etNombre = findViewById(R.id.etNombre);
        etApellido = findViewById(R.id.etApellido);
        etCorreo = findViewById(R.id.etCorreo);
        etTelefono = findViewById(R.id.etTelefono);
        etContrasena = findViewById(R.id.etContraseña);
        btnContinuar = findViewById(R.id.btnContinuar);

        // Listener del botón
        btnContinuar.setOnClickListener(v -> {
            if (validarCampos()) {
                realizarRegistro(
                        etNombre.getText().toString().trim(),
                        etApellido.getText().toString().trim(),
                        etCorreo.getText().toString().trim(),
                        etTelefono.getText().toString().trim(),
                        etContrasena.getText().toString().trim()
                );
            }
        });
    }

    private boolean validarCampos() {
        boolean valido = true;

        // Validar nombre
        if (TextUtils.isEmpty(etNombre.getText())) {
            etNombre.setError("Ingresa tu nombre");
            valido = false;
        }

        // Validar apellido
        if (TextUtils.isEmpty(etApellido.getText())) {
            etApellido.setError("Ingresa tu apellido");
            valido = false;
        }

        // Validar correo
        String correo = etCorreo.getText().toString();
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            etCorreo.setError("Correo inválido");
            valido = false;
        }

        // Validar teléfono
        String telefono = etTelefono.getText().toString();
        if (!telefono.matches("\\d{10}")) {
            etTelefono.setError("Debe tener 10 dígitos");
            valido = false;
        }

        // Validar contraseña
        if (TextUtils.isEmpty(etContrasena.getText())) {
            etContrasena.setError("Ingresa tu contraseña");
            valido = false;
        }

        return valido;
    }

    private void realizarRegistro(String nombre, String apellido, String correo, String telefono, String contrasena) {
        OkHttpClient client = new OkHttpClient();

        // Armar el JSON con los nombres que tu PHP espera
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("nombre", nombre);
            jsonBody.put("apellido", apellido);
            jsonBody.put("correo_electronico", correo);
            jsonBody.put("numero_telefonico", telefono);
            jsonBody.put("contrasena", contrasena);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(
                jsonBody.toString(),
                MediaType.parse("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url(URL_API)
                .post(body)
                .build();

        // Enqueue con "okhttp3.Callback"
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Error si no conecta con el servidor
                runOnUiThread(() -> {
                    String msg = "Error de conexión: " + e.getMessage();
                    Toast.makeText(RegistroActivity.this, msg, Toast.LENGTH_LONG).show();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // Manejar la respuesta HTTP
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response.body().string());
                        boolean success = jsonResponse.getBoolean("success");
                        String message = jsonResponse.getString("message");

                        runOnUiThread(() -> {
                            if (success) {
                                // Registro exitoso
                                Toast.makeText(RegistroActivity.this, message, Toast.LENGTH_SHORT).show();
                                // Por ejemplo, tras registrarse lo llevas al login
                                startActivity(new Intent(RegistroActivity.this, LoginActivity.class));
                                finish(); // opcional, para no volver a esta Activity con back
                            } else {
                                // Falló (correo ya registrado, etc.)
                                Toast.makeText(RegistroActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Error HTTP (404, 500, etc.)
                    runOnUiThread(() -> {
                        Toast.makeText(RegistroActivity.this,
                                "Error HTTP: " + response.code(),
                                Toast.LENGTH_LONG).show();
                    });
                }
            }
        });
    }
}
