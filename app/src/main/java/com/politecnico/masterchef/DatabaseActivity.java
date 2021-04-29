package com.politecnico.masterchef;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DatabaseActivity extends AppCompatActivity {

    EditText edtCodigo, edtProducto, edtPrecio;
    Button btnBuscar, btnAñadir, btnEditar, btnBorrar;
    String codigo, producto, precio;

    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_base);

        edtCodigo = findViewById(R.id.edtCodigo);
        edtProducto = findViewById(R.id.edtProducto);
        edtPrecio = findViewById(R.id.edtPrecio);

        btnBuscar = findViewById(R.id.btnBuscar);
        btnAñadir = findViewById(R.id.btnAñadir);
        btnEditar = findViewById(R.id.btnEditar);
        btnBorrar = findViewById(R.id.btnBorrar);


        btnAñadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                codigo = edtCodigo.getText().toString();
                producto = edtProducto.getText().toString();
                precio = edtPrecio.getText().toString();

                //añadir mas comprobaciones , solo letras...
                if (!codigo.isEmpty() && !producto.isEmpty() && !precio.isEmpty()) {

                    //insertamos datos//ANDROID ESTUFDIO EMULADOR SIMULAR
                    añadirRegistro("http://10.0.2.2/masterchef/insertar.php");
                    edtCodigo.setText("");
                    edtProducto.setText("");
                    edtPrecio.setText("");


                } else {
                    Toast.makeText(DatabaseActivity.this, "Nos se permiten campos vacios", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                codigo = edtCodigo.getText().toString();
                producto = edtProducto.getText().toString();
                precio = edtPrecio.getText().toString();

                //añadir mas comprobaciones , solo letras...
                if (!codigo.isEmpty() && !producto.isEmpty() && !precio.isEmpty()) {

                    //insertamos datos//ANDROID ESTUFDIO EMULADOR SIMULAR
                    añadirRegistro("http://10.0.2.2/masterchef/editar.php");
                    edtCodigo.setText("");
                    edtProducto.setText("");
                    edtPrecio.setText("");


                } else {
                    Toast.makeText(DatabaseActivity.this, "Nos se permiten campos vacios", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                codigo = edtCodigo.getText().toString();


                //añadir mas comprobaciones , solo letras...
                if (!codigo.isEmpty()) {

                    //buscar datos
                    buscarRegistro("http://10.0.2.2/masterchef/buscar.php?codigo=" + codigo);


                } else {
                    Toast.makeText(DatabaseActivity.this, "Nos se permiten campos vacios", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                codigo = edtCodigo.getText().toString();


                //añadir mas comprobaciones , solo letras...
                if (!codigo.isEmpty()) {

                    //buscar datos
                    borrarRegistro("http://10.0.2.2/masterchef/borrar.php");
                    edtCodigo.setText("");
                    edtProducto.setText("");
                    edtPrecio.setText("");

                } else {
                    Toast.makeText(DatabaseActivity.this, "Nos se permiten campos vacios", Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    private void añadirRegistro(String URL) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                Toast.makeText(DatabaseActivity.this, "Operación exitosa", Toast.LENGTH_LONG).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {


                Toast.makeText(DatabaseActivity.this, volleyError.toString(), Toast.LENGTH_LONG).show();
            }
        }


        ) {
            protected Map<String, String> getParams() throws AuthFailureError {

                //utilizar objetos en vez de String para tener campos con otro tipo de valores//pendiente
                Map<String, String> param = new HashMap<String, String>();
                param.put("codigo", codigo);
                param.put("producto", producto);
                param.put("precio", precio);
                return param;
            }


        };

        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

    private void buscarRegistro(String URL) {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        edtProducto.setText(jsonObject.getString("producto"));
                        edtPrecio.setText(jsonObject.getString("precio"));
                    } catch (JSONException e) {

                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
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

    private void borrarRegistro(String URL) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                Toast.makeText(DatabaseActivity.this, "Operación exitosa", Toast.LENGTH_LONG).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {


                Toast.makeText(DatabaseActivity.this, volleyError.toString(), Toast.LENGTH_LONG).show();
            }
        }


        ) {
            protected Map<String, String> getParams() throws AuthFailureError {

                //utilizar objetos en vez de String para tener campos con otro tipo de valores//pendiente
                Map<String, String> param = new HashMap<String, String>();
                param.put("codigo", codigo);

                return param;
            }


        };

        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }
}