package com.politecnico.masterchef;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.security.spec.ECField;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;


public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder>  {

    ArrayList<Evento> listadoEventos;
    Context context;

    public CustomAdapter(Context context, ArrayList<Evento> listadoEventos) {
        this.context = context;
        this.listadoEventos = listadoEventos;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowlayout, parent, false);
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // set the data in items
        Evento evento1 = listadoEventos.get(position);
        holder.nombre.setText(evento1.getNombre());
        holder.fecha.setText(evento1.getFecha());
        holder.hora.setText(evento1.getHora());
        holder.descripcion.setText(evento1.getDescripcion());
        if (evento1.getEstado().equals("En curso")){
            holder.itemView.setBackgroundColor(Color.GREEN);
        } else if (evento1.getEstado().equals("Finalizado")){
            holder.itemView.setBackgroundColor(Color.DKGRAY);
        }


        // implement setOnClickListener event on item view.
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // display a toast with person name on item click
                Toast.makeText(context, "prueba", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(context.getApplicationContext(), DetallesEventoActivity.class);

                //seleccionamos el evento
               Evento  eventoSeleccionado = listadoEventos.get(position);

                //pasar contenido por intent//implement serializable para objetos
                i.putExtra("evento", (Serializable) eventoSeleccionado);


                context.startActivity(i);
            }
        });

    }


    @Override
    public int getItemCount() {
        return listadoEventos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nombre, fecha, hora, descripcion;// init the item view's

        public MyViewHolder(View itemView) {
            super(itemView);

            // get the reference of item view's
            nombre = (TextView) itemView.findViewById(R.id.nombre);
            fecha = (TextView) itemView.findViewById(R.id.fecha);
            hora = (TextView) itemView.findViewById(R.id.hora);
            descripcion = (TextView) itemView.findViewById(R.id.descripcion);

        }
    }
}