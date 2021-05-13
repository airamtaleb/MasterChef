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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class VotacionActivity extends BaseAppCompatMenu {

    List<String> grupos;

    EditText votoPresentacion, votoServicio, votoSabor, votoTriptico, votoImagenPersonal;

    SeekBar seekBarPresentacion, seekBarServicio, seekBarSabor, seekBarTriptico, seekBarImagenPersonal;

    Spinner spinner;

    RequestQueue requestQueue;

    Votacion datosVotacion;

    Button btnGuardar, btnVolver, btnEnviar;

    String usuario;


    String nombreEquipoAnterior = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_votacion);
        String idevento = getIntent().getStringExtra("id_evento");

        SharedPreferences preferences = VotacionActivity.this.getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
        usuario = preferences.getString("id", "");

        spinner = (Spinner) findViewById(R.id.spinnerGrupos);

        cargarGrupos("http://10.0.2.2/masterchef/cargarGruposEvento.php?idevento=" + idevento);


        definirEditYSeeks();


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


        btnGuardar = findViewById(R.id.btnGuardar);
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                guardarVotosEquipo(spinner.getSelectedItem().toString());

            }
        });

        btnEnviar = findViewById(R.id.btnEnviar);
        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SQLiteAdmin sqlite = new SQLiteAdmin(VotacionActivity.this);

                JSONArray array = sqlite.cargarVotaciones(getIntent().getStringExtra("id_evento"), usuario);


                AlertDialog.Builder builder = new AlertDialog.Builder(VotacionActivity.this);

                builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                    }
                });

                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                    }
                });

                builder.setMessage("mensaje")
                        .setTitle("dialogo");

                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

        btnVolver = findViewById(R.id.btnVolver);
        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), DetallesEventoActivity.class);
                startActivity(i);
                finish();
            }
        });


        //solo para probar
        Button btnCargar = findViewById(R.id.btnCargar);
        btnCargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SQLiteAdmin sqlite = new SQLiteAdmin(VotacionActivity.this);
                Votacion votacion = sqlite.leerDatos(spinner.getSelectedItem().toString(), getIntent().getStringExtra("id_evento"), usuario);

                Toast.makeText(VotacionActivity.this, votacion.getNombre_equipo() + " evento " + votacion.getId_evento() + "presentacion  " + votacion.getPresentacion(), Toast.LENGTH_LONG).show();

            }
        });

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

        Toast.makeText(VotacionActivity.this, "Votacion guardada", Toast.LENGTH_LONG).show();

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
                Toast.makeText(getApplicationContext(), "ERROR DE CONEXIÓN", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);

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