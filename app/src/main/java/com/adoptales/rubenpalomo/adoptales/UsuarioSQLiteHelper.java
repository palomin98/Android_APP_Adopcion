package com.adoptales.rubenpalomo.adoptales;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Ruben Palomo on 13/12/2017.
 */
public class UsuarioSQLiteHelper extends SQLiteOpenHelper {

    // SQL para crea la tabla
    String crearTabla1 = "CREATE TABLE animales(id INTEGER PRIMARY KEY ,nombre TEXT, tipo TEXT, raza TEXT ,descripcion TEXT ,imganimal TEXT ,edad TEXT ,iduser TEXT ,vacunado TEXT ,chip TEXT ,genero TEXT)";
    String crearTabla2 = "CREATE TABLE refugios(id INTEGER PRIMARY KEY ,nombrerefugio TEXT, nombreadmin TEXT, email TEXT, telefono TEXT, imgperfil TEXT, direccion TEXT)";

    /**
     * @param context
     * @param name
     * @param factory
     * @param version
     */
    public UsuarioSQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /**
     * Si se ejecuta se crea la tabla
     * @param sqLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(crearTabla1);
        sqLiteDatabase.execSQL(crearTabla2);
    }

    /**
     * Cuando se actualiza la version se actualiza la base de datos
     * @param sqLiteDatabase
     * @param i
     * @param i1
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Borramos la tabla
        sqLiteDatabase.execSQL("Delete from animales");
        sqLiteDatabase.execSQL("Delete from refugios");

        // La quitamos si existe
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS animales");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS refugios");

        // Creamos la nueva table
        sqLiteDatabase.execSQL(crearTabla1);
        sqLiteDatabase.execSQL(crearTabla2);
    }
}
