package com.politecnico.masterchef;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DetallesEventoActivity extends BaseAppCompatMenu {

    TextView txtvNombre, txtvFecha ,txtvHora ,txtvEstado ,txtvDescripcion ,txtvLugar ;

    Button btnVolverListado, btnAccederEvento;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_evento);

        //obtener el objeto/evento seleccionado
        Evento evento = (Evento) getIntent().getSerializableExtra("evento");

        txtvNombre = findViewById(R.id.textViewNombre);
        txtvFecha = findViewById(R.id.textViewFecha);
        txtvHora = findViewById(R.id.textViewHora);
        txtvEstado = findViewById(R.id.textViewEstado);
        txtvDescripcion = findViewById(R.id.textViewDescripcion);
        txtvLugar = findViewById(R.id.textViewLugar);


        txtvNombre.setText(evento.getNombre());
        txtvFecha.setText(evento.getFecha());
        txtvHora.setText(evento.getHora());
        txtvEstado.setText(evento.getEstado());
        txtvDescripcion.setText(evento.getDescripcion());
        txtvLugar.setText(evento.getLugar());


        btnVolverListado = findViewById(R.id.btnVolverListado);
        btnVolverListado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), EventosActivity.class);
                startActivity(i);
            }
        });

        btnAccederEvento = findViewById(R.id.btnAccederEvento);
        btnAccederEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), VotacionActivity.class);
                startActivity(i);
            }
        });






    }
}