package mx.unam.fciencias.moviles.materialdesign;

import android.os.Bundle;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DetailActivity extends BaseActivity {

    public static final String ENTRY_MESSAGE_KEY = "mx.unam.fciencias.moviles.materialdesign.MESSAGE_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Bundle detailFragmentArgs = getBundle();

        //Crea y lanza el fragmento de detalles dentro del contenedor
        DetailsFragment detailsFragment = new DetailsFragment();
        detailsFragment.setArguments(detailFragmentArgs);
        getSupportFragmentManager().beginTransaction().add(
                R.id.color_detail_holder, detailsFragment
        ).commit();
    }

    @NonNull
    private Bundle getBundle() {
        Intent startIntent = getStartIntent();

        Bundle detailFragmentArgs = new Bundle();
        detailFragmentArgs.putInt(DetailsFragment.INDEX_KEY,
                startIntent.getIntExtra(DetailsFragment.INDEX_KEY, -1));
        detailFragmentArgs.putInt(DetailsFragment.MASTER_LIST_SIZE_KEY,
                startIntent.getIntExtra(DetailsFragment.MASTER_LIST_SIZE_KEY, -1));
        return detailFragmentArgs;
    }

    @NonNull
    private Intent getStartIntent() {
        Intent startIntent = getIntent();
        ActionBar actionBar = getSupportActionBar();
        String detailFragmentTitle = null;
        //Configura la barra de acción con el título recibido por intent
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(startIntent.getStringExtra(ENTRY_MESSAGE_KEY));
        }else{
            //En caso de que no haya barra de acción, guarda el título
            detailFragmentTitle = startIntent.getStringExtra(ENTRY_MESSAGE_KEY);
        }
        return startIntent;
    }
}