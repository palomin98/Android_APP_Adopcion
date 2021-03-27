package com.adoptales.rubenpalomo.adoptales;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class tuanimal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // Fuentes
    Typeface enunciado, texto;

    // TextView del refugio
    TextView txtNombreAnimal, txtNumeroAnimal, txtEmailAnimal, txtDescripcionAnimal, txtChipTu, txtVacunaTu, txtGeneroTu
            , txtEdadTu, txtTipoAnimal, txtInfoAnimal, txtInformacion;

    // ImageView del refugio
    ImageView imgAnimal;

    // Registro
    SharedPreferences mPrefs;
    SharedPreferences.Editor myEditor;

    // Datos
    String id, nombre, raza, tipo, descripcion, imganimal, edad, iduser, email, telefono;
    String chip;
    String vacuna;
    String genero;

    LinearLayout ly1, ly2, ly3, ly4;

    // PHP
    private static String url;
    private static final String TAG_RESPUESTAINFO = "respuesta";
    private static final String TAG_RESPUESTA = "resp";
    String respuesta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tuanimal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Creamos un registro con esa id
        mPrefs = getSharedPreferences("IDvalue", 0);
        // Creamos un editor del registro
        myEditor = mPrefs.edit();
        // Actualizmos la variable el entero con clave logueado
        Boolean logeado = mPrefs.getBoolean("logeado", false);
        // Hacemos persistentes los datos
        myEditor.commit();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Creamos el menu y le damos el funcionamiento
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ly1 = findViewById(R.id.ly1);
        ly2 = findViewById(R.id.ly2);
        ly3 = findViewById(R.id.ly3);
        ly4 = findViewById(R.id.ly4);

        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth() / 4;  // obsoleto (deprecated)

        ly1.getLayoutParams().width=width;
        ly2.getLayoutParams().width=width;
        ly3.getLayoutParams().width=width;
        ly4.getLayoutParams().width=width;

        // Comprobamos que esta logueado
        // Se pone un menu o otro
        if(logeado == true) {
            navigationView.inflateMenu(R.menu.activity_pagina_conregistro_drawer);
        }else{
            navigationView.inflateMenu(R.menu.activity_pagina_sinregistro_drawer);
        }

        // Inicializamos las fuentes
        enunciado = Typeface.createFromAsset(getAssets(),"fonts/Happy-Camper-Regular.ttf");
        texto = Typeface.createFromAsset(getAssets(),"fonts/Crocodile Feet DEMO.otf");
        Typeface texto2 = Typeface.createFromAsset(getAssets(),"fonts/secrcode.ttf");

        // Inicializamos los widgets
        txtNombreAnimal = findViewById(R.id.txtNombreAnimal);
        txtNumeroAnimal = findViewById(R.id.txtNumeroAnimal);
        txtEmailAnimal = findViewById(R.id.txtEmailAnimal);
        txtDescripcionAnimal = findViewById(R.id.txtDescripcionAnimal);
        txtChipTu = findViewById(R.id.txtChipTu);
        txtVacunaTu = findViewById(R.id.txtVacunaTu);
        txtGeneroTu = findViewById(R.id.txtGeneroTu);
        txtEdadTu = findViewById(R.id.txtEdadTu);
        txtTipoAnimal = findViewById(R.id.txtTipoAnimal);
        txtInfoAnimal = findViewById(R.id.txtInfoAnimal);
        txtInformacion = findViewById(R.id.txtInformacion);
        imgAnimal = findViewById(R.id.imgAnimal);

        // Ponemos las fuentes
        txtNombreAnimal.setTypeface(enunciado);
        txtChipTu.setTypeface(texto);
        txtVacunaTu.setTypeface(texto);
        txtGeneroTu.setTypeface(texto);
        txtEdadTu.setTypeface(texto);
        txtInformacion.setTypeface(texto);
        txtInfoAnimal.setTypeface(texto);
        txtTipoAnimal.setTypeface(texto2);
        txtNumeroAnimal.setTypeface(texto2);
        txtEmailAnimal.setTypeface(texto2);
        txtDescripcionAnimal.setTypeface(texto2);


        // Cogemos los extras que nos pasan en la anterio activity
        Bundle extras = getIntent().getExtras();

        // Si los extras no son nulos
        if (extras != null) {
            // Guardo los datos con su clave
            id = extras.getString("id");
            nombre = extras.getString("nombre");
            raza = extras.getString("raza");
            tipo = extras.getString("tipo");
            descripcion = extras.getString("descripcion");
            imganimal = extras.getString("imganimal");
            edad = extras.getString("edad");
            iduser = extras.getString("iduser");
            chip = extras.getString("chip");
            vacuna = extras.getString("vacuna");
            genero = extras.getString("genero");
            telefono = extras.getString("telefono");
            email = extras.getString("email");
        } else {
            // Se queda vacio
            id = "";
            nombre = "";
            raza = "";
            tipo = "";
            descripcion = "";
            imganimal = "";
            edad = "";
            iduser = "";
        }

        // Ponemos los textos
        txtNombreAnimal.setText(nombre);
        txtDescripcionAnimal.setText(descripcion);
        txtTipoAnimal.setText("-"+tipo+", "+ raza);
        txtEmailAnimal.setText("-"+ email);
        txtNumeroAnimal.setText("-"+ telefono);
        txtEdadTu.setText(edad);
        txtDescripcionAnimal.setText("-"+descripcion);
        txtChipTu.setText(chip);
        txtVacunaTu.setText(vacuna);
        txtGeneroTu.setText(genero);

        // Ponemos la imagen de perfil
        Picasso.with(this)
                .load("https://www.losnaranjosdam.online/2dam/ruben/adoptales/fotos/"+ imganimal)
                .error(R.mipmap.ic_launcher)
                .into(imgAnimal);

    }

    /**
     * Borramos un animal
     */
    public void borrarAnimal(){

        url = "https://www.losnaranjosdam.online/2dam/ruben/adoptales/borrarAnimal.php?id="+ id;

        // Ejecutamos el metodo GetMeme
        new GetDatosDueno().execute();
    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class GetDatosDueno extends AsyncTask<Void, Void, Void> {

        // Hashmap for ListView
        ArrayList<HashMap<String, String>> userList;
        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            WebRequest webreq = new WebRequest();

            // Making a request to url and getting response
            String json = webreq.makeWebServiceCall(url, WebRequest.GET);

            Log.d("Response: ", "> " + respuesta);

            // Llamamos al metodo que te convierte el json
            ParseJSON(json);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            // Si la respuesta es hecho
            if(respuesta.equals("hecho")){
                // Mensaje de informacion
                //Toast toast1 = Toast.makeText(getApplicationContext(),
                //"Se ha borrado la mascota" , Toast.LENGTH_SHORT);
                //toast1.show();

                // Nos movemos a otra activity
                Intent i = new Intent(tuanimal.this, cuenta.class);
                startActivity(i);
            }else{
                // Mensaje de error
                Toast toast1 = Toast.makeText(getApplicationContext(),
                        "No se ha borrado la mascota" , Toast.LENGTH_SHORT);
                toast1.show();
            }
        }
    }

    private void ParseJSON(String json) {
        // Si el json es nulo
        if (json != null) {
            try {
                // Hacemos un objeto json con el json pasado por parametro
                JSONObject jsonObj = new JSONObject(json);

                // Cogemos el nodo del JSON y creamos un array
                JSONArray usuario = jsonObj.getJSONArray(TAG_RESPUESTAINFO);

                // Recorremos todos los usuarios
                for (int i = 0; i < usuario.length(); i++) {
                    // Cogemos el objeto json en esa posivion
                    JSONObject c = usuario.getJSONObject(i);

                    // Ponemos la respuesta, con esa clave
                    respuesta = c.getString(TAG_RESPUESTA);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("ServiceHandler", "Couldn't get any data from the url");
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
        // Creamos el menu
        getMenuInflater().inflate(R.menu.pagina_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // Creamos un intento
        Intent i;

        // Depende de lo que pulse hace una opcion
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

        // Creamos un intento
        Intent i;

        // Depende de lo que pulse hace una opcion
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
        }  else if (id == R.id.nav_cerrarsesion){
            // Actualizmos el registro
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


    public BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_llamar:
                    borrarAnimal();
                    return true;
                case R.id.navigation_maps:
                    borrarAnimal();
                    return true;
            }
            return false;
        }
    };
}
