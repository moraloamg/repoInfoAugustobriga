package com.example.infoaugustobriga.horarios;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.infoaugustobriga.AcMenuPrincipal;
import com.example.infoaugustobriga.Interfaces.IConfigurarActividad;
import com.example.infoaugustobriga.R;
import com.example.infoaugustobriga.adaptadoresListas.AdaptadorListaGenerico;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class AcHorarios extends AppCompatActivity implements IConfigurarActividad {

    //LinearLayout del titulo para la animacion
    LinearLayout lyTituloHorarios;
    //TextView para el titulo
    TextView txtTitulo;
    //Animaciones
    Animation animTitulo;
    //lista de modalidades
    JSONArray listaJsonModalidades;
    //listview para rellenar
    ListView lViewModalidades;
    //fuente
    Typeface fuenteCabecera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //TODO OBTENER TAMBIEN LA CONFIGURACION DEL IDIOMA DEL MOVIL

        //obtenemos la configuracion del dispositivo para el modo dia-noche
        Configuration configuracion = Resources.getSystem().getConfiguration();
        //cargamos el layout que corresponde segun el modo de iluminacion del movil
        cambiarDiaNoche(configuracion);
        recibirDatos();
        //identificamos lo elementos de la interfaz
        identificarElementosInterfaz(configuracion);;
        cargarAnimaciones();
        //obtenemos la lista de modalidades del array Json que hemos pasado a esta actividad
        ArrayList<String> listaModalidades = obtenerValorModalidades();
        lViewModalidades.setAdapter(new AdaptadorListaGenerico(this,listaModalidades,null,configuracion));
        activarBotones();

        //TODO COMPROBAR QUE HAY CONEXION A INTERNET AL FINAL DE LA CARGA Y MOSTRAR UN MENSAJE DE ERROR

    }

    private ArrayList<String> obtenerValorModalidades() {
        ArrayList<String> resultado = new ArrayList<>();
        for (int i = 0; i < listaJsonModalidades.length(); i++) {
            String modalidad = null;
            try {
                modalidad = listaJsonModalidades.getJSONObject(i).getString("modalidad");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            resultado.add(modalidad);
        }
        return resultado;
    }

    private void recibirDatos(){
        Bundle extras = getIntent().getExtras();
        try {
            listaJsonModalidades = new JSONArray(extras.getString("listaModalidades"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<String> obtenerDatosFichero(String url){
        ArrayList<String> resultado = new ArrayList<>();

        LecturaFicheroHorarios lector=new LecturaFicheroHorarios(url);
        //una vez se ejecute el siguiente método, el hilo de la clase actual acabará y empezará el hilo de lectura
        //el hilo principal de la aplicacion se congelara hasta que se lea el fichero (es imperceptible)
        try {
             resultado = (ArrayList<String>) lector.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return resultado;
    }

    @Override
    public void cambiarDiaNoche(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.uiMode == MODO_CLARO) {
            setContentView(R.layout.activity_general_claro);
        } else if (newConfig.uiMode == MODO_OSCURO){
            setContentView(R.layout.activity_general_osc);
        }
    }

    @Override
    public void identificarElementosInterfaz(Configuration newConfig) {
        lyTituloHorarios = findViewById(R.id.ly_titulo_claro);
        txtTitulo = findViewById(R.id.txt_titulo_claro);
        txtTitulo.setText(R.string.horarios);
        if(newConfig.uiMode == MODO_CLARO){
            lViewModalidades = findViewById(R.id.listViewClaro);
        }else if (newConfig.uiMode == MODO_OSCURO){
            lViewModalidades = findViewById(R.id.listViewOsc);
        }
    }

    @Override
    public void activarBotones() {
        lViewModalidades.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    String urlLista=listaJsonModalidades.getJSONObject(i).getString("enlace_lista");
                    String urlCarpeta=listaJsonModalidades.getJSONObject(i).getString("enlace_carpeta");
                    String extension=listaJsonModalidades.getJSONObject(i).getString("extension_imagenes");

                    ArrayList<String> listaCursos = obtenerDatosFichero(urlLista);

                    Intent irListaCursos=new Intent(AcHorarios.this, AcListaCursos.class);
                    irListaCursos.putExtra("listaCursos",listaCursos);
                    irListaCursos.putExtra("url_carpeta",urlCarpeta);
                    irListaCursos.putExtra("extension",extension);

                    irListaCursos.putExtra("listaJsonModalidades",listaJsonModalidades.toString());
                    startActivity(irListaCursos);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void cargarAnimaciones() {
        animTitulo = AnimationUtils.loadAnimation(this,R.anim.anim_cabecera);
        lyTituloHorarios.startAnimation(animTitulo);
    }

    @Override
    public void onBackPressed() {
        Intent i=new Intent(getApplicationContext(), AcMenuPrincipal.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }
}