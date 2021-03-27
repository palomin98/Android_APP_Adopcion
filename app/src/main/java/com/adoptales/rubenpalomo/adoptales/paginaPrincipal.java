package com.adoptales.rubenpalomo.adoptales;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class paginaPrincipal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // Fuentes
    Typeface enunciado, texto, texto2;

    // ListView pagina principal
    GridView listAnimalesPrincipal;

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

    int versionBDRefugio;

    // URL to get contacts JSON
    private static String url = "https://www.losnaranjosdam.online/2dam/ruben/adoptales/todosAnimales.php";

    // JSON Node names
    private static final String TAG_ANIMALESINFO = "animales";
    private static final String TAG_ID = "id";
    private static final String TAG_NOMBRE = "nombre";
    private static final String TAG_RAZA = "raza";
    private static final String TAG_TIPO = "tipo";
    private static final String TAG_DESCRIPCION = "descripcion";
    private static final String TAG_IMGANIMAL = "imganimal";
    private static final String TAG_EDAD = "edad";
    private static final String TAG_IDUSER = "iduser";
    private static final String TAG_GENERO = "genero";
    private static final String TAG_CHIP = "chip";
    private static final String TAG_VACUNA = "vacuna";

    // Registro
    SharedPreferences mPrefs;
    SharedPreferences.Editor myEditor;

    ImageButton btnBuscar;

    Dialog filtros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagina_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        filtros = new Dialog(this);

        // Pedimos los permisos
        final int REQUEST = 112;
        String[] PERMISSIONS = {android.Manifest.permission.CALL_PHONE, Manifest.permission.INTERNET};
        ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST );

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

        // Inicializamos los widgets
        listAnimalesPrincipal = findViewById(R.id.listAnimalesPrincipal);

        // Inicializamos las fuentes
        enunciado = Typeface.createFromAsset(getAssets(),"fonts/Happy-Camper-Regular.ttf");
        texto = Typeface.createFromAsset(getAssets(),"fonts/Crocodile Feet DEMO.otf");
        texto2 = Typeface.createFromAsset(getAssets(),"fonts/secrcode.ttf");

        btnBuscar = findViewById(R.id.btnBuscar);

        // Ejecutamos el metodo GetMeme
        new GetPartida().execute();
    }

    public void VerFiltros(View v){
        filtros.setContentView(R.layout.contenido_filtros);

        Button btnAtrasFiltros = filtros.findViewById(R.id.btnAtrasFiltros);
        Button btnBuscarAnimal = filtros.findViewById(R.id.btnBuscarAnimal);

        TextView enunciadoBuscar = filtros.findViewById(R.id.enunciadoBuscar);
        TextView txtNombreAnimalBuscar = filtros.findViewById(R.id.txtNombreAnimalBuscar);
        TextView txtRazaAnimalBuscar = filtros.findViewById(R.id.txtRazaAnimalBuscar);
        TextView txtEdadAnimalBuscar = filtros.findViewById(R.id.txtEdadAnimalBuscar);
        TextView txtGeneroAnimalBuscar = filtros.findViewById(R.id.txtGeneroAnimalBuscar);
        TextView txtVacunaAnimalBuscar = filtros.findViewById(R.id.txtVacunaAnimalBuscar);
        TextView txtChipAnimalBuscar = filtros.findViewById(R.id.txtChipAnimalBuscar);

        // EditText de contacto
        final EditText edtNombreAnimalBuscar = filtros.findViewById(R.id.edtNombreAnimalBuscar);
        final EditText edtRazaAnimalBuscar = filtros.findViewById(R.id.edtRazaAnimalBuscar);
        final EditText edtEdadAnimalBuscar = filtros.findViewById(R.id.edtEdadAnimalBuscar);

        final RadioButton rbGeneroMAnimalBuscar = filtros.findViewById(R.id.rbGeneroMAnimalBuscar);
        final RadioButton rbGeneroHAnimalBuscar = filtros.findViewById(R.id.rbGeneroHAnimalBuscar);
        final RadioButton rbVacunaSAnimalBuscar = filtros.findViewById(R.id.rbVacunaSAnimalBuscar);
        final RadioButton rbVacunaNAnimalBuscar = filtros.findViewById(R.id.rbVacunaNAnimalBuscar);
        final RadioButton rbChipSAnimalBuscar = filtros.findViewById(R.id.rbChipSAnimalBuscar);
        final RadioButton rbChipNAnimalBuscar = filtros.findViewById(R.id.rbChipNAnimalBuscar);

        enunciadoBuscar.setTypeface(enunciado);
        txtNombreAnimalBuscar.setTypeface(texto);
        txtRazaAnimalBuscar.setTypeface(texto);
        txtEdadAnimalBuscar.setTypeface(texto);
        txtGeneroAnimalBuscar.setTypeface(texto);
        txtVacunaAnimalBuscar.setTypeface(texto);
        txtChipAnimalBuscar.setTypeface(texto);
        rbGeneroMAnimalBuscar.setTypeface(texto2);
        rbGeneroHAnimalBuscar.setTypeface(texto2);
        rbVacunaSAnimalBuscar.setTypeface(texto2);
        rbVacunaNAnimalBuscar.setTypeface(texto2);
        rbChipSAnimalBuscar.setTypeface(texto2);
        rbChipNAnimalBuscar.setTypeface(texto2);
        btnBuscarAnimal.setTypeface(texto);
        btnAtrasFiltros.setTypeface(texto);

        // Adaptamos los datos a un spinner
        //ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.tipoanimal, android.R.layout.simple_spinner_item);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spTipoAnimalBuscar.setAdapter(adapter);

        btnAtrasFiltros.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                filtros.dismiss();
            }
        });

        btnBuscarAnimal.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Boolean empezado = false;


                String sql = "Select * from animales where ";

                if(!edtNombreAnimalBuscar.getText().toString().isEmpty()){
                    sql = sql + "nombre = '" + edtNombreAnimalBuscar.getText().toString() + "'  ";
                    empezado = true;
                }

                if(!edtRazaAnimalBuscar.getText().toString().isEmpty() && empezado){
                    sql = sql + "or raza = '" + edtRazaAnimalBuscar.getText().toString() + "'";
                }else if(!edtRazaAnimalBuscar.getText().toString().isEmpty() && !empezado) {
                    sql = sql + "raza = '" + edtRazaAnimalBuscar.getText().toString() + "'";
                    empezado = true;
                }

                if(!edtEdadAnimalBuscar.getText().toString().isEmpty() && empezado){
                    sql = sql + "or edad = '" + edtEdadAnimalBuscar.getText().toString() + "'";
                }else if(!edtEdadAnimalBuscar.getText().toString().isEmpty() && !empezado){
                    sql = sql + "edad = '" + edtEdadAnimalBuscar.getText().toString() + "'";
                    empezado = true;
                }

                if((rbGeneroHAnimalBuscar.isChecked() || rbGeneroMAnimalBuscar.isChecked()) && empezado){
                    if(rbGeneroHAnimalBuscar.isChecked()){
                        sql = sql + "or genero = '" + rbGeneroHAnimalBuscar.getText().toString() + "' ";
                    }else{
                        sql = sql + "or genero = '" + rbGeneroMAnimalBuscar.getText().toString() + "'";
                    }
                }else if((rbGeneroHAnimalBuscar.isChecked() || rbGeneroMAnimalBuscar.isChecked()) && !empezado){
                    if(rbGeneroHAnimalBuscar.isChecked()){
                        sql = sql + "genero = '" + rbGeneroHAnimalBuscar.getText().toString() + "' ";
                        empezado = true;
                    }else{
                        sql = sql + "genero = '" + rbGeneroMAnimalBuscar.getText().toString() + "'";
                        empezado = true;
                    }
                }

                if((rbVacunaNAnimalBuscar.isChecked() || rbVacunaSAnimalBuscar.isChecked()) && empezado){
                    if(rbVacunaNAnimalBuscar.isChecked()){
                        sql = sql + "or vacunado = '0'";
                    }else{
                        sql = sql + "or vacunado = '1'";
                    }
                }else if((rbVacunaNAnimalBuscar.isChecked() || rbVacunaSAnimalBuscar.isChecked()) && !empezado){
                    if(rbVacunaNAnimalBuscar.isChecked()){
                        sql = sql + "vacunado = '0'";
                        empezado = true;
                    }else{
                        sql = sql + "vacunado = '1'";
                        empezado = true;
                    }
                }

                if((rbChipNAnimalBuscar.isChecked() || rbChipSAnimalBuscar.isChecked()) && empezado){
                    if(rbChipNAnimalBuscar.isChecked()){
                        sql = sql + "or chip = '0'";
                    }else{
                        sql = sql + "or chip = '1'";
                    }
                }else if((rbChipNAnimalBuscar.isChecked() || rbChipSAnimalBuscar.isChecked()) && !empezado){
                    if(rbChipNAnimalBuscar.isChecked()){
                        sql = sql + "chip = '0'";
                        empezado = true;
                    }else{
                        sql = sql + "chip = '1'";
                        empezado = true;
                    }
                }

                if(empezado){

                    url = "https://www.losnaranjosdam.online/2dam/ruben/adoptales/todosAnimalesFiltros.php?sqlFiltros="+sql;

                    // Ejecutamos el metodo GetMeme
                    new GetPartida().execute();
                }else{
                    url = "https://www.losnaranjosdam.online/2dam/ruben/adoptales/todosAnimalesFiltros.php?sqlFiltros=Select * from animales";

                    // Ejecutamos el metodo GetMeme
                    new GetPartida().execute();
                }

                filtros.dismiss();
            }
        });

        filtros.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        filtros.show();
    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class GetPartida extends AsyncTask<Void, Void, Void> {

        // Hashmap for ListView
        ArrayList<HashMap<String, String>> studentList;

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            WebRequest webreq = new WebRequest();
            Log.d("Response: ", "> " + url);
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

                DisplayMetrics metrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(metrics);
                int width = metrics.widthPixels;
                int height = metrics.heightPixels;

                AdaptadorAnimales adapter = new AdaptadorAnimales(paginaPrincipal.this, nomAnimal, imgAnimal, texto);
                listAnimalesPrincipal.setAdapter(adapter);
                listAnimalesPrincipal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View v, int posicion, long id) {
                        // Creo un intento
                        Intent in = new Intent(listAnimalesPrincipal.getContext(), animal.class);

                        in.putExtra("nombre", nomAnimal[posicion]);
                        in.putExtra("raza", razaAnimal[posicion]);
                        in.putExtra("tipo", tipoAnimal[posicion]);
                        in.putExtra("descripcion", descripcionAnimal[posicion]);
                        in.putExtra("edad", edadAnimal[posicion]);
                        in.putExtra("iduser", iduserAnimal[posicion]);
                        in.putExtra("imgAnimal", imgAnimal[posicion]);
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

                        // empiezo activity
                        startActivity(in);
                    }
                });
            } catch (Exception e) {
                listAnimalesPrincipal.setAdapter(null);
            }
        }

    }

    private ArrayList<HashMap<String, String>> ParseJSON(String json) {
        if (json != null) {
            try {
                // Hashmap for ListView
                ArrayList<HashMap<String, String>> animalList = new ArrayList<HashMap<String, String>>();



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

                    String id = c.getString(TAG_ID);
                    String nombre = c.getString(TAG_NOMBRE);
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
                    iduserAnimal[i] = iduser;
                    imgAnimal[i] = imganimal;
                    vacunaAnimal[i] = vacuna;
                    chipAnimal[i] = chip;
                    generoAnimal[i] = genero;

                    // adding student to students list
                    animalList.add(animal);
                }

                return animalList;
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
