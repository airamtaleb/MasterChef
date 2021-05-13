package com.politecnico.masterchef;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.widget.Toast;

import com.politecnico.masterchef.data.AdminDbHelper;
import com.politecnico.masterchef.data.Contract;

import java.util.ArrayList;

public class SQLiteAdmin {


    private AdminDbHelper dbHelper;
    private Votacion votacion;


    public SQLiteAdmin(Context context) {

        dbHelper = new AdminDbHelper(context);

    }



    public Votacion leerDatos(String nombre_equipo, String id_evento){

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String query = "SELECT * FROM " + Contract.Entry.TABLE_NAME + " WHERE " + Contract.Entry.NOMBRE_EQUIPO +" = '"+ nombre_equipo +"'  AND " + Contract.Entry.ID_EVENTO +" = '"+ id_evento +"'" ;
        Cursor cursor = db.rawQuery(query, null);

        Votacion votacion = new Votacion();

        while (cursor.moveToNext()) {

            votacion.setNombre_equipo(cursor.getString((cursor.getColumnIndex(Contract.Entry.NOMBRE_EQUIPO))));
            votacion.setId_juez(cursor.getString((cursor.getColumnIndex(Contract.Entry.ID_JUEZ))));
            votacion.setId_evento(cursor.getString((cursor.getColumnIndex(Contract.Entry.ID_EVENTO))));
            votacion.setPresentacion(cursor.getString((cursor.getColumnIndex(Contract.Entry.PRESENTACION))));
            votacion.setServicio(cursor.getString((cursor.getColumnIndex(Contract.Entry.SERVICIO))));
            votacion.setSabor(cursor.getString((cursor.getColumnIndex(Contract.Entry.SABOR))));
            votacion.setImagen(cursor.getString((cursor.getColumnIndex(Contract.Entry.IMAGEN))));
            votacion.setTriptico(cursor.getString((cursor.getColumnIndex(Contract.Entry.TRIPTICO))));

        }
        cursor.close();

        return votacion;
    }

    public void guardarDatos(Votacion votacion) {

        this.votacion = votacion;

        // Gets the data repository in write mode
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(Contract.Entry.NOMBRE_EQUIPO, votacion.getNombre_equipo());
        values.put(Contract.Entry.ID_JUEZ, votacion.getId_juez());
        values.put(Contract.Entry.ID_EVENTO, votacion.getId_evento());
        values.put(Contract.Entry.PRESENTACION, votacion.getPresentacion());
        values.put(Contract.Entry.SERVICIO, votacion.getServicio());
        values.put(Contract.Entry.SABOR, votacion.getSabor());
        values.put(Contract.Entry.IMAGEN, votacion.getImagen());
        values.put(Contract.Entry.TRIPTICO, votacion.getTriptico());


        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(Contract.Entry.TABLE_NAME, null, values);

    }

}
