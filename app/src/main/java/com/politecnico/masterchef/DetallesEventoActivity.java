package com.politecnico.masterchef;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class DetallesEventoActivity extends BaseAppCompatMenu {

    TextView txtvNombre, txtvFecha ,txtvHora ,txtvEstado ,txtvDescripcion ,txtvLugar ;

    Button btnVolverListado, btnAccederEvento;

    RequestQueue requestQueue;

    String usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_evento);

        SharedPreferences preferences = DetallesEventoActivity.this.getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
        usuario = preferences.getString("id", "");

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
                //seleccionamos el evento
                String idevento = evento.getIdEvento()+"";
                validarJuez("http://10.0.2.2/masterchef/validarJuezEvento.php",  usuario, idevento );



            }
        });

    }

    private void validarJuez(String URL, String idjuez, String idevento) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.isEmpty()) {
                    Toast.makeText(DetallesEventoActivity.this, "Juez validado para el evento", Toast.LENGTH_LONG).show();
                    //pasar contenido por intent// clase evento implement serializable para pasar objetos
                    Intent i = new Intent(getApplicationContext(), VotacionActivity.class);
                    i.putExtra("id_evento", idevento);
                    startActivity(i);
                }else {

                    Toast.makeText(DetallesEventoActivity.this, "Juez no autorizado en el evento", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(DetallesEventoActivity.this, volleyError.toString(), Toast.LENGTH_LONG).show();
            }
        }

        ) {
            protected Map<String, String> getParams() throws AuthFailureError {
                //utilizar objetos en vez de String para tener campos con otro tipo de valores//pendiente
                Map<String, String> param = new HashMap<String, String>();
                param.put("idjuez", idjuez);
                param.put("idevento", idevento);
                return param;
            }
        };
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}