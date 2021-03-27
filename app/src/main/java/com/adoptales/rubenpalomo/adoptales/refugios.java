package com.adoptales.rubenpalomo.adoptales;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class refugios extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // Fuentes
    Typeface enunciado, texto;

    // ListView de refugios
    GridView listRefugios;

    // Datos refugios
    String [] idRefugio;
    String [] nomRefugio;
    String [] imgRefugio;
    String [] emailRefugio;
    String [] telefonoRefugio;
    String [] direccionRefugio;

    int versionBDRefugio;

    // URL to get contacts JSON
    private static String url = "https://www.losnaranjosdam.online/2dam/ruben/adoptales/todosRefugios.php";

    // JSON Node names
    private static final String TAG_STUDENTINFO = "refugios";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "nombrerefugio";
    private static final String TAG_IMG = "imgperfil";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_TELEFONO = "telefono";
    private static final String TAG_DIRECCION = "direccion";

    // Registro
    SharedPreferences mPrefs;
    SharedPreferences.Editor myEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refugios);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Creamos un registro con esa id
        mPrefs = getSharedPreferences("IDvalue", 0);
        // Creamos un editor del registro
        myEditor = mPrefs.edit();
        // Actualizmos la variable el entero con clave version
        Boolean logeado = mPrefs.getBoolean("logeado", false);
        // Hacemos persistentes los datos
        myEditor.commit();

        // Inicializamos las fuentes
        enunciado = Typeface.createFromAsset(getAssets(),"fonts/Happy-Camper-Regular.ttf");
        texto = Typeface.createFromAsset(getAssets(),"fonts/Crocodile Feet DEMO.otf");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        // Si esta logueado
        if(logeado == true) {
            navigationView.inflateMenu(R.menu.activity_pagina_conregistro_drawer);
        }else{
            navigationView.inflateMenu(R.menu.activity_pagina_sinregistro_drawer);
        }

        // Inicializamos los widgets
        listRefugios = findViewById(R.id.listRefugios);
        // Ejecutamos el metodo GetMeme

        // Creamos un registro con esa id
        SharedPreferences mPrefs = getSharedPreferences("IDvalue", 0);
        // Creamos un editor del registro
        SharedPreferences.Editor myEditor = mPrefs.edit();

        // Actualizmos la variable el entero con clave version
        versionBDRefugio = mPrefs.getInt("versionbdrefugio", 0);

        // Si es igual a 0
        if(versionBDRefugio == 0) {
            // La version se pondra a 0
            myEditor.putInt("versionbdrefugio", 0);
        }

        // Hacemos persistentes los datos
        myEditor.commit();

        new GetRegistro().execute();
    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class GetRegistro extends AsyncTask<Void, Void, Void> {

        // Hashmap for ListView
        ArrayList<HashMap<String, String>> studentList;

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            WebRequest webreq = new WebRequest();

            // Making a request to url and getting response
            String jsonStr = webreq.makeWebServiceCall(url, WebRequest.GET);

            Log.d("Response: ", "> " + jsonStr);

            studentList = ParseJSON(jsonStr);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            try {
                AdaptadorRefugio adapter = new AdaptadorRefugio(refugios.this, nomRefugio, imgRefugio, texto);
                listRefugios.setAdapter(adapter);
                listRefugios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View v, int posicion, long id) {
                        // Creo un intento
                        Intent in = new Intent(listRefugios.getContext(), refugio.class);

                        in.putExtra("id", idRefugio[posicion]);
                        in.putExtra("nombre", nomRefugio[posicion]);
                        in.putExtra("img", imgRefugio[posicion]);
                        in.putExtra("email", emailRefugio[posicion]);
                        in.putExtra("telefono", telefonoRefugio[posicion]);
                        in.putExtra("direccion", direccionRefugio[posicion]);

                        // empiezo activity
                        startActivity(in);
                    }
                });
            } catch (Exception e) {
                listRefugios.setAdapter(null);
            }
        }
    }

    private ArrayList<HashMap<String, String>> ParseJSON(String json) {
        if (json != null) {
            try {
                // Hashmap for ListView
                ArrayList<HashMap<String, String>> studentList = new ArrayList<HashMap<String, String>>();

                JSONObject jsonObj = new JSONObject(json);

                // Getting JSON Array node
                JSONArray students = jsonObj.getJSONArray(TAG_STUDENTINFO);

                nomRefugio = new String[students.length()];
                idRefugio = new String[students.length()];
                imgRefugio = new String[students.length()];
                emailRefugio  = new String[students.length()];
                telefonoRefugio  = new String[students.length()];
                direccionRefugio  = new String[students.length()];

                // looping through All Students
                for (int i = 0; i < students.length(); i++) {
                    JSONObject c = students.getJSONObject(i);

                    String id = c.getString(TAG_ID);
                    String name = c.getString(TAG_NAME);
                    String img = c.getString(TAG_IMG);

                    // tmp hashmap for single student
                    HashMap<String, String> student = new HashMap<String, String>();

                    // adding each child node to HashMap key => value
                    student.put(TAG_ID, id);
                    student.put(TAG_NAME, name);
                    student.put(TAG_IMG, img);

                    idRefugio[i] = c.getString(TAG_ID);
                    nomRefugio[i] = c.getString(TAG_NAME);
                    imgRefugio[i] = c.getString(TAG_IMG);
                    emailRefugio[i]  = c.getString(TAG_EMAIL);
                    telefonoRefugio[i]  = c.getString(TAG_TELEFONO);
                    direccionRefugio[i]  = c.getString(TAG_DIRECCION);

                    // adding student to students list
                    studentList.add(student);
                }
                return studentList;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            Log.e("ServiceHandler", "Couldn't get any data from the url");
            return null;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pagina_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        Intent i;

        if (id == R.id.action_contacto) {
            i = new Intent(this, contacto.class);
            startActivity(i);
            return true;
        } else if (id == R.id.action_info){
            i = new Intent(this, info.class);
            startActivity(i);
            return true;
        } else if (id == R.id.action_ayuda){
            i = new Intent(this, ayuda.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        Intent i;

        if (id == R.id.nav_inicio) {
            i = new Intent(this, paginaPrincipal.class);
            startActivity(i);
        } else if (id == R.id.nav_inicioSesion) {
            i = new Intent(this, login.class);
            startActivity(i);
        } else if (id == R.id.nav_registro) {
            i = new Intent(this, registro.class);
            startActivity(i);
        } else if (id == R.id.nav_refugios) {
            i = new Intent(this, refugios.class);
            startActivity(i);
        } else if (id == R.id.nav_cuenta) {
            i = new Intent(this, cuenta.class);
            startActivity(i);
        } else if (id == R.id.nav_compartir) {
            i  = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_TEXT, "App para dar vida a los animales.");
            startActivity(i);
        } else if (id == R.id.nav_contacto) {
            i = new Intent(this, contacto.class);
            startActivity(i);
        } else if (id == R.id.nav_info) {
            i = new Intent(this, info.class);
            startActivity(i);
        } else if (id == R.id.nav_cerrarsesion){
            // Actualizmos la variable el entero con clave version
            myEditor.putBoolean("logeado", false);
            myEditor.putString("emailuser", "");
            myEditor.putString("password", "");
            // Hacemos persistentes los datos
            myEditor.commit();

            i = new Intent(this, paginaPrincipal.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
