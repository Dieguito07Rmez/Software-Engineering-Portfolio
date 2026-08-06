package com.example.ecowatt;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ElectrodomesticoAdapter
        extends RecyclerView.Adapter<ElectrodomesticoAdapter.ViewHolder> {


    private List<Electrodomestico> listaElectrodomesticos;


    public ElectrodomesticoAdapter(
            List<Electrodomestico> listaElectrodomesticos
    ) {

        this.listaElectrodomesticos =
                listaElectrodomesticos;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view =
                LayoutInflater
                        .from(
                                parent.getContext()
                        )
                        .inflate(
                                R.layout.item_electrodomestico,
                                parent,
                                false
                        );


        return new ViewHolder(
                view
        );
    }


    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position
    ) {

        Electrodomestico electrodomestico =
                listaElectrodomesticos.get(
                        position
                );


        holder.txtTipo.setText(
                electrodomestico.getTipo()
        );


        holder.txtMarca.setText(
                "Marca: "
                        + electrodomestico.getMarca()
        );


        holder.txtVoltaje.setText(
                "Voltaje: "
                        + electrodomestico.getVoltaje()
                        + " V"
        );


        holder.txtPotencia.setText(
                "Potencia: "
                        + electrodomestico.getPotencia()
                        + " W"
        );


        holder.txtCantidad.setText(
                "Cantidad: "
                        + electrodomestico.getCantidad()
        );


        holder.txtHorasUso.setText(
                "Uso diario: "
                        + electrodomestico.getHorasUso()
                        + " horas"
        );
    }


    @Override
    public int getItemCount() {

        return listaElectrodomesticos.size();
    }


    public static class ViewHolder
            extends RecyclerView.ViewHolder {


        TextView txtTipo;
        TextView txtMarca;
        TextView txtVoltaje;
        TextView txtPotencia;
        TextView txtCantidad;
        TextView txtHorasUso;


        public ViewHolder(
                @NonNull View itemView
        ) {

            super(
                    itemView
            );


            txtTipo =
                    itemView.findViewById(
                            R.id.txtTipo
                    );


            txtMarca =
                    itemView.findViewById(
                            R.id.txtMarca
                    );


            txtVoltaje =
                    itemView.findViewById(
                            R.id.txtVoltaje
                    );


            txtPotencia =
                    itemView.findViewById(
                            R.id.txtPotencia
                    );


            txtCantidad =
                    itemView.findViewById(
                            R.id.txtCantidad
                    );


            txtHorasUso =
                    itemView.findViewById(
                            R.id.txtHorasUso
                    );
        }
    }
}