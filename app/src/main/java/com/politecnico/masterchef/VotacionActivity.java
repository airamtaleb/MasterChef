package com.politecnico.masterchef;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VotacionActivity extends BaseAppCompatMenu {

    List<String> grupos;

    EditText votoPresentacion, votoServicio, votoSabor, votoTriptico, votoImagenPersonal;

    SeekBar seekBarPresentacion, seekBarServicio, seekBarSabor, seekBarTriptico, seekBarImagenPersonal;

    Spinner spinner;

    RequestQueue requestQueue;

    Votacion datosVotacion;

    Button btnGuardar, btnVolver, btnEnviar;

    String usuario, idevento;

    JSONArray array;
    String nombreEquipoAnterior = null;

    TextView tvSeleccione ;

    boolean haVotado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new NukeSSLCerts().nuke();
        setContentView(R.layout.activity_votacion);
        idevento = getIntent().getStringExtra("id_evento");
        Evento evento = (Evento) getIntent().getSerializableExtra("evento");
        boolean voto = (Boolean) getIntent().getSerializableExtra("estadoVoto");
        String estado = evento.getEstado();
        btnGuardar = findViewById(R.id.btnGuardar);
        btnEnviar = findViewById(R.id.btnEnviar);
        SharedPreferences preferences = VotacionActivity.this.getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
        usuario = preferences.getString("id", "");
        haVotado = voto;

        spinner = (Spinner) findViewById(R.id.spinnerGrupos);

        tvSeleccione = findViewById(R.id.textViewSeleccione);

        if (estado.equals("Finalizado")){

            tvSeleccione.setText(R.string.votacionGuardada);
            btnEnviar.setVisibility(View.GONE);
            btnGuardar.setVisibility(View.GONE);
            spinner.setVisibility(View.GONE);
            definirEditYSeeks();
            cargarVotos("http://10.0.2.2/masterchef/cargarVotos.php?idevento=" + idevento);
            bloquearVotos();

        } else if(estado.equals("En curso")){
            cargarGrupos("http://10.0.2.2/masterchef/cargarGruposEvento.php?idevento=" + idevento);
            definirEditYSeeks();
            //comprobarRealizada("http://10.0.2.2/masterchef/comprobarVotacionRealizada.php?idevento="+ idevento+"&idjuez="+usuario);
            //String t =(String) tvSeleccione.getText();
            if(haVotado) {
                btnEnviar.setEnabled(false);
                btnGuardar.setEnabled(false);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String equipo = spinner.getSelectedItem().toString();
                        cargarVotos("http://10.0.2.2/masterchef/cargarVotosIntroducidos.php" +
                                "?idevento=" + idevento+"&idjuez="+usuario + "&equipo='"+equipo+"'");
                        bloquearVotos();
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            } else{

                btnEnviar.setEnabled(true);
                btnGuardar.setEnabled(true);
                tvSeleccione.setText(R.string.seleccione);


                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (nombreEquipoAnterior != null) {
                            guardarVotosEquipo(nombreEquipoAnterior);
                        }

                        SQLiteAdmin sqlite = new SQLiteAdmin(VotacionActivity.this);
                        Votacion votacion = sqlite.leerDatos(spinner.getSelectedItem().toString(), getIntent().getStringExtra("id_evento"), usuario);

                        if (votacion.getPresentacion() == null) {
                            votoPresentacion.setText("" + 0);
                            votoServicio.setText("" + 0);
                            votoSabor.setText("" + 0);
                            votoImagenPersonal.setText("" + 0);
                            votoTriptico.setText("" + 0);
                        } else {
                            votoPresentacion.setText(votacion.getPresentacion());
                            votoServicio.setText(votacion.getServicio());
                            votoSabor.setText(votacion.getSabor());
                            votoImagenPersonal.setText(votacion.getImagen());
                            votoTriptico.setText(votacion.getTriptico());
                        }
                        nombreEquipoAnterior = spinner.getSelectedItem().toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });


                btnGuardar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        guardarVotosEquipo(spinner.getSelectedItem().toString());
                    }
                });

                btnEnviar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        SQLiteAdmin sqlite = new SQLiteAdmin(VotacionActivity.this);

                        array = sqlite.cargarVotaciones(getIntent().getStringExtra("id_evento"), usuario);

                        String prevision = "";

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = array.getJSONObject(i);
                                prevision += "Equipo: " + jsonObject.getString("Nombre_equipo").toString() + "\n"
                                        + " - " + jsonObject.getString("Presentacion").toString()
                                        + " - " + jsonObject.getString("Servicio").toString()
                                        + " - " + jsonObject.getString("Sabor").toString()
                                        + " - " + jsonObject.getString("Triptico").toString()
                                        + " - " + jsonObject.getString("Imagen").toString() + "\n";
                            } catch (JSONException e) {
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                        }
                        AlertDialog.Builder builder = new AlertDialog.Builder(VotacionActivity.this);
                        builder.setPositiveButton(R.string.confirmar, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String registros = array.toString();
                                añadirRegistro("http://10.0.2.2/masterchef/insertarVotacion.php", registros);
                                Intent i = new Intent(getApplicationContext(), DetallesEventoActivity.class);
                                Toast.makeText(getApplicationContext(), "Votaciones introducidas", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        });

                        builder.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User clicked OK button
                            }
                        });
                        builder.setMessage(prevision)
                                .setTitle(R.string.enviar_votaciones);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });

            }
        }

        btnVolver = findViewById(R.id.btnVolver);
        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), DetallesEventoActivity.class);
                finish();
            }
        });
    }

    private void añadirRegistro(String URL, String registros) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(VotacionActivity.this, R.string.valoraciones_insertadas, Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(VotacionActivity.this, volleyError.toString(), Toast.LENGTH_LONG).show();
            }
        }

        ) {
            protected Map<String, String> getParams() throws AuthFailureError {
                //utilizar objetos en vez de String para tener campos con otro tipo de valores//pendiente
                Map<String, String> param = new HashMap<>();
                param.put("registros", registros);
                return param;
            }
        };
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void guardarVotosEquipo(String nombre) {

        datosVotacion = new Votacion();
        datosVotacion.setNombre_equipo(nombre);

        datosVotacion.setId_juez(usuario);
        datosVotacion.setId_evento(getIntent().getStringExtra("id_evento"));
        datosVotacion.setPresentacion(votoPresentacion.getText().toString());
        datosVotacion.setServicio(votoServicio.getText().toString());
        datosVotacion.setSabor(votoSabor.getText().toString());
        datosVotacion.setImagen(votoImagenPersonal.getText().toString());
        datosVotacion.setTriptico(votoTriptico.getText().toString());

        SQLiteAdmin sqlite = new SQLiteAdmin(VotacionActivity.this);
        sqlite.guardarDatos(datosVotacion);

        Toast.makeText(VotacionActivity.this, R.string.votacion_guardada, Toast.LENGTH_LONG).show();

    }

    private void cargarGrupos(String URL) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                grupos = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    String g = "";
                    JSONObject jsonObject;
                    try {
                        jsonObject = response.getJSONObject(i);
                        g = jsonObject.getString("Nombre_equipo");
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    grupos.add(g);

                }

                ArrayAdapter<String> spinnerAdapter =
                        new ArrayAdapter<String>(VotacionActivity.this, android.R.layout.simple_spinner_item, grupos);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(spinnerAdapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), R.string.error_de_conexion, Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);

    }

    private void cargarVotos(String URL) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                grupos = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    String g = "";
                    JSONObject jsonObject;
                    try {
                        jsonObject = response.getJSONObject(i);
                        votoPresentacion.setText(jsonObject.getString("Presentacion"));
                        votoServicio.setText(jsonObject.getString("Servicio"));
                        votoSabor.setText(jsonObject.getString("Sabor"));
                        votoTriptico.setText(jsonObject.getString("Triptico"));
                        votoImagenPersonal.setText(jsonObject.getString("Imagen"));
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), R.string.error_de_conexion, Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);

    }





    private void bloquearVotos(){
        votoPresentacion.setEnabled(false);
        seekBarPresentacion.setEnabled(false);
        votoServicio.setEnabled(false);
        seekBarServicio.setEnabled(false);
        votoSabor.setEnabled(false);
        seekBarSabor.setEnabled(false);
        votoTriptico.setEnabled(false);
        seekBarTriptico.setEnabled(false);
        votoImagenPersonal.setEnabled(false);
        seekBarImagenPersonal.setEnabled(false);
    }

    private void definirEditYSeeks() {
        // Defincicion y metodos de SeekBars y EditText
        votoPresentacion = (EditText) findViewById(R.id.votoPresentacion);
        seekBarPresentacion = (SeekBar) findViewById(R.id.seekBarPresentacion);

        seekBarPresentacion.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                votoPresentacion.setText("" + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        votoPresentacion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    seekBarPresentacion.setProgress(Integer.parseInt(s.toString()));
                } catch (Exception e) {
                }

            }
        });

        votoServicio = (EditText) findViewById(R.id.votoServicio);
        seekBarServicio = (SeekBar) findViewById(R.id.seekBarServicio);

        seekBarServicio.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                votoServicio.setText("" + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        votoServicio.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    seekBarServicio.setProgress(Integer.parseInt(s.toString()));
                } catch (Exception e) {
                }

            }
        });

        votoSabor = (EditText) findViewById(R.id.votoSabor);
        seekBarSabor = (SeekBar) findViewById(R.id.seekBarSabor);

        seekBarSabor.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                votoSabor.setText("" + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        votoSabor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    seekBarSabor.setProgress(Integer.parseInt(s.toString()));
                } catch (Exception e) {
                }

            }
        });

        votoTriptico = (EditText) findViewById(R.id.votoTriptico);
        seekBarTriptico = (SeekBar) findViewById(R.id.seekBarTriptico);

        seekBarTriptico.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                votoTriptico.setText("" + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        votoTriptico.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    seekBarTriptico.setProgress(Integer.parseInt(s.toString()));
                } catch (Exception e) {
                }

            }
        });

        votoImagenPersonal = (EditText) findViewById(R.id.votoImagenPersonal);
        seekBarImagenPersonal = (SeekBar) findViewById(R.id.seekBarImagenPersonal);

        seekBarImagenPersonal.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                votoImagenPersonal.setText("" + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        votoImagenPersonal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    seekBarImagenPersonal.setProgress(Integer.parseInt(s.toString()));
                } catch (Exception e) {
                }

            }
        });

    }
    /*

    ArrayList<EditText> votos = new ArrayList<EditText>();
    ArrayList<SeekBar> seeks = new ArrayList<SeekBar>();
    ArrayList<Integer> votosNombres = new ArrayList<Integer>();
    ArrayList<Integer> seeksNombres = new ArrayList<Integer>();

    votos.add(votoPresentacion);
        votos.add(votoServicio);
        votos.add(votoSabor);
        votos.add(votoTriptico);
        votos.add(votoImagenPersonal);

        votosNombres.add(R.id.votoPresentacion);
        votosNombres.add(R.id.votoServicio);
        votosNombres.add(R.id.votoSabor);
        votosNombres.add(R.id.votoTriptico);
        votosNombres.add(R.id.votoImagenPersonal);

        seeks.add(seekBarPresentacion);
        seeks.add(seekBarServicio);
        seeks.add(seekBarSabor);
        seeks.add(seekBarTriptico);
        seeks.add(seekBarImagenPersonal);

        seeksNombres.add(R.id.seekBarPresentacion);
        seeksNombres.add(R.id.seekBarServicio);
        seeksNombres.add(R.id.seekBarSabor);
        seeksNombres.add(R.id.seekBarTriptico);
        seeksNombres.add(R.id.seekBarImagenPersonal);

        for (int i=0; i<votos.size(); i++){
            EditText voto = votos.get(i);
            int nombreEditText = votosNombres.get(i);
            SeekBar seek = seeks.get(i);
            int nombreSeeks = seeksNombres.get(i);
            voto = (EditText) findViewById(nombreEditText);
            seek = (SeekBar)findViewById(nombreSeeks);

            EditText finalVoto = voto;
            seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    finalVoto.setText(""+progress);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });

            SeekBar finalSeek = seek;
            voto.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    try{
                        finalSeek.setProgress(Integer.parseInt(s.toString()));
                    } catch (Exception e) {}

                }
            });
        }

     */


}