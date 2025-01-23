package com.example.integrador_restaurante;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;

public class LoginActivity extends AppCompatActivity {

    private EditText etNombre, etApellido, etCorreo, etTelefono, etContrasena;
    private Button btnContinuar;
    private TextView hypertext;
    private final String URL_API = "http://10.0.2.2/login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 1. Enlazar vistas
        etNombre = findViewById(R.id.etNombre);
        etApellido = findViewById(R.id.etApellido);
        etCorreo = findViewById(R.id.etCorreo);
        etTelefono = findViewById(R.id.etTelefono);
        etContrasena = findViewById(R.id.etContraseña);
        btnContinuar = findViewById(R.id.btnContinuar);
        hypertext = findViewById(R.id.hypertext);

        // 2. Configurar hipervínculo
        hypertext.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, PasswordActivity.class);
            startActivity(intent);
        });

        // 3. Botón de login
        btnContinuar.setOnClickListener(v -> {
            if (validarCampos()) {
                realizarLogin(
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

    private void realizarLogin(String nombre, String apellido, String correo, String telefono, String contrasena) {
        OkHttpClient client = new OkHttpClient();

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

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> {
                    String msg = "Error de conexión: " + e.getMessage();
                    Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_LONG).show();
                });
            }


            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response.body().string());
                        boolean success = jsonResponse.getBoolean("success");

                        runOnUiThread(() -> {
                            if (success) {
                                Toast.makeText(LoginActivity.this, "¡Bienvenido!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this, ComidaActivity.class));
                            } else {
                                Toast.makeText(LoginActivity.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}