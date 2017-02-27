package chema.egea.canales.EjercicioSQLite;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class VerificacionUsuarioSMS extends AppCompatActivity {


    int idMostrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verificacion_usuario_sms);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Obtenemos valores del intent
        idMostrar = getIntent().getIntExtra("idMostrar",0);
        Log.e("Datos id", "" + idMostrar);
        comprobarPermisosSMS();

    }

    public void buscarSMSValidacion(View view)
    {

        Log.e("Vamos a buscar el SMS", "Creamos content provider");


        if(comprobarPermisosSMS()) {
            boolean SMSEncontrado = false;

            // Create Inbox box URI
            Uri inboxURI = Uri.parse("content://sms/inbox");
            // List required columns
            String[] reqCols = new String[]{"_id", "address", "body"};
            // Get Content Resolver object, which will deal with Content Provider
            ContentResolver cr = getContentResolver();
            // Fetch Inbox SMS Message from Built-in Content Provider
            Cursor cursor = cr.query(inboxURI, reqCols, null, null, null);

            if (cursor.moveToFirst()) {
                do {
                    Log.e("id sms", cursor.getString(0));
                    Log.e("direccion sms", cursor.getString(1));
                    Log.e("cuerpo sms", cursor.getString(2));

                    if (cursor.getString(1).equals("12345678")) {
                        if (cursor.getString(2).indexOf("A3489HG") != -1) {
                            SMSEncontrado = true;
                            break;
                        }
                    }
                }
                while (cursor.moveToNext());
            }

            if (SMSEncontrado) {
                Intent abridor = new Intent();
                abridor.setClass(getApplicationContext(), BienvenidoUsuario.class);
                abridor.putExtra("idMostrar", idMostrar);
                startActivityForResult(abridor, 5454);
                this.finish();
            } else {
                Toast.makeText(this, "No se pudo verificar el SMS (no encontrado)", Toast.LENGTH_LONG).show();
                finish();
            }

        }
        else
        {
            Toast.makeText(this, "Inténtalo de nuevo, si no has concedido permisos, concédelos", Toast.LENGTH_SHORT).show();
        }

    }

    public void saltarVerificacion(View view)
    {
        Intent abridor = new Intent();
        abridor.setClass(getApplicationContext(), BienvenidoUsuario.class);
        abridor.putExtra("idMostrar", idMostrar);
        startActivityForResult(abridor, 5454);
        this.finish();
    }


    private boolean comprobarPermisosSMS()
    {
        String permission = Manifest.permission.READ_SMS;
        int grant = ContextCompat.checkSelfPermission(this, permission);
        // Here, thisActivity is the current activity
        if ( grant != PackageManager.PERMISSION_GRANTED)
        {
            String[] permission_list = new String[1];
            permission_list[0] = permission;
            ActivityCompat.requestPermissions(this, permission_list, 1);
        }
        else
        {
            return true;
        }
        return false;
    }

}
