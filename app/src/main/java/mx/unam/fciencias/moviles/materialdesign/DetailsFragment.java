package mx.unam.fciencias.moviles.materialdesign;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import androidx.fragment.app.Fragment;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 *
 */
public class DetailsFragment extends Fragment {


    protected static final String INDEX_KEY = "mx.unam.fciencias.moviles.materialdesign.INDEX";
    protected static final String MASTER_LIST_SIZE_KEY = "mx.unam.fciencias.moviles.materialdesign.MASTER_LIST_SIZE";

    private int selectedIndex;
    private int masterListSize;

    public DetailsFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            selectedIndex = args.getInt(INDEX_KEY);
            masterListSize = args.getInt(MASTER_LIST_SIZE_KEY);
        }else{
            selectedIndex = -1;
            masterListSize = -1;
            Log.w(DetailsFragment.class.getSimpleName(), "Has seleccionado una entrada incorrecta");
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_details, container, false);
        View colorView = rootView.findViewById(R.id.color_view);
        TextView red = rootView.findViewById(R.id.red_value_tv);
        TextView green = rootView.findViewById(R.id.green_value_tv);
        TextView blue = rootView.findViewById(R.id.blue_value_tv);
        TextView hex = rootView.findViewById(R.id.hex_value_tv);
        float[] rgb = generateColorFromIndex();
        int[] intRGB = new int[] {
                (int) rgb[0] * 255,
                (int) rgb[1] * 255,
                (int) rgb[2] * 255 };
        int indexColor = Build.VERSION.SDK_INT < Build.VERSION_CODES.O ?
                Color.rgb(intRGB[0], intRGB[1], intRGB[2]) :
                Color.rgb(rgb[0], rgb[1], rgb[2]);
        colorView.setBackgroundColor(indexColor);
        red.setText(rootView.getResources().getString(
                R.string.red_color_component, rgb[0], intRGB[0]
        ));
        green.setText(rootView.getResources().getString(
                R.string.green_color_component, rgb[1], intRGB[1]
        ));
        blue.setText(rootView.getResources().getString(
                R.string.blue_color_component, rgb[2], intRGB[2]
        ));
        hex.setText(String.format("#%06X", (0xFFFFFF & indexColor)));
        //hex.setText(Integer.toHexString(indexColor));

        return rootView;
    }


    private float[] generateColorFromIndex(){

        float datasetThird = masterListSize /3f; //Calcula el tamaño de un tercio del total de elementos
        byte third;
        int previousThird;
        // Determina en que tercio está el índice seleccionado
        if (masterListSize == 1 || selectedIndex < datasetThird){
            previousThird = selectedIndex + 1; // Si esta en el primer tercio
            third = 1;
        }else{
            previousThird = (int) datasetThird;
            third = (byte) (selectedIndex < datasetThird * 2 ? 2 : 3);
        }
        float [] rgb = new float[3];
        // Componente rojo
        rgb[0] = (selectedIndex + 1) / (third * datasetThird);
        //Componente verde
        rgb[1] = (selectedIndex + 1 - previousThird) / (2 * datasetThird);
        //Componente azul
        rgb[2] = third == 3 ? (selectedIndex + 1f) / masterListSize : 0;
        // Asegura que los valores estén en el rango [0,1]
        if(rgb[0] > 1) rgb[0] = 1;
        if(rgb[1] > 1) rgb[1] = 1;
        return rgb;
    }
}