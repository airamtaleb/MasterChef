package com.politecnico.masterchef;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.politecnico.masterchef.data.AdminDbHelper;
import com.politecnico.masterchef.data.Contract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SQLiteAdmin {


    private AdminDbHelper dbHelper;
    private Votacion votacion;


    public SQLiteAdmin(Context context) {

        dbHelper = new AdminDbHelper(context);

    }


    public JSONArray cargarVotaciones(String id_evento, String id_juez) {


        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String query = "SELECT * FROM " + Contract.Entry.TABLE_NAME + " WHERE " + Contract.Entry.ID_EVENTO + " = '" + id_evento + "' AND " + Contract.Entry.ID_JUEZ + " = '" + id_juez + "'";
        Cursor cursor = db.rawQuery(query, null);


        JSONArray array = new JSONArray();


        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {


                while (cursor.moveToNext()) {
                    JSONObject object = new JSONObject();
                    try {


                        object.put("Presentacion", cursor.getString((cursor.getColumnIndex(Contract.Entry.PRESENTACION))));
                        object.put("Servicio", cursor.getString((cursor.getColumnIndex(Contract.Entry.SERVICIO))));
                        object.put("Sabor", cursor.getString((cursor.getColumnIndex(Contract.Entry.SABOR))));
                        object.put("Imagen", cursor.getString((cursor.getColumnIndex(Contract.Entry.IMAGEN))));
                        object.put("Triptico", cursor.getString((cursor.getColumnIndex(Contract.Entry.TRIPTICO))));
                        object.put("ID_juez", cursor.getString((cursor.getColumnIndex(Contract.Entry.ID_JUEZ))));
                        object.put("ID_evento", cursor.getString((cursor.getColumnIndex(Contract.Entry.ID_EVENTO))));
                        object.put("Nombre_equipo", cursor.getString((cursor.getColumnIndex(Contract.Entry.NOMBRE_EQUIPO))));

                        array.put(object);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


            }

        }

        cursor.close();



        return array;
    }


    public Votacion leerDatos(String nombre_equipo, String id_evento, String id_juez) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String query = "SELECT * FROM " + Contract.Entry.TABLE_NAME + " WHERE " + Contract.Entry.NOMBRE_EQUIPO + " = '" + nombre_equipo + "'  AND " + Contract.Entry.ID_EVENTO + " = '" + id_evento + "' AND " + Contract.Entry.ID_JUEZ + " = '" + id_juez + "'";
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
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String query = "SELECT * FROM " + Contract.Entry.TABLE_NAME + " WHERE " + Contract.Entry.NOMBRE_EQUIPO + " = '" + votacion.getNombre_equipo() + "'  AND " + Contract.Entry.ID_EVENTO + " = '" + votacion.getId_evento() + "'";
        Cursor cursor = db.rawQuery(query, null);

        int count = cursor.getCount();

        if (count == 0) {


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

        } else {


            //db.execSQL(); "SELECT * FROM " + Contract.Entry.TABLE_NAME + " WHERE " + Contract.Entry.NOMBRE_EQUIPO +" = '"+ votacion.getNombre_equipo() +"'  AND " + Contract.Entry.ID_EVENTO +" = '"+ votacion.getId_evento() +"'" ;

            db.execSQL("UPDATE " + Contract.Entry.TABLE_NAME + " SET "
                    + Contract.Entry.PRESENTACION + " = '" + votacion.getPresentacion() + "' , "
                    + Contract.Entry.SERVICIO + " = '" + votacion.getServicio() + "' ,"
                    + Contract.Entry.SABOR + " = '" + votacion.getSabor() + "' ,"
                    + Contract.Entry.IMAGEN + " = '" + votacion.getImagen() + "' ,"
                    + Contract.Entry.TRIPTICO + " = '" + votacion.getTriptico() + "' "
                    + " WHERE " + Contract.Entry.NOMBRE_EQUIPO + " = '" + votacion.getNombre_equipo() + "'  AND " + Contract.Entry.ID_EVENTO + " = '" + votacion.getId_evento() + "' AND " + Contract.Entry.ID_JUEZ + " = '" + votacion.getId_juez() + "'");


        }
    }

}
