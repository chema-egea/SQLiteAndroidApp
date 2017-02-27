package chema.egea.canales.EjercicioSQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by chema on 15/12/2015.
 */
public class UsuariosDBAdapter
{
    /**
     * Definimos constante con el nombre de la tabla
     */
    public static final String C_TABLA = "USUARIOS" ;

    /**
     * Definimos constantes con el nombre de las columnas de la tabla
     */
    public static final String C_COLUMNA_ID   = "_id";
    public static final String C_COLUMNA_NOMBRE = "user_nombre";
    public static final String C_COLUMNA_CONTRASENA = "user_contrasena";
    public static final String C_COLUMNA_NOMBREREAL = "user_nombreReal";
    public static final String C_COLUMNA_EMAIL = "user_email";

    private Context contexto;
    private UsuariosSQLiteHelper dbHelper;
    private SQLiteDatabase db;

    /**
     * Definimos lista de columnas de la tabla para utilizarla en las consultas a la base de datos
     */
    private String[] columnas = new String[]{ C_COLUMNA_ID, C_COLUMNA_NOMBRE, C_COLUMNA_CONTRASENA, C_COLUMNA_NOMBREREAL} ;
    private String[] columnasActualizadas = new String[]{ C_COLUMNA_ID, C_COLUMNA_NOMBRE, C_COLUMNA_CONTRASENA, C_COLUMNA_NOMBREREAL ,C_COLUMNA_EMAIL } ;

    public UsuariosDBAdapter(Context context)
    {
        this.contexto = context;
    }

    public UsuariosDBAdapter abrir() throws SQLException
    {
        dbHelper = new UsuariosSQLiteHelper(contexto);
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void cerrar()
    {
        dbHelper.close();
    }

    /**
     * Devuelve cursor con todos las columnas de la tabla
     */
    public Cursor getCursor() throws SQLException
    {
        Cursor c;
        if (UsuariosSQLiteHelper.m_version==2)
        {
            c = db.query(true, C_TABLA, columnasActualizadas, null, null, null, null, null, null);
        }
        else
        {
            c = db.query(true, C_TABLA, columnas, null, null, null, null, null, null);
        }
        return c;
    }

    /**
     * Inserta los valores en un registro de la tabla
     */
    public long insert(ContentValues reg)
    {
        if (db == null)
            abrir();

        return db.insert(C_TABLA, null, reg);
    }
    /**
     * Eliminar el registro con el identificador indicado
     */
    public long delete(long id)
    {
        if (db == null)
            abrir();

        return db.delete(C_TABLA, "_id=" + id, null);
    }
    /**
     * Modificar el registro
     */
    public long update(ContentValues reg)
    {
        long result = 0;

        if (db == null)
            abrir();

        if (reg.containsKey(C_COLUMNA_ID))
        {
            //
            // Obtenemos el id y lo borramos de los valores
            //
            long id = reg.getAsLong(C_COLUMNA_ID);

            reg.remove(C_COLUMNA_ID);

            //
            // Actualizamos el registro con el identificador que hemos extraido
            //
            result = db.update(C_TABLA, reg, "_id=" + id, null);
        }
        return result;
    }
    public void ActualizarVersionBD()
    {
        Log.e("Vamos a actualizar", "Estamos en UsuariosDBAdapter");
        dbHelper.onUpgrade(db, 1, 2);
    }
    public void CambiarNombreBD(String nuevoNombre)
    {
        Log.e("Nuevo nombre:", nuevoNombre);
        UsuariosSQLiteHelper.m_name = nuevoNombre;
        Log.e("Nombre tras cambio:", UsuariosSQLiteHelper.m_name);
    }
}
