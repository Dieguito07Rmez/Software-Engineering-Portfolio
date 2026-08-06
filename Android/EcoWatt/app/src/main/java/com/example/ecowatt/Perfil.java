package com.example.ecowatt;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class Perfil extends Fragment {

// =====================================================
// ELEMENTOS DE LA VISTA
// =====================================================

    private CircleImageView imgPerfil;

    private TextView txtNombre;
    private TextView txtCorreo;
    private TextView txtIdUsuario;
    private TextView txtConsumoPerfil;
    private TextView txtElectrodomesticosPerfil;

    private EditText edtNombreCompleto;

    private Button btnEditarPerfil;
    private Button btnCambiarFoto;
    private Button btnGuardarCambios;


// =====================================================
// FIREBASE
// =====================================================

    private FirebaseAuth auth;

    private DatabaseReference usuariosRef;

    private StorageReference storageRef;


// =====================================================
// DATOS
// =====================================================

    private String idUsuario;

    private Uri imagenSeleccionada;


// =====================================================
// SELECTOR DE IMAGEN
// =====================================================

    private final ActivityResultLauncher<Intent> selectorImagen =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    resultado -> {

                        if (
                                resultado.getResultCode()
                                        == Activity.RESULT_OK
                                        && resultado.getData() != null
                        ) {

                            Uri uri =
                                    resultado.getData().getData();

                            if (uri != null) {

                                imagenSeleccionada = uri;

                                try {

                                    requireContext()
                                            .getContentResolver()
                                            .takePersistableUriPermission(
                                                    uri,
                                                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                                            );

                                } catch (Exception ignored) {
                                }


                                imgPerfil.setImageURI(
                                        imagenSeleccionada
                                );


                                Toast.makeText(
                                        requireContext(),
                                        "Imagen seleccionada correctamente",
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
                        R.layout.fragment_perfil,
                        container,
                        false
                );


        // =================================================
        // REFERENCIAS XML
        // =================================================

        imgPerfil =
                view.findViewById(
                        R.id.imgPerfil
                );

        txtNombre =
                view.findViewById(
                        R.id.txtNombre
                );

        txtCorreo =
                view.findViewById(
                        R.id.txtCorreo
                );

        txtIdUsuario =
                view.findViewById(
                        R.id.txtIdUsuario
                );

        txtConsumoPerfil =
                view.findViewById(
                        R.id.txtConsumoPerfil
                );

        txtElectrodomesticosPerfil =
                view.findViewById(
                        R.id.txtElectrodomesticosPerfil
                );

        edtNombreCompleto =
                view.findViewById(
                        R.id.edtNombreCompleto
                );

        btnEditarPerfil =
                view.findViewById(
                        R.id.btnEditarPerfil
                );

        btnCambiarFoto =
                view.findViewById(
                        R.id.btnCambiarFoto
                );

        btnGuardarCambios =
                view.findViewById(
                        R.id.btnGuardarCambios
                );


        // =================================================
        // FIREBASE
        // =================================================

        auth =
                FirebaseAuth.getInstance();

        usuariosRef =
                FirebaseDatabase.getInstance()
                        .getReference("usuarios");

        storageRef =
                FirebaseStorage.getInstance()
                        .getReference()
                        .child("fotosPerfil");


        // =================================================
        // ESTADO INICIAL
        // =================================================

        edtNombreCompleto.setEnabled(
                false
        );

        btnCambiarFoto.setEnabled(
                false
        );

        btnGuardarCambios.setEnabled(
                false
        );


        // =================================================
        // BOTÓN EDITAR PERFIL
        // =================================================

        btnEditarPerfil.setOnClickListener(
                v -> {

                    edtNombreCompleto.setEnabled(
                            true
                    );

                    btnCambiarFoto.setEnabled(
                            true
                    );

                    btnGuardarCambios.setEnabled(
                            true
                    );

                    edtNombreCompleto.requestFocus();


                    Toast.makeText(
                            requireContext(),
                            "Ahora puedes editar tu perfil",
                            Toast.LENGTH_SHORT
                    ).show();
                }
        );


        // =================================================
        // BOTÓN CAMBIAR FOTO
        // =================================================

        btnCambiarFoto.setOnClickListener(
                v -> seleccionarImagen()
        );


        // =================================================
        // BOTÓN GUARDAR
        // =================================================

        btnGuardarCambios.setOnClickListener(
                v -> guardarCambios()
        );


        // =================================================
        // CARGAR DATOS
        // =================================================

        cargarInformacionUsuario();


        return view;
    }


// =====================================================
// SELECCIONAR IMAGEN
// =====================================================

    private void seleccionarImagen() {

        Intent intent =
                new Intent(
                        Intent.ACTION_OPEN_DOCUMENT
                );


        intent.addCategory(
                Intent.CATEGORY_OPENABLE
        );


        intent.setType(
                "image/*"
        );


        intent.addFlags(
                Intent.FLAG_GRANT_READ_URI_PERMISSION
                        | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
        );


        selectorImagen.launch(
                intent
        );
    }


// =====================================================
// CARGAR USUARIO
// =====================================================

    private void cargarInformacionUsuario() {

        if (
                auth.getCurrentUser() == null
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
                correoUsuario == null
                        || correoUsuario.trim().isEmpty()
        ) {

            Toast.makeText(
                    requireContext(),
                    "El usuario no tiene correo asociado",
                    Toast.LENGTH_LONG
            ).show();

            return;
        }


        txtCorreo.setText(
                correoUsuario
        );


        usuariosRef
                .orderByChild("correo")
                .equalTo(correoUsuario)
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
                                            "No se encontró el usuario en Realtime Database",
                                            Toast.LENGTH_LONG
                                    ).show();

                                    return;
                                }


                                for (
                                        DataSnapshot usuarioSnapshot
                                        : snapshot.getChildren()
                                ) {

                                    idUsuario =
                                            usuarioSnapshot.getKey();


                                    if (
                                            idUsuario == null
                                    ) {

                                        return;
                                    }


                                    cargarDatosPersonales(
                                            usuarioSnapshot
                                    );


                                    cargarResumenEnergetico(
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
                                        "Error al cargar el usuario: "
                                                + error.getMessage(),
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        }
                );
    }


// =====================================================
// CARGAR DATOS PERSONALES
// =====================================================

    private void cargarDatosPersonales(
            DataSnapshot usuarioSnapshot
    ) {

        String nombre =
                usuarioSnapshot
                        .child("nombre")
                        .getValue(String.class);


        if (
                nombre == null
                        || nombre.trim().isEmpty()
        ) {

            nombre =
                    "Nombre no registrado";
        }


        txtNombre.setText(
                nombre
        );


        edtNombreCompleto.setText(
                nombre
        );


        txtIdUsuario.setText(
                idUsuario
        );


        // =================================================
        // FOTO DE PERFIL
        // =================================================

        String fotoPerfil =
                usuarioSnapshot
                        .child("fotoPerfil")
                        .getValue(String.class);


        if (
                fotoPerfil != null
                        && !fotoPerfil.trim().isEmpty()
        ) {

            Glide.with(
                            requireContext()
                    )
                    .load(
                            fotoPerfil
                    )
                    .placeholder(
                            R.drawable.user
                    )
                    .error(
                            R.drawable.user
                    )
                    .into(
                            imgPerfil
                    );

        } else {

            imgPerfil.setImageResource(
                    R.drawable.user
            );
        }
    }


// =====================================================
// RESUMEN ENERGÉTICO
// =====================================================

    private void cargarResumenEnergetico(
            DataSnapshot usuarioSnapshot
    ) {

        // =================================================
        // ELECTRODOMÉSTICOS
        // =================================================

        long cantidadElectrodomesticos =
                usuarioSnapshot
                        .child("electrodomesticos")
                        .getChildrenCount();


        txtElectrodomesticosPerfil.setText(
                String.valueOf(
                        cantidadElectrodomesticos
                )
        );


        // =================================================
        // ÚLTIMO RECIBO
        // =================================================

        DataSnapshot recibosSnapshot =
                usuarioSnapshot
                        .child("recibos");


        DataSnapshot ultimoRecibo =
                null;


        long fechaMasReciente =
                0;


        for (
                DataSnapshot recibo
                : recibosSnapshot.getChildren()
        ) {

            Long fechaRegistro =
                    recibo
                            .child("fechaRegistro")
                            .getValue(Long.class);


            if (
                    fechaRegistro != null
                            && fechaRegistro
                            > fechaMasReciente
            ) {

                fechaMasReciente =
                        fechaRegistro;


                ultimoRecibo =
                        recibo;
            }
        }


        if (
                ultimoRecibo != null
        ) {

            String consumo =
                    ultimoRecibo
                            .child("consumo")
                            .getValue(String.class);


            if (
                    consumo != null
            ) {

                txtConsumoPerfil.setText(
                        consumo
                );

            } else {

                txtConsumoPerfil.setText(
                        "0 kWh"
                );
            }

        } else {

            txtConsumoPerfil.setText(
                    "0 kWh"
            );
        }
    }


// =====================================================
// GUARDAR CAMBIOS
// =====================================================

    private void guardarCambios() {

        if (
                idUsuario == null
                        || idUsuario.trim().isEmpty()
        ) {

            Toast.makeText(
                    requireContext(),
                    "No se encontró el ID del usuario",
                    Toast.LENGTH_LONG
            ).show();

            return;
        }


        String nuevoNombre =
                edtNombreCompleto
                        .getText()
                        .toString()
                        .trim();


        if (
                nuevoNombre.isEmpty()
        ) {

            edtNombreCompleto.setError(
                    "Ingresa tu nombre completo"
            );

            edtNombreCompleto.requestFocus();

            return;
        }


        btnGuardarCambios.setEnabled(
                false
        );


        // =================================================
        // ACTUALIZAR NOMBRE EN REALTIME DATABASE
        // =================================================

        usuariosRef
                .child(idUsuario)
                .child("nombre")
                .setValue(nuevoNombre)

                .addOnSuccessListener(
                        unused -> {

                            txtNombre.setText(
                                    nuevoNombre
                            );


                            if (
                                    imagenSeleccionada != null
                            ) {

                                subirFotografia();

                            } else {

                                finalizarEdicion();
                            }
                        }
                )

                .addOnFailureListener(
                        e -> {

                            btnGuardarCambios.setEnabled(
                                    true
                            );


                            Toast.makeText(
                                    requireContext(),
                                    "Error al actualizar el nombre: "
                                            + e.getMessage(),
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                );
    }


// =====================================================
// SUBIR FOTOGRAFÍA
// =====================================================

    private void subirFotografia() {

        if (
                imagenSeleccionada == null
        ) {

            finalizarEdicion();

            return;
        }


        // =================================================
        // OBTENER TIPO DE IMAGEN
        // =================================================

        String tipoMime =
                requireContext()
                        .getContentResolver()
                        .getType(
                                imagenSeleccionada
                        );


        String extension =
                ".jpg";


        if (
                tipoMime != null
        ) {

            if (
                    tipoMime.equals(
                            "image/png"
                    )
            ) {

                extension =
                        ".png";

            } else if (
                    tipoMime.equals(
                            "image/jpeg"
                    )
                            || tipoMime.equals(
                            "image/jpg"
                    )
            ) {

                extension =
                        ".jpg";
            }
        }


        // =================================================
        // REFERENCIA A STORAGE
        // =================================================

        StorageReference fotoRef =
                storageRef
                        .child(
                                idUsuario
                                        + extension
                        );


        // =================================================
        // SUBIR IMAGEN
        // =================================================

        fotoRef
                .putFile(
                        imagenSeleccionada
                )

                .addOnSuccessListener(
                        taskSnapshot -> {


                            // Obtener URL
                            fotoRef
                                    .getDownloadUrl()

                                    .addOnSuccessListener(
                                            uri -> {


                                                // =================================================
                                                // CREAR FOTO EN REALTIME DATABASE
                                                // =================================================

                                                usuariosRef
                                                        .child(idUsuario)
                                                        .child("fotoPerfil")
                                                        .setValue(
                                                                uri.toString()
                                                        )

                                                        .addOnSuccessListener(
                                                                unused -> {

                                                                    Toast.makeText(
                                                                            requireContext(),
                                                                            "Fotografía guardada correctamente",
                                                                            Toast.LENGTH_LONG
                                                                    ).show();


                                                                    finalizarEdicion();
                                                                }
                                                        )

                                                        .addOnFailureListener(
                                                                e -> {

                                                                    btnGuardarCambios.setEnabled(
                                                                            true
                                                                    );


                                                                    Toast.makeText(
                                                                            requireContext(),
                                                                            "Error al guardar la URL en Realtime Database: "
                                                                                    + e.getMessage(),
                                                                            Toast.LENGTH_LONG
                                                                    ).show();
                                                                }
                                                        );
                                            }
                                    )

                                    .addOnFailureListener(
                                            e -> {

                                                btnGuardarCambios.setEnabled(
                                                        true
                                                );


                                                Toast.makeText(
                                                        requireContext(),
                                                        "Error al obtener la URL de la imagen: "
                                                                + e.getMessage(),
                                                        Toast.LENGTH_LONG
                                                ).show();
                                            }
                                    );
                        }
                )

                .addOnFailureListener(
                        e -> {

                            btnGuardarCambios.setEnabled(
                                    true
                            );


                            Toast.makeText(
                                    requireContext(),
                                    "Error al subir la imagen a Firebase Storage: "
                                            + e.getMessage(),
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                );
    }


// =====================================================
// FINALIZAR EDICIÓN
// =====================================================

    private void finalizarEdicion() {

        edtNombreCompleto.setEnabled(
                false
        );


        btnCambiarFoto.setEnabled(
                false
        );


        btnGuardarCambios.setEnabled(
                false
        );


        imagenSeleccionada =
                null;


        Toast.makeText(
                requireContext(),
                "Perfil actualizado correctamente",
                Toast.LENGTH_LONG
        ).show();
    }

}
