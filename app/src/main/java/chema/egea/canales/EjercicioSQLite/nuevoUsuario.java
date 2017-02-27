package chema.egea.canales.EjercicioSQLite;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class nuevoUsuario extends AppCompatActivity {

    EditText nombreUsuario;
    EditText passwordUsuario;
    EditText nombreReal;
    EditText emailUsuario;

    boolean dbActualizada;

    private UsuariosDBAdapter dbAdapter;
    private Cursor cursor;
    private UsuariosCursorAdapter usuariosAdapter ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_usuario);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nombreUsuario = (EditText)findViewById(R.id.ET_nombreUsuarioNU);
        passwordUsuario = (EditText)findViewById(R.id.ET_passwordNU);
        nombreReal = (EditText)findViewById(R.id.ET_nombreNU);
        emailUsuario = (EditText)findViewById(R.id.ET_emailNU);

        TextView textemail = (TextView)findViewById(R.id.TV_emailNU);
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

    }

    //Crear el nuevo usuario
    public void ActualizarDatosUsuario(View view)
    {

        if (nombreUsuario.getText().toString().length()!=0 && passwordUsuario.getText().toString().length()!=0 && nombreReal.getText().toString().length()!=0)
        {
            //Creamos un adaptador y cursor para comprobar resultados
            dbAdapter = new UsuariosDBAdapter(this);
            dbAdapter.abrir();
            cursor = dbAdapter.getCursor();
            //comprobamos que no existe y lo creamos
            boolean esNuevo=false;

            if (cursor.moveToFirst())
            {
                do
                {
                    String nombre = cursor.getString(1);
                    if (!nombre.equals(nombreUsuario.getText().toString()))
                    {
                        esNuevo = true;
                        break;
                    }
                }
                while (cursor.moveToNext());
            }

            if (esNuevo)
            {
                //
                // Obtenemos los datos del formulario
                //
                ContentValues reg = new ContentValues();

                reg.put(UsuariosDBAdapter.C_COLUMNA_NOMBRE, nombreUsuario.getText().toString());
                reg.put(UsuariosDBAdapter.C_COLUMNA_CONTRASENA, passwordUsuario.getText().toString());
                reg.put(UsuariosDBAdapter.C_COLUMNA_NOMBREREAL, nombreReal.getText().toString());
                if (dbActualizada)
                    reg.put(UsuariosDBAdapter.C_COLUMNA_EMAIL, emailUsuario.getText().toString());

                dbAdapter.insert(reg);

                //
                // Devolvemos el control
                //
                setResult(RESULT_OK);
                dbAdapter.cerrar();
                Toast.makeText(getBaseContext(), "Usuario Creado.", Toast.LENGTH_LONG).show();
                finish();
            }
            else
            {
                Toast.makeText(getBaseContext(), "Ese nombre de usuario ya existe.", Toast.LENGTH_LONG).show();
            }

        }
        else
        {
            Toast.makeText(getBaseContext(), "Faltan campos por rellenar.", Toast.LENGTH_LONG).show();
        }

    }
    public void RegresarUserManagementNU(View view)
    {
        finish();
    }

}
