package com.example.login_clase_30_junio;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegistroActivity extends AppCompatActivity {

    EditText usuario, correo, contrasena, confirmar;
    Button registrar;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro);
        usuario = findViewById(R.id.txtusuario);
        correo = findViewById(R.id.txtcorreo);
        contrasena = findViewById(R.id.txtcontraseña);
        confirmar = findViewById(R.id.txtconfirmar);
        registrar = findViewById(R.id.btnregistrardos);

        mAuth = FirebaseAuth.getInstance();

        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String u = usuario.getText().toString().trim();
                String c = correo.getText().toString().trim();
                String p = contrasena.getText().toString().trim();
                String s = confirmar.getText().toString().trim();

                if(u.isEmpty() || c.isEmpty() || p.isEmpty() || s.isEmpty()){
                    Toast.makeText(RegistroActivity.this, "No dejes este campo vacio ", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!p.equals(s)){
                    Toast.makeText(RegistroActivity.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(c, p).
                        addOnCompleteListener(task -> {
                            if(task.isSuccessful()){
                                FirebaseUser user = mAuth.getCurrentUser();
                                if (user != null) {
                                    DatabaseReference database = FirebaseDatabase.getInstance()
                                            .getReference("usuarios")
                                            .child(user.getUid());

                                    HashMap<String,Object> datos = new HashMap<>();
                                    datos.put("nombre", u);
                                    datos.put("correo", c);
                                    database.setValue(datos);

                                    Toast.makeText(RegistroActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(RegistroActivity.this, "Error al registrar: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}