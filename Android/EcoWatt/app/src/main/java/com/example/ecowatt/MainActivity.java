package com.example.ecowatt;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    MaterialToolbar toolbar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        // Fragmento inicial
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.contenedor, new Inicio())
                .commit();

        // Abrir menú lateral
        toolbar.setNavigationOnClickListener(view -> {
            drawerLayout.openDrawer(GravityCompat.START);
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                Fragment fragmentSeleccionado = null;

                int id = menuItem.getItemId();

                if (id == R.id.nav_inicio)
                {

                    fragmentSeleccionado = new Inicio();

                }
                else if (id == R.id.nav_consumo_luz)
                {

                    fragmentSeleccionado = new ConsumoLuz();

                }
                else if (id == R.id.nav_electrodomesticos)
                {

                    fragmentSeleccionado = new Electrodomesticos();

                }
                else if (id == R.id.nav_analisis)
                {

                    fragmentSeleccionado = new AnalisisConsumo();

                }
                else if (id == R.id.nav_recomendaciones)
                {

                    fragmentSeleccionado = new Recomendaciones();

                }
                else if (id == R.id.nav_perfil)
                {

                    fragmentSeleccionado = new Perfil();

                }
                else if (id == R.id.nav_cerrar_sesion) {

                    // Cerrar sesión de Firebase
                    FirebaseAuth.getInstance().signOut();

                    // Ir a la pantalla de Login
                    Intent intent = new Intent(
                            MainActivity.this,
                            Login.class
                    );

                    // Evitar que el usuario regrese al menú con el botón atrás
                    intent.setFlags(
                            Intent.FLAG_ACTIVITY_NEW_TASK
                                    | Intent.FLAG_ACTIVITY_CLEAR_TASK
                    );

                    startActivity(intent);

                    return true;
                }

                if (fragmentSeleccionado != null) {

                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.contenedor, fragmentSeleccionado)
                            .commit();
                }

                drawerLayout.closeDrawers();

                return true;
            }
        });
    }
}