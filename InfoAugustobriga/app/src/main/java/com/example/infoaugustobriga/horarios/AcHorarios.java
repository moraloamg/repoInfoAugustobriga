package com.example.infoaugustobriga.horarios;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.infoaugustobriga.AcMenuPrincipal;
import com.example.infoaugustobriga.Interfaces.IConfigurarActividad;
import com.example.infoaugustobriga.R;
import com.example.infoaugustobriga.adaptadoresListas.AdaptadorListaGenerico;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.Serializable;
import java.util.ArrayList;

public class AcHorarios extends AppCompatActivity implements IConfigurarActividad {

    //LinearLayout de los contenedores
    LinearLayout lyFp, lyBachillerato, lyEso;
    //LinearLayout del titulo
    LinearLayout lyTituloHorarios;
    //Animaciones
    Animation animCont1, animCont2, animCont3, animTitulo;
    //lista de modalidades
    JSONArray listaJsonModalidades;
    //listview para rellenar
    ListView lViewModalidades;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //TODO OBTENER TAMBIEN LA CONFIGURACION DEL IDIOMA DEL MOVIL

        //obtenemos la configuracion del dispositivo para el modo dia-noche
        Configuration configuracion = Resources.getSystem().getConfiguration();
        //cargamos el layout que corresponde segun el modo de iluminacion del movil
        cambiarDiaNoche(configuracion);

        try {
            listaJsonModalidades = new JSONArray(recibirDatos());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //identificamos lo elementos de la interfaz
        identificarElementosInterfaz(configuracion);;
        cargarAnimaciones();
        ArrayList<String> modalidades = obtenerValorModalidades();
        lViewModalidades.setAdapter(new AdaptadorListaGenerico(this,modalidades,null,configuracion));
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

    private String recibirDatos(){
        String resultado;
        Bundle extras = getIntent().getExtras();
        resultado = extras.getString("listaModalidades");
        return resultado;
    }

    private void obtenerDatosFichero(int indice){
        String url = "";
        try {
            url=listaJsonModalidades.getJSONObject(indice).getString("enlace_lista");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LecturaFicheroHorarios lector=new LecturaFicheroHorarios(url);
        //creamos una referencia de esta objeto en la clase LecturaFichero
        lector.delegar = this;
        //una vez se ejecute el siguiente método, el hilo de la clase actual acabará y empezará el hilo de lectura
        lector.execute();
    }


    public void procesoFinalizado(ArrayList<String> salida) {
        //una vez conseguidos todos los datos se cambia a la actividad
        Intent i=new Intent(this, AcListaCursos.class);
        i.putExtra("datos",salida);
        startActivity(i);
    }

    @Override
    public void cambiarDiaNoche(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.uiMode == MODO_CLARO) {
            setContentView(R.layout.activity_horarios_claro);
        } else if (newConfig.uiMode == MODO_OSCURO){
            setContentView(R.layout.activity_horarios_osc);
        }
    }

    @Override
    public void identificarElementosInterfaz(Configuration newConfig) {
        lyTituloHorarios = findViewById(R.id.ly_titulo_horarios_claro);
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
                obtenerDatosFichero(i);
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