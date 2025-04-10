package mx.unam.fciencias.moviles.materialdesign;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Infla el menú que definiste en menu_main.xml
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_about) {
            // Muestra la información acerca de la aplicación
            showAboutDialog();
            return true;
        } else if (id == R.id.action_minimize) {
            // Minimiza la aplicación (lleva la tarea al fondo)
            moveTaskToBack(true);
            return true;
        }else if(id == R.id.menu_settings){
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showAboutDialog() {
        // Cambia este contenido con la información real de tu app
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Acerca de");
        builder.setMessage("Descripción breve de la aplicación.\n" +
                "Versión: 1.0 (Fecha de publicación)\n" +
                "Desarrollado por: Nombre de los desarrolladores");
        builder.setPositiveButton("Aceptar", null);
        builder.show();
    }
}
