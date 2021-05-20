package com.politecnico.masterchef;


import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.Toast;


import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class EventosActivity extends BaseAppCompatMenu {

    ArrayList<Evento> listadoEventos, listadoEncurso, listadoFinalizado;
    //String idEvento, nombre, fecha, hora, estado, descripcion, lugar;
    RadioGroup radioGroup;
   // RadioButton eventosTodos, eventosEncurso, eventosFinalizado;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new NukeSSLCerts().nuke();
        setContentView(R.layout.activity_eventos);
        radioGroup = (RadioGroup) findViewById(R.id.opcionesVistaEventos);
        //eventosTodos = (RadioButton) findViewById(R.id.eventosTodos);
        //eventosEncurso = (RadioButton) findViewById(R.id.eventosEncurso);
        //eventosFinalizado = (RadioButton) findViewById(R.id.eventosFinalizados);

        listadoEventos = new ArrayList<>();
        listadoEncurso = new ArrayList<>();
        listadoFinalizado = new ArrayList<>();

        cargarEventos("https://politecnico-estella.ddns.net:10443/masterchef_01/php/cargarEventos.php");
        // get the reference of RecyclerView
        // set the Adapter to RecyclerView
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                ArrayList<Evento> listar = new ArrayList<>();
                if(checkedId == R.id.eventosTodos){
                    listar = listadoEventos;
                } else if(checkedId == R.id.eventosEncurso){
                    listar = listadoEncurso;
                } else if(checkedId == R.id.eventosFinalizados) {
                    listar = listadoFinalizado;
                }
                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
                // set a LinearLayoutManager with default vertical orientation
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(linearLayoutManager);
                CustomAdapter customAdapter = new CustomAdapter(EventosActivity.this, listar);
                recyclerView.setAdapter(customAdapter);
            }
        });

    }

    private void cargarEventos(String URL) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,URL,null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i < response.length(); i++) {
                    Evento evento = new Evento();

                    JSONObject jsonObject = null;
                    try {
                        jsonObject = response.getJSONObject(i);
                        evento.setIdEvento(jsonObject.getString("ID_evento"));
                        evento.setNombre(jsonObject.getString("Nombre_evento"));
                        evento.setFecha(jsonObject.getString("Fecha"));
                        evento.setHora(jsonObject.getString("hora"));
                        evento.setEstado(jsonObject.getString("Estado"));
                        evento.setDescripcion(jsonObject.getString("Descripcion"));
                        evento.setLugar(jsonObject.getString("Lugar"));
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                    String estado= evento.getEstado();
                    listadoEventos.add(evento);
                    if (estado.equals("En curso")){
                        listadoEncurso.add(evento);
                    } else if (estado.equals("Finalizado")){
                        listadoFinalizado.add(evento);
                    }
                    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

                    //obtenemos la orientacion
                    int orientation = getResources().getConfiguration().orientation;
                    if(orientation == Configuration.ORIENTATION_PORTRAIT) {

                        // set a LinearLayoutManager with default vertical orientation
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                        recyclerView.setLayoutManager(linearLayoutManager);
                    }else {
                        // lanscape grid

                        Context context = getApplicationContext();
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);

                        recyclerView.setLayoutManager(gridLayoutManager);
                    }

                    CustomAdapter customAdapter = new CustomAdapter(EventosActivity.this, listadoEventos);
                    recyclerView.setAdapter(customAdapter);
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
