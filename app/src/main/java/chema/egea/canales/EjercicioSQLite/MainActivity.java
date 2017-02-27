package chema.egea.canales.EjercicioSQLite;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class MainActivity extends AppCompatActivity {

    EditText nombreUsuario;
    EditText passwordUsuario;


    private UsuariosDBAdapter dbAdapter;
    private Cursor cursor;
    private UsuariosCursorAdapter usuariosAdapter ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Para salir de la aplicacion
        if( getIntent().getBooleanExtra("Exit me", false)){
            finish();
            return; // add this to prevent from doing unnecessary stuffs
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nombreUsuario = (EditText)findViewById(R.id.ET_usuario);
        passwordUsuario = (EditText)findViewById(R.id.ET_passAcceso);

        /*
        * Declaramos el controlador de la BBDD y accedemos en modo escritura
        */

        /*
        UsuariosSQLiteHelper dbHelper = new UsuariosSQLiteHelper(this, UsuariosSQLiteHelper.m_name, null, UsuariosSQLiteHelper.m_version);

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Toast.makeText(getBaseContext(), "Base de datos preparada", Toast.LENGTH_LONG).show();

        */
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Log.e("SP de NOMBRE", pref.getString("opcionNombreBD", "Usuarios"));
        Log.e("SP de UBICACION",pref.getString("opcionUbicacion", "Interno"));
        Log.e("SP de NUEVA BD",""+pref.getBoolean("opcionVersionBD", false));

        Log.e("PATH INTERNAL BD", "" + getApplicationContext().getDatabasePath(UsuariosSQLiteHelper.m_name));
        //getApplicationContext().getDatabasePath(UsuariosSQLiteHelper.m_name);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_createBackup)
        {
            crearBackupDB();
            return true;
        }
        if (id == R.id.action_restoreBackup)
        {
            restaurarBackup();
            return true;
        }
        if (id == R.id.action_userManagement)
        {
            abrirGestionUsuarios();
        }
        if (id == R.id.action_settings)
        {
            abrirPantallaPreferencias();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void crearBackupDB()
    {
        //File sd = Environment.getExternalStorageDirectory();

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        File sd = null;
        if ("0".equals(pref.getString("opcionUbicacion", "Interno")))
        {
            sd = Environment.getDataDirectory();
        }
        else
        {
            sd = getExternalFilesDir(null);
        }
        File data = Environment.getDataDirectory();
        FileChannel source=null;
        FileChannel destination=null;
        String currentDBPath = "/data/"+ "chema.egea.canales.pddm_ejerciciosqlite" +"/databases/"+UsuariosSQLiteHelper.m_name;
        String backupDBPath = "backup.db";
        if ("0".equals(pref.getString("opcionUbicacion", "Interno")))
        {
            backupDBPath = "/data/"+ "chema.egea.canales.pddm_ejerciciosqlite" +"/databases/"+"backup.db";
        }
        else
        {
            backupDBPath = "backup.db";
        }
        File currentDB = new File(data, currentDBPath);
        File backupDB = new File(sd, backupDBPath);
        try
        {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
            Toast.makeText(this, "Base de datos exportada", Toast.LENGTH_LONG).show();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            Toast.makeText(this, "Hubo un error al exportar", Toast.LENGTH_LONG).show();
            Log.e("ERROR EXPORTAR",""+e.getMessage());
        }

    }

    public void restaurarBackup()
    {
        //File sd = Environment.getExternalStorageDirectory();

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        File sd = null;
        if ("0".equals(pref.getString("opcionUbicacion", "Interno")))
        {
            sd = Environment.getDataDirectory();
        }
        else
        {
            sd = getExternalFilesDir(null);
        }
        File data = Environment.getDataDirectory();
        FileChannel source=null;
        FileChannel destination=null;
        String currentDBPath = "/data/"+ "chema.egea.canales.pddm_ejerciciosqlite" +"/databases/"+UsuariosSQLiteHelper.m_name;
        String backupDBPath = "backup.db";
        if ("0".equals(pref.getString("opcionUbicacion", "Interno")))
        {
            backupDBPath = "/data/"+ "chema.egea.canales.pddm_ejerciciosqlite" +"/databases/"+"backup.db";
        }
        else
        {
            backupDBPath = "backup.db";
        }
        File currentDB = new File(sd, backupDBPath);
        File backupDB = new File(data, currentDBPath);
        try
        {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
            Toast.makeText(this, "Base de datos restaurada", Toast.LENGTH_LONG).show();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            Toast.makeText(this, "Error al Restaurar. Quizá no hay BD de Backup", Toast.LENGTH_LONG).show();
            Log.e("ERROR RESTAURAR",""+e.getMessage());
        }
    }

    public void abrirGestionUsuarios()
    {
        Intent abridor = new Intent();
        abridor.setClass(getApplicationContext(), userManagement.class);
        startActivityForResult(abridor, 1111);
    }

    public void AccederaBienvenido(View view)
    {

        //Creamos un adaptador y cursor para comprobar resultados
        dbAdapter = new UsuariosDBAdapter(this);
        dbAdapter.abrir();
        cursor = dbAdapter.getCursor();

        boolean usuarioCorrecto = false;

        int idUsuarioMostrar = 0;

        if (cursor.moveToFirst())
        {
            do
            {
                String nombre = cursor.getString(1);
                String passwo = cursor.getString(2);
                if (nombre.equals(nombreUsuario.getText().toString()) && passwo.equals(passwordUsuario.getText().toString()))
                {
                    idUsuarioMostrar = cursor.getInt(0);
                    usuarioCorrecto = true;
                    break;
                }
            }
            while (cursor.moveToNext());
        }

        if (usuarioCorrecto)
        {
            usuarioCorrecto = false;
            dbAdapter.cerrar();

            Intent abridor = new Intent();
            abridor.setClass(getApplicationContext(), VerificacionUsuarioSMS.class);
            abridor.putExtra("idMostrar", idUsuarioMostrar);
            startActivityForResult(abridor, 5555);
        }
        else
        {
            Toast.makeText(getBaseContext(), "Error usuario/password incorrectos", Toast.LENGTH_LONG).show();
        }
    }

    public void abrirPantallaPreferencias()
    {
        Intent abridor = new Intent();
        abridor.setClass(getApplicationContext(), PreferenciasAplicacion.class);
        startActivityForResult(abridor, 1010);
    }


    public void salirAplicacion(View view)
    {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("Exit me", true);
        startActivity(intent);
        finish();
    }
}
