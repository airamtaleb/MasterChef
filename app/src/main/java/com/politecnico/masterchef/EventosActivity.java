package com.politecnico.masterchef;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class EventosActivity extends AppCompatActivity {

    Button btnLogout , btnAcces;

    // ArrayList for person names, email Id's and mobile numbers
    ArrayList<String> nombres = new ArrayList<>();
    ArrayList<String> direcciones = new ArrayList<>();
    ArrayList<String> poblaciones = new ArrayList<>();
    ArrayList<String> telefonos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventos);

        btnLogout=findViewById(R.id.btnLogout);
        btnAcces= findViewById(R.id.btnAcces);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
                preferences.edit().clear().commit();


                finish();
            }
        });

        btnAcces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), DatabaseActivity.class);
                startActivity(i);
            }
        });


        // get the reference of RecyclerView
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        // set a LinearLayoutManager with default vertical orientation
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);


        try {
            // get JSONObject from JSON file
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            // fetch JSONArray named users
            JSONArray userArray = obj.getJSONArray("ITEMS");
            // implement for loop for getting users list data
            for (int i = 0; i < userArray.length(); i++) {
                // create a JSONObject for fetching single user data
                JSONObject userDetail = userArray.getJSONObject(i);
                // fetch email and name and store it in arraylist
                nombres.add(userDetail.getString("NOMBRE"));
                direcciones.add(userDetail.getString("DIRECCIÓN "));
                // create a object for getting contact data from JSONObject
                // JSONObject contact = userDetail.getJSONObject("contact");
                // fetch mobile number and store it in arraylist
                //mobileNumbers.add(contact.getString("mobile"));
                poblaciones.add(userDetail.getString("LOCALIDAD"));
                telefonos.add(userDetail.getString("TELÉFONO"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //  call the constructor of CustomAdapter to send the reference and data to Adapter
        CustomAdapter customAdapter = new CustomAdapter(EventosActivity.this, nombres, direcciones, poblaciones, telefonos);
        recyclerView.setAdapter(customAdapter); // set the Adapter to RecyclerView
    }

    private void setSupportActionBar(Toolbar myToolbar) {
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("CentrosSanitarios.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
