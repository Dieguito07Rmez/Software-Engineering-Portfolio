package com.example.ecowatt;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Electrodomesticos
        extends Fragment {


    // ==========================================
    // CONTROLES
    // ==========================================

    private Spinner spElectrodomestico;
    private Spinner spCantidad;

    private EditText edtMarca;
    private EditText edtVoltaje;
    private EditText edtPotencia;
    private EditText edtHoras;

    private Button btnGuardar;

    private RecyclerView rvElectrodomesticos;


    // ==========================================
    // FIREBASE
    // ==========================================

    private FirebaseAuth auth;

    private DatabaseReference referenciaElectrodomesticos;


    // ==========================================
    // LISTA Y ADAPTER
    // ==========================================

    private List<Electrodomestico>
            listaElectrodomesticos;

    private ElectrodomesticoAdapter
            adapter;


    // ==========================================
    // CREAR VISTA
    // ==========================================

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {


        View view =
                inflater.inflate(
                        R.layout.fragment_electrodomesticos,
                        container,
                        false
                );


        // ==========================================
        // REFERENCIAS XML
        // ==========================================

        spElectrodomestico =
                view.findViewById(
                        R.id.spElectrodomestico
                );


        spCantidad =
                view.findViewById(
                        R.id.spCantidad
                );


        edtMarca =
                view.findViewById(
                        R.id.edtMarca
                );


        edtVoltaje =
                view.findViewById(
                        R.id.edtVoltaje
                );


        edtPotencia =
                view.findViewById(
                        R.id.edtPotencia
                );


        edtHoras =
                view.findViewById(
                        R.id.edtHoras
                );


        btnGuardar =
                view.findViewById(
                        R.id.btnGuardar
                );


        rvElectrodomesticos =
                view.findViewById(
                        R.id.rvElectrodomesticos
                );


        // ==========================================
        // CONFIGURAR SPINNERS
        // ==========================================

        configurarSpinnerElectrodomesticos();

        configurarSpinnerCantidad();


        // ==========================================
        // CONFIGURAR RECYCLERVIEW
        // ==========================================

        listaElectrodomesticos =
                new ArrayList<>();


        adapter =
                new ElectrodomesticoAdapter(
                        listaElectrodomesticos
                );


        rvElectrodomesticos.setLayoutManager(
                new LinearLayoutManager(
                        requireContext()
                )
        );


        rvElectrodomesticos.setAdapter(
                adapter
        );


        // ==========================================
        // FIREBASE AUTH
        // ==========================================

        auth =
                FirebaseAuth.getInstance();


        // ==========================================
        // VERIFICAR USUARIO
        // ==========================================

        if (
                auth.getCurrentUser()
                        == null
        ) {


            Toast.makeText(
                    requireContext(),
                    "No hay un usuario autenticado",
                    Toast.LENGTH_LONG
            ).show();


        } else {


            String uidUsuario =
                    auth.getCurrentUser()
                            .getUid();


            referenciaElectrodomesticos =
                    FirebaseDatabase
                            .getInstance()
                            .getReference(
                                    "usuarios"
                            )
                            .child(
                                    uidUsuario
                            )
                            .child(
                                    "electrodomesticos"
                            );


            cargarElectrodomesticos();
        }


        // ==========================================
        // BOTÓN GUARDAR
        // ==========================================

        btnGuardar.setOnClickListener(
                v -> guardarElectrodomestico()
        );


        return view;
    }


    // ==========================================
    // SPINNER ELECTRODOMÉSTICOS
    // ==========================================

    private void configurarSpinnerElectrodomesticos() {


        String[] electrodomesticos = {

                "Selecciona un electrodoméstico",
                "Refrigerador",
                "Televisión",
                "Lavadora",
                "Secadora",
                "Microondas",
                "Computadora",
                "Laptop",
                "Ventilador",
                "Aire acondicionado",
                "Licuadora",
                "Horno eléctrico",
                "Plancha",
                "Otro"
        };


        ArrayAdapter<String> adapterTipos =
                new ArrayAdapter<>(
                        requireContext(),
                        android.R.layout.simple_spinner_item,
                        electrodomesticos
                );


        adapterTipos.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );


        spElectrodomestico.setAdapter(
                adapterTipos
        );
    }


    // ==========================================
    // SPINNER CANTIDAD
    // ==========================================

    private void configurarSpinnerCantidad() {


        String[] cantidades = {

                "1",
                "2",
                "3",
                "4",
                "5",
                "6",
                "7",
                "8",
                "9",
                "10"
        };


        ArrayAdapter<String> adapterCantidad =
                new ArrayAdapter<>(
                        requireContext(),
                        android.R.layout.simple_spinner_item,
                        cantidades
                );


        adapterCantidad.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );


        spCantidad.setAdapter(
                adapterCantidad
        );
    }


    // ==========================================
    // GUARDAR ELECTRODOMÉSTICO
    // ==========================================

    private void guardarElectrodomestico() {


        // ==========================================
        // VALIDAR USUARIO
        // ==========================================

        if (
                auth.getCurrentUser()
                        == null
        ) {


            Toast.makeText(
                    requireContext(),
                    "No hay un usuario autenticado",
                    Toast.LENGTH_LONG
            ).show();


            return;
        }


        // OBTENER DATOS
        String tipo =
                spElectrodomestico
                        .getSelectedItem()
                        .toString();


        String marca =
                edtMarca
                        .getText()
                        .toString()
                        .trim();


        String voltaje =
                edtVoltaje
                        .getText()
                        .toString()
                        .trim();


        String potencia =
                edtPotencia
                        .getText()
                        .toString()
                        .trim();


        String cantidad =
                spCantidad
                        .getSelectedItem()
                        .toString();


        String horasUso =
                edtHoras
                        .getText()
                        .toString()
                        .trim();


        // ==========================================
        // VALIDACIONES
        // ==========================================

        if (
                tipo.equals(
                        "Selecciona un electrodoméstico"
                )
        ) {


            Toast.makeText(
                    requireContext(),
                    "Selecciona un electrodoméstico",
                    Toast.LENGTH_LONG
            ).show();


            return;
        }


        if (
                TextUtils.isEmpty(
                        marca
                )
        ) {


            edtMarca.setError(
                    "Ingresa la marca"
            );


            edtMarca.requestFocus();


            return;
        }


        if (
                TextUtils.isEmpty(
                        voltaje
                )
        ) {


            edtVoltaje.setError(
                    "Ingresa el voltaje"
            );


            edtVoltaje.requestFocus();


            return;
        }


        if (
                TextUtils.isEmpty(
                        potencia
                )
        ) {


            edtPotencia.setError(
                    "Ingresa la potencia"
            );


            edtPotencia.requestFocus();


            return;
        }


        if (
                TextUtils.isEmpty(
                        horasUso
                )
        ) {


            edtHoras.setError(
                    "Ingresa las horas de uso"
            );


            edtHoras.requestFocus();


            return;
        }


        // ==========================================
        // CREAR OBJETO
        // ==========================================

        Electrodomestico electrodomestico =
                new Electrodomestico(
                        tipo,
                        marca,
                        voltaje,
                        potencia,
                        cantidad,
                        horasUso
                );


        // ==========================================
        // GENERAR ID
        // ==========================================

        String idElectrodomestico =
                referenciaElectrodomesticos
                        .push()
                        .getKey();


        if (
                idElectrodomestico
                        == null
        ) {


            Toast.makeText(
                    requireContext(),
                    "No se pudo generar el registro",
                    Toast.LENGTH_LONG
            ).show();


            return;
        }


        // ==========================================
        // GUARDAR EN REALTIME DATABASE
        // ==========================================

        referenciaElectrodomesticos
                .child(
                        idElectrodomestico
                )
                .setValue(
                        electrodomestico
                )
                .addOnSuccessListener(
                        unused -> {


                            Toast.makeText(
                                    requireContext(),
                                    "Electrodoméstico guardado correctamente",
                                    Toast.LENGTH_LONG
                            ).show();


                            limpiarFormulario();
                        }
                )
                .addOnFailureListener(
                        e -> {


                            Toast.makeText(
                                    requireContext(),
                                    "Error al guardar: "
                                            + e.getMessage(),
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                );
    }


    // ==========================================
    // CARGAR ELECTRODOMÉSTICOS
    // ==========================================

    private void cargarElectrodomesticos() {


        referenciaElectrodomesticos
                .addValueEventListener(
                        new ValueEventListener() {


                            @Override
                            public void onDataChange(
                                    @NonNull DataSnapshot snapshot
                            ) {


                                listaElectrodomesticos
                                        .clear();


                                for (
                                        DataSnapshot dataSnapshot
                                        : snapshot.getChildren()
                                ) {


                                    Electrodomestico electrodomestico =
                                            dataSnapshot.getValue(
                                                    Electrodomestico.class
                                            );


                                    if (
                                            electrodomestico
                                                    != null
                                    ) {


                                        listaElectrodomesticos
                                                .add(
                                                        electrodomestico
                                                );
                                    }
                                }


                                adapter.notifyDataSetChanged();
                            }


                            @Override
                            public void onCancelled(
                                    @NonNull DatabaseError error
                            ) {


                                Toast.makeText(
                                        requireContext(),
                                        "Error al cargar los electrodomésticos",
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        }
                );
    }


    // ==========================================
    // LIMPIAR FORMULARIO
    // ==========================================

    private void limpiarFormulario() {


        spElectrodomestico
                .setSelection(
                        0
                );


        edtMarca
                .setText(
                        ""
                );


        edtVoltaje
                .setText(
                        ""
                );


        edtPotencia
                .setText(
                        ""
                );


        spCantidad
                .setSelection(
                        0
                );


        edtHoras
                .setText(
                        ""
                );
    }
}