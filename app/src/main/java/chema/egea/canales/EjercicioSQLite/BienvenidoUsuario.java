package chema.egea.canales.EjercicioSQLite;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class BienvenidoUsuario extends AppCompatActivity {

    TextView nombreReal;
    TextView nombreUsuario;
    TextView emailUsuario;

    int idMostrar;
    boolean dbActualizada;

    private UsuariosDBAdapter dbAdapter;
    private Cursor cursor;
    private UsuariosCursorAdapter usuariosAdapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bienvenido_usuario);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nombreReal = (TextView)findViewById(R.id.TV_bienvenido);
        nombreUsuario = (TextView)findViewById(R.id.TV_nombreUsuarioBienvenido);
        emailUsuario = (TextView)findViewById(R.id.TV_emailBienvenido);

        //Obtenemos valores del intent
        idMostrar = getIntent().getIntExtra("idMostrar",0);
        Log.e("Datos id", "" + idMostrar);

        if (UsuariosSQLiteHelper.m_version==2)
        {
            dbActualizada = true;
            emailUsuario.setVisibility(View.VISIBLE);
            emailUsuario.setEnabled(true);
        }
        else
        {
            dbActualizada = false;
            emailUsuario.setVisibility(View.INVISIBLE);
            emailUsuario.setEnabled(false);
        }

        //Creamos un adaptador y cursor para comprobar resultados
        dbAdapter = new UsuariosDBAdapter(this);
        dbAdapter.abrir();
        cursor = dbAdapter.getCursor();

        String nombreUsuarioDB ="";
        String emailUsuarioDB="";
        String nombreRealUsuarioDB="";

        if (cursor.moveToFirst())
        {
            do
            {
                int idcursor = cursor.getInt(0);
                if (idcursor == idMostrar)
                {
                    nombreUsuarioDB = cursor.getString(1);
                    nombreRealUsuarioDB = cursor.getString(3);
                    if (dbActualizada)
                    {
                        if (cursor.getString(4) != null)
                            emailUsuarioDB = cursor.getString(4);
                        else
                            emailUsuarioDB = "Sin email";
                    }
                    break;
                }
            }
            while (cursor.moveToNext());
        }

        nombreUsuario.setText("Usuario: "+nombreUsuarioDB);
        nombreReal.setText("Bienvenido "+nombreRealUsuarioDB);
        if (dbActualizada)
            emailUsuario.setText("Email: "+emailUsuarioDB);

    }

    public void RegresarMainActivityBienvenido(View view)
    {
        finish();
    }

}
