package com.adoptales.rubenpalomo.adoptales;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class subiranimal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    // Fuentes
    Typeface enunciado, texto;

    // TextView del refugio
    TextView enunciadoSubir, txtNombreAnimalSubir, txtTipoAnimalSubir, txtRazaAnimalSubir, txtDescripcionAnimalSubir, txtEdadAnimalSubir
            , txtGeneroAnimalSubir, txtVacunaAnimalSubir, txtChipAnimalSubir;

    // Button del refugio
    Button btnSubirAnimal, btnAvatar;

    Spinner spTipoAnimalSubir;

    // EditText de contacto
    EditText edtNombreAnimalSubir, edtRazaAnimalSubir, edtDescripcionAnimalSubir, edtEdadAnimalSubir;

    RadioButton rbGeneroMAnimalSubir, rbGeneroHAnimalSubir, rbVacunaSAnimalSubir, rbVacunaNAnimalSubir,rbChipSAnimalSubir
        ,rbChipNAnimalSubir;
    ImageView imgSubirAnimal;

    // Registro
    SharedPreferences mPrefs;
    SharedPreferences.Editor myEditor;

    // URL to get contacts JSON
    private static String url;
    String result;
    String iduser;

    // Subir imagen
    private Bitmap bitmap;
    private int PICK_IMAGE_REQUEST = 1;
    private String UPLOAD_URL ="https://www.losnaranjosdam.online/2dam/ruben/adoptales/subirFoto.php";
    private String KEY_IMAGEN = "foto";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subiranimal);
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

        // Cogemos los extras que nos pasan en la anterio activity
        Bundle extras = getIntent().getExtras();

        // Si los extras no son nulos
        if (extras != null) {
            // Guardo los datos que tengan como clave ano
            iduser = extras.getString("iduser");
        } else {
            // Se queda vacio
            iduser = "";
        }

        // Inicializamos las fuentes
        enunciado = Typeface.createFromAsset(getAssets(),"fonts/Happy-Camper-Regular.ttf");
        texto = Typeface.createFromAsset(getAssets(),"fonts/Crocodile Feet DEMO.otf");

        enunciadoSubir = findViewById(R.id.enunciadoSubir);
        txtNombreAnimalSubir = findViewById(R.id.txtNombreAnimalSubir);
        txtTipoAnimalSubir = findViewById(R.id.txtTipoAnimalSubir);
        txtRazaAnimalSubir = findViewById(R.id.txtRazaAnimalSubir);
        txtDescripcionAnimalSubir = findViewById(R.id.txtDescripcionAnimalSubir);
        txtEdadAnimalSubir = findViewById(R.id.txtEdadAnimalSubir);
        txtGeneroAnimalSubir= findViewById(R.id.txtGeneroAnimalSubir);
        txtVacunaAnimalSubir = findViewById(R.id.txtVacunaAnimalSubir);
        txtChipAnimalSubir = findViewById(R.id.txtChipAnimalSubir);
        rbGeneroMAnimalSubir = findViewById(R.id.rbGeneroMAnimalSubir);
        rbGeneroHAnimalSubir = findViewById(R.id.rbGeneroHAnimalSubir);
        rbVacunaSAnimalSubir = findViewById(R.id.rbVacunaSAnimalSubir);
        rbVacunaNAnimalSubir = findViewById(R.id.rbVacunaNAnimalSubir);
        rbChipSAnimalSubir = findViewById(R.id.rbChipSAnimalSubir);
        rbChipNAnimalSubir = findViewById(R.id.rbChipNAnimalSubir);
        btnSubirAnimal = findViewById(R.id.btnSubirAnimal);
        btnAvatar = findViewById(R.id.btnAvatar);
        spTipoAnimalSubir = findViewById(R.id.spTipoAnimalSubir);
        edtNombreAnimalSubir = findViewById(R.id.edtNombreAnimalSubir);
        edtRazaAnimalSubir = findViewById(R.id.edtRazaAnimalSubir);
        edtDescripcionAnimalSubir = findViewById(R.id.edtDescripcionAnimalSubir);
        edtEdadAnimalSubir = findViewById(R.id.edtEdadAnimalSubir);
        imgSubirAnimal = findViewById(R.id.imgSubirAnimal);

        // Ponemos las fuentes
        enunciadoSubir.setTypeface(enunciado);
        txtNombreAnimalSubir.setTypeface(texto);
        txtTipoAnimalSubir.setTypeface(texto);
        txtRazaAnimalSubir.setTypeface(texto);
        txtDescripcionAnimalSubir.setTypeface(texto);
        txtEdadAnimalSubir.setTypeface(texto);
        txtGeneroAnimalSubir.setTypeface(texto);
        txtVacunaAnimalSubir.setTypeface(texto);
        txtChipAnimalSubir.setTypeface(texto);
        rbGeneroMAnimalSubir.setTypeface(texto);
        rbGeneroHAnimalSubir.setTypeface(texto);
        rbVacunaSAnimalSubir.setTypeface(texto);
        rbVacunaNAnimalSubir.setTypeface(texto);
        rbChipSAnimalSubir.setTypeface(texto);
        rbChipNAnimalSubir.setTypeface(texto);
        btnSubirAnimal.setTypeface(texto);
        btnAvatar.setTypeface(texto);

        // Adaptamos los datos a un spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.tipoanimal, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTipoAnimalSubir.setAdapter(adapter);

        btnAvatar.setOnClickListener(this);
    }

    public void subirNuevoAnimal(View v){
        if(comprobarNombre() && comprobarEdad() && comprobarRaza() && comprobarDescripcion() && comprobarImg()
                && comprobarChip() && comprobarGenero() && comprobarVacuna()){
            String nombre = edtNombreAnimalSubir.getText().toString();
            String tipo = spTipoAnimalSubir.getSelectedItem().toString();
            String raza = edtRazaAnimalSubir.getText().toString();
            String descripcion = edtDescripcionAnimalSubir.getText().toString();
            String edad = edtEdadAnimalSubir.getText().toString();
            String vacuna = "0";
            String chip = "0";
            String genero = "Hembra";

            if(rbVacunaSAnimalSubir.isChecked()){
                vacuna = "1";
            }
            if(rbChipSAnimalSubir.isChecked()){
                chip = "1";
            }
            if(rbGeneroMAnimalSubir.isChecked()){
                genero = "Macho";
            }


            Toast toast1 = Toast.makeText(getApplicationContext(),
                    iduser , Toast.LENGTH_SHORT);

            toast1.show();

            if (!iduser.equals("0")) {
                url = "https://www.losnaranjosdam.online/2dam/ruben/adoptales/subirAnimal.php?nombre="+nombre+"&tipo="+tipo+
                        "&raza="+raza+"&descripcion="+descripcion+"&imganimal=img1&edad="+edad+
                        "&iduser="+iduser+"&genero="+genero+"&chip="+chip+"&vacuna="+vacuna;

                uploadImage();

                new GetPartida().execute();
            }
        }
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

            ParseJSON(respuesta);

            return null;
        }

        @Override
        protected void onPostExecute(Void resulte) {
            super.onPostExecute(resulte);

            if(result.equals("hecho")){
                Intent i = new Intent(subiranimal.this, cuenta.class);
                startActivity(i);

                Toast toast1 = Toast.makeText(getApplicationContext(),
                        "Se ha subido correctamente." , Toast.LENGTH_SHORT);

                toast1.show();
            }else{
                Toast toast1 = Toast.makeText(getApplicationContext(),
                        "La mascota no se ha podido subir correctamente" , Toast.LENGTH_SHORT);

                toast1.show();
            }
        }

    }

    private ArrayList<HashMap<String, String>> ParseJSON(String json) {
        if (json != null) {
            try {
                // Hashmap for ListView
                ArrayList<HashMap<String, String>> usuarioList = new ArrayList<>();

                JSONObject jsonObj = new JSONObject(json);

                // Getting JSON Array node
                JSONArray animales = jsonObj.getJSONArray("subir");

                // looping through All Students
                for (int i = 0; i < animales.length(); i++) {
                    JSONObject c = animales.getJSONObject(i);

                    result = c.getString("resu");

                    // tmp hashmap for single student
                    HashMap<String, String> animal = new HashMap<String, String>();

                    // adding each child node to HashMap key => value
                    animal.put("resu", result);

                    // adding student to students list
                    usuarioList.add(animal);
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

    public boolean comprobarGenero(){
        Boolean correcto;

        correcto = false;

        if(rbGeneroHAnimalSubir.isChecked() || rbGeneroMAnimalSubir.isChecked()){
            correcto = true;
        }else{
            Toast toast1 = Toast.makeText(getApplicationContext(),
                    "Error en el genero." , Toast.LENGTH_SHORT);

            toast1.show();
        }

        return correcto;
    }

    public boolean comprobarChip(){
        Boolean correcto;

        correcto = false;

        if(rbChipNAnimalSubir.isChecked() || rbChipSAnimalSubir.isChecked()){
            correcto = true;
        }else{
            Toast toast1 = Toast.makeText(getApplicationContext(),
                    "Error en el chip." , Toast.LENGTH_SHORT);

            toast1.show();
        }

        return correcto;
    }

    public boolean comprobarVacuna(){
        Boolean correcto;

        correcto = false;

        if(rbVacunaNAnimalSubir.isChecked() || rbVacunaSAnimalSubir.isChecked()){
            correcto = true;
        }else{
            Toast toast1 = Toast.makeText(getApplicationContext(),
                    "Error en la vacuna." , Toast.LENGTH_SHORT);

            toast1.show();
        }

        return correcto;
    }

    public boolean comprobarNombre(){

        String nombre;
        Boolean correcto;

        nombre = edtNombreAnimalSubir.getText().toString();
        correcto = false;

        if(!nombre.isEmpty() && nombre.length()>4){
            correcto = true;
        }else{
            Toast toast1 = Toast.makeText(getApplicationContext(),
                    "Error en el nombre." , Toast.LENGTH_SHORT);

            toast1.show();
        }

        return correcto;

    }

    public boolean comprobarEdad(){

        String edad;
        int edadNumero;
        Boolean correcto = false;

        try {
            edad = edtEdadAnimalSubir.getText().toString();
            edadNumero = Integer.parseInt(edtEdadAnimalSubir.getText().toString());
            correcto = false;

            if (!edad.isEmpty() && edadNumero > 0 && edadNumero < 60) {
                correcto = true;
            } else {
                Toast toast1 = Toast.makeText(getApplicationContext(),
                        "Error en la edad.", Toast.LENGTH_SHORT);

                toast1.show();
            }

        }catch (NumberFormatException e){
            Toast toast1 = Toast.makeText(getApplicationContext(),
                    "Error en la edad.", Toast.LENGTH_SHORT);

            toast1.show();
        }

        return correcto;

    }

    public boolean comprobarDescripcion(){
        String descripcion;
        Boolean correcto;

        descripcion = edtDescripcionAnimalSubir.getText().toString();
        correcto = false;

        if(!descripcion.isEmpty() && descripcion.length()>10){
            correcto = true;
        }else{
            Toast toast1 = Toast.makeText(getApplicationContext(),
                    "Necesitamos una descripcion un poco mas larga." , Toast.LENGTH_SHORT);

            toast1.show();
        }

        return correcto;
    }

    public boolean comprobarRaza(){

        String raza;
        Boolean correcto;

        raza = edtRazaAnimalSubir.getText().toString();
        correcto = false;

        if(!raza.isEmpty() && raza.length()>4){
            correcto = true;
        }else{
            Toast toast1 = Toast.makeText(getApplicationContext(),
                    "Error en la raza." , Toast.LENGTH_SHORT);

            toast1.show();
        }

        return correcto;

    }

    public boolean comprobarImg(){

        Boolean correcto;

        correcto = true;
/**
        if(!imgSubirAnimal.getDrawable().equals(R.drawable.icono)){
            correcto = true;
        }else{
            Toast toast1 = Toast.makeText(getApplicationContext(),
                    "No has puesto una imagen." , Toast.LENGTH_SHORT);

            toast1.show();
        }*/

        return correcto;

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

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Imagen"), PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
//Cómo obtener el mapa de bits de la Galería
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
//Configuración del mapa de bits en ImageView
                imgSubirAnimal.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        if(v == btnAvatar){
            showFileChooser();
        }
    }

    public String getStringImagen(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void uploadImage(){
        //Mostrar el diálogo de progreso
        final ProgressDialog loading = ProgressDialog.show(this,"Subiendo...","Espere por favor...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Descartar el diálogo de progreso
                        loading.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Descartar el diálogo de progreso
                        loading.dismiss();

                        //Showing toast
                        Toast.makeText(subiranimal.this, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                bitmap = Bitmap.createScaledBitmap(bitmap,1000,800, false);

                //Convertir bits a cadena
                String imagen = getStringImagen(bitmap);

                //Creación de parámetros
                Map<String,String> params = new Hashtable<String, String>();
                //Agregando de parámetros
                params.put(KEY_IMAGEN, imagen);
                //Agregando de parámetros
                params.put("id", iduser+".png");

                //Parámetros de retorno
                return params;
            }
        };
        //Creación de una cola de solicitudes
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //Agregar solicitud a la cola
        requestQueue.add(stringRequest);
    }

}
