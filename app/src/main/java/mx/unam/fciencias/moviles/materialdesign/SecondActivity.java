package mx.unam.fciencias.moviles.materialdesign;

import android.content.pm.ActivityInfo;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.activity.EdgeToEdge;

public class SecondActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private List<String> itemList;
    private SimpleAdapter adapter;
    private boolean isDetailsPanelAvailable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);

        // Ajusta los insets para bordes seguros
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializa RecyclerView
        recyclerView = findViewById(R.id.recycler_infinite);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Inicializa la lista con al menos un elemento (requisito C)
        itemList = new ArrayList<>();
        itemList.add(getString(R.string.initial_element));

        // Crea el adaptador y lo asigna al RecyclerView
        adapter = new SimpleAdapter(itemList,(position, entryText, listSize) -> {
            if(isDetailsPanelAvailable){
                Bundle detailFragmentsArgs = new Bundle();
                detailFragmentsArgs.putInt(DetailsFragment.INDEX_KEY, position);
                detailFragmentsArgs.putInt(DetailsFragment.MASTER_LIST_SIZE_KEY, listSize);
                DetailsFragment detailsFragment = new DetailsFragment();
                detailsFragment.setArguments(detailFragmentsArgs);
                getSupportFragmentManager().beginTransaction().replace(
                  R.id.color_detail_holder, detailsFragment
                ).commit();
            }else{
                Intent intent = new Intent(this, DetailActivity.class);
                intent.putExtra(DetailsFragment.INDEX_KEY, position);
                intent.putExtra(DetailActivity.ENTRY_MESSAGE_KEY, entryText);
                intent.putExtra(DetailsFragment.MASTER_LIST_SIZE_KEY, listSize);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);

        isDetailsPanelAvailable = findViewById(R.id.color_detail_holder) != null;

    }

    // Método llamado al presionar el botón (definido en el XML con android:onClick="addItem")
    // Requisito B: El botón debe agregar una entrada de texto a la Lista Infinita
    public void addItem(View view) {
        int position = itemList.size() + 1;
        itemList.add(getString(R.string.element_position, position));
        adapter.notifyItemInserted(itemList.size() - 1);
        recyclerView.scrollToPosition(itemList.size() - 1);
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && isDetailsPanelAvailable){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE);
        }
    }




    /******************************************************************
     *
     *              Adaptador para el RecyclerView
     *
     *******************************************************************
     */
    public static class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.ViewHolder> {

        private List<String> items;
        private final MasterListItemClickHandler CLICK_HANDLER;

        public SimpleAdapter(List<String> items, MasterListItemClickHandler listItemClickHandler) {
            this.items = items;
            CLICK_HANDLER = listItemClickHandler;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_layout, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            //holder.textView.setText(items.get(position));
            holder.bind(items.get(position), position, items.size(), CLICK_HANDLER);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }


        /******************************************************************
         *
         *  ViewHolder que representa cada elemento de la lista
         *
         *******************************************************************
         */
        public static class ViewHolder extends RecyclerView.ViewHolder{
            public final TextView textView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.item_text);
                textView.setClickable(true);
                textView.setFocusable(true);
            }



            public void bind(String text, int position, int listSize, MasterListItemClickHandler handler){
                textView.setText(text);
                itemView.setOnClickListener(v -> handler.onItemClicked(position, text, listSize));
            }

        }


        /*
         *  Interfaz para manejar clics en elementos de la lista
         */
        public interface MasterListItemClickHandler{
            void onItemClicked(int clickItemIndex, String entryText, int masterListSize);
        }


    }




}