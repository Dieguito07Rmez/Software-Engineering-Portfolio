package com.example.ecowatt;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    EditText correo, password;
    Button login;
    TextView registro, recuperar;

    FirebaseAuth miAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        miAuth = FirebaseAuth.getInstance();

        correo = findViewById(R.id.edtCorreo);
        password = findViewById(R.id.edtPassword);
        login = findViewById(R.id.btnLogin);
        registro = findViewById(R.id.txtRegistro);
        recuperar = findViewById(R.id.txtRecuperar);

        login.setOnClickListener(v -> {

            String c = correo.getText().toString().trim();
            String p = password.getText().toString().trim();

            if (c.isEmpty())
            {
                correo.setError("Ingrese su correo");
                correo.requestFocus();
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(c).matches())
            {
                correo.setError("Correo no válido");
                correo.requestFocus();
                return;
            }

            if (p.isEmpty())
            {
                password.setError("Ingrese su contraseña");
                password.requestFocus();
                return;
            }

            iniciarSesion(c, p);

        });

        registro.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this, Registrate.class);
            startActivity(intent);
        });

        /*recuperar.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this, RecuperarPassword.class);
            startActivity(intent);
        });*/

    }

    private void iniciarSesion(String correoUsuario, String contraseña) {

        login.setEnabled(false);

        miAuth.signInWithEmailAndPassword(correoUsuario, contraseña)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        login.setEnabled(true);

                        if (task.isSuccessful()) {

                            Toast.makeText(Login.this, "Bienvenido", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(Login.this, MainActivity.class);
                            startActivity(intent);
                            finish();

                        } else {

                            Toast.makeText(Login.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();

                        }

                    }
                });
    }
}