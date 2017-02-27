package chema.egea.canales.EjercicioSQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by chema on 14/12/2015.
 */
public class UsuariosSQLiteHelper extends android.database.sqlite.SQLiteOpenHelper
{

    String sqlCreate = "CREATE TABLE USUARIOS(_id INTEGER PRIMARY KEY, user_nombre TEXT NOT NULL, user_contrasena TEXT NOT NULL, user_nombreReal TEXT NOT NULL)";

    public static int m_version = 1;
    public static String m_name = "DBUsuarios" ;
    private static SQLiteDatabase.CursorFactory m_factory = null;

    UsuariosSQLiteHelper(Context contexto)
    {
        super(contexto,m_name,m_factory,m_version);
    }

    UsuariosSQLiteHelper(Context contexto, String nombre, SQLiteDatabase.CursorFactory cursor, int version)
    {
        super(contexto,nombre,cursor,version);
    }
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        Log.i(this.getClass().toString(), "Creando base de datos");
        db.execSQL(sqlCreate);
        db.execSQL( "CREATE UNIQUE INDEX user_nombre ON USUARIOS(user_nombre ASC)" );
        Log.i(this.getClass().toString(), "Tabla USUARIOS creada");

         /*
    * Insertamos datos iniciales
    */
        db.execSQL("INSERT INTO USUARIOS(_id, user_nombre, user_contrasena, user_nombreReal) VALUES(1,'El Botas', 'converse', 'Gato')"       );
        db.execSQL("INSERT INTO USUARIOS(_id, user_nombre, user_contrasena, user_nombreReal) VALUES(2,'Da Homa', 'donut', 'Homer')"          );
        db.execSQL("INSERT INTO USUARIOS(_id, user_nombre, user_contrasena, user_nombreReal) VALUES(3,'Milhe', 'gtaviladrau', 'Milhouse')"   );
        db.execSQL("INSERT INTO USUARIOS(_id, user_nombre, user_contrasena, user_nombreReal) VALUES(4,'Morgan', 'mayor', 'Marge')"           );
        db.execSQL("INSERT INTO USUARIOS(_id, user_nombre, user_contrasena, user_nombreReal) VALUES(5,'Laisy', 'vegetariana', 'Lisa')"       );
        db.execSQL("INSERT INTO USUARIOS(_id, user_nombre, user_contrasena, user_nombreReal) VALUES(6,'Bort', 'amante', 'Bart')"             );
        db.execSQL("INSERT INTO USUARIOS(_id, user_nombre, user_contrasena, user_nombreReal) VALUES(7,'Fransha', 'tonto', 'Flanders')"       );

        Log.i(this.getClass().toString(), "Datos iniciales USUARIOS insertados");

        Log.i(this.getClass().toString(), "Base de datos creada");

        // ******************************************************************
        //                      QUITAR ESTO
        // ******************************************************************
        //upgrade_2(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS USUARIOS");
        db.execSQL(sqlCreate);
        db.execSQL( "CREATE UNIQUE INDEX user_nombre ON USUARIOS(user_nombre ASC)" );

        db.execSQL("INSERT INTO USUARIOS(_id, user_nombre, user_contrasena, user_nombreReal) VALUES(1,'El Botas', 'converse', 'Gato')"       );
        db.execSQL("INSERT INTO USUARIOS(_id, user_nombre, user_contrasena, user_nombreReal) VALUES(2,'Da Homa', 'donut', 'Homer')"          );
        db.execSQL("INSERT INTO USUARIOS(_id, user_nombre, user_contrasena, user_nombreReal) VALUES(3,'Milhe', 'gtaviladrau', 'Milhouse')"   );
        db.execSQL("INSERT INTO USUARIOS(_id, user_nombre, user_contrasena, user_nombreReal) VALUES(4,'Morgan', 'mayor', 'Marge')"           );
        db.execSQL("INSERT INTO USUARIOS(_id, user_nombre, user_contrasena, user_nombreReal) VALUES(5,'Laisy', 'vegetariana', 'Lisa')"       );
        db.execSQL("INSERT INTO USUARIOS(_id, user_nombre, user_contrasena, user_nombreReal) VALUES(6,'Bort', 'amante', 'Bart')"             );
        db.execSQL("INSERT INTO USUARIOS(_id, user_nombre, user_contrasena, user_nombreReal) VALUES(7,'Fransha', 'tonto', 'Flanders')"       );

        // Actualización a versión 2
        if (oldVersion < 2)
        {
            upgrade_2(db);
        }
    }

    private void upgrade_2(SQLiteDatabase db)
    {
        db.execSQL("ALTER TABLE USUARIOS ADD user_email TEXT");
        Log.e(this.getClass().toString(), "Actualización versión 2 finalizada");
        m_version=2;
    }


}
