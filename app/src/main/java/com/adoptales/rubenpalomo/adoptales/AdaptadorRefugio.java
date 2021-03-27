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
class AdaptadorRefugio extends ArrayAdapter<String> {

    // Atributos del objeto
    private final Activity context;
    private final String[] nombreRefugio;
    private final String[] imgRefugio;
    Typeface texto;

    /**
     * Constructor
     * @param context
     * @param nombreRefugio
     * @param imgRefugio
     */
    public AdaptadorRefugio(Activity context, String[] nombreRefugio, String[] imgRefugio, Typeface texto) {
        super(context, R.layout.listarefugio, nombreRefugio);
        // TODO Auto-generated constructor stub

        // Actualizamos el contenido
        this.context=context;
        this.nombreRefugio=nombreRefugio;
        this.imgRefugio=imgRefugio;
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
        LayoutInflater inflater=context.getLayoutInflater();
        // Hacemos una vista en el listView
        View rowView=inflater.inflate(R.layout.listarefugio,null,true);

        // Inizializamos los widget
        TextView txtTitle = rowView.findViewById(R.id.nombreListaRefugios);
        ImageView imageView = rowView.findViewById(R.id.img);

        // Ponemos el texto en el textView
        txtTitle.setText(nombreRefugio[posicion]);
        txtTitle.setTypeface(texto);

        // Cogemos y ponemos la imagen del servidor
        Picasso.with(context)
                .load("https://www.losnaranjosdam.online/2dam/ruben/adoptales/fotosRefugio/"+ imgRefugio[posicion])
                .error(R.mipmap.ic_launcher)
                .into(imageView);

        // Retornamos la vista
        return rowView;
    }
}