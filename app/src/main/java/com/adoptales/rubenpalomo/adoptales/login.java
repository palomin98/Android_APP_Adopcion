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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class login extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // Fuentes
    Typeface enunciado, texto;

    // TextView de login
    TextView enunciadoLogin, txtEmailLogin, txtPasswordLogin;

    // EditText de login
    EditText edtEmailLogin, edtPasswordLogin;

    // Button de login
    Button btnEntrarLogin;

    // URL to get contacts JSON
    private static String url = "https://www.losnaranjosdam.online/2dam/ruben/adoptales/todosAnimales.php";

    // JSON Node names
    private static final String TAG_ANIMALESINFO = "animales";
    private static final String TAG_ID = "id";
    private static final String TAG_NOMBRE = "nombre";
    private static final String TAG_RAZA = "raza";

    // Registro
    SharedPreferences mPrefs;
    SharedPreferences.Editor myEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Creamos un registro con esa id
        mPrefs = getSharedPreferences("IDvalue", 0);
        // Creamos un editor del registro
        myEditor = mPrefs.edit();
        // Actualizmos la variable el entero con clave version
        Boolean logeado = mPrefs.getBoolean("logeado", false);
        // Hacemos persistentes los datos
        myEditor.commit();

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

        // Inicializamos todos los widgets
        enunciadoLogin = findViewById(R.id.enunciadoLogin);
        txtEmailLogin = findViewById(R.id.txtEmailLogin);
        txtPasswordLogin = findViewById(R.id.txtPasswordLogin);
        btnEntrarLogin = findViewById(R.id.btnEntrarLogin);
        edtEmailLogin = findViewById(R.id.edtEmailLogin);
        edtPasswordLogin = findViewById(R.id.edtPasswordLogin);

        // Ponemos las fuentes
        enunciadoLogin.setTypeface(enunciado);
        txtEmailLogin.setTypeface(texto);
        txtPasswordLogin.setTypeface(texto);
        btnEntrarLogin.setTypeface(texto);
    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class GetPartida extends AsyncTask<Void, Void, Void> {

        // Hashmap for ListView
        String respuesta;

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            WebRequest webreq = new WebRequest();

            // Making a request to url and getting response
            respuesta = webreq.makeWebServiceCall(url, WebRequest.GET);

            Log.d("Response: ", ">" + respuesta);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if(Integer.parseInt(respuesta) == 1){
                // Creamos un registro con esa id
                SharedPreferences mPrefs = getSharedPreferences("IDvalue", 0);
                // Creamos un editor del registro
                SharedPreferences.Editor myEditor = mPrefs.edit();
                // Actualizmos la variable el entero con clave version
                myEditor.putBoolean("logeado", true);
                myEditor.putString("emailuser", edtEmailLogin.getText().toString());
                myEditor.putString("password", edtPasswordLogin.getText().toString());
                // Hacemos persistentes los datos
                myEditor.commit();

                Intent i = new Intent(login.this, paginaPrincipal.class);
                startActivity(i);
            }else{
                Toast toast1 = Toast.makeText(getApplicationContext(),
                        "Email o contraseña incorrecta." , Toast.LENGTH_SHORT);

                toast1.show();
            }
        }

    }

    public boolean comprobarEmail(){
        String email;
        Boolean correcto;

        email = edtEmailLogin.getText().toString();
        correcto = false;

        if(!email.isEmpty() &&
                android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            correcto = true;
        }else{
            Toast toast1 = Toast.makeText(getApplicationContext(),
                    "Error en el email." , Toast.LENGTH_SHORT);

            toast1.show();
        }

        return correcto;
    }

    public boolean comprobarPassword(){
        String password;
        Boolean correcto;

        password = edtPasswordLogin.getText().toString();
        correcto = false;

        if(!password.isEmpty() && password.length()>6){
            correcto = true;
        }else{
            Toast toast1 = Toast.makeText(getApplicationContext(),
                    "Error en la contraseña." , Toast.LENGTH_SHORT);

            toast1.show();
        }

        return correcto;
    }

    public void loguearse(View v){

        String email;
        String password;

        email = edtEmailLogin.getText().toString();
        password = edtPasswordLogin.getText().toString();

        if(comprobarEmail() && comprobarPassword()){
            url = "https://www.losnaranjosdam.online/2dam/ruben/adoptales/login.php?email="+email+"&password="+password+"";

            // Ejecutamos el metodo GetMeme
            new GetPartida().execute();
        }else{
            Toast toast1 = Toast.makeText(getApplicationContext(),
                    "Error al loguearse." , Toast.LENGTH_SHORT);

            toast1.show();
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
