package mx.unam.fciencias.moviles.materialdesign;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.activity.EdgeToEdge;

public class SecondActivity extends BaseActivity implements SimpleAdapter.MasterListItemClickHandler {

    private SimpleAdapter adapter;
    private boolean isDetailsPanelAvailable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        setContentView(R.layout.activity_main2);

        // Ajusta los insets para bordes seguros
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button button = findViewById(R.id.button_first);
        button.setOnClickListener(this::addListElement);
        RecyclerView recyclerView = findViewById(R.id.recycler_infinite);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new SimpleAdapter(getResources(), this);
        addListElement(null);
        recyclerView.setAdapter(adapter);
        isDetailsPanelAvailable = findViewById(R.id.color_detail_holder) != null;
    }

    // Método llamado al presionar el botón (android:onClick="addItem")

    public void addListElement(View button){
        adapter.addItem();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && isDetailsPanelAvailable) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE);
        }
    }

    @Override
    public void onItemClicked(int clickedItemIndex, String entryText, int masterListSize) {
        if (isDetailsPanelAvailable) {
            Bundle detailFragmentsArgs = new Bundle();
            detailFragmentsArgs.putInt(DetailsFragment.INDEX_KEY, clickedItemIndex);
            detailFragmentsArgs.putInt(DetailsFragment.MASTER_LIST_SIZE_KEY, masterListSize);
            DetailsFragment detailFragment = new DetailsFragment();
            detailFragment.setArguments(detailFragmentsArgs);
            getSupportFragmentManager().beginTransaction().replace(
                    R.id.color_detail_holder, detailFragment
            ).commit();
        } else {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(DetailsFragment.INDEX_KEY, clickedItemIndex);
            intent.putExtra(DetailActivity.ENTRY_MESSAGE_KEY, entryText);
            intent.putExtra(DetailsFragment.MASTER_LIST_SIZE_KEY, masterListSize);
            startActivity(intent);
        }
    }
}

/******************************************************************
 *
 *              Adaptador para el RecyclerView
 *
 *******************************************************************/
class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.ViewHolder> {

    private final List<String> ITEM_LIST;

    private final MasterListItemClickHandler CLICK_HANDLER;
    private final Resources RESOURCES;

    public SimpleAdapter(Resources res, MasterListItemClickHandler clickHandler) {
        ITEM_LIST = new LinkedList<>();
        RESOURCES = res;
        CLICK_HANDLER = clickHandler;
    }


    public void addItem(){
        int i = ITEM_LIST.size();
        ITEM_LIST.add(i, "Elemento " + (i + 1));
        notifyItemInserted(i);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TextView view = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView.setText(ITEM_LIST.get(position));
    }

    @Override
    public int getItemCount() {
        return ITEM_LIST.size();
    }

    /******************************************************************
     *
     *  ViewHolder que representa cada elemento de la lista
     *
     *******************************************************************/
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final TextView textView;

        public ViewHolder(@NonNull TextView itemView) {
            super(itemView);
            textView = itemView;
            textView.setFocusable(true);
            textView.setClickable(true);
            textView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            CLICK_HANDLER.onItemClicked(getAdapterPosition(), textView.getText().toString(), ITEM_LIST.size());
        }
    }

    /*
     *  Interfaz para manejar clics en elementos de la lista
     */
    public interface MasterListItemClickHandler {
        void onItemClicked(int clickItemIndex, String entryText, int masterListSize);
    }

}
