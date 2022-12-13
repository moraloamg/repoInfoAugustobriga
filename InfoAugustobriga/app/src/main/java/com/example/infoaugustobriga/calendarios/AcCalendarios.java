package com.example.infoaugustobriga.calendarios;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
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

public class AcCalendarios extends AppCompatActivity implements IConfigurarActividad {

    //LinearLayout del titulo para las animaciones
    LinearLayout lyTituloCalendarios;
    //TextView del titulo
    TextView txtTitulo;
    //Animaciones
    Animation animTitulo;
    //lista de modalidades
    JSONArray listaApartados;
    //listview para rellenar
    ListView lViewApartados;
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
        ArrayList<String> listaApartados = obtenerValorApartados();
        lViewApartados.setAdapter(new AdaptadorListaGenerico(this,listaApartados,null,configuracion));
        activarBotones();

        //TODO COMPROBAR QUE HAY CONEXION A INTERNET AL FINAL DE LA CARGA Y MOSTRAR UN MENSAJE DE ERROR

    }

    private ArrayList<String> obtenerValorApartados() {
        ArrayList<String> resultado = new ArrayList<>();
        for (int i = 0; i < listaApartados.length(); i++) {
            String apartado = null;
            try {
                apartado = listaApartados.getJSONObject(i).getString("apartado");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            resultado.add(apartado);
        }
        return resultado;
    }

    private void recibirDatos(){
        Bundle extras = getIntent().getExtras();
        try {
            listaApartados = new JSONArray(extras.getString("listaApartados"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
        lyTituloCalendarios = findViewById(R.id.ly_titulo_claro);
        txtTitulo = findViewById(R.id.txt_titulo_claro);
        txtTitulo.setText(R.string.calendario);
        if(newConfig.uiMode == MODO_CLARO){
            lViewApartados = findViewById(R.id.listViewClaro);
        }else if (newConfig.uiMode == MODO_OSCURO){
            lViewApartados = findViewById(R.id.listViewOsc);
        }
    }

    @Override
    public void activarBotones() {
        lViewApartados.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    String enlaceFichero= listaApartados.getJSONObject(i).getString("enlace");

                    Intent irApartado=new Intent(Intent.ACTION_VIEW, Uri.parse(enlaceFichero));
                    startActivity(irApartado);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void cargarAnimaciones() {
        animTitulo = AnimationUtils.loadAnimation(this,R.anim.anim_cabecera);
        lyTituloCalendarios.startAnimation(animTitulo);
    }

    @Override
    public void onBackPressed() {
        Intent i=new Intent(getApplicationContext(), AcMenuPrincipal.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }
}