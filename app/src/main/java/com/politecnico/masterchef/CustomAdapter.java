package com.politecnico.masterchef;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    ArrayList<String> nombres;
    ArrayList<String> direcciones;
    ArrayList<String> poblaciones;
    ArrayList<String> telefonos;
    Context context;

    public CustomAdapter(Context context, ArrayList<String> nombres, ArrayList<String> direcciones, ArrayList<String> poblaciones, ArrayList<String> telefonos) {
        this.context = context;
        this.nombres = nombres;
        this.direcciones = direcciones;
        this.poblaciones = poblaciones;
        this.telefonos = telefonos;
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
        holder.nombre.setText(nombres.get(position));
        holder.direccion.setText(direcciones.get(position));
        holder.poblacion.setText(poblaciones.get(position));
        holder.telefono.setText(telefonos.get(position));

        // implement setOnClickListener event on item view.
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // display a toast with person name on item click
                Toast.makeText(context, telefonos.get(position), Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public int getItemCount() {
        return nombres.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nombre, direccion, poblacion, telefono;// init the item view's

        public MyViewHolder(View itemView) {
            super(itemView);

            // get the reference of item view's
            nombre = (TextView) itemView.findViewById(R.id.nombre);
            direccion = (TextView) itemView.findViewById(R.id.direccion);
            poblacion = (TextView) itemView.findViewById(R.id.poblacion);
            telefono = (TextView) itemView.findViewById(R.id.telefono);

        }
    }
}