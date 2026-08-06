package com.example.ecowatt;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.tom_roush.pdfbox.android.PDFBoxResourceLoader;
import com.tom_roush.pdfbox.pdmodel.PDDocument;
import com.tom_roush.pdfbox.text.PDFTextStripper;

import java.io.InputStream;
import java.text.Normalizer;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ConsumoLuz extends Fragment {


    // =========================
    // BOTONES
    // =========================

    private Button btnSeleccionarPDF;
    private Button btnAnalizar;
    private Button btnGuardar;


    // =========================
    // TEXTVIEWS
    // =========================

    private TextView txtArchivo;
    private TextView txtConsumo;
    private TextView txtInicio;
    private TextView txtFin;
    private TextView txtPago;
    private TextView txtTarifa;


    // =========================
    // PROGRESSBAR
    // =========================

    private ProgressBar progressAnalisis;


    // =========================
    // ARCHIVO PDF
    // =========================

    private Uri pdfSeleccionado;


    // =========================
    // SELECTOR DE ARCHIVOS
    // =========================

    private final ActivityResultLauncher<Intent> selectorPDF =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    resultado -> {

                        if (resultado.getResultCode()
                                == Activity.RESULT_OK
                                && resultado.getData() != null) {

                            Uri uri =
                                    resultado.getData().getData();


                            if (uri != null) {

                                pdfSeleccionado =
                                        uri;


                                // =========================
                                // CONSERVAR PERMISO DE LECTURA
                                // =========================

                                try {

                                    requireContext()
                                            .getContentResolver()
                                            .takePersistableUriPermission(
                                                    uri,
                                                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                                            );

                                } catch (Exception e) {

                                    // Algunos dispositivos no permiten
                                    // conservar este permiso
                                }


                                // =========================
                                // MOSTRAR NOMBRE DEL ARCHIVO
                                // =========================

                                txtArchivo.setText(
                                        obtenerNombreArchivo(
                                                uri
                                        )
                                );


                                Toast.makeText(
                                        requireContext(),
                                        "Archivo seleccionado correctamente",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        }
                    }
            );


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
                        R.layout.fragment_consumo_luz,
                        container,
                        false
                );


        // =========================
        // INICIALIZAR PDFBOX
        // =========================

        PDFBoxResourceLoader.init(
                requireContext()
                        .getApplicationContext()
        );


        // =========================
        // REFERENCIAS XML
        // =========================

        btnSeleccionarPDF =
                view.findViewById(
                        R.id.btnSeleccionarPDF
                );


        btnAnalizar =
                view.findViewById(
                        R.id.btnAnalizar
                );


        btnGuardar =
                view.findViewById(
                        R.id.btnGuardar
                );


        txtArchivo =
                view.findViewById(
                        R.id.txtArchivo
                );


        txtConsumo =
                view.findViewById(
                        R.id.txtConsumo
                );


        txtInicio =
                view.findViewById(
                        R.id.txtInicio
                );


        txtFin =
                view.findViewById(
                        R.id.txtFin
                );


        txtPago =
                view.findViewById(
                        R.id.txtPago
                );


        txtTarifa =
                view.findViewById(
                        R.id.txtTarifa
                );


        progressAnalisis =
                view.findViewById(
                        R.id.progressAnalisis
                );


        // =====================================================
        // BOTÓN SELECCIONAR PDF
        // =====================================================

        btnSeleccionarPDF.setOnClickListener(
                v -> seleccionarPDF()
        );


        // =====================================================
        // BOTÓN ANALIZAR
        // =====================================================

        btnAnalizar.setOnClickListener(
                v -> {


                    if (pdfSeleccionado == null) {


                        Toast.makeText(
                                requireContext(),
                                "Primero selecciona un recibo PDF",
                                Toast.LENGTH_LONG
                        ).show();


                        return;
                    }


                    analizarPDF();
                }
        );


        // =====================================================
        // BOTÓN GUARDAR
        // =====================================================

        btnGuardar.setOnClickListener(
                v -> {


                    // VALIDAR CAMPOS
                    if (!validarCampos()) {


                        return;
                    }


                    Toast.makeText(
                            requireContext(),
                            "Validación correcta. Guardando...",
                            Toast.LENGTH_SHORT
                    ).show();


                    guardarInformacion();
                }
        );


        return view;
    }


    // =====================================================
    // SELECCIONAR ARCHIVO PDF
    // =====================================================

    private void seleccionarPDF() {


        Intent intent =
                new Intent(
                        Intent.ACTION_OPEN_DOCUMENT
                );


        intent.addCategory(
                Intent.CATEGORY_OPENABLE
        );


        intent.setType(
                "*/*"
        );


        intent.addFlags(
                Intent.FLAG_GRANT_READ_URI_PERMISSION
                        | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
        );


        selectorPDF.launch(
                intent
        );
    }


    // =====================================================
    // OBTENER NOMBRE DEL ARCHIVO
    // =====================================================

    private String obtenerNombreArchivo(
            Uri uri
    ) {


        String nombre =
                "Archivo seleccionado";


        Cursor cursor =
                requireActivity()
                        .getContentResolver()
                        .query(
                                uri,
                                null,
                                null,
                                null,
                                null
                        );


        if (cursor != null) {


            int indiceNombre =
                    cursor.getColumnIndex(
                            OpenableColumns.DISPLAY_NAME
                    );


            if (cursor.moveToFirst()
                    && indiceNombre >= 0) {


                nombre =
                        cursor.getString(
                                indiceNombre
                        );
            }


            cursor.close();
        }


        return nombre;
    }


    // =====================================================
    // ANALIZAR PDF
    // =====================================================

    private void analizarPDF() {


        progressAnalisis.setVisibility(
                View.VISIBLE
        );


        btnAnalizar.setEnabled(
                false
        );


        new Thread(
                () -> {


                    PDDocument documento =
                            null;


                    try {


                        InputStream inputStream =
                                requireContext()
                                        .getContentResolver()
                                        .openInputStream(
                                                pdfSeleccionado
                                        );


                        if (inputStream == null) {


                            throw new Exception(
                                    "No se pudo abrir el archivo"
                            );
                        }


                        documento =
                                PDDocument.load(
                                        inputStream
                                );


                        PDFTextStripper stripper =
                                new PDFTextStripper();


                        String texto =
                                stripper.getText(
                                        documento
                                );


                        documento.close();


                        inputStream.close();


                        // =========================
                        // EXTRAER INFORMACIÓN
                        // =========================

                        String consumo =
                                extraerConsumo(
                                        texto
                                );


                        String[] periodo =
                                extraerPeriodo(
                                        texto
                                );


                        String pago =
                                extraerTotalPagado(
                                        texto
                                );


                        String tarifa =
                                extraerTarifa(
                                        texto
                                );


                        requireActivity()
                                .runOnUiThread(
                                        () -> {


                                            txtConsumo.setText(
                                                    consumo
                                            );


                                            txtInicio.setText(
                                                    periodo[0]
                                            );


                                            txtFin.setText(
                                                    periodo[1]
                                            );


                                            txtPago.setText(
                                                    pago
                                            );


                                            txtTarifa.setText(
                                                    tarifa
                                            );


                                            progressAnalisis.setVisibility(
                                                    View.GONE
                                            );


                                            btnAnalizar.setEnabled(
                                                    true
                                            );


                                            Toast.makeText(
                                                    requireContext(),
                                                    "PDF analizado correctamente",
                                                    Toast.LENGTH_SHORT
                                            ).show();
                                        }
                                );


                    } catch (Exception e) {


                        e.printStackTrace();


                        if (documento != null) {


                            try {


                                documento.close();


                            } catch (Exception ignored) {


                            }
                        }


                        requireActivity()
                                .runOnUiThread(
                                        () -> {


                                            progressAnalisis.setVisibility(
                                                    View.GONE
                                            );


                                            btnAnalizar.setEnabled(
                                                    true
                                            );


                                            Toast.makeText(
                                                    requireContext(),
                                                    "El archivo seleccionado no es un PDF válido o no se pudo leer",
                                                    Toast.LENGTH_LONG
                                            ).show();
                                        }
                                );
                    }


                }
        ).start();
    }


    // =====================================================
    // EXTRAER CONSUMO
    // =====================================================

    private String extraerConsumo(
            String texto
    ) {


        String textoNormalizado =
                normalizarTexto(
                        texto
                );


        Pattern patron =
                Pattern.compile(
                        "(\\d+(?:[.,]\\d+)?)\\s*kwh",
                        Pattern.CASE_INSENSITIVE
                );


        Matcher matcher =
                patron.matcher(
                        textoNormalizado
                );


        if (matcher.find()) {


            return matcher.group(1)
                    .replace(
                            ",",
                            "."
                    )
                    + " kWh";
        }


        return "No identificado";
    }


    // =====================================================
    // EXTRAER PERIODO FACTURADO
    // =====================================================

    private String[] extraerPeriodo(
            String texto
    ) {


        String fechaInicio =
                "--/--/----";


        String fechaFin =
                "--/--/----";


        String textoNormalizado =
                normalizarTexto(
                        texto
                );


        /*
         * FORMATO DEL RECIBO:
         *
         * PERIODO FACTURADO: 27 FEB 26 - 30 ABR 26
         *
         * RESULTADO:
         *
         * 27/02/2026
         * 30/04/2026
         */


        Pattern patron =
                Pattern.compile(
                        "PERIODO\\s+FACTURADO\\s*:\\s*"
                                + "(\\d{1,2})\\s+"
                                + "(ENE|FEB|MAR|ABR|MAY|JUN|JUL|AGO|SEP|OCT|NOV|DIC)\\s+"
                                + "(\\d{2,4})"
                                + "\\s*-\\s*"
                                + "(\\d{1,2})\\s+"
                                + "(ENE|FEB|MAR|ABR|MAY|JUN|JUL|AGO|SEP|OCT|NOV|DIC)\\s+"
                                + "(\\d{2,4})",
                        Pattern.CASE_INSENSITIVE
                );


        Matcher matcher =
                patron.matcher(
                        textoNormalizado
                );


        if (matcher.find()) {


            fechaInicio =
                    convertirFecha(
                            matcher.group(1),
                            matcher.group(2),
                            matcher.group(3)
                    );


            fechaFin =
                    convertirFecha(
                            matcher.group(4),
                            matcher.group(5),
                            matcher.group(6)
                    );
        }


        return new String[]{
                fechaInicio,
                fechaFin
        };
    }


    // =====================================================
    // CONVERTIR FECHA
    // =====================================================

    private String convertirFecha(
            String dia,
            String mes,
            String anio
    ) {


        Map<String, String> meses =
                new HashMap<>();


        meses.put(
                "ene",
                "01"
        );


        meses.put(
                "feb",
                "02"
        );


        meses.put(
                "mar",
                "03"
        );


        meses.put(
                "abr",
                "04"
        );


        meses.put(
                "may",
                "05"
        );


        meses.put(
                "jun",
                "06"
        );


        meses.put(
                "jul",
                "07"
        );


        meses.put(
                "ago",
                "08"
        );


        meses.put(
                "sep",
                "09"
        );


        meses.put(
                "oct",
                "10"
        );


        meses.put(
                "nov",
                "11"
        );


        meses.put(
                "dic",
                "12"
        );


        String mesNumero =
                meses.get(
                        mes.toLowerCase(
                                Locale.ROOT
                        )
                );


        if (anio.length() == 2) {


            anio =
                    "20"
                            + anio;
        }


        return String.format(
                Locale.getDefault(),
                "%02d/%s/%s",
                Integer.parseInt(
                        dia
                ),
                mesNumero,
                anio
        );
    }


    // =====================================================
    // EXTRAER TOTAL PAGADO
    // =====================================================

    private String extraerTotalPagado(
            String texto
    ) {


        String textoNormalizado =
                normalizarTexto(
                        texto
                );


        Pattern patron =
                Pattern.compile(
                        "(?:total\\s+a\\s+pagar|total\\s+a\\s+pagarse|importe\\s+a\\s+pagar)"
                                + "[^$\\d]{0,30}"
                                + "\\$?\\s*"
                                + "(\\d+(?:[,.]\\d{2})?)",
                        Pattern.CASE_INSENSITIVE
                );


        Matcher matcher =
                patron.matcher(
                        textoNormalizado
                );


        if (matcher.find()) {


            return "$"
                    + matcher.group(1);
        }


        return "$0.00";
    }


    // =====================================================
    // EXTRAER TARIFA
    // =====================================================

    private String extraerTarifa(
            String texto
    ) {


        String textoNormalizado =
                normalizarTexto(
                        texto
                );


        Pattern patron =
                Pattern.compile(
                        "(?:tarifa|tipo\\s+de\\s+tarifa)"
                                + "\\s*[:\\-]?\\s*"
                                + "([\\w\\-]+)",
                        Pattern.CASE_INSENSITIVE
                );


        Matcher matcher =
                patron.matcher(
                        textoNormalizado
                );


        if (matcher.find()) {


            return matcher.group(1);
        }


        return "No disponible";
    }


    // =====================================================
    // NORMALIZAR TEXTO
    // =====================================================

    private String normalizarTexto(
            String texto
    ) {


        String normalizado =
                Normalizer.normalize(
                        texto,
                        Normalizer.Form.NFD
                );


        normalizado =
                normalizado.replaceAll(
                        "\\p{InCombiningDiacriticalMarks}+",
                        ""
                );


        normalizado =
                normalizado.replaceAll(
                        "[\\r\\n]+",
                        " "
                );


        normalizado =
                normalizado.replaceAll(
                        "\\s+",
                        " "
                );


        return normalizado.trim();
    }


    // =====================================================
    // VALIDAR CAMPOS
    // =====================================================

    private boolean validarCampos() {


        String consumo =
                txtConsumo.getText()
                        .toString()
                        .trim();


        String inicio =
                txtInicio.getText()
                        .toString()
                        .trim();


        String fin =
                txtFin.getText()
                        .toString()
                        .trim();


        String pago =
                txtPago.getText()
                        .toString()
                        .trim();


        String tarifa =
                txtTarifa.getText()
                        .toString()
                        .trim();


        // =========================
        // VALIDAR CONSUMO
        // =========================

        if (consumo.isEmpty()
                || consumo.equalsIgnoreCase(
                "No identificado"
        )
                || consumo.equalsIgnoreCase(
                "0 kWh"
        )
        ) {


            Toast.makeText(
                    requireContext(),
                    "El consumo no está completo",
                    Toast.LENGTH_LONG
            ).show();


            return false;
        }


        // =========================
        // VALIDAR FECHA DE INICIO
        // =========================

        if (inicio.isEmpty()
                || inicio.equals(
                "--/--/----"
        )
        ) {


            Toast.makeText(
                    requireContext(),
                    "La fecha de inicio no está completa",
                    Toast.LENGTH_LONG
            ).show();


            return false;
        }


        // =========================
        // VALIDAR FECHA FINAL
        // =========================

        if (fin.isEmpty()
                || fin.equals(
                "--/--/----"
        )
        ) {


            Toast.makeText(
                    requireContext(),
                    "La fecha final no está completa",
                    Toast.LENGTH_LONG
            ).show();


            return false;
        }


        // =========================
        // VALIDAR TOTAL PAGADO
        // =========================

        if (pago.isEmpty()
                || pago.equals(
                "$0.00"
        )
                || pago.equalsIgnoreCase(
                "No identificado"
        )
        ) {


            Toast.makeText(
                    requireContext(),
                    "El total pagado no está completo",
                    Toast.LENGTH_LONG
            ).show();


            return false;
        }


        // =========================
        // VALIDAR TARIFA
        // =========================

        if (tarifa.isEmpty()
                || tarifa.equalsIgnoreCase(
                "No disponible"
        )
        ) {


            Toast.makeText(
                    requireContext(),
                    "La tarifa no está completa",
                    Toast.LENGTH_LONG
            ).show();


            return false;
        }


        return true;
    }


    // =====================================================
    // GUARDAR INFORMACIÓN
    // =====================================================

    private void guardarInformacion() {


        FirebaseAuth auth =
                FirebaseAuth.getInstance();


        // =========================
        // VERIFICAR USUARIO
        // =========================

        if (auth.getCurrentUser() == null) {


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


        if (correoUsuario == null
                || correoUsuario.isEmpty()
        ) {


            Toast.makeText(
                    requireContext(),
                    "El usuario no tiene un correo asociado",
                    Toast.LENGTH_LONG
            ).show();


            return;
        }


        // =========================
        // REFERENCIA A USUARIOS
        // =========================

        DatabaseReference usuariosRef =
                FirebaseDatabase.getInstance()
                        .getReference(
                                "usuarios"
                        );


        /*
         * BUSCAR AL USUARIO ACTUAL
         * POR SU CORREO
         */


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


                                if (!snapshot.exists()) {


                                    Toast.makeText(
                                            requireContext(),
                                            "No se encontró el usuario en la base de datos",
                                            Toast.LENGTH_LONG
                                    ).show();


                                    return;
                                }


                                for (
                                        DataSnapshot usuarioSnapshot
                                        : snapshot.getChildren()
                                ) {


                                    // =========================
                                    // ID DEL USUARIO
                                    // =========================

                                    String idUsuario =
                                            usuarioSnapshot.getKey();


                                    if (idUsuario == null) {


                                        continue;
                                    }


                                    // =========================
                                    // REFERENCIA A RECIBOS
                                    // =========================

                                    DatabaseReference recibosRef =
                                            usuariosRef
                                                    .child(
                                                            idUsuario
                                                    )
                                                    .child(
                                                            "recibos"
                                                    );


                                    // =========================
                                    // CREAR ID DEL RECIBO
                                    // =========================

                                    String idRecibo =
                                            recibosRef
                                                    .push()
                                                    .getKey();


                                    if (idRecibo == null) {


                                        Toast.makeText(
                                                requireContext(),
                                                "No se pudo generar el ID del recibo",
                                                Toast.LENGTH_LONG
                                        ).show();


                                        return;
                                    }


                                    // =========================
                                    // CREAR OBJETO RECIBO
                                    // =========================

                                    Map<String, Object> recibo =
                                            new HashMap<>();


                                    recibo.put(
                                            "consumo",
                                            txtConsumo
                                                    .getText()
                                                    .toString()
                                                    .trim()
                                    );


                                    recibo.put(
                                            "fechaInicio",
                                            txtInicio
                                                    .getText()
                                                    .toString()
                                                    .trim()
                                    );


                                    recibo.put(
                                            "fechaFin",
                                            txtFin
                                                    .getText()
                                                    .toString()
                                                    .trim()
                                    );


                                    recibo.put(
                                            "totalPagado",
                                            txtPago
                                                    .getText()
                                                    .toString()
                                                    .trim()
                                    );


                                    recibo.put(
                                            "tarifa",
                                            txtTarifa
                                                    .getText()
                                                    .toString()
                                                    .trim()
                                    );


                                    recibo.put(
                                            "fechaRegistro",
                                            System.currentTimeMillis()
                                    );


                                    // =========================
                                    // GUARDAR RECIBO
                                    // =========================

                                    recibosRef
                                            .child(
                                                    idRecibo
                                            )
                                            .setValue(
                                                    recibo
                                            )
                                            .addOnSuccessListener(
                                                    unused -> {


                                                        Toast.makeText(
                                                                requireContext(),
                                                                "Información guardada correctamente",
                                                                Toast.LENGTH_LONG
                                                        ).show();
                                                    }
                                            )
                                            .addOnFailureListener(
                                                    e -> {


                                                        Toast.makeText(
                                                                requireContext(),
                                                                "Error al guardar la información: "
                                                                        + e.getMessage(),
                                                                Toast.LENGTH_LONG
                                                        ).show();
                                                    }
                                            );


                                    // SOLO UN USUARIO
                                    break;
                                }
                            }


                            @Override
                            public void onCancelled(
                                    @NonNull DatabaseError error
                            ) {


                                Toast.makeText(
                                        requireContext(),
                                        "Error al buscar el usuario: "
                                                + error.getMessage(),
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        }
                );
    }
}