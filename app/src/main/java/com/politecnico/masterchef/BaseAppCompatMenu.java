package com.politecnico.masterchef;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;



//clase que extiende la clase AppCompatActivity añadiendole el menu
public class BaseAppCompatMenu extends AppCompatActivity {


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

            Intent i = new Intent(getApplicationContext(), EventosActivity.class);
            startActivity(i);
            finish();

        }
        return super.onOptionsItemSelected(item);
    }
}
