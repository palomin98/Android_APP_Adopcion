package com.adoptales.rubenpalomo.adoptales;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
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

public class registro extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener  {

    // TextView enunciado
    TextView enunciadoRegistro;

    // TextView del registro
    TextView txtNombreRegistro, txtNombreUserRegistro, txtEmailRegistro, txtPasswordRegistro
            , txtTelefonoRegistro, txtDireccionRegistro, txtNacimientoRegistro;

    // EditText del registro
    EditText edtNombreRegistro, edtNombreUserRegistro, edtEmailRegistro, edtPasswordRegistro,edtPasswordRepiteRegistro, edtTelefonoRegistro, edtDireccionRegistro;

    // Spinner del registro
    Spinner spDiaRegistro, spMesRegistro, spAnoRegistro;

    // Checbox del registro
    CheckBox chkAceptarTerminosRegistro;

    // Button del registro
    Button btnRegistrar, btnAvatar, btnVerTerminos;

    // Avatar
    ImageView imgSubirAnimal;

    // Fuentes
    Typeface enunciado, texto;

    // Registro
    SharedPreferences mPrefs;
    SharedPreferences.Editor myEditor;

    // Declaro variables
    String correo;
    String password;

    String contar;

    // Creo una sesion
    Session session;

    // URL to get contacts JSON
    private static String url;

    // Subir imagen
    private Bitmap bitmap;
    private int PICK_IMAGE_REQUEST = 1;
    private String UPLOAD_URL ="https://www.losnaranjosdam.online/2dam/ruben/adoptales/subirFotoAvatar.php";
    private String KEY_IMAGEN = "foto";

    Dialog terminos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        terminos = new Dialog(this);

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

        // Inicializamos los wigdets
        enunciadoRegistro = findViewById(R.id.enunciadoRegistro);
        txtNombreRegistro = findViewById(R.id.txtNombreRegistro);
        txtNombreUserRegistro = findViewById(R.id.txtNombreUserRegistro);
        txtEmailRegistro = findViewById(R.id.txtEmailRegistro);
        txtPasswordRegistro = findViewById(R.id.txtPasswordRegistro);
        txtTelefonoRegistro = findViewById(R.id.txtTelefonoRegistro);
        txtDireccionRegistro = findViewById(R.id.txtDireccionRegistro);
        txtNacimientoRegistro = findViewById(R.id.txtNacimientoRegistro);
        btnRegistrar = findViewById(R.id.btnRegistrar);
        btnAvatar = findViewById(R.id.btnAvatar);
        btnVerTerminos = findViewById(R.id.btnVerTerminos);
        imgSubirAnimal = findViewById(R.id.imgSubirAnimal);
        spDiaRegistro = findViewById(R.id.spDiaRegistro);
        spMesRegistro = findViewById(R.id.spMesRegistro);
        spMesRegistro = findViewById(R.id.spMesRegistro);
        spAnoRegistro = findViewById(R.id.spAnoRegistro);
        edtNombreRegistro = findViewById(R.id.edtNombreRegistro);
        edtNombreUserRegistro = findViewById(R.id.edtNombreUserRegistro);
        edtEmailRegistro = findViewById(R.id.edtEmailRegistro);
        edtPasswordRegistro = findViewById(R.id.edtPasswordRegistro);
        edtPasswordRepiteRegistro = findViewById(R.id.edtPasswordRepiteRegistro);
        edtTelefonoRegistro = findViewById(R.id.edtTelefonoRegistro);
        edtDireccionRegistro = findViewById(R.id.edtDireccionRegistro);
        chkAceptarTerminosRegistro = findViewById(R.id.chkAceptarTerminosRegistro);

        // Ponemos las fuentes
        enunciadoRegistro.setTypeface(enunciado);
        txtNombreRegistro.setTypeface(texto);
        txtNombreUserRegistro.setTypeface(texto);
        txtEmailRegistro.setTypeface(texto);
        txtPasswordRegistro.setTypeface(texto);
        btnVerTerminos.setTypeface(texto);
        txtTelefonoRegistro.setTypeface(texto);
        txtDireccionRegistro.setTypeface(texto);
        txtNacimientoRegistro.setTypeface(texto);
        btnRegistrar.setTypeface(texto);
        btnAvatar.setTypeface(texto);
        chkAceptarTerminosRegistro.setTypeface(texto);


