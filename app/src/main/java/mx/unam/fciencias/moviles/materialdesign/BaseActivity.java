package mx.unam.fciencias.moviles.materialdesign;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.app.NavUtils;
import androidx.preference.PreferenceManager;

public class BaseActivity extends AppCompatActivity {


    protected String lightThemeId, themePreferenceKey;
    protected SharedPreferences sharedPreferences;

    public static final byte RESULT_CHECK_STYLE = 2;

    protected final ActivityResultLauncher<Intent> resultLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            int resultCode = result.getResultCode();
                            Intent data = result.getData();
                            if (resultCode == RESULT_CHECK_STYLE && data != null) {
                                try {
                                    int selectedTheme = getThemeResourceIdFromPreferenceId(
                                            data.getStringExtra(themePreferenceKey)
                                    );
                                    if (getPackageManager().getActivityInfo(
                                            getComponentName(), 0
                                    ).getThemeResource() == selectedTheme) {
                                        return;
                                    }
                                    setTheme(selectedTheme);
                                    recreate();
                                } catch (PackageManager.NameNotFoundException e) {
                                    Log.w(this.getClass().getSimpleName(), "No se pudo obtener el estilo", e);
                                }
                            }
                        }
                    }
            );

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lightThemeId = getString(R.string.light_theme_preference_id);
        themePreferenceKey = getString(R.string.theme_preferences_key);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        applyTheme(sharedPreferences.getString(
                themePreferenceKey, lightThemeId), false);

    }

    protected void applyTheme(String themeKey, boolean recreate){
        setTheme(getThemeResourceIdFromPreferenceId(themeKey));
        if(recreate) recreate();
    }

    private int getThemeResourceIdFromPreferenceId(String stylePreferenceId){
        if(lightThemeId.equals(stylePreferenceId)) return R.style.LightTheme;
        else return R.style.DarkTheme;
    }

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
        else if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
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

    @Override
    public void startActivity(Intent intent) {
        resultLauncher.launch(intent);
    }

    public void startActivity(Intent intent, ActivityOptionsCompat options){
        resultLauncher.launch(intent, options);
    }

    @Override
    public void finish() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(themePreferenceKey,
                sharedPreferences.getString(themePreferenceKey, lightThemeId));
        setResult(RESULT_CHECK_STYLE, resultIntent);
        super.finish();
    }
}
