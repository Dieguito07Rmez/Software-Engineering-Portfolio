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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class AnalisisConsumo extends Fragment {


    // =====================================
    // TEXTVIEWS
    // =====================================

    private TextView txtConsumoActualAnalisis;
    private TextView txtPeriodoActualAnalisis;

    private TextView txtConsumoAnteriorAnalisis;
    private TextView txtPeriodoAnteriorAnalisis;

    private TextView txtComparacionConsumo;
    private TextView txtPorcentajeCambio;

    private TextView txtEstadoAnalisis;

    private TextView txtPagoActualAnalisis;
    private TextView txtPagoAnteriorAnalisis;
    private TextView txtTarifaAnalisis;


    // =====================================
    // FIREBASE
    // =====================================

    private FirebaseAuth auth;

    private DatabaseReference usuariosRef;


    // =====================================
    // CREAR VISTA
    // =====================================

    @Nullable
    @Override
    public View onCreateView(

            @NonNull LayoutInflater inflater,

            @Nullable ViewGroup container,

            @Nullable Bundle savedInstanceState

    ) {


        View view = inflater.inflate(

                R.layout.fragment_analisis_consumo,

                container,

                false

        );


        // =====================================
        // INICIALIZAR FIREBASE
        // =====================================

        auth = FirebaseAuth.getInstance();


        usuariosRef = FirebaseDatabase.getInstance()

                .getReference("usuarios");


        // =====================================
        // REFERENCIAS XML
        // =====================================

        txtConsumoActualAnalisis = view.findViewById(

                R.id.txtConsumoActualAnalisis

        );


        txtPeriodoActualAnalisis = view.findViewById(

                R.id.txtPeriodoActualAnalisis

        );


        txtConsumoAnteriorAnalisis = view.findViewById(

                R.id.txtConsumoAnteriorAnalisis

        );


        txtPeriodoAnteriorAnalisis = view.findViewById(

                R.id.txtPeriodoAnteriorAnalisis

        );


        txtComparacionConsumo = view.findViewById(

                R.id.txtComparacionConsumo

        );


        txtPorcentajeCambio = view.findViewById(

                R.id.txtPorcentajeCambio

        );


        txtEstadoAnalisis = view.findViewById(

                R.id.txtEstadoAnalisis

        );


        txtPagoActualAnalisis = view.findViewById(

                R.id.txtPagoActualAnalisis

        );


        txtPagoAnteriorAnalisis = view.findViewById(

                R.id.txtPagoAnteriorAnalisis

        );


        txtTarifaAnalisis = view.findViewById(

                R.id.txtTarifaAnalisis

        );


        // =====================================
        // CARGAR INFORMACIÓN
        // =====================================

        cargarAnalisis();


        return view;

    }


    // =====================================
    // CARGAR ANÁLISIS
    // =====================================

    private void cargarAnalisis() {


        if (auth.getCurrentUser() == null) {


            Toast.makeText(

                    requireContext(),

                    "No hay un usuario autenticado",

                    Toast.LENGTH_LONG

            ).show();


            return;

        }


        String correoUsuario = auth.getCurrentUser().getEmail();


        if (correoUsuario == null || correoUsuario.isEmpty()) {


            Toast.makeText(

                    requireContext(),

                    "No se encontró el correo del usuario",

                    Toast.LENGTH_LONG

            ).show();


            return;

        }


        usuariosRef

                .orderByChild("correo")

                .equalTo(correoUsuario)

                .addListenerForSingleValueEvent(

                        new ValueEventListener() {


                            @Override
                            public void onDataChange(

                                    @NonNull DataSnapshot snapshot

                            ) {


                                if (!snapshot.exists()) {


                                    Toast.makeText(

                                            requireContext(),

                                            "No se encontró el usuario",

                                            Toast.LENGTH_LONG

                                    ).show();


                                    return;

                                }


                                for (

                                        DataSnapshot usuarioSnapshot

                                        : snapshot.getChildren()

                                ) {


                                    DataSnapshot recibosSnapshot =

                                            usuarioSnapshot

                                                    .child("recibos");


                                    cargarUltimosRecibos(

                                            recibosSnapshot

                                    );


                                    break;

                                }

                            }


                            @Override
                            public void onCancelled(

                                    @NonNull DatabaseError error

                            ) {


                                Toast.makeText(

                                        requireContext(),

                                        "Error al consultar los datos",

                                        Toast.LENGTH_LONG

                                ).show();

                            }

                        }

                );

    }


    // =====================================
    // CARGAR LOS DOS ÚLTIMOS RECIBOS
    // =====================================

    private void cargarUltimosRecibos(

            DataSnapshot recibosSnapshot

    ) {


        List<DataSnapshot> listaRecibos =

                new ArrayList<>();


        for (

                DataSnapshot recibo

                : recibosSnapshot.getChildren()

        ) {


            listaRecibos.add(recibo);

        }


        if (listaRecibos.isEmpty()) {


            Toast.makeText(

                    requireContext(),

                    "No hay recibos registrados",

                    Toast.LENGTH_LONG

            ).show();


            return;

        }


        // =====================================
        // ORDENAR POR FECHA DE REGISTRO
        // MÁS RECIENTE PRIMERO
        // =====================================

        Collections.sort(

                listaRecibos,

                new Comparator<DataSnapshot>() {


                    @Override
                    public int compare(

                            DataSnapshot recibo1,

                            DataSnapshot recibo2

                    ) {


                        Long fecha1 =

                                recibo1

                                        .child("fechaRegistro")

                                        .getValue(Long.class);


                        Long fecha2 =

                                recibo2

                                        .child("fechaRegistro")

                                        .getValue(Long.class);


                        if (fecha1 == null) {

                            fecha1 = 0L;

                        }


                        if (fecha2 == null) {

                            fecha2 = 0L;

                        }


                        return Long.compare(

                                fecha2,

                                fecha1

                        );

                    }

                }

        );


        // =====================================
        // RECIBO ACTUAL
        // =====================================

        DataSnapshot reciboActual =

                listaRecibos.get(0);


        mostrarReciboActual(

                reciboActual

        );


        // =====================================
        // RECIBO ANTERIOR
        // =====================================

        if (listaRecibos.size() >= 2) {


            DataSnapshot reciboAnterior =

                    listaRecibos.get(1);


            mostrarReciboAnterior(

                    reciboAnterior

            );


            compararConsumos(

                    reciboActual,

                    reciboAnterior

            );


        } else {


            txtConsumoAnteriorAnalisis.setText(

                    "No disponible"

            );


            txtPeriodoAnteriorAnalisis.setText(

                    "No existe un recibo anterior"

            );


            txtComparacionConsumo.setText(

                    "No hay suficientes recibos para comparar"

            );


            txtPorcentajeCambio.setText(

                    "Cambio porcentual: No disponible"

            );


            txtEstadoAnalisis.setText(

                    "Sin comparación"

            );

        }

    }


    // =====================================
    // MOSTRAR RECIBO ACTUAL
    // =====================================

    private void mostrarReciboActual(

            DataSnapshot recibo

    ) {


        String consumo =

                obtenerValor(

                        recibo,

                        "consumo",

                        "0 kWh"

                );


        String fechaInicio =

                obtenerValor(

                        recibo,

                        "fechaInicio",

                        "--/--/----"

                );


        String fechaFin =

                obtenerValor(

                        recibo,

                        "fechaFin",

                        "--/--/----"

                );


        String pago =

                obtenerValor(

                        recibo,

                        "totalPagado",

                        "$0.00"

                );


        String tarifa =

                obtenerValor(

                        recibo,

                        "tarifa",

                        "No disponible"

                );


        txtConsumoActualAnalisis.setText(

                consumo

        );


        txtPeriodoActualAnalisis.setText(

                "Periodo: "

                        + fechaInicio

                        + " - "

                        + fechaFin

        );


        txtPagoActualAnalisis.setText(

                "Último pago: "

                        + pago

        );


        txtTarifaAnalisis.setText(

                "Tarifa: "

                        + tarifa

        );

    }


    // =====================================
    // MOSTRAR RECIBO ANTERIOR
    // =====================================

    private void mostrarReciboAnterior(

            DataSnapshot recibo

    ) {


        String consumo =

                obtenerValor(

                        recibo,

                        "consumo",

                        "0 kWh"

                );


        String fechaInicio =

                obtenerValor(

                        recibo,

                        "fechaInicio",

                        "--/--/----"

                );


        String fechaFin =

                obtenerValor(

                        recibo,

                        "fechaFin",

                        "--/--/----"

                );


        String pago =

                obtenerValor(

                        recibo,

                        "totalPagado",

                        "$0.00"

                );


        txtConsumoAnteriorAnalisis.setText(

                consumo

        );


        txtPeriodoAnteriorAnalisis.setText(

                "Periodo: "

                        + fechaInicio

                        + " - "

                        + fechaFin

        );


        txtPagoAnteriorAnalisis.setText(

                "Pago anterior: "

                        + pago

        );

    }


    // =====================================
    // COMPARAR CONSUMOS
    // =====================================

    private void compararConsumos(

            DataSnapshot reciboActual,

            DataSnapshot reciboAnterior

    ) {


        double consumoActual =

                convertirConsumo(

                        obtenerValor(

                                reciboActual,

                                "consumo",

                                "0"

                        )

                );


        double consumoAnterior =

                convertirConsumo(

                        obtenerValor(

                                reciboAnterior,

                                "consumo",

                                "0"

                        )

                );


        if (consumoAnterior <= 0) {


            txtComparacionConsumo.setText(

                    "No se puede calcular la comparación"

            );


            txtPorcentajeCambio.setText(

                    "Cambio porcentual: No disponible"

            );


            return;

        }


        double diferencia =

                consumoActual

                        - consumoAnterior;


        double porcentaje =

                (diferencia / consumoAnterior)

                        * 100;


        // =====================================
        // AUMENTÓ
        // =====================================

        if (diferencia > 0) {


            txtComparacionConsumo.setText(

                    String.format(

                            Locale.getDefault(),

                            "Aumentó %.2f kWh respecto al recibo anterior",

                            diferencia

                    )

            );


            txtPorcentajeCambio.setText(

                    String.format(

                            Locale.getDefault(),

                            "Aumento porcentual: %.1f%%",

                            porcentaje

                    )

            );


            txtEstadoAnalisis.setText(

                    "Consumo elevado"

            );


        }

        // =====================================
        // DISMINUYÓ
        // =====================================

        else if (diferencia < 0) {


            txtComparacionConsumo.setText(

                    String.format(

                            Locale.getDefault(),

                            "Disminuyó %.2f kWh respecto al recibo anterior",

                            Math.abs(diferencia)

                    )

            );


            txtPorcentajeCambio.setText(

                    String.format(

                            Locale.getDefault(),

                            "Disminución porcentual: %.1f%%",

                            Math.abs(porcentaje)

                    )

            );


            txtEstadoAnalisis.setText(

                    "Consumo reducido"

            );


        }

        // =====================================
        // IGUAL
        // =====================================

        else {


            txtComparacionConsumo.setText(

                    "El consumo se mantuvo igual"

            );


            txtPorcentajeCambio.setText(

                    "Cambio porcentual: 0%"

            );


            txtEstadoAnalisis.setText(

                    "Consumo estable"

            );

        }

    }


    // =====================================
    // CONVERTIR CONSUMO
    // =====================================

    private double convertirConsumo(

            String consumo

    ) {


        try {


            String valor =

                    consumo

                            .replace(

                                    "kWh",

                                    ""

                            )

                            .replace(

                                    "KWh",

                                    ""

                            )

                            .replace(

                                    "kwh",

                                    ""

                            )

                            .trim()

                            .replace(

                                    ",",

                                    "."

                            );


            return Double.parseDouble(

                    valor

            );


        } catch (Exception e) {


            return 0;

        }

    }


    // =====================================
    // OBTENER VALOR DE FIREBASE
    // =====================================

    private String obtenerValor(

            DataSnapshot snapshot,

            String campo,

            String valorPredeterminado

    ) {


        Object valor =

                snapshot

                        .child(campo)

                        .getValue();


        if (valor == null) {


            return valorPredeterminado;

        }


        return String.valueOf(

                valor

        );

    }

}