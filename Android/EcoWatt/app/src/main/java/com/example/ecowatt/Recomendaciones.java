package com.example.ecowatt;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Recomendaciones extends Fragment {


    // =====================================================
    // TEXTVIEWS
    // =====================================================

    private TextView tvAlerta;
    private TextView tvConsumoReal;
    private TextView tvConsumoEstimado;

    private TextView tvTip1;
    private TextView tvTip2;
    private TextView tvTip3;


    // =====================================================
    // FIREBASE
    // =====================================================

    private DatabaseReference referenciaElectrodomesticos;

    private FirebaseAuth auth;


    // =====================================================
    // VARIABLES DE CONSUMO
    // =====================================================

    private double consumoEstimado = 0;

    private double consumoReal = 0;


    // =====================================================
    // LISTA
    // =====================================================

    private List<Electrodomestico> listaElectrodomesticos;


    // =====================================================
    // CREAR VISTA
    // =====================================================

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {

        View view =
                inflater.inflate(
                        R.layout.fragment_analisis_consumo,
                        container,
                        false
                );


        // =================================================
        // REFERENCIAS XML
        // =================================================

        tvAlerta =
                view.findViewById(
                        R.id.tvAlerta
                );


        tvConsumoReal =
                view.findViewById(
                        R.id.tvConsumoReal
                );


        tvConsumoEstimado =
                view.findViewById(
                        R.id.tvConsumoEstimado
                );


        tvTip1 =
                view.findViewById(
                        R.id.tvTip1
                );


        tvTip2 =
                view.findViewById(
                        R.id.tvTip2
                );


        tvTip3 =
                view.findViewById(
                        R.id.tvTip3
                );


        // =================================================
        // INICIALIZAR FIREBASE
        // =================================================

        auth =
                FirebaseAuth.getInstance();


        listaElectrodomesticos =
                new ArrayList<>();


        // =================================================
        // CARGAR ELECTRODOMÉSTICOS
        // =================================================

        cargarElectrodomesticos();


        return view;
    }


    // =====================================================
    // CARGAR ELECTRODOMÉSTICOS
    // =====================================================

    private void cargarElectrodomesticos() {


        // ================================================
        // VERIFICAR USUARIO
        // ================================================

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


        // ================================================
        // OBTENER UID
        // ================================================

        String uidUsuario =
                auth.getCurrentUser()
                        .getUid();


        // ================================================
        // REFERENCIA A REALTIME DATABASE
        // ================================================

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


        // ================================================
        // LEER DATOS
        // ================================================

        referenciaElectrodomesticos
                .addListenerForSingleValueEvent(
                        new ValueEventListener() {

                            @Override
                            public void onDataChange(
                                    @NonNull DataSnapshot snapshot
                            ) {


                                consumoEstimado =
                                        0;


                                listaElectrodomesticos
                                        .clear();


                                // ========================================
                                // RECORRER ELECTRODOMÉSTICOS
                                // ========================================

                                for (
                                        DataSnapshot dataSnapshot
                                        :
                                        snapshot.getChildren()
                                ) {


                                    Electrodomestico
                                            electrodomestico =
                                            dataSnapshot
                                                    .getValue(
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


                                        consumoEstimado +=
                                                calcularConsumoMensual(
                                                        electrodomestico
                                                );
                                    }
                                }


                                // ========================================
                                // MOSTRAR CONSUMO ESTIMADO
                                // ========================================

                                tvConsumoEstimado
                                        .setText(
                                                String.format(
                                                        Locale.getDefault(),
                                                        "%.2f kWh",
                                                        consumoEstimado
                                                )
                                        );


                                // ========================================
                                // GENERAR RECOMENDACIONES
                                // ========================================

                                generarRecomendaciones();


                                // ========================================
                                // COMPARAR CONSUMO
                                // ========================================

                                compararConsumos();
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


    // =====================================================
    // CALCULAR CONSUMO MENSUAL
    // =====================================================

    private double calcularConsumoMensual(
            Electrodomestico electrodomestico
    ) {


        try {


            // ============================================
            // CONVERTIR POTENCIA
            // ============================================

            double potencia =
                    Double.parseDouble(
                            electrodomestico
                                    .getPotencia()
                                    .replace(
                                            ",",
                                            "."
                                    )
                                    .trim()
                    );


            // ============================================
            // CONVERTIR CANTIDAD
            // ============================================

            int cantidad =
                    Integer.parseInt(
                            electrodomestico
                                    .getCantidad()
                                    .trim()
                    );


            // ============================================
            // CONVERTIR HORAS
            // ============================================

            double horasUso =
                    Double.parseDouble(
                            electrodomestico
                                    .getHorasUso()
                                    .replace(
                                            ",",
                                            "."
                                    )
                                    .trim()
                    );


            // ============================================
            // FÓRMULA
            // ============================================

            return (
                    potencia
                            *
                            cantidad
                            *
                            horasUso
                            *
                            30
            )
                    /
                    1000;


        } catch (
                Exception e
        ) {


            return 0;
        }
    }


    // =====================================================
    // GENERAR RECOMENDACIONES
    // =====================================================

    private void generarRecomendaciones() {


        if (
                listaElectrodomesticos
                        .isEmpty()
        ) {

            tvTip1.setText(
                    "Registra tus electrodomésticos para obtener recomendaciones."
            );


            tvTip2.setText(
                    ""
            );


            tvTip3.setText(
                    ""
            );


            return;
        }


        // ================================================
        // OBTENER MAYORES CONSUMIDORES
        // ================================================

        Electrodomestico mayor1 =
                null;


        Electrodomestico mayor2 =
                null;


        Electrodomestico mayor3 =
                null;


        double consumoMayor1 =
                0;


        double consumoMayor2 =
                0;


        double consumoMayor3 =
                0;


        for (
                Electrodomestico electrodomestico
                :
                listaElectrodomesticos
        ) {


            double consumo =
                    calcularConsumoMensual(
                            electrodomestico
                    );


            if (
                    consumo
                            >
                            consumoMayor1
            ) {


                consumoMayor3 =
                        consumoMayor2;


                mayor3 =
                        mayor2;


                consumoMayor2 =
                        consumoMayor1;


                mayor2 =
                        mayor1;


                consumoMayor1 =
                        consumo;


                mayor1 =
                        electrodomestico;


            } else if (
                    consumo
                            >
                            consumoMayor2
            ) {


                consumoMayor3 =
                        consumoMayor2;


                mayor3 =
                        mayor2;


                consumoMayor2 =
                        consumo;


                mayor2 =
                        electrodomestico;


            } else if (
                    consumo
                            >
                            consumoMayor3
            ) {


                consumoMayor3 =
                        consumo;


                mayor3 =
                        electrodomestico;
            }
        }


        // ================================================
        // MOSTRAR RECOMENDACIÓN 1
        // ================================================

        if (
                mayor1
                        !=
                        null
        ) {

            tvTip1.setText(
                    generarConsejo(
                            mayor1
                    )
            );
        }


        // ================================================
        // MOSTRAR RECOMENDACIÓN 2
        // ================================================

        if (
                mayor2
                        !=
                        null
        ) {

            tvTip2.setText(
                    generarConsejo(
                            mayor2
                    )
            );
        }


        // ================================================
        // MOSTRAR RECOMENDACIÓN 3
        // ================================================

        if (
                mayor3
                        !=
                        null
        ) {

            tvTip3.setText(
                    generarConsejo(
                            mayor3
                    )
            );
        }
    }


    // =====================================================
    // GENERAR CONSEJO
    // =====================================================

    private String generarConsejo(
            Electrodomestico electrodomestico
    ) {


        String tipo =
                electrodomestico
                        .getTipo()
                        .toLowerCase(
                                Locale.ROOT
                        );


        if (
                tipo.contains(
                        "refrigerador"
                )
                        ||
                        tipo.contains(
                                "refrigerador"
                        )
        ) {

            return "Revisa el sello de la puerta y evita abrirlo muy seguido.";
        }


        if (
                tipo.contains(
                        "aire"
                )
        ) {

            return "Usa una temperatura moderada y mantén limpios los filtros.";
        }


        if (
                tipo.contains(
                        "television"
                )
                        ||
                        tipo.contains(
                                "televisión"
                        )
        ) {

            return "Apaga completamente el televisor cuando no lo estés utilizando.";
        }


        if (
                tipo.contains(
                        "lavadora"
                )
        ) {

            return "Utiliza cargas completas para reducir el consumo de energía.";
        }


        if (
                tipo.contains(
                        "computadora"
                )
                        ||
                        tipo.contains(
                                "laptop"
                        )
        ) {

            return "Activa el modo de ahorro de energía cuando no estés utilizando el equipo.";
        }


        if (
                tipo.contains(
                        "foco"
                )
                        ||
                        tipo.contains(
                                "lampara"
                        )
                        ||
                        tipo.contains(
                                "lámpara"
                        )
        ) {

            return "Utiliza focos LED y apaga las luces cuando no sean necesarias.";
        }


        return "Reduce las horas de uso y desconecta el aparato cuando no lo estés utilizando.";
    }


    // =====================================================
    // COMPARAR CONSUMOS
    // =====================================================

    private void compararConsumos() {


        /*
         * Por el momento el consumo real debe cargarse
         * desde la información de los recibos guardados.
         *
         * Aquí se utiliza el consumo estimado como referencia
         * hasta conectar la lectura del recibo real.
         */


        consumoReal =
                consumoEstimado;


        tvConsumoReal
                .setText(
                        String.format(
                                Locale.getDefault(),
                                "%.2f kWh",
                                consumoReal
                        )
                );


        tvAlerta.setText(
                "Tu consumo real coincide con el consumo estimado. Continúa monitoreando tus electrodomésticos."
        );
    }
}