package mx.unam.fciencias.moviles.materialdesign;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.activity.EdgeToEdge;

public class SecondActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private ArrayList<String> itemList;
    private SimpleAdapter adapter;

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
        adapter = new SimpleAdapter(itemList);
        recyclerView.setAdapter(adapter);
    }

    // Método llamado al presionar el botón (definido en el XML con android:onClick="addItem")
    // Requisito B: El botón debe agregar una entrada de texto a la Lista Infinita
    public void addItem(View view) {
        int position = itemList.size() + 1;
        itemList.add(getString(R.string.element_position, position));
        adapter.notifyItemInserted(itemList.size() - 1);
        recyclerView.scrollToPosition(itemList.size() - 1);
    }

    // Adaptador para el RecyclerView
    public class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.ViewHolder> {

        private ArrayList<String> items;

        public SimpleAdapter(ArrayList<String> items) {
            this.items = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_layout, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.textView.setText(items.get(position));
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView textView;

            public ViewHolder(View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.item_text);
            }
        }
    }
}