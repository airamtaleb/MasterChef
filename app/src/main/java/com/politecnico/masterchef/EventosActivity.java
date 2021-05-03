package com.politecnico.masterchef;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import android.view.Menu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class EventosActivity extends AppCompatActivity {

    ArrayList<Evento> listadoEventos = new ArrayList<>();;
    //String idEvento, nombre, fecha, hora, estado, descripcion, lugar;

    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventos);



        cargarEventos("http://10.0.2.2/masterchef/cargarEventos.php");
        // get the reference of RecyclerView
        // set the Adapter to RecyclerView


    }



    private void setSupportActionBar(Toolbar myToolbar) {
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
                    }
                    listadoEventos.add(evento);
                }
                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
                // set a LinearLayoutManager with default vertical orientation
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(linearLayoutManager);
                CustomAdapter customAdapter = new CustomAdapter(EventosActivity.this, listadoEventos);
                recyclerView.setAdapter(customAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "ERROR DE CONEXIÓN", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);

    }

    //metodo para mostrar y ocultar menu
    public boolean onCreateOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.overflow, menu);
        return true;
    }


    //metodo para asignar las funciones a las opciones del menu
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.item1) {

            SharedPreferences preferences = getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
            preferences.edit().clear().commit();
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);
            finish();


        }else if(id == R.id.item2) {

            finishAffinity();

        }else if(id == R.id.item3) {

            Intent i = new Intent(getApplicationContext(), DatabaseActivity.class);
            startActivity(i);

        }
        return super.onOptionsItemSelected(item);
    }

}
