package com.politecnico.masterchef;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    TextInputLayout editUser, editPassword;
    Button btnLogin, btnIrRegistro;

    String usuario, password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        editUser = findViewById(R.id.editCorreo);
        editPassword = findViewById(R.id.editPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnIrRegistro = findViewById(R.id.btnIrRegistro);

        //comprobar si hay datos en sharedPreferences
        getPreferences();

        btnIrRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), RegistroActivity.class);
                startActivity(i);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                usuario = editUser.getEditText().getText().toString();

                password = editPassword.getEditText().getText().toString();

                if (!usuario.isEmpty() && !password.isEmpty()) {
                    //en este caso ponemos 10.0.2.2 en vez de localhost si no no lo reconoce...
                    validarUsuario("http://10.0.2.2/masterchef/validar_usuario.php");
                } else
                    Toast.makeText(LoginActivity.this, "No se permiten los campos vacios", Toast.LENGTH_LONG).show();
            }
        });


    }

    private void validarUsuario(String URL) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.isEmpty()) {
                    guardarSharedPreferences();
                    Intent i = new Intent(getApplicationContext(), EventosActivity.class);
                   startActivity(i);
                } else {

                    Toast.makeText(LoginActivity.this, "Usuario y/o contraseña incorrectos", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String message = null;
                if (error instanceof NetworkError) {
                    message = "Imposible conectarse a la red. Comprueba la conexion";
                } else if (error instanceof ServerError) {
                    message = "Imposibñle conectarse con el servidor";
                } else if (error instanceof AuthFailureError) {
                    message = "Error de autenticacion. Compruebe la conexion";
                } else if (error instanceof ParseError) {
                    message = "Error de parseo. Intentelo de nuevo un poco mas tarde";
                } else if (error instanceof NoConnectionError) {
                    message = "Imposible conectarse a Interner. Compruebe la conexion";
                } else if (error instanceof TimeoutError) {
                    message = "Tiempo de espera agotado . compruebe la conexion a interner";
                }

                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
            }
        }


        ) {
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> param = new HashMap<String, String>();
                param.put("usuario", usuario);
                param.put("password", password);
                return param;
            }


        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

    private void guardarSharedPreferences() {
        SharedPreferences preferences = getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("usuario", usuario);
        editor.putString("password", password);
        editor.putBoolean("sesion", true);
        editor.commit();

    }

    private void getPreferences() {

        SharedPreferences preferences = getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
        editUser.getEditText().setText(preferences.getString("usuario", ""));
        editPassword.getEditText().setText(preferences.getString("password", ""));
    }
}