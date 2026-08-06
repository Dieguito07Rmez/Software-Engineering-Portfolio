package com.example.ecowatt;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Registrate extends AppCompatActivity {

    // Firebase
    private FirebaseAuth mAuth;

    private EditText nombre, correoo, pass,confirmarr;
    private Button registrar;
    private TextView volver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrate);

        mAuth = FirebaseAuth.getInstance();

        nombre = findViewById(R.id.edtNombre);
        correoo = findViewById(R.id.edtCorreoRegistro);
        pass = findViewById(R.id.edtPasswordRegistro);
        confirmarr = findViewById(R.id.edtConfirmarPassword);

        registrar = findViewById(R.id.btnRegistrar);
        volver = findViewById(R.id.txtVolverLogin);

        volver.setOnClickListener(v -> {
            startActivity(new Intent(Registrate.this, Login.class));
            finish();
        });

        registrar.setOnClickListener(v -> registrarUsuario());
    }

    private void registrarUsuario() {

        String usuario = nombre.getText().toString().trim();
        String correo = correoo.getText().toString().trim();
        String password = pass.getText().toString().trim();
        String confirmar = confirmarr.getText().toString().trim();

        if (usuario.isEmpty()) {
            nombre.setError("Ingrese su nombre");
            nombre.requestFocus();
            return;
        }

        if (correo.isEmpty()) {
            correoo.setError("Ingrese un correo");
            correoo.requestFocus();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            correoo.setError("Correo no válido");
            correoo.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            pass.setError("Ingrese una contraseña");
            pass.requestFocus();
            return;
        }

        if (password.length() < 6) {
            pass.setError("La contraseña debe tener al menos 6 caracteres");
            pass.requestFocus();
            return;
        }

        if (confirmar.isEmpty()) {
            confirmarr.setError("Confirme la contraseña");
            confirmarr.requestFocus();
            return;
        }

        if (!password.equals(confirmar)) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(correo, password)
                .addOnCompleteListener(this, task -> {

                    if (task.isSuccessful()) {

                        FirebaseUser user = mAuth.getCurrentUser();

                        if (user != null) {

                            DatabaseReference database = FirebaseDatabase.getInstance()
                                    .getReference("usuarios")
                                    .child(user.getUid());

                            HashMap<String, Object> datos = new HashMap<>();
                            datos.put("nombre", usuario);
                            datos.put("correo", correo);

                            database.setValue(datos).addOnCompleteListener(task1 -> {

                                if (task1.isSuccessful()) {

                                    Toast.makeText(Registrate.this, "Registro exitoso", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(Registrate.this, Login.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else
                                {
                                    Toast.makeText(Registrate.this, "No se pudieron guardar los datos", Toast.LENGTH_SHORT).show();
                                }

                            });

                        }

                    }
                    else
                    {

                        Exception exception = task.getException();

                        if (exception instanceof com.google.firebase.auth.FirebaseAuthUserCollisionException) {

                            Toast.makeText(Registrate.this,
                                    "Este correo ya está registrado.",
                                    Toast.LENGTH_LONG).show();

                        } else if (exception instanceof com.google.firebase.auth.FirebaseAuthWeakPasswordException) {

                            Toast.makeText(Registrate.this,
                                    "La contraseña debe tener al menos 6 caracteres.",
                                    Toast.LENGTH_LONG).show();

                        } else if (exception instanceof com.google.firebase.auth.FirebaseAuthInvalidCredentialsException) {

                            Toast.makeText(Registrate.this,
                                    "El correo electrónico no es válido.",
                                    Toast.LENGTH_LONG).show();

                        } else {

                            Toast.makeText(Registrate.this,
                                    exception != null ? exception.getMessage() : "Error al registrar.",
                                    Toast.LENGTH_LONG).show();

                        }

                    }

                });

    }

}