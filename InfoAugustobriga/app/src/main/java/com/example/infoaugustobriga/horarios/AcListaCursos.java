package com.example.infoaugustobriga.horarios;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.infoaugustobriga.Interfaces.IConfigurarActividad;
import com.example.infoaugustobriga.R;
import com.example.infoaugustobriga.miscelanea.AdaptadorListaGenerico;
import com.example.infoaugustobriga.miscelanea.Mensaje;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class AcListaCursos extends AppCompatActivity implements IConfigurarActividad {

    ArrayList<String> listaCursos;
    ListView lViewCursos;
    Typeface fuenteContenedores;
    String urlCarpeta, extension;

    //es necesario volver a enviar los datos de vuelta a la actividad anterior
    JSONArray listaJsonModalidades;
    //Mensaje para casos de error
    Mensaje m;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        m = new Mensaje("Error","Ha ocurrido un error al acceder a esta opción",this);

        //obtenemos la configuracion del dispositivo para el modo dia-noche
        Configuration configuracion = Resources.getSystem().getConfiguration();
        //cargamos el layout que corresponde segun el modo de iluminacion del movil
        cambiarDiaNoche(configuracion);
        //identificamos lo elementos de la interfaz
        identificarElementosInterfaz(configuracion);
        recibirDatos();
        if(listaJsonModalidades==null){
            m.mostrarMensaje();
        }else {
            lViewCursos.setAdapter(new AdaptadorListaGenerico(this, listaCursos, null, configuracion));
            activarBotones();
        }
    }

    private void recibirDatos(){
        Bundle extras = getIntent().getExtras();
        listaCursos = extras.getStringArrayList("listaCursos");
        urlCarpeta = extras.getString("url_carpeta");
        extension = extras.getString("extension");

        try {
            listaJsonModalidades = new JSONArray(extras.getString("listaJsonModalidades"));
        } catch (JSONException e) {
            e.printStackTrace();
            m.mostrarMensaje();
        }
    }

    @Override
    public void onBackPressed() {
        Intent i=new Intent(getApplicationContext(), AcHorarios.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.putExtra("listaModalidades",listaJsonModalidades.toString());
        startActivity(i);
    }

    @Override
    public void cambiarDiaNoche(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.uiMode == MODO_CLARO) {
            setContentView(R.layout.activity_lista_generica_claro);
        } else if (newConfig.uiMode == MODO_OSCURO){
            setContentView(R.layout.activity_lista_generica_osc);
        }
    }

    @Override
    public void identificarElementosInterfaz(Configuration newConfig) {
        if (newConfig.uiMode == MODO_CLARO) {
            lViewCursos = findViewById(R.id.listViewClaro);
        } else if (newConfig.uiMode == MODO_OSCURO){
            lViewCursos = findViewById(R.id.listViewOsc);
        }
    }

    @Override
    public void activarBotones() {
        lViewCursos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String horario = lViewCursos.getItemAtPosition(i).toString();
                Intent irFoto=new Intent(Intent.ACTION_VIEW,
                        Uri.parse(urlCarpeta+horario+extension));
                startActivity(irFoto);
            }
        });
    }


}