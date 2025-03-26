package mx.unam.fciencias.moviles.materialdesignexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;

import mx.unam.fciencias.moviles.materialdesignexample.R;

public class BaseActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Infla el menú definido en res/menu/menu_app.xml
        getMenuInflater().inflate(R.menu.menu_app, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Maneja las acciones del menú
        switch (item.getItemId()) {
            case R.id.action_about:
                mostrarAcercaDe();
                return true;
            case R.id.action_minimize:
                minimizarAplicacion();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void mostrarAcercaDe() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Acerca de la aplicación")
                .setMessage("Descripción: Esta aplicación sirve para [describir funcionalidad].\n" +
                        "Versión: 1.0 (01/03/2025)\n" +
                        "Desarrolladores: [Nombre(s) de los desarrolladores]")
                .setPositiveButton("OK", null)
                .show();
    }

    private void minimizarAplicacion() {
        // Envía la aplicación al fondo
        moveTaskToBack(true);
    }
}
