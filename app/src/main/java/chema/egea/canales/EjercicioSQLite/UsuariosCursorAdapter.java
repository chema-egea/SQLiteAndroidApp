package chema.egea.canales.EjercicioSQLite;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by chema on 15/12/2015.
 */
public class UsuariosCursorAdapter extends CursorAdapter
{
    private UsuariosDBAdapter dbAdapter = null ;

    public UsuariosCursorAdapter(Context context, Cursor c)
    {
        super(context, c);
        dbAdapter = new UsuariosDBAdapter(context);
        dbAdapter.abrir();
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor)
    {
        Log.e("DATOS CURSOR", cursor.getString(cursor.getColumnIndex(UsuariosDBAdapter.C_COLUMNA_NOMBRE)));
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent)
    {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(android.R.layout.simple_dropdown_item_1line, parent, false);

        return view;
    }
}
