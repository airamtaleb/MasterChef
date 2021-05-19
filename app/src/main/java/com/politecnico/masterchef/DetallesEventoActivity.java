package com.politecnico.masterchef;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
        String fecha1 = evento.getFecha();
        String[] partes = fecha1.split("-");

        txtvFecha.setText(getText(R.string.fecha) + partes[2]+"/"+partes[1]+"/"+partes[0]);
        txtvHora.setText(getText(R.string.hora)+ evento.getHora());
        txtvEstado.setText(getText(R.string.estado)+ evento.getEstado());
        txtvDescripcion.setText(getText(R.string.descripcion)+ evento.getDescripcion());
        txtvLugar.setText(getText(R.string.lugar)+ evento.getLugar());

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
            btnApuntarse.setVisibility(View.GONE);
            btnCancelarParticipacion.setVisibility(View.GONE);
            //btnApuntarse.setEnabled(false);
            //btnCancelarParticipacion.setEnabled(false);
            btnAccederEvento.setText(R.string.ver_resultados);
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
                        Toast.makeText(DetallesEventoActivity.this, R.string.juez_validado_para_el_evento, Toast.LENGTH_LONG).show();
                        //pasar contenido por intent// clase evento implement serializable para pasar objetos
                        comprobarRealizada("http://10.0.2.2/masterchef/comprobarVotacionRealizada.php?idevento="+ idevento+"&idjuez="+idjuez, idevento, evento);
                        //Intent i = new Intent(getApplicationContext(), VotacionActivity.class);
                        //i.putExtra("id_evento", idevento);
                        //i.putExtra("evento", evento);
                        //i.putExtra("estadoVoto", true);
                        //startActivity(i);
                    } else if (estado.equals("En espera")) {
                        Toast.makeText(DetallesEventoActivity.this, R.string.autorizacion_sin_confirmar, Toast.LENGTH_LONG).show();
                    } else if (estado.equals("Denegado")) {
                        Toast.makeText(DetallesEventoActivity.this, R.string.juez_no_validado_en_el_evento, Toast.LENGTH_LONG).show();
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
                Toast.makeText(DetallesEventoActivity.this, R.string.juez_apuntado_al_evento, Toast.LENGTH_LONG).show();
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
                Toast.makeText(DetallesEventoActivity.this, R.string.cancelada_participacion_al_evento, Toast.LENGTH_LONG).show();
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

    private void comprobarRealizada(String URL, String idevento, Evento evento) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,URL,null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (0 < response.length()) {
                    Intent i = new Intent(getApplicationContext(), VotacionActivity.class);
                    i.putExtra("id_evento", idevento);
                    i.putExtra("evento", evento);
                    i.putExtra("estadoVoto", true);
                    startActivity(i);
                } else {
                    Intent i = new Intent(getApplicationContext(), VotacionActivity.class);
                    i.putExtra("id_evento", idevento);
                    i.putExtra("evento", evento);
                    i.putExtra("estadoVoto", false);
                    startActivity(i);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), R.string.error_de_conexion, Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }
}