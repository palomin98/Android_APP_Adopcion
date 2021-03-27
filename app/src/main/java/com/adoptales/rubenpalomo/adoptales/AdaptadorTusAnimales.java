package com.adoptales.rubenpalomo.adoptales;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by Ruben Palomo on 13/12/2017.
 */
class AdaptadorTusAnimales extends ArrayAdapter<String> {

    // Atributos del objeto
    private final Activity context;
    private final String[] nombreMeme;
    private final String[] imgAnimal;
    Typeface texto;

    /**
     * Constructor
     * @param context
     * @param nombreMeme
     */
    public AdaptadorTusAnimales(Activity context, String[] nombreMeme, String[] imgAnimal, Typeface texto) {
        super(context, R.layout.listaanimales, nombreMeme);
        // TODO Auto-generated constructor stub

        // Actualizamos el contenido
        this.context=context;
        this.nombreMeme=nombreMeme;
        this.imgAnimal=imgAnimal;
        this.texto = texto;
    }

    /**
     * Ponemos la vista
     * @param posicion
     * @param view
     * @param parent
     * @return
     */
    public View getView(int posicion,View view, ViewGroup parent){

        // Creamos una variable de tipo LayoutInflater y lo igualamos metodo de la clase Activity
        LayoutInflater inflater = LayoutInflater.from(context);
        // Hacemos una vista en el listView
        View rowView=inflater.inflate(R.layout.listaanimales,null);

        // Inizializamos los widget
        TextView txtTitle = rowView.findViewById(R.id.nombreLista);
        ImageView imageView = rowView.findViewById(R.id.img);

        // Ponemos el texto en el textView
        txtTitle.setText(nombreMeme[posicion]);
        txtTitle.setTypeface(texto);

        Picasso.with(context)
                .load("https://www.losnaranjosdam.online/2dam/ruben/adoptales/fotos/"+ imgAnimal[posicion])
                .error(R.mipmap.ic_launcher)
                .into(imageView);

        // Retornamos la vista
        return rowView;
    }
}