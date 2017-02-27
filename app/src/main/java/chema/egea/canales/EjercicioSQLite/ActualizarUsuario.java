package chema.egea.canales.EjercicioSQLite;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ActualizarUsuario extends AppCompatActivity
{
    EditText nombreUsuario;
    EditText passwordUsuario;
    EditText nombreReal;
    EditText emailUsuario;

    boolean dbActualizada;
    int idActualizar;

    private UsuariosDBAdapter dbAdapter;
    private Cursor cursor;
    private UsuariosCursorAdapter usuariosAdapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_usuario);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nombreUsuario = (EditText)findViewById(R.id.ET_nombreUsuarioAU);
        passwordUsuario = (EditText)findViewById(R.id.ET_passwordAU);
        nombreReal = (EditText)findViewById(R.id.ET_nombreAU);
        emailUsuario = (EditText)findViewById(R.id.ET_emailAU);

        //Obtenemos valores del intent
        idActualizar = getIntent().getIntExtra("id",0);
        Log.e("Datos id",""+idActualizar);

        //Creamos un adaptador y cursor para comprobar resultados
        dbAdapter = new UsuariosDBAdapter(this);
        dbAdapter.abrir();
        cursor = dbAdapter.getCursor();

        String nombreUsuarioDB ="";
        String passwordUsuarioDB="";
        String nombreRealUsuarioDB="";
        String emailUsuarioDB="";


        TextView textemail = (TextView)findViewById(R.id.TV_emailAU);
        if (UsuariosSQLiteHelper.m_version==2)
        {
            dbActualizada = true;
            emailUsuario.setVisibility(View.VISIBLE);
            emailUsuario.setEnabled(true);
            textemail.setVisibility(View.VISIBLE);
        }
        else
        {
            dbActualizada = false;
            emailUsuario.setVisibility(View.INVISIBLE);
            emailUsuario.setEnabled(false);
            textemail.setVisibility(View.INVISIBLE);
        }

        if (cursor.moveToFirst())
        {
            do
            {
                int idcursor = cursor.getInt(0);
                if (idcursor == idActualizar)
                {
                    nombreUsuarioDB = cursor.getString(1);
                    passwordUsuarioDB = cursor.getString(2);
                    nombreRealUsuarioDB = cursor.getString(3);
                    if (dbActualizada)
                    {
                        if (cursor.getString(4) != null)
                            emailUsuarioDB = cursor.getString(4);
                        else
                            emailUsuarioDB = "";
                    }
                    break;
                }
            }
            while (cursor.moveToNext());
        }

        nombreUsuario.setText(nombreUsuarioDB);
        passwordUsuario.setText(passwordUsuarioDB);
        nombreReal.setText(nombreRealUsuarioDB);
        if (dbActualizada)
            emailUsuario.setText(emailUsuarioDB);

    }

    public void ActualizarlosDatosUsuario(View view)
    {
        //
        // Obtenemos los datos del formulario
        //
        ContentValues reg = new ContentValues();

        reg.put(UsuariosDBAdapter.C_COLUMNA_ID, idActualizar);
        reg.put(UsuariosDBAdapter.C_COLUMNA_NOMBRE, nombreUsuario.getText().toString());
        reg.put(UsuariosDBAdapter.C_COLUMNA_CONTRASENA, passwordUsuario.getText().toString());
        reg.put(UsuariosDBAdapter.C_COLUMNA_NOMBREREAL, nombreReal.getText().toString());
        if (dbActualizada)
            reg.put(UsuariosDBAdapter.C_COLUMNA_EMAIL, emailUsuario.getText().toString());

        Toast.makeText(getBaseContext(), "Usuario Actualizado", Toast.LENGTH_LONG).show();
        dbAdapter.update(reg);


        //
        // Devolvemos el control
        //
        setResult(RESULT_OK);
        finish();
    }

    public void RegresarUserManagementAU(View view)
    {
        finish();
    }

}
