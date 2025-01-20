package com.example.integrador_restaurante;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText etNombre, etApellido, etCorreo, etTelefono, etDireccion;
    private Button btnContinuar;

    // Lista simplemente enlazada para almacenar usuarios
    private ListaUsuarios listaUsuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etNombre = findViewById(R.id.etNombre);
        etApellido = findViewById(R.id.etApellido);
        etCorreo = findViewById(R.id.etCorreo);
        etTelefono = findViewById(R.id.etTelefono);
        btnContinuar = findViewById(R.id.btnContinuar);

        // Inicializar la lista simplemente enlazada
        listaUsuarios = new ListaUsuarios();

        btnContinuar.setOnClickListener(v -> {
            if (validarCampos()) {
                // Guardar los datos en la lista
                String nombre = etNombre.getText().toString();
                String apellido = etApellido.getText().toString();
                String correo = etCorreo.getText().toString();
                String telefono = etTelefono.getText().toString();

                Usuario nuevoUsuario = new Usuario(nombre, apellido, correo, telefono);
                listaUsuarios.agregarUsuario(nuevoUsuario);

                // Mostrar un mensaje de éxito
                Toast.makeText(this, "Usuario agregado correctamente", Toast.LENGTH_SHORT).show();

                // Navegar a ComidaActivity si la validación es exitosa
                Intent intent = new Intent(LoginActivity.this, ComidaActivity.class);
                startActivity(intent);
            }
        });
    }

    private boolean validarCampos() {
        boolean esValido = true;

        // Validar nombre
        String nombre = etNombre.getText().toString();
        if (TextUtils.isEmpty(nombre)) {
            etNombre.setError("Por favor, ingresa tu nombre");
            esValido = false;
        } else if (!nombre.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) {
            etNombre.setError("El nombre solo debe contener letras");
            esValido = false;
        }

        // Validar apellido
        String apellido = etApellido.getText().toString();
        if (TextUtils.isEmpty(apellido)) {
            etApellido.setError("Por favor, ingresa tu apellido");
            esValido = false;
        } else if (!apellido.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) {
            etApellido.setError("El apellido solo debe contener letras");
            esValido = false;
        }

        // Validar correo
        String correo = etCorreo.getText().toString();
        if (TextUtils.isEmpty(correo)) {
            etCorreo.setError("Por favor, ingresa tu correo electrónico");
            esValido = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            etCorreo.setError("Por favor, ingresa un correo válido");
            esValido = false;
        }

        // Validar teléfono
        String telefono = etTelefono.getText().toString();
        if (TextUtils.isEmpty(telefono)) {
            etTelefono.setError("Por favor, ingresa tu número de teléfono");
            esValido = false;
        } else if (!telefono.matches("[0-9]{10}")) {
            etTelefono.setError("El número de teléfono debe tener 10 dígitos");
            esValido = false;
        }

        // Mostrar mensaje de error
        if (!esValido) {
            Toast.makeText(this, "Por favor, corrige los errores antes de continuar", Toast.LENGTH_SHORT).show();
        }

        return esValido;
    }
}

class Usuario {
    String nombre;
    String apellido;
    String correo;
    String telefono;
    Usuario siguiente;

    public Usuario(String nombre, String apellido, String correo, String telefono) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.telefono = telefono;
        this.siguiente = null;
    }
}


class ListaUsuarios {
    private Usuario cabeza;

    public ListaUsuarios() {
        this.cabeza = null;
    }


    public void agregarUsuario(Usuario nuevoUsuario) {
        if (cabeza == null) {
            cabeza = nuevoUsuario;
        } else {
            Usuario actual = cabeza;
            while (actual.siguiente != null) {
                actual = actual.siguiente;
            }
            actual.siguiente = nuevoUsuario;
        }
    }
}
