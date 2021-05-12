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


    private ArrayList votaciones;
    private AdminDbHelper dbHelper;
    private Votacion votacion;


    public SQLiteAdmin(Context context) {

        dbHelper = new AdminDbHelper(context);

        //para no duplicar el guardado de datos ////mejoara
        if (comprobarTablaLlena() == 0) {
            //guardarDatos();
        }
        leerDatos();

    }


    public ArrayList getVotaciones() {
        return votaciones;
    }

    public int comprobarTablaLlena() {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String query = "SELECT * FROM " + Contract.Entry.TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();
        cursor.close();
        return count;

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

    public void leerDatos() {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                BaseColumns._ID,
                Contract.Entry.NOMBRE_EQUIPO,

        };

        Cursor cursor = db.query(
                Contract.Entry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all) projection
                null,              // The columns for the WHERE clause  //selection
                null,          // The values for the WHERE clause //selectionArgs
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order  //sortOrder
        );

        votaciones = new ArrayList<>();


        while (cursor.moveToNext()) {

            String nombreEquipo = cursor.getString((cursor.getColumnIndex(Contract.Entry.NOMBRE_EQUIPO)));

            votaciones.add(nombreEquipo);

        }
        cursor.close();
    }

}
