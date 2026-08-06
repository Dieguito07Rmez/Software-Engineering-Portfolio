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


public class Inicio extends Fragment {


    // =========================
    // TEXTVIEWS
    // =========================

    private TextView txtBienvenida;
    private TextView txtConsumoActual;
    private TextView txtElectrodomesticos;
    private TextView txtEstadoConsumo;
    private TextView txtComparacionConsumo;
    private TextView txtUltimoPago;
    private TextView txtPeriodo;

    private TextView txtConsumoAnterior;
    private TextView txtPagoAnterior;
    private TextView txtPeriodoAnterior;


    // =========================
    // FIREBASE
    // =========================

    private FirebaseAuth auth;
    private DatabaseReference usuariosRef;


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
                        R.layout.fragment_inicio,
                        container,
                        false
                );


        // =========================
        // REFERENCIAS XML
        // =========================

        txtBienvenida =
                view.findViewById(
                        R.id.txtBienvenida
                );


        txtConsumoActual =
                view.findViewById(
                        R.id.txtConsumoActual
                );


        txtElectrodomesticos =
                view.findViewById(
                        R.id.txtElectrodomesticos
                );


        txtEstadoConsumo =
                view.findViewById(
                        R.id.txtEstadoConsumo
                );


        txtComparacionConsumo =
                view.findViewById(
                        R.id.txtComparacionConsumo
                );


        txtUltimoPago =
                view.findViewById(
                        R.id.txtUltimoPago
                );


        txtPeriodo =
                view.findViewById(
                        R.id.txtPeriodo
                );


        txtConsumoAnterior =
                view.findViewById(
                        R.id.txtConsumoAnterior
                );


        txtPagoAnterior =
                view.findViewById(
                        R.id.txtPagoAnterior
                );


        txtPeriodoAnterior =
                view.findViewById(
                        R.id.txtPeriodoAnterior
                );


        // =========================
        // FIREBASE
        // =========================

        auth =
                FirebaseAuth.getInstance();


        usuariosRef =
                FirebaseDatabase.getInstance()
                        .getReference(
                                "usuarios"
                        );


        // =========================
        // CARGAR INFORMACIÓN
        // =========================

        cargarInformacion();


        return view;
    }


    // =====================================================
    // CARGAR INFORMACIÓN DEL USUARIO
    // =====================================================

    private void cargarInformacion() {


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


        String correoUsuario =
                auth.getCurrentUser()
                        .getEmail();


        if (
                correoUsuario
                        == null
                        || correoUsuario.isEmpty()
        ) {


            return;
        }


        usuariosRef
                .orderByChild(
                        "correo"
                )
                .equalTo(
                        correoUsuario
                )
                .addListenerForSingleValueEvent(
                        new ValueEventListener() {


                            @Override
                            public void onDataChange(
                                    @NonNull DataSnapshot snapshot
                            ) {


                                if (
                                        !snapshot.exists()
                                ) {


                                    Toast.makeText(
                                            requireContext(),
                                            "No se encontró la información del usuario",
                                            Toast.LENGTH_LONG
                                    ).show();


                                    return;
                                }


                                for (
                                        DataSnapshot usuarioSnapshot
                                        : snapshot.getChildren()
                                ) {


                                    cargarNombreUsuario(
                                            usuarioSnapshot
                                    );


                                    cargarRecibos(
                                            usuarioSnapshot
                                    );


                                    cargarElectrodomesticos(
                                            usuarioSnapshot
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
                                        "Error al cargar la información: "
                                                + error.getMessage(),
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        }
                );
    }


    // =====================================================
    // CARGAR NOMBRE DEL USUARIO
    // =====================================================

    private void cargarNombreUsuario(
            DataSnapshot usuarioSnapshot
    ) {


        String nombre =
                usuarioSnapshot
                        .child(
                                "nombre"
                        )
                        .getValue(
                                String.class
                        );


        if (
                nombre != null
                        && !nombre.isEmpty()
        ) {


            txtBienvenida.setText(
                    "¡Bienvenido, "
                            + nombre
                            + "!"
            );
        }
    }


    // =====================================================
    // CARGAR RECIBOS
    // =====================================================

    private void cargarRecibos(
            DataSnapshot usuarioSnapshot
    ) {


        List<DataSnapshot> recibos =
                new ArrayList<>();


        DataSnapshot recibosSnapshot =
                usuarioSnapshot
                        .child(
                                "recibos"
                        );


        for (
                DataSnapshot recibo
                : recibosSnapshot.getChildren()
        ) {


            recibos.add(
                    recibo
            );
        }


        if (
                recibos.isEmpty()
        ) {


            mostrarSinRecibos();


            return;
        }


        // =========================
        // ORDENAR POR FECHA
        // MÁS RECIENTE PRIMERO
        // =========================

        Collections.sort(
                recibos,
                new Comparator<DataSnapshot>() {


                    @Override
                    public int compare(
                            DataSnapshot recibo1,
                            DataSnapshot recibo2
                    ) {


                        Long fecha1 =
                                recibo1
                                        .child(
                                                "fechaRegistro"
                                        )
                                        .getValue(
                                                Long.class
                                        );


                        Long fecha2 =
                                recibo2
                                        .child(
                                                "fechaRegistro"
                                        )
                                        .getValue(
                                                Long.class
                                        );


                        if (
                                fecha1
                                        == null
                        ) {


                            fecha1 =
                                    0L;
                        }


                        if (
                                fecha2
                                        == null
                        ) {


                            fecha2 =
                                    0L;
                        }


                        return Long.compare(
                                fecha2,
                                fecha1
                        );
                    }
                }
        );


        // =========================
        // ÚLTIMO RECIBO
        // =========================

        DataSnapshot ultimoRecibo =
                recibos.get(
                        0
                );


        mostrarUltimoRecibo(
                ultimoRecibo
        );


        // =========================
        // RECIBO ANTERIOR
        // =========================

        if (
                recibos.size()
                        > 1
        ) {


            DataSnapshot reciboAnterior =
                    recibos.get(
                            1
                    );


            mostrarReciboAnterior(
                    reciboAnterior
            );


            compararConsumos(
                    ultimoRecibo,
                    reciboAnterior
            );

        } else {


            txtConsumoAnterior.setText(
                    "Consumo: No disponible"
            );


            txtPagoAnterior.setText(
                    "Pago: No disponible"
            );


            txtPeriodoAnterior.setText(
                    "Periodo: No disponible"
            );


            txtEstadoConsumo.setText(
                    "Sin comparación"
            );


            txtComparacionConsumo.setText(
                    "Se necesita un recibo anterior"
            );
        }
    }


    // =====================================================
    // MOSTRAR ÚLTIMO RECIBO
    // =====================================================

    private void mostrarUltimoRecibo(
            DataSnapshot recibo
    ) {


        String consumo =
                obtenerTexto(
                        recibo,
                        "consumo",
                        "0 kWh"
                );


        String pago =
                obtenerTexto(
                        recibo,
                        "totalPagado",
                        "$0.00"
                );


        String fechaInicio =
                obtenerTexto(
                        recibo,
                        "fechaInicio",
                        "--/--/----"
                );


        String fechaFin =
                obtenerTexto(
                        recibo,
                        "fechaFin",
                        "--/--/----"
                );


        txtConsumoActual.setText(
                consumo
        );


        txtUltimoPago.setText(
                pago
        );


        txtPeriodo.setText(
                fechaInicio
                        + " - "
                        + fechaFin
        );
    }


    // =====================================================
    // MOSTRAR RECIBO ANTERIOR
    // =====================================================

    private void mostrarReciboAnterior(
            DataSnapshot recibo
    ) {


        String consumo =
                obtenerTexto(
                        recibo,
                        "consumo",
                        "No disponible"
                );


        String pago =
                obtenerTexto(
                        recibo,
                        "totalPagado",
                        "No disponible"
                );


        String fechaInicio =
                obtenerTexto(
                        recibo,
                        "fechaInicio",
                        "--/--/----"
                );


        String fechaFin =
                obtenerTexto(
                        recibo,
                        "fechaFin",
                        "--/--/----"
                );


        txtConsumoAnterior.setText(
                "Consumo: "
                        + consumo
        );


        txtPagoAnterior.setText(
                "Pago: "
                        + pago
        );


        txtPeriodoAnterior.setText(
                "Periodo: "
                        + fechaInicio
                        + " - "
                        + fechaFin
        );
    }


    // =====================================================
    // COMPARAR CONSUMOS
    // =====================================================

    private void compararConsumos(
            DataSnapshot ultimoRecibo,
            DataSnapshot reciboAnterior
    ) {


        double consumoActual =
                obtenerNumeroConsumo(
                        ultimoRecibo
                );


        double consumoAnterior =
                obtenerNumeroConsumo(
                        reciboAnterior
                );


        if (
                consumoActual
                        == -1
                        || consumoAnterior
                        == -1
        ) {


            txtEstadoConsumo.setText(
                    "Sin comparación"
            );


            txtComparacionConsumo.setText(
                    "No se pudieron comparar los consumos"
            );


            return;
        }


        double diferencia =
                consumoActual
                        - consumoAnterior;


        double porcentaje =
                0;


        if (
                consumoAnterior
                        > 0
        ) {


            porcentaje =
                    (
                            diferencia
                                    / consumoAnterior
                    )
                            * 100;
        }


        if (
                consumoActual
                        > consumoAnterior
        ) {


            txtEstadoConsumo.setText(
                    "Consumo aumentado"
            );


            txtComparacionConsumo.setText(
                    String.format(
                            Locale.getDefault(),
                            "Aumentó %.2f kWh (%.1f%%) respecto al recibo anterior",
                            diferencia,
                            porcentaje
                    )
            );


        } else if (
                consumoActual
                        < consumoAnterior
        ) {


            txtEstadoConsumo.setText(
                    "Consumo reducido"
            );


            txtComparacionConsumo.setText(
                    String.format(
                            Locale.getDefault(),
                            "Disminuyó %.2f kWh (%.1f%%) respecto al recibo anterior",
                            Math.abs(
                                    diferencia
                            ),
                            Math.abs(
                                    porcentaje
                            )
                    )
            );


        } else {


            txtEstadoConsumo.setText(
                    "Consumo estable"
            );


            txtComparacionConsumo.setText(
                    "El consumo es igual al del recibo anterior"
            );
        }
    }


    // =====================================================
    // OBTENER CONSUMO COMO NÚMERO
    // =====================================================

    private double obtenerNumeroConsumo(
            DataSnapshot recibo
    ) {


        String consumo =
                recibo
                        .child(
                                "consumo"
                        )
                        .getValue(
                                String.class
                        );


        if (
                consumo
                        == null
        ) {


            return -1;
        }


        try {


            String numero =
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
                    numero
            );


        } catch (
                Exception e
        ) {


            return -1;
        }
    }


    // =====================================================
    // CARGAR ELECTRODOMÉSTICOS
    // =====================================================

    private void cargarElectrodomesticos(
            DataSnapshot usuarioSnapshot
    ) {


        DataSnapshot electrodomesticosSnapshot =
                usuarioSnapshot
                        .child(
                                "electrodomesticos"
                        );


        long cantidad =
                electrodomesticosSnapshot
                        .getChildrenCount();


        txtElectrodomesticos.setText(
                String.valueOf(
                        cantidad
                )
        );
    }


    // =====================================================
    // OBTENER TEXTO
    // =====================================================

    private String obtenerTexto(
            DataSnapshot snapshot,
            String campo,
            String valorPredeterminado
    ) {


        String valor =
                snapshot
                        .child(
                                campo
                        )
                        .getValue(
                                String.class
                        );


        if (
                valor
                        == null
                        || valor.trim().isEmpty()
        ) {


            return valorPredeterminado;
        }


        return valor;
    }


    // =====================================================
    // SIN RECIBOS
    // =====================================================

    private void mostrarSinRecibos()
    {


        txtConsumoActual.setText(
                "0 kWh"
        );


        txtUltimoPago.setText(
                "$0.00"
        );


        txtPeriodo.setText(
                "--/--/---- - --/--/----"
        );


        txtConsumoAnterior.setText(
                "Consumo: No disponible"
        );


        txtPagoAnterior.setText(
                "Pago: No disponible"
        );


        txtPeriodoAnterior.setText(
                "Periodo: No disponible"
        );


        txtEstadoConsumo.setText(
                "Sin información"
        );


        txtComparacionConsumo.setText(
                "Aún no hay recibos registrados"
        );
    }
}