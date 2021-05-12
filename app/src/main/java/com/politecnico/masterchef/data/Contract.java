package com.politecnico.masterchef.data;

import android.provider.BaseColumns;

public class Contract {

    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private Contract() {}

    /* Inner class that defines the table contents */
    public static class Entry implements BaseColumns {
        public static final String TABLE_NAME = "votacionesTable";
        public static final String NOMBRE_EQUIPO = "nombre_equipo";
        public static final String ID_EVENTO = "id_evento";
        public static final String ID_JUEZ= "id_juez";
        public static final String PRESENTACION= "presentacion";
        public static final String SERVICIO = "servicio";
        public static final String SABOR = "sabor";
        public static final String IMAGEN = "imagen";
        public static final String TRIPTICO = "triptico";
    }
}
