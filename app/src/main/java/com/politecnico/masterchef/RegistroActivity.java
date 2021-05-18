package com.politecnico.masterchef;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Map;

public class RegistroActivity extends AppCompatActivity {

    TextInputLayout editNombre, editApellidos, editDepartamento, editIntolerancia, editCorreo, editPassword;
    Button  btnIrLogin, btnRegistro;
    String nombre, apellidos, departamento, intolerancia, correo, password;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        editNombre = findViewById(R.id.editNombre);
        editApellidos = findViewById(R.id.editApellidos);
        editDepartamento = findViewById(R.id.editDepartamento);
        editIntolerancia = findViewById(R.id.editIntolerancia);
        editCorreo = findViewById(R.id.editCorreo);
        editPassword = findViewById(R.id.editPassword);

        btnIrLogin = findViewById(R.id.btnIrLogin);
        btnRegistro = findViewById(R.id.btnRegistro);

        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nombre = editNombre.getEditText().getText().toString();
                apellidos = editApellidos.getEditText().getText().toString();
                departamento = editDepartamento.getEditText().getText().toString();
                intolerancia = editIntolerancia.getEditText().getText().toString();
                correo = editCorreo.getEditText().getText().toString();
                password = editPassword.getEditText().getText().toString();

                if (!nombre.isEmpty() && !apellidos.isEmpty() && !correo.isEmpty() && !password.isEmpty() ) {
                    añadirRegistro("http://10.0.2.2/masterchef/registrarJuez.php");
                    editNombre.getEditText().setText("");
                    editApellidos.getEditText().setText("");
                    editDepartamento.getEditText().setText("");
                    editIntolerancia.getEditText().setText("");
                    editCorreo.getEditText().setText("");
                    editPassword.getEditText().setText("");
                } else {
                    Toast.makeText(RegistroActivity.this, R.string.no_se_permiten_campos_vacios, Toast.LENGTH_LONG).show();
                }
            }
        });

        btnIrLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    private void añadirRegistro(String URL) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(RegistroActivity.this, R.string.juez_registrado, Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(RegistroActivity.this, volleyError.toString(), Toast.LENGTH_LONG).show();
            }
        }

        ) {
            protected Map<String, String> getParams() throws AuthFailureError {
                //utilizar objetos en vez de String para tener campos con otro tipo de valores//pendiente
                Map<String, String> param = new HashMap<String, String>();
                param.put("nombre", nombre);
                param.put("apellidos", apellidos);
                param.put("departamento", departamento);
                param.put("intolerancia", intolerancia);
                param.put("correo", correo);
                param.put("password", password);
                return param;
            }
        };
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}