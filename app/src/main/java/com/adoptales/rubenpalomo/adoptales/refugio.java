package com.adoptales.rubenpalomo.adoptales;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class refugio extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // Fuentes
    Typeface enunciado, texto;

    // TextView del refugio
    TextView txtNombreRefugio, txtNumeroRefugio, txtDireccionRefugio, txtEmailRefugio, txtInformacion
            , txtEmail ,txtMensaje;

    // ImageView del refugio
    ImageView imgRefugio;

    // Registro
    SharedPreferences mPrefs;
    SharedPreferences.Editor myEditor;

    String idRefugio;
    String nomRefugio;
    String imgRef;
    String emailRefugio;
    String telefonoRefugio;
    String direccionRefugio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refugio);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

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
        Typeface texto2 = Typeface.createFromAsset(getAssets(),"fonts/secrcode.ttf");

        // Inicializamos los widgets
        txtNombreRefugio = findViewById(R.id.txtNombreRefugio);
        txtNumeroRefugio = findViewById(R.id.txtNumeroRefugio);
        txtDireccionRefugio = findViewById(R.id.txtDireccionRefugio);
        txtEmailRefugio = findViewById(R.id.txtEmailRefugio);
        imgRefugio = findViewById(R.id.imgRefugio);
        txtInformacion = findViewById(R.id.txtInformacion);
        txtEmail = findViewById(R.id.txtEmail);
        txtMensaje = findViewById(R.id.txtMensaje);

        // Ponemos el tipo de letra
        txtNombreRefugio.setTypeface(enunciado);
        txtEmailRefugio.setTypeface(texto);
        txtInformacion.setTypeface(texto);
        txtEmail.setTypeface(texto);
        txtMensaje.setTypeface(texto);
        txtDireccionRefugio.setTypeface(texto2);
        txtNumeroRefugio.setTypeface(texto2);

        // Cogemos los extras que nos pasan en la anterio activity
        Bundle extras = getIntent().getExtras();

        // Si los extras no son nulos
        if (extras != null) {
            // Guardo los datos que tengan como clave ano
            idRefugio = extras.getString("id");
            nomRefugio = extras.getString("nombre");
            imgRef = extras.getString("img");
            emailRefugio = extras.getString("email");
            telefonoRefugio = extras.getString("telefono");
            direccionRefugio = extras.getString("direccion");
        } else {
            // Se queda vacio
            idRefugio = "";
            nomRefugio = "";
            imgRef = "";
            emailRefugio = "";
            telefonoRefugio = "";
            direccionRefugio = "";
        }

        // Ponemos las fuentes
        txtNombreRefugio.setText(nomRefugio);
        txtNumeroRefugio.setText("-"+ telefonoRefugio);
        txtDireccionRefugio.setText("-"+direccionRefugio);
        txtEmailRefugio.setText(emailRefugio);

        // Cogemos y ponemos la imagen del servidor
        Picasso.with(this)
                .load("https://www.losnaranjosdam.online/2dam/ruben/adoptales/fotosRefugio/"+ imgRef)
                .error(R.mipmap.ic_launcher)
                .into(imgRefugio);
    }

    public BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_llamar:
                    llamar();
                    return true;
                case R.id.navigation_maps:
                    maps();
                    return true;
            }
            return false;
        }
    };

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
            i = new Intent(this, refugio.class);
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

    /**
     * Se hace una llamada al contacto
     */
    @SuppressLint("MissingPermission")
    public void hacerLlamada() {
        // Hacemos un intento de llamada
        Intent i=new Intent(Intent.ACTION_CALL);
        // Llamamos al telefono
        i.setData(Uri.parse("tel:"+telefonoRefugio));
        // Inicializmaos la activitad
        startActivity(i);
    }

    /**
     * Compruebo los permisos
     * @param context
     * @param permissions
     * @return
     */
    private static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Compruebo los permisos aceptados
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        final int REQUEST = 112;
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    hacerLlamada();
                } else {
                    Toast.makeText(refugio.this, "The app was not allowed to call.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    /**
     * Comprueba que tod esta listo para llamar
     */
    public void llamar(){

        final int REQUEST = 112;
        if (Build.VERSION.SDK_INT >= 23) {
            // Pedimos los permisos
            String[] PERMISSIONS = {android.Manifest.permission.CALL_PHONE};
            if (!hasPermissions(refugio.this, PERMISSIONS)) {
                ActivityCompat.requestPermissions(refugio.this, PERMISSIONS, REQUEST );
            } else {
                hacerLlamada();
            }
        } else {
            hacerLlamada();
        }
    }

    /**
     * Ver la posicion en el mapa.
     */
    public void maps(){
        Uri gmmIntentUri = Uri.parse("geo:0,0?q="+direccionRefugio);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }
}
