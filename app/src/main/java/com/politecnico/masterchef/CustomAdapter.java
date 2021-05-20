package com.politecnico.masterchef;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder>  {

    ArrayList<Evento> listadoEventos;
    private final Context context;
    String usuario;
    RequestQueue requestQueue;

    public CustomAdapter(Context context, ArrayList<Evento> listadoEventos) {
        this.context = context;
        this.listadoEventos = listadoEventos;
        SharedPreferences preferences = context.getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
        usuario = preferences.getString("id", "");
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowlayout, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // set the data in items
        new NukeSSLCerts().nuke();

        Evento evento1 = listadoEventos.get(position);
        holder.nombre.setText(evento1.getNombre());


        String fecha1 = evento1.getFecha();
        String[] partes = fecha1.split("-");
        holder.fecha.setText(partes[2]+"/"+partes[1]+"/"+partes[0]);
        holder.hora.setText(evento1.getHora());
        holder.descripcion.setText(evento1.getDescripcion());
        if (evento1.getEstado().equals("En curso")){
            //holder.itemView.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.md_green_A200)));
            holder.itemView.setBackgroundResource(R.drawable.borde_verde);

        } else if (evento1.getEstado().equals("Finalizado")){
            //holder.itemView.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.grey_300)));
            holder.itemView.setBackgroundResource(R.drawable.bordes_redondos);
            holder.apuntarse.setVisibility(View.GONE);
            holder.cancelar.setVisibility(View.GONE);
            //holder.apuntarse.setEnabled(false);
            //holder.cancelar.setEnabled(false);
        }

        holder.apuntarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apuntarJuez("https://politecnico-estella.ddns.net:10443/masterchef_01/php/apuntarseJuez.php", usuario, evento1.getIdEvento());
                //Toast t= Toast.makeText(context, "Solicitada participacion: "+usuario, Toast.LENGTH_SHORT);
                //t.show();
            }
        });


        holder.cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anularParticipacionJuez("https://politecnico-estella.ddns.net:10443/masterchef_01/php/anularParticipacion.php", usuario, evento1.getIdEvento());
                //Toast t= Toast.makeText(context, "Cancelada participacion", Toast.LENGTH_SHORT);
                //t.show();
            }
        });

        // implement setOnClickListener event on item view.
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // display a toast with person name on item click
                //Toast.makeText(context, "prueba", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(context.getApplicationContext(), DetallesEventoActivity.class);

                //seleccionamos el evento
                Evento  eventoSeleccionado = listadoEventos.get(position);

                //pasar contenido por intent// clase evento implement serializable para pasar objetos
                i.putExtra("evento", (Serializable) eventoSeleccionado);

                context.startActivity(i);
            }
        });



    }


    @Override
    public int getItemCount() {
        return listadoEventos.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nombre, fecha, hora, descripcion;// init the item view's
        Button apuntarse, cancelar;

        public MyViewHolder(View itemView) {
            super(itemView);

            // get the reference of item view's
            nombre = (TextView) itemView.findViewById(R.id.nombre);
            fecha = (TextView) itemView.findViewById(R.id.fecha);
            hora = (TextView) itemView.findViewById(R.id.hora);
            descripcion = (TextView) itemView.findViewById(R.id.descripcion);

            apuntarse = (Button) itemView.findViewById(R.id.btnApuntarse);
            cancelar = (Button) itemView.findViewById(R.id.btnCancelarParticipacion);

        }
    }

    private void apuntarJuez(String URL, String idjuez, String idevento) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(context, "Juez Apuntado al Evento", Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(context, volleyError.toString(), Toast.LENGTH_LONG).show();
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
        requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    private void anularParticipacionJuez(String URL, String idjuez, String idevento) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(context, "Cancelada participacion en el Evento", Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(context, volleyError.toString(), Toast.LENGTH_LONG).show();
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
        requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
}