        // Creamos una array de años
        String [] anos = new String[60];
        int ano = 2003;
        for(int i = 0; i < anos.length; i++){
            anos[i] = String.valueOf(ano);
            ano--;
        }

        // Adaptamos los datos a un spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.dias, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDiaRegistro.setAdapter(adapter);

        // Adaptamos los datos a un spinner
        adapter = ArrayAdapter.createFromResource(this, R.array.meses, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMesRegistro.setAdapter(adapter);

        // Adaptamos los datos a un spinner
        spAnoRegistro.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, anos));
        btnAvatar.setOnClickListener(this);

    }

    public void VerTerminos(View v){
        terminos.setContentView(R.layout.contenido_terminos);
        Button btnAtrasTerminos = terminos.findViewById(R.id.btnAtrasTerminos);
        TextView txtEnunciadoTerminos = terminos.findViewById(R.id.txtEnunciadoTerminos);

        btnAtrasTerminos.setTypeface(texto);
        txtEnunciadoTerminos.setTypeface(enunciado);
        btnAtrasTerminos.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                terminos.dismiss();
            }
        });

        terminos.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        terminos.show();
    }

    public boolean comprobarNombre(){
        String nombre;
        Boolean correcto;

        nombre = edtNombreRegistro.getText().toString();
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

    public boolean comprobarNombreUsuario(){
        String nombreUser;
        Boolean correcto;

        nombreUser = edtNombreUserRegistro.getText().toString();
        correcto = false;

        if(!nombreUser.isEmpty() && nombreUser.length()>4){
            correcto = true;
        }else{
            Toast toast1 = Toast.makeText(getApplicationContext(),
                    "Error en el nombre de usuario." , Toast.LENGTH_SHORT);

            toast1.show();
        }

        return correcto;
    }

    public boolean comprobarEmail(){
        String email;
        Boolean correcto;

        email = edtEmailRegistro.getText().toString();
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
        String passwordRepite;

        char [] cPassword;
        char [] cMayusculas;
        char [] cNumeros;

        Boolean correcto;
        Boolean mayuscula;
        Boolean numero;


        cNumeros = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        cMayusculas = new char[]{'0','1','2','3','4','5','6','7','8','9'};

        password = edtPasswordRegistro.getText().toString();
        passwordRepite = edtPasswordRepiteRegistro.getText().toString();
        cPassword = password.toCharArray();
        correcto = false;
        mayuscula = false;
        numero = false;

        if(password.equals(passwordRepite)){
            for(int i = 0; i < cPassword.length; i++){
                for(int f = 0; f < cNumeros.length; f++){
                    if(cPassword[i]==cNumeros[f]){
                        numero = true;
                    }
                }

                for(int f = 0; f < cMayusculas.length; f++){
                    if(cPassword[i]==cMayusculas[f]){
                        mayuscula = true;
                    }
                }
            }

            if(!password.isEmpty() && password.length()>6 && mayuscula && numero){
                correcto = true;
            }else{
                Toast toast1 = Toast.makeText(getApplicationContext(),
                        "Error en la contraseña." , Toast.LENGTH_SHORT);

                toast1.show();
            }
        }else{
            Toast toast1 = Toast.makeText(getApplicationContext(),
                    "Las contraseñas no coinciden." , Toast.LENGTH_SHORT);

            toast1.show();
        }
        return correcto;
    }

    public boolean comprobarTelefono(){
        String telefono;
        Boolean correcto;

        telefono = edtTelefonoRegistro.getText().toString();
        correcto = false;

        if(!telefono.isEmpty() && telefono.length()==9){
            correcto = true;
        }else{
            Toast toast1 = Toast.makeText(getApplicationContext(),
                    "Error en telefono." , Toast.LENGTH_SHORT);

            toast1.show();
        }

        return correcto;
    }

    public boolean comprobarDireccion(){
        String direcion;
        Boolean correcto;

        direcion = edtDireccionRegistro.getText().toString();
        correcto = false;

        if(!direcion.isEmpty() && direcion.length()>5){
            correcto = true;
        }else{
            Toast toast1 = Toast.makeText(getApplicationContext(),
                    "Error en la direccion." , Toast.LENGTH_SHORT);

            toast1.show();
        }

        return correcto;
    }

    public boolean nacimiento(){
        int ano;
        Boolean correcto;

        correcto = false;

        ano = Integer.parseInt(spAnoRegistro.getSelectedItem().toString());

        if(ano <= 1999){
            correcto = true;
        }else{
            Toast toast1 = Toast.makeText(getApplicationContext(),
                    "Error en el nacimiento, mayor de 18 años" , Toast.LENGTH_SHORT);

            toast1.show();
        }

        return correcto;
    }

    public void registrarse(View v){
        if(comprobarNombre() && comprobarNombreUsuario() && comprobarEmail() && comprobarPassword()
                && comprobarTelefono() && comprobarDireccion() && chkAceptarTerminosRegistro.isChecked()
                && nacimiento()){

            String nombre = edtNombreRegistro.getText().toString();
            String nombreusuario = edtNombreUserRegistro.getText().toString();
            String email = edtEmailRegistro.getText().toString();
            String password = edtPasswordRegistro.getText().toString();
            String telefono = edtTelefonoRegistro.getText().toString();
            String direccion = edtDireccionRegistro.getText().toString();
            String img = "";
            String nacimiento = spDiaRegistro.getSelectedItem().toString() +
                    spMesRegistro.getSelectedItem().toString() + spAnoRegistro.getSelectedItem().toString();

            url = "https://www.losnaranjosdam.online/2dam/ruben/adoptales/registro.php?nombre="+nombre+"&nombreusuario="+nombreusuario+
                    "&email="+email+"&password="+password+"&nacimiento="+nacimiento+"&telefono="+telefono+
                    "&imgperfil="+img+"&direccion="+direccion;

            uploadImage();

            // Ejecutamos el metodo GetMeme
            new GetPartida().execute();
        }else{
            Toast toast1 = Toast.makeText(getApplicationContext(),
                    "No se ha podido registar." , Toast.LENGTH_SHORT);

            toast1.show();
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
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if(contar.equals("hecho")){
                Intent i = new Intent(registro.this, login.class);
                startActivity(i);

                enviarCorreo();

                Toast toast1 = Toast.makeText(getApplicationContext(),
                        "Registro completado, te hemos enviado un correo." , Toast.LENGTH_SHORT);

                toast1.show();
            }else{
                Toast toast1 = Toast.makeText(getApplicationContext(),
                        "El email o usuario ya existe." , Toast.LENGTH_SHORT);

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
                JSONArray animales = jsonObj.getJSONArray("registro");

                // looping through All Students
                for (int i = 0; i < animales.length(); i++) {
                    JSONObject c = animales.getJSONObject(i);

                    contar = c.getString("contar");

                    // tmp hashmap for single student
                    HashMap<String, String> animal = new HashMap<String, String>();

                    // adding each child node to HashMap key => value
                    animal.put("contar", contar);

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

    public void enviarCorreo(){

        // pongo email y contraseña
        correo = "";
        password = "";

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
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(edtEmailRegistro.getText().toString()));

                // Mensaje y su forma
                message.setContent("Buenas "+edtNombreUserRegistro.getText().toString()
                        +", hemos registrado tu correo "+ edtEmailRegistro.getText().toString()
                        +" en nuestros servidores, esperemos ser de gran ayuda a la hora de encontra su animal.", "text/html; charset=utf-8");

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
            i = new Intent(this, registro.class);
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
                        Toast.makeText(registro.this, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                bitmap = Bitmap.createScaledBitmap(bitmap,700,500, false);

                //Convertir bits a cadena
                String imagen = getStringImagen(bitmap);

                //Creación de parámetros
                Map<String,String> params = new Hashtable<String, String>();
                //Agregando de parámetros
                params.put(KEY_IMAGEN, imagen);

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
