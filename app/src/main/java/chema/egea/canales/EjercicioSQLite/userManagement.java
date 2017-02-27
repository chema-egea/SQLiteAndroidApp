package chema.egea.canales.EjercicioSQLite;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class userManagement extends AppCompatActivity {


    Spinner spinnerListaUsuarios;
    Button botonEliminarUsuario;

    private UsuariosDBAdapter dbAdapter;
    private Cursor cursor;
    private UsuariosCursorAdapter usuariosAdapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_management);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        spinnerListaUsuarios = (Spinner)findViewById(R.id.listaUsuarios);
        botonEliminarUsuario = (Button)findViewById(R.id.B_eliminar);


        // Creamos el adaptador del Spinner
        dbAdapter = new UsuariosDBAdapter(this);
        dbAdapter.abrir();
        cursor = dbAdapter.getCursor();

        SimpleCursorAdapter adapterUsuarios = new SimpleCursorAdapter(this,android.R.layout.simple_spinner_item, cursor, new String[] {UsuariosDBAdapter.C_COLUMNA_NOMBRE}, new int[] {android.R.id.text1});
        adapterUsuarios.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerListaUsuarios.setAdapter(adapterUsuarios);


        dbAdapter.cerrar();

        Log.e("item actual: ", spinnerListaUsuarios.getAdapter().getItem(0).toString());


    }

    @Override
    protected void onResume() {
        super.onResume();
        ActualizarSpinner();
    }

    void ActualizarSpinner()
    {
        // Creamos el adaptador del Spinner
        dbAdapter = new UsuariosDBAdapter(this);
        dbAdapter.abrir();
        cursor = dbAdapter.getCursor();

        SimpleCursorAdapter adapterUsuarios = new SimpleCursorAdapter(this,android.R.layout.simple_spinner_item, cursor, new String[] {UsuariosDBAdapter.C_COLUMNA_NOMBRE}, new int[] {android.R.id.text1});
        adapterUsuarios.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerListaUsuarios.setAdapter(adapterUsuarios);

        dbAdapter.cerrar();
    }

    public void CrearNuevoUsuario(View view)
    {
        Intent abridor = new Intent();
        abridor.setClass(getApplicationContext(),nuevoUsuario.class);
        startActivityForResult(abridor,3333);
    }

    public void IrActualizarUsuario(View view)
    {
        dbAdapter = new UsuariosDBAdapter(this);
        dbAdapter.abrir();


        if (cursor.moveToFirst())
        {
            cursor.moveToPosition(spinnerListaUsuarios.getSelectedItemPosition());
            int id = cursor.getInt(0);

            dbAdapter.cerrar();

            Intent abridor = new Intent();
            abridor.setClass(getApplicationContext(), ActualizarUsuario.class);
            abridor.putExtra("id", id);
            startActivityForResult(abridor, 2222);
        }
        else
        {
            Toast.makeText(getBaseContext(), "No hay nada que actualizar.", Toast.LENGTH_LONG).show();
        }
    }
    public void EliminarUsuario(View view)
    {

        //Creamos un adaptador y cursor para comprobar resultados

        dbAdapter = new UsuariosDBAdapter(this);
        dbAdapter.abrir();


        if (cursor.moveToFirst())
        {
            cursor.moveToPosition(spinnerListaUsuarios.getSelectedItemPosition());
            int id = cursor.getInt(0);

            Log.e("borramos datos de id", "" + id);
            dbAdapter.delete(id);
            dbAdapter.cerrar();
            ActualizarSpinner();
            Toast.makeText(getBaseContext(), "Usuario seleccionado eliminado.", Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(getBaseContext(), "No hay nada que borrar.", Toast.LENGTH_LONG).show();
        }

    }

    public void RegresarMainActivityUM(View view)
    {
        finish();
    }


}
