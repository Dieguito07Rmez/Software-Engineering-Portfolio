package com.example.login_clase_30_junio;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DatabaseError;
import android.graphics.Color;

import com.google.android.material.card.MaterialCardView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity {

    private TextView txtTemperatura;
    private TextView txtHumedad;
    private TextView txtEstado;
    private TextView txtVentilador;
    private TextView txtAlarma;
    private TextView txtConexion;
    private TextView txtHora;
    private MaterialCardView cardEstado;
    private MaterialCardView cardVentilador;
    private MaterialCardView cardAlarma;
    private LineChart chartTemperatura;

    private LineChart chartHumedad;

    private ArrayList<Entry> listaHumedad =
            new ArrayList<>();

    private LineDataSet dataSetHumedad;

    private LineData lineDataHumedad;


    private ArrayList<Entry> listaTemperaturas =
            new ArrayList<>();

    private LineDataSet dataSet;
    private LineData lineData;
    private int contador = 0;

    Button siguiente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        txtTemperatura = findViewById(R.id.txtTemperatura);
        txtHumedad = findViewById(R.id.txtHumedad);
        txtEstado = findViewById(R.id.txtEstado);
        txtVentilador = findViewById(R.id.txtVentilador);
        txtAlarma = findViewById(R.id.txtAlarma);
        txtConexion = findViewById(R.id.txtConexion);
        txtHora = findViewById(R.id.txtHora);
        cardEstado = findViewById(R.id.cardEstado);
        cardVentilador = findViewById(R.id.cardVentilador);
        cardAlarma = findViewById(R.id.cardAlarma);
        siguiente = findViewById(R.id.btnsiguiente);


        siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, RegistroActivity.class);
                startActivity(intent);
                finish();
            }
        });

        chartTemperatura =
                findViewById(R.id.chartTemperatura);

        chartHumedad =
                findViewById(R.id.chartHumedad);

        configurarGrafica();
        configurarGraficaHumedad();

        DatabaseReference referencia =
                FirebaseDatabase.getInstance().getReference("sensor");

        referencia.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {

                SensorData sensor = snapshot.getValue(SensorData.class);

                if(sensor != null){

                    txtTemperatura.setText(
                            String.format(Locale.getDefault(),
                                    "%.1f °C",
                                    sensor.temperatura)
                    );

                    listaTemperaturas.add(

                            new Entry(
                                    contador,
                                    (float) sensor.temperatura
                            )

                    );

                    listaHumedad.add(

                            new Entry(
                                    contador,
                                    (float) sensor.humedad
                            )

                    );

                    contador++;

                    if(listaTemperaturas.size() > 20){

                        listaTemperaturas.remove(0);

                        for(int i = 0; i < listaTemperaturas.size(); i++){

                            listaTemperaturas.get(i).setX(i);

                        }

                        contador = listaTemperaturas.size();

                    }

                    if(listaHumedad.size() > 20){

                        listaHumedad.remove(0);

                        for(int i = 0; i < listaHumedad.size(); i++){

                            listaHumedad.get(i).setX(i);

                        }

                    }

                    dataSet.notifyDataSetChanged();

                    lineData.notifyDataChanged();

                    chartTemperatura.notifyDataSetChanged();

                    chartTemperatura.invalidate();

                    dataSetHumedad.notifyDataSetChanged();

                    lineDataHumedad.notifyDataChanged();

                    chartHumedad.notifyDataSetChanged();

                    chartHumedad.invalidate();

                    txtHumedad.setText(
                            String.format(Locale.getDefault(),
                                    "%.1f %%", sensor.humedad)
                    );

                    txtEstado.setText(sensor.estado);

                    txtVentilador.setText(
                            sensor.ventilador ?
                                    "ENCENDIDO" :
                                    "APAGADO"
                    );

                    txtAlarma.setText(
                            sensor.alarma ?
                                    "ACTIVADA" :
                                    "DESACTIVADA"
                    );

                    switch (sensor.estado){
                        case "NORMAL":
                            cardEstado.setCardBackgroundColor(Color.parseColor("#C8E6C9"));
                            txtEstado.setTextColor(Color.parseColor("#2E7D32"));
                            break;

                        case "PRECAUCION":
                            cardEstado.setCardBackgroundColor(Color.parseColor("#FFF9C4"));
                            txtEstado.setTextColor(Color.parseColor("#F9A825"));
                            break;

                        case "ALERTA":
                            cardEstado.setCardBackgroundColor(Color.parseColor("#FFE0B2"));
                            txtEstado.setTextColor(Color.parseColor("#EF6C00"));
                            break;

                        case "CRITICA":
                            cardEstado.setCardBackgroundColor(Color.parseColor("#FFCDD2"));
                            txtEstado.setTextColor(Color.parseColor("#C62828"));
                            break;
                    }

                    if(sensor.ventilador){

                        cardVentilador.setCardBackgroundColor(
                                Color.parseColor("#BBDEFB")
                        );

                    }else{

                        cardVentilador.setCardBackgroundColor(
                                Color.WHITE
                        );

                    }

                    if(sensor.alarma){

                        cardAlarma.setCardBackgroundColor(
                                Color.parseColor("#FFCDD2")
                        );

                    }else{

                        cardAlarma.setCardBackgroundColor(
                                Color.parseColor("#C8E6C9")
                        );

                    }

                    SimpleDateFormat formato =
                            new SimpleDateFormat(
                                    "HH:mm:ss",
                                    Locale.getDefault()
                            );

                    txtHora.setText(
                            "Última actualización: "
                                    + formato.format(new Date())
                    );

                    txtConexion.setText("🟢 ESP32 Conectado");

                    txtConexion.setTextColor(
                            Color.parseColor("#2E7D32")
                    );
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    private void configurarGrafica(){

        dataSet = new LineDataSet(
                listaTemperaturas,
                "Temperatura"
        );

        dataSet.setLineWidth(3f);
        dataSet.setCircleRadius(4f);
        dataSet.setValueTextSize(10f);
        lineData = new LineData(dataSet);
        chartTemperatura.setData(lineData);
        Description descripcion = new Description();
        descripcion.setText("Tiempo");
        chartTemperatura.setDescription(descripcion);
        chartTemperatura.invalidate();

    }

    private void configurarGraficaHumedad(){

        dataSetHumedad = new LineDataSet(
                listaHumedad,
                "Humedad"
        );

        dataSetHumedad.setLineWidth(3f);

        dataSetHumedad.setCircleRadius(4f);

        dataSetHumedad.setValueTextSize(10f);

        lineDataHumedad = new LineData(dataSetHumedad);

        chartHumedad.setData(lineDataHumedad);

        Description descripcion = new Description();

        descripcion.setText("Tiempo");

        chartHumedad.setDescription(descripcion);

        chartHumedad.invalidate();

    }
}