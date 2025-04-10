package mx.unam.fciencias.moviles.materialdesign;

import android.os.Bundle;
import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DetailActivity extends BaseActivity {

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

        Bundle detailFragmentArgs = new Bundle();
        detailFragmentArgs.putInt(DetailsFragment.INDEX_KEY,
                startIntent.getIntExtra(DetailsFragment.INDEX_KEY, -1));
        detailFragmentArgs.putInt(DetailsFragment.MASTER_LIST_SIZE_KEY,
                startIntent.getIntExtra(DetailsFragment.MASTER_LIST_SIZE_KEY, -1));

        //Crea y lanza el fragmento de detalles dentro del contenedor
        DetailsFragment detailsFragment = new DetailsFragment();
        detailsFragment.setArguments(detailFragmentArgs);
        getSupportFragmentManager().beginTransaction().add(
                R.id.color_detail_holder, detailsFragment
        ).commit();
    }
}