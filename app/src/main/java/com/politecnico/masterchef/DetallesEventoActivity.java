package com.politecnico.masterchef;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class DetallesEventoActivity extends BaseAppCompatMenu {

    TextView txtvNombre, txtvFecha ,txtvHora ,txtvEstado ,txtvDescripcion ,txtvLugar ;

    Button btnVolverListado, btnAccederEvento, btnApuntarse, btnCancelarParticipacion;

    RequestQueue requestQueue;

    String usuario;

    LinearLayout panelEvento;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_evento);

        panelEvento = findViewById(R.id.panelEvento);

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
        txtvFecha.setText(" Fecha: " +evento.getFecha());
        txtvHora.setText(" Hora: "+evento.getHora());
        txtvEstado.setText(" Estado: "+evento.getEstado());
        txtvDescripcion.setText(" Descripción: "+evento.getDescripcion());
        txtvLugar.setText(" Lugar: "+evento.getLugar());

        btnAccederEvento = findViewById(R.id.btnAccederEvento);
        btnApuntarse = findViewById(R.id.btnApuntarse);
        btnCancelarParticipacion = findViewById(R.id.btnCancelarParticipacion);
        btnVolverListado = findViewById(R.id.btnVolverListado);

        btnVolverListado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), EventosActivity.class);
                startActivity(i);
            }
        });

        if (evento.getEstado().equals("En curso")){

            //panelEvento.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(DetallesEventoActivity.this, R.color.md_green_A200)));
            panelEvento.setBackgroundResource(R.drawable.borde_verde);
            //panelEvento.setBackgroundResource(R.color.md_green_A200);


            btnApuntarse.setEnabled(true);
            btnCancelarParticipacion.setEnabled(true);
            btnAccederEvento.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //seleccionamos el evento
                    String idevento = evento.getIdEvento()+"";
                    validarJuez("http://10.0.2.2/masterchef/validarJuezEvento.php",  usuario, idevento, evento );
                }
            });
        } else if (evento.getEstado().equals("Finalizado")){

            panelEvento.setBackgroundResource(R.drawable.borde_gris);

            btnApuntarse.setEnabled(false);
            btnCancelarParticipacion.setEnabled(false);
            btnAccederEvento.setText("Ver resultados");
            btnAccederEvento.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //seleccionamos el evento
                    String idevento = evento.getIdEvento()+"";
                    String estadoEvento = evento.getEstado();
                    Intent i = new Intent(getApplicationContext(), VotacionActivity.class);
                    i.putExtra("id_evento", idevento);
                    i.putExtra("evento", evento);
                    startActivity(i);
                }
            });
        }

        btnApuntarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apuntarJuez("http://10.0.2.2/masterchef/apuntarseJuez.php", usuario, evento.getIdEvento());
            }
        });

        btnCancelarParticipacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anularParticipacionJuez("http://10.0.2.2/masterchef/anularParticipacion.php", usuario, evento.getIdEvento());
            }
        });

    }

    private void validarJuez(String URL, String idjuez, String idevento, Evento evento) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.isEmpty()) {
                    String estado="";
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        estado = jsonObject.getString("Solicitud");
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    if (estado.equals("Admitido")) {
                        Toast.makeText(DetallesEventoActivity.this, "Juez validado para el evento", Toast.LENGTH_LONG).show();
                        //pasar contenido por intent// clase evento implement serializable para pasar objetos
                        Intent i = new Intent(getApplicationContext(), VotacionActivity.class);
                        i.putExtra("id_evento", idevento);
                        i.putExtra("evento", evento);
                        startActivity(i);
                    } else if (estado.equals("En espera")) {
                        Toast.makeText(DetallesEventoActivity.this, "Autorizacion en sin confirmar", Toast.LENGTH_LONG).show();
                    } else if (estado.equals("Denegado")) {
                        Toast.makeText(DetallesEventoActivity.this, "Juez NO autorizado en el evento", Toast.LENGTH_LONG).show();
                    }
                }else {
                    //Toast.makeText(DetallesEventoActivity.this, "Juez no autorizado en el evento", Toast.LENGTH_LONG).show();
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

    private void apuntarJuez(String URL, String idjuez, String idevento) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(DetallesEventoActivity.this, "Juez Apuntado al Evento", Toast.LENGTH_LONG).show();
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
        requestQueue = Volley.newRequestQueue(DetallesEventoActivity.this);
        requestQueue.add(stringRequest);
    }

    private void anularParticipacionJuez(String URL, String idjuez, String idevento) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(DetallesEventoActivity.this, "Cancelada participacion en el Evento", Toast.LENGTH_LONG).show();
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
        requestQueue = Volley.newRequestQueue(DetallesEventoActivity.this);
        requestQueue.add(stringRequest);
    }
}