package com.adoptales.rubenpalomo.adoptales;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * Created by Ruben Palomo on 16/03/2018.
 */

public class splashscreen extends Activity {

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);

        // Tiempo de delay de la activity
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                // Creamos un intento a la paginaPrincipal
                Intent i = new Intent(splashscreen.this, paginaPrincipal.class);
                // Empezamos la actividad
                startActivity(i);
                // Tiempo de espera
            }
        }, 1500);
    }
}
