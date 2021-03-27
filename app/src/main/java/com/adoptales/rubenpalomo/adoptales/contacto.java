package com.adoptales.rubenpalomo.adoptales;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

public class contacto extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // Fuentes
    Typeface enunciado, texto;

    // TextView de contacto
    TextView enunciadoContacto, txtNombreContacto, txtEmail, txtMensaje;

    // Button de contacto
    Button btnEnviarContacto;

    // EditText de contacto
    EditText edtMensaje, edtEmail, edtNombreContacto;

    // Declaro variables
    String correo;
    String password;

    // Declaro los widget
    Button enviar;

    // Creo una sesion
    Session session;

    // Registro
    SharedPreferences mPrefs;
    SharedPreferences.Editor myEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacto);
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

        // Inicializamos las fuentes
        enunciado = Typeface.createFromAsset(getAssets(),"fonts/Happy-Camper-Regular.ttf");
        texto = Typeface.createFromAsset(getAssets(),"fonts/Crocodile Feet DEMO.otf");

        // Inicializamos los widgets
        enunciadoContacto = findViewById(R.id.enunciadoContacto);
        txtNombreContacto = findViewById(R.id.txtNombreContacto);
        txtEmail = findViewById(R.id.txtEmail);
        txtMensaje = findViewById(R.id.txtMensaje);
        edtMensaje = findViewById(R.id.edtMensaje);
        edtEmail = findViewById(R.id.edtEmail);
        edtNombreContacto = findViewById(R.id.edtNombreContacto);
        btnEnviarContacto = findViewById(R.id.btnEnviarContacto);

        // Ponemos las fuentes
        enunciadoContacto.setTypeface(enunciado);
        txtNombreContacto.setTypeface(texto);
        txtEmail.setTypeface(texto);
        txtMensaje.setTypeface(texto);
        btnEnviarContacto.setTypeface(texto);
    }

    public boolean compararNombre(){

        String nombre;
        Boolean correcto;

        nombre = edtNombreContacto.getText().toString();
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

    public boolean compararEmail(){

        String email;
        Boolean correcto;

        email = edtEmail.getText().toString();
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

    public boolean compararMensaje(){

        String mensaje;
        Boolean correcto;

        mensaje = edtMensaje.getText().toString();
        correcto = false;

        if(!mensaje.isEmpty()){
            correcto = true;
        }else{
            Toast toast1 = Toast.makeText(getApplicationContext(),
                    "Error en el mensaje." , Toast.LENGTH_SHORT);

            toast1.show();
        }

        return correcto;
    }


    public void enviarCorreo(View v){

        if(compararNombre() && compararEmail() && compararMensaje()){
            // pongo email y contraseña
            correo = "adoptalescontacto@gmail.com";
            password = "Ru83nP1l0m03105";

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

            // intentamos
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
                    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(correo));

                    // Mensaje y su forma
                    message.setContent("Buenas, soy " + edtNombreContacto.getText().toString() +
                            "<br>Email: " + edtEmail.getText().toString() +
                            "<br>Te quiero decir: "
                            + edtMensaje.getText().toString(), "text/html; charset=utf-8");

                    // Enviamos el mensaje
                    Transport.send(message);

                }

                Toast toast1 = Toast.makeText(getApplicationContext(),
                        "Correo enviado.", Toast.LENGTH_SHORT);

                toast1.show();
            } catch (AddressException e) {
                e.printStackTrace();
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }else{
            Toast toast1 = Toast.makeText(getApplicationContext(),
                    "Correo no enviado." , Toast.LENGTH_SHORT);

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
