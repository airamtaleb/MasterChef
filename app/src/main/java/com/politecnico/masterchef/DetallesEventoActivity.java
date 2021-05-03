package com.politecnico.masterchef;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class DetallesEventoActivity extends BaseAppCompatMenu {

    TextView txtvNombre ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_evento);

        //obtener el objeto/evento seleccionado
        Evento evento = (Evento) getIntent().getSerializableExtra("evento");

        txtvNombre = findViewById(R.id.textViewNombre);
        System.out.println(evento.getNombre());
        txtvNombre.setText(evento.getNombre().toString());

    }
}