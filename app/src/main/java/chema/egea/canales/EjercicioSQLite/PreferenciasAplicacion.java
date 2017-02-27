package chema.egea.canales.EjercicioSQLite;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;

public class PreferenciasAplicacion extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener
{

    EditTextPreference nombreBaseDatos;
    ListPreference ubicacionBaseDatos;
    CheckBoxPreference actualizarBaseDatos;

    private UsuariosDBAdapter dbAdapter;
    private Cursor cursor;
    private UsuariosCursorAdapter usuariosAdapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferenciasxml);

        nombreBaseDatos = (EditTextPreference)findPreference("opcionNombreBD");
        ubicacionBaseDatos = (ListPreference)findPreference("opcionUbicacion");
        actualizarBaseDatos = (CheckBoxPreference)findPreference("opcionVersionBD");

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(PreferenciasAplicacion.this);


        nombreBaseDatos.setText(pref.getString("opcionNombreBD", UsuariosSQLiteHelper.m_name));
        ubicacionBaseDatos.setValue(pref.getString("opcionUbicacion", "Interno"));
        actualizarBaseDatos.setChecked(pref.getBoolean("opcionVersionBD", false));

        if(actualizarBaseDatos.isChecked())
        {
            actualizarBaseDatos.setEnabled(false);
        }

        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);


    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
    {
        if (key.equals("opcionNombreBD"))
        {
            Preference connectionPref = findPreference(key);
            Log.e("Cambios en NombreDB", ""+connectionPref.getTitle());

            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(PreferenciasAplicacion.this);

            //Creamos un adaptador y cursor para comprobar resultados
            dbAdapter = new UsuariosDBAdapter(this);
            dbAdapter.abrir();
            cursor = dbAdapter.getCursor();
            dbAdapter.cerrar();

            dbAdapter.CambiarNombreBD(pref.getString("opcionNombreBD", UsuariosSQLiteHelper.m_name));

        }
        if (key.equals("opcionUbicacion"))
        {
            Preference connectionPref = findPreference(key);
            Log.e("Cambios en UbicacionDB", ""+connectionPref.getTitle());
        }
        if (key.equals("opcionVersionBD"))
        {
            Preference connectionPref = findPreference(key);
            Log.e("Cambios en VersionDB", ""+connectionPref.getTitle());

            if(actualizarBaseDatos.isChecked())
            {
                actualizarBaseDatos.setEnabled(false);

                //Creamos un adaptador y cursor para comprobar resultados
                dbAdapter = new UsuariosDBAdapter(this);
                dbAdapter.abrir();
                cursor = dbAdapter.getCursor();

                dbAdapter.ActualizarVersionBD();

                dbAdapter.cerrar();
            }

        }
        Log.e("Ha cambiado algo", key);
    }
}
