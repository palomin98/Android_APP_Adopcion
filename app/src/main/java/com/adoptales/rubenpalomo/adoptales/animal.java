package com.adoptales.rubenpalomo.adoptales;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import de.hdodenhof.circleimageview.CircleImageView;

public class animal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // Fuentes
    Typeface enunciado, texto;

    // TextView del refugio
    TextView txtNombreAnimal, txtEdadAnimal, txtEmailAnimal, txtEmail, txtMensaje, txtDescripcionAnimal, txtTipoAnimal
            , txtNumeroAnimal, txtChip, txtVacuna, txtGenero, txtInformacion, txtInfoAnimal;

    // Button del refugio
    Button btnEnviarAnimal;

    LinearLayout ly1, ly2, ly3, ly4;

    // ImageView del refugio
    ImageView imgAnimal;

    // EditText de contacto
    EditText edtMensaje, edtEmail;

    String id;
    String nombre;
    String raza;
    String tipo;
    String descripcion;
    String imganimal;
    String edad;
    String iduser;
    String chip;
    String vacuna;
    String genero;

    private static final String TAG_USUARIOSINFO = "usuario";
    private static final String TAG_TELEFONO = "telefono";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_DIRECCION = "direccion";

    // Declaro variables
    String correo;
    String password;
    // Creo una sesion
    Session session;

    String email;
    String telefono;
    String direccion;

    // Registro
    SharedPreferences mPrefs;
    SharedPreferences.Editor myEditor;

    private static String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal);
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

        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth() / 4;  // obsoleto (deprecated)

        // Si esta logueado
        if(logeado == true) {
            navigationView.inflateMenu(R.menu.activity_pagina_conregistro_drawer);
        }else{
            navigationView.inflateMenu(R.menu.activity_pagina_sinregistro_drawer);
        }

        ly1 = findViewById(R.id.ly1);
        ly2 = findViewById(R.id.ly2);
        ly3 = findViewById(R.id.ly3);
        ly4 = findViewById(R.id.ly4);

        ly1.getLayoutParams().width=width;
        ly2.getLayoutParams().width=width;
        ly3.getLayoutParams().width=width;
        ly4.getLayoutParams().width=width;

        // Inicializamos las fuentes
        enunciado = Typeface.createFromAsset(getAssets(),"fonts/Happy-Camper-Regular.ttf");
        texto = Typeface.createFromAsset(getAssets(),"fonts/Crocodile Feet DEMO.otf");
        Typeface texto2 = Typeface.createFromAsset(getAssets(),"fonts/secrcode.ttf");

        // Inicializamos los widgets
        txtNombreAnimal = findViewById(R.id.txtNombreAnimal);
        txtEdadAnimal = findViewById(R.id.txtEdadAnimal);
        txtTipoAnimal = findViewById(R.id.txtTipoAnimal);
        txtEmailAnimal = findViewById(R.id.txtEmailAnimal);
        txtNumeroAnimal = findViewById(R.id.txtNumeroAnimal);
        txtEmail = findViewById(R.id.txtEmail);
        txtMensaje = findViewById(R.id.txtMensaje);
        txtDescripcionAnimal = findViewById(R.id.txtDescripcionAnimal);
        txtChip = findViewById(R.id.txtChip);
        txtVacuna = findViewById(R.id.txtVacuna);
        txtGenero = findViewById(R.id.txtGenero);
        txtInformacion = findViewById(R.id.txtInformacion);
        txtInfoAnimal = findViewById(R.id.txtInfoAnimal);
        edtMensaje = findViewById(R.id.edtMensaje);
        edtEmail = findViewById(R.id.edtEmail);
        btnEnviarAnimal = findViewById(R.id.btnEnviarAnimal);
        imgAnimal = findViewById(R.id.imgAnimal);

        // Ponemos las fuentes
        txtNombreAnimal.setTypeface(enunciado);
        txtEdadAnimal.setTypeface(texto);
        txtEmail.setTypeface(texto);
        txtMensaje.setTypeface(texto);
        txtInformacion.setTypeface(texto);
        txtInfoAnimal.setTypeface(texto);
        txtVacuna.setTypeface(texto);
        txtGenero.setTypeface(texto);
        txtChip.setTypeface(texto);
        btnEnviarAnimal.setTypeface(texto);
        txtEmailAnimal.setTypeface(texto2);
        txtTipoAnimal.setTypeface(texto2);
        txtDescripcionAnimal.setTypeface(texto2);
        txtNumeroAnimal.setTypeface(texto2);

        // Cogemos los extras que nos pasan en la anterio activity
        Bundle extras = getIntent().getExtras();

        // Si los extras no son nulos
        if (extras != null) {
            // Guardo los datos que tengan como clave ano
            id = extras.getString("id");
            nombre = extras.getString("nombre");
            raza = extras.getString("raza");
            tipo = extras.getString("tipo");
            descripcion = extras.getString("descripcion");
            imganimal = extras.getString("imgAnimal");
            edad = extras.getString("edad");
            iduser = extras.getString("iduser");
            chip = extras.getString("chip");
            vacuna = extras.getString("vacuna");
            genero = extras.getString("genero");
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

        // Ponemos las fuentes
        txtNombreAnimal.setText(nombre);
        txtTipoAnimal.setText("- "+tipo+", "+ raza);
        txtEdadAnimal.setText(edad);
        txtDescripcionAnimal.setText("- "+descripcion);
        txtChip.setText(chip);
        txtVacuna.setText(vacuna);
        txtGenero.setText(genero);

        // Descargamos la imagen
        Picasso.with(this)
                .load("https://www.losnaranjosdam.online/2dam/ruben/adoptales/fotos/"+ imganimal)
                .error(R.mipmap.ic_launcher)
                .into(imgAnimal);

        // Ponemos la url
        url = "https://www.losnaranjosdam.online/2dam/ruben/adoptales/datosAnimalUsuario.php?iduser="+ iduser;

        // Ejecutamos el metodo GetDatosDueno
        new GetDatosDueno().execute();
    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class GetDatosDueno extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creamos una instancia de la clase WebRequest
            WebRequest webreq = new WebRequest();

            // Obtenemos el json se los datos
            String jsonStr = webreq.makeWebServiceCall(url, WebRequest.GET);

            // Mostramos el json en el terminal
            Log.d("Response: ", "> " + jsonStr);

            // Ejecutamos el metodo que nos convierte el json
            ParseJSON(jsonStr);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            // Ponemos el email del animal.
            txtEmailAnimal.setText("- "+ email);
            txtNumeroAnimal.setText("- "+ telefono);

        }
    }

    /**
     * Metodo de analizar el json
     * @param json
     */
    private void ParseJSON(String json) {
        // Si es nulo
        if (json != null) {
            // Intentamos
            try {
                // Creamos un objeto JSONObject
                JSONObject jsonObj = new JSONObject(json);

                // Obtenemos el nodo del json pasado por parametro
                JSONArray usuario = jsonObj.getJSONArray(TAG_USUARIOSINFO);

                // Recorremos todos los usuarios
                for (int i = 0; i < usuario.length(); i++) {
                    // Obtenemos un elemento json
                    JSONObject c = usuario.getJSONObject(i);

                    // Obtenemos los datos de el elemento json
                    telefono = c.getString(TAG_TELEFONO);
                    email = c.getString(TAG_EMAIL);
                    direccion = c.getString(TAG_DIRECCION);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            // Mensaje de error por el terminal
            Log.e("ServiceHandler", "Couldn't get any data from the url");
        }
    }

    /**
     * Comparamos el email
     * @return
     */
    public boolean compararEmail(){

        // Declaramos variables
        String email;
        Boolean correcto;

        // Inicializamos variables
        email = edtEmail.getText().toString();
        correcto = false;

        // Si no esta vacio o es igual que un email
        if(!email.isEmpty() &&
                android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            // Actualizamos variable
            correcto = true;
        }else{
            // Mensaje de error
            Toast toast1 = Toast.makeText(getApplicationContext(),
                    "Error en el email." , Toast.LENGTH_SHORT);

            toast1.show();
        }

        return correcto;
    }

    public boolean compararMensaje(){

        // Declaramos variables
        String mensaje;
        Boolean correcto;

        // Inicializamos variables
        mensaje = edtMensaje.getText().toString();
        correcto = false;

        // Si no esta vacio
        if(!mensaje.isEmpty()){
            // Actualizamos variable
            correcto = true;
        }else{
            // Mensaje de error
            Toast toast1 = Toast.makeText(getApplicationContext(),
                    "Error en el mensaje." , Toast.LENGTH_SHORT);

            toast1.show();
        }

        return correcto;
    }

    /**
     * Enviamos el correo al dueño
     * @param v
     */
    public void enviarCorreo(View v){

        // Si los dos metodos son correctos
        if(compararEmail() && compararMensaje()){
            // Pongo email y contraseña
            correo = "adoptalescontacto@gmail.com";
            password = "Ru83nP1l0m03105";

            // Mensaje correcto
            Toast toast1 = Toast.makeText(getApplicationContext(),
                    "Espere estamos enviando el correo.", Toast.LENGTH_SHORT);
            toast1.show();

            // Ponemos las politica
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            // Creamos unas propiedades
            Properties properties = new Properties();
            // ponemos la conexion
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.host", "smtp.gmail.com");
            properties.put("mail.smtp.port", "587");


            // Intentamos
            try {
                // Inicializamos la sesion
                session = Session.getDefaultInstance(properties, new Authenticator() {
                    @Override
                    // Autenticacion
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(correo, password);
                    }
                });

                // Si la sesion es erronea
                if (session != null) {
                    // Iniciamos sesion
                    Message message = new MimeMessage(session);

                    // Quien lo envia
                    message.setFrom(new InternetAddress(correo));

                    // Titulo
                    message.setSubject("Mensaje de contacto");

                    // Quien lo recibe
                    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));

                    // Mensaje y su forma
                    message.setContent("Buenas, con email " + edtEmail.getText().toString() +
                            "<br>Te quiero decir: "
                            + edtMensaje.getText().toString(), "text/html; charset=utf-8");

                    // Enviamos el mensaje
                    Transport.send(message);

                }

                // Mensaje correcto
                Toast toast2 = Toast.makeText(getApplicationContext(),
                        "Correo enviado.", Toast.LENGTH_SHORT);

                toast2.show();
            } catch (AddressException e) {
                e.printStackTrace();
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }else{
            // Mensaje de error
            Toast toast1 = Toast.makeText(getApplicationContext(),
                    "Correo no enviado." , Toast.LENGTH_SHORT);

            toast1.show();
        }
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

            // Inicializamos el intento
            i = new Intent(this, paginaPrincipal.class);
            // Emepezamos la activity
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
        i.setData(Uri.parse("tel:"+telefono));
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
                    Toast.makeText(animal.this, "The app was not allowed to call.", Toast.LENGTH_LONG).show();
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
            if (!hasPermissions(animal.this, PERMISSIONS)) {
                ActivityCompat.requestPermissions(animal.this, PERMISSIONS, REQUEST );
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
        Uri gmmIntentUri = Uri.parse("geo:0,0?q="+direccion);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }
}
