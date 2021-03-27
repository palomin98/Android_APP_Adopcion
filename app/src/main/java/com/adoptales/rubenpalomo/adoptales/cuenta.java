package com.adoptales.rubenpalomo.adoptales;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
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
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class cuenta extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // Fuentes
    Typeface enunciado, texto;

    // TextView en tu cuenta
    TextView txtNombreCuenta, txtEmailCuenta, txtIdCuenta;

    // ImageView de tu cuenta
    CircleImageView imgCuenta;

    // ListView de tu cuenta
    GridView listAnimalesCuenta;

    // Registro
    SharedPreferences mPrefs;
    SharedPreferences.Editor myEditor;

    // URL to get contacts JSON
    private static String url, urlAnimal;

    // JSON Node names
    private static final String TAG_USERINFO = "usuario";
    private static final String TAG_ID = "id";
    private static final String TAG_NOMBRE = "nombre";
    private static final String TAG_NOMBREUSER = "nombreuser";
    private static final String TAG_IMG = "imgperfil";
    private static final String TAG_TELEFONO = "telefono";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_DIRECCION = "direccion";

    // JSON Node names
    private static final String TAG_ANIMALESINFO = "animales";
    private static final String TAG_IDANIMAL = "id";
    private static final String TAG_NOMBREANIMAL = "nombre";
    private static final String TAG_RAZA = "raza";
    private static final String TAG_TIPO = "tipo";
    private static final String TAG_DESCRIPCION = "descripcion";
    private static final String TAG_IMGANIMAL = "imganimal";
    private static final String TAG_EDAD = "edad";
    private static final String TAG_IDUSER = "iduser";
    private static final String TAG_GENERO = "genero";
    private static final String TAG_CHIP = "chip";
    private static final String TAG_VACUNA = "vacuna";

    String id ,nombre, nombreuser, imgperfil, telefono, email, direccion;

    String [] nomAnimal;
    String [] idAnimal;
    String [] razaAnimal;
    String [] tipoAnimal;
    String [] descripcionAnimal;
    String [] imgAnimal;
    String [] edadAnimal;
    String [] iduserAnimal;
    String [] chipAnimal;
    String [] generoAnimal;
    String [] vacunaAnimal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuenta);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(cuenta.this, subiranimal.class);
                i.putExtra("iduser", id);
                startActivity(i);
            }
        });

        // Creamos un registro con esa id
        mPrefs = getSharedPreferences("IDvalue", 0);
        // Creamos un editor del registro
        myEditor = mPrefs.edit();
        // Actualizmos la variable el entero con clave version
        Boolean logeado = mPrefs.getBoolean("logeado", false);
        // Hacemos persistentes los datos
        myEditor.commit();

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

        // Inicializamos las fuentes
        enunciado = Typeface.createFromAsset(getAssets(),"fonts/Happy-Camper-Regular.ttf");
        texto = Typeface.createFromAsset(getAssets(),"fonts/Crocodile Feet DEMO.otf");

        // Inicializamos los widgets
        txtNombreCuenta = findViewById(R.id.txtNombreCuenta);
        txtEmailCuenta = findViewById(R.id.txtEmailCuenta);
        txtIdCuenta = findViewById(R.id.txtIdCuenta);
        imgCuenta = findViewById(R.id.imgCuenta);
        listAnimalesCuenta = findViewById(R.id.listAnimalesCuenta);

        // Ponemos las fuentes
        txtNombreCuenta.setTypeface(enunciado);
        txtEmailCuenta.setTypeface(texto);

        String email = mPrefs.getString("emailuser", "no email");
        String password = mPrefs.getString("password", "no password");

        url = "https://www.losnaranjosdam.online/2dam/ruben/adoptales/datosTuCuenta.php?email="+email+"&password="+ password;
        // Ejecutamos el metodo GetMeme
        new GetDatos().execute();

    }


    /**
     * Async task class to get json by making HTTP call
     */
    private class GetDatos extends AsyncTask<Void, Void, Void> {

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

            txtIdCuenta.setText("#"+id);
            txtNombreCuenta.setText(nombreuser);
            txtEmailCuenta.setText(email);


            Log.e("Response: ", "> " + imgperfil);

            Picasso.with(cuenta.this)
                    .load("https://www.losnaranjosdam.online/2dam/ruben/adoptales/avatar/"+ imgperfil)
                    .error(R.mipmap.ic_launcher)
                    .into(imgCuenta);

            urlAnimal = "https://www.losnaranjosdam.online/2dam/ruben/adoptales/todosTusAnimales.php?iduser="+id;
            // Ejecutamos el metodo GetMeme
            new GetAnimal().execute();
        }
    }

    private ArrayList<HashMap<String, String>> ParseJSON(String json) {
        if (json != null) {
            try {
                // Hashmap for ListView
                ArrayList<HashMap<String, String>> usuarioList = new ArrayList<>();

                JSONObject jsonObj = new JSONObject(json);

                // Getting JSON Array node
                JSONArray animales = jsonObj.getJSONArray(TAG_USERINFO);

                // looping through All Students
                for (int i = 0; i < animales.length(); i++) {
                    JSONObject c = animales.getJSONObject(i);

                    nombre = c.getString(TAG_NOMBRE);
                    nombreuser = c.getString(TAG_NOMBREUSER);
                    id = c.getString(TAG_ID);
                    telefono = c.getString(TAG_TELEFONO);
                    email = c.getString(TAG_EMAIL);
                    direccion = c.getString(TAG_DIRECCION);
                    imgperfil = c.getString(TAG_IMG);

                }

                return usuarioList;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            Log.e("ServiceHandler", "Couldn't get any data from the url");
            return null;
        }
    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class GetAnimal extends AsyncTask<Void, Void, Void> {

        // Hashmap for ListView
        ArrayList<HashMap<String, String>> studentList;

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            WebRequest webreq = new WebRequest();

            // Making a request to url and getting response
            String jsonStr = webreq.makeWebServiceCall(urlAnimal, WebRequest.GET);

            Log.d("Response: ", "> " + jsonStr);

            studentList = ParseJSON2(jsonStr);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            try {
                AdaptadorTusAnimales adapter = new AdaptadorTusAnimales(cuenta.this, nomAnimal, imgAnimal, texto);
                listAnimalesCuenta.setAdapter(adapter);
                listAnimalesCuenta.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View v, int posicion, long id) {
                        // Creo un intento
                        Intent in = new Intent(listAnimalesCuenta.getContext(), tuanimal.class);

                        in.putExtra("id", idAnimal[posicion]);
                        in.putExtra("nombre", nomAnimal[posicion]);
                        in.putExtra("raza", razaAnimal[posicion]);
                        in.putExtra("tipo", tipoAnimal[posicion]);
                        in.putExtra("descripcion", descripcionAnimal[posicion]);
                        in.putExtra("imganimal", imgAnimal[posicion]);
                        in.putExtra("edad",  edadAnimal[posicion]);
                        in.putExtra("iduser", iduserAnimal[posicion]);
                        in.putExtra("genero", generoAnimal[posicion]);
                        if(Integer.parseInt(chipAnimal[posicion]) == 1){
                            in.putExtra("chip", "Si");
                        }else{
                            in.putExtra("chip", "No");
                        }
                        if(Integer.parseInt(vacunaAnimal[posicion]) == 1){
                            in.putExtra("vacuna", "Si");
                        }else{
                            in.putExtra("vacuna", "No");
                        }

                        in.putExtra("telefono", telefono);
                        in.putExtra("email", email);

                        // empiezo activity
                        startActivity(in);
                    }
                });
            } catch (Exception e) {
                listAnimalesCuenta.setAdapter(null);
            }
        }
    }

    private ArrayList<HashMap<String, String>> ParseJSON2(String json) {
        if (json != null) {
            try {
                // Hashmap for ListView
                ArrayList<HashMap<String, String>> usuarioList = new ArrayList<>();

                JSONObject jsonObj = new JSONObject(json);

                // Getting JSON Array node
                JSONArray animales = jsonObj.getJSONArray(TAG_ANIMALESINFO);

                idAnimal = new String[animales.length()];
                nomAnimal = new String[animales.length()];
                razaAnimal = new String[animales.length()];
                tipoAnimal = new String[animales.length()];
                descripcionAnimal = new String[animales.length()];
                edadAnimal = new String[animales.length()];
                iduserAnimal = new String[animales.length()];
                imgAnimal = new String[animales.length()];
                vacunaAnimal = new String[animales.length()];
                generoAnimal = new String[animales.length()];
                chipAnimal = new String[animales.length()];

                // looping through All Students
                for (int i = 0; i < animales.length(); i++) {
                    JSONObject c = animales.getJSONObject(i);

                    String id = c.getString(TAG_IDANIMAL);
                    String nombre = c.getString(TAG_NOMBREANIMAL);
                    String raza = c.getString(TAG_RAZA);
                    String tipo = c.getString(TAG_TIPO);
                    String descripcion = c.getString(TAG_DESCRIPCION);
                    String imganimal = c.getString(TAG_IMGANIMAL);
                    String edad = c.getString(TAG_EDAD);
                    String iduser = c.getString(TAG_IDUSER);
                    String vacuna = c.getString(TAG_VACUNA);
                    String chip = c.getString(TAG_CHIP);
                    String genero = c.getString(TAG_GENERO);

                    // tmp hashmap for single student
                    HashMap<String, String> animal = new HashMap<String, String>();

                    // adding each child node to HashMap key => value
                    animal.put(TAG_ID, id);
                    animal.put(TAG_NOMBRE, nombre);
                    animal.put(TAG_RAZA, imganimal);
                    animal.put(TAG_TIPO, id);
                    animal.put(TAG_DESCRIPCION, nombre);
                    animal.put(TAG_IMGANIMAL, imganimal);
                    animal.put(TAG_EDAD, nombre);
                    animal.put(TAG_IDUSER, imganimal);
                    animal.put(TAG_VACUNA, vacuna);
                    animal.put(TAG_CHIP, chip);
                    animal.put(TAG_GENERO, genero);

                    idAnimal[i] = id;
                    nomAnimal[i] = nombre;
                    razaAnimal[i] = raza;
                    tipoAnimal[i] = tipo;
                    descripcionAnimal[i] = descripcion;
                    edadAnimal[i] = edad;
                    imgAnimal[i] = imganimal;
                    iduserAnimal[i] = iduser;
                    vacunaAnimal[i] = vacuna;
                    chipAnimal[i] = chip;
                    generoAnimal[i] = genero;
                }

                return usuarioList;
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
