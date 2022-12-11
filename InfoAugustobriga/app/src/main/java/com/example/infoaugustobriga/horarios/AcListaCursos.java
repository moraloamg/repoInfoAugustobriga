package com.example.infoaugustobriga.horarios;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
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
import android.widget.ListView;

import com.example.infoaugustobriga.Interfaces.IConfigurarActividad;
import com.example.infoaugustobriga.Interfaces.IModosClaroOscuro;
import com.example.infoaugustobriga.R;
import com.example.infoaugustobriga.adaptadoresListas.AdaptadorListaGenerico;

import java.util.ArrayList;

public class AcListaCursos extends AppCompatActivity implements IConfigurarActividad {

    ArrayList<String> contenidoFichero;
    ListView lViewCursos;
    Animation anim;
    Context contexto;
    Typeface fuenteContenedores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //obtenemos la configuracion del dispositivo para el modo dia-noche
        Configuration configuracion = Resources.getSystem().getConfiguration();
        //cargamos el layout que corresponde segun el modo de iluminacion del movil
        cambiarDiaNoche(configuracion);
        //identificamos lo elementos de la interfaz
        identificarElementosInterfaz(configuracion);
        contenidoFichero = recibirDatos();
        contenidoFichero.remove(0); //TODO arreglar fichero mas adelante
        lViewCursos.setAdapter(new AdaptadorListaGenerico(this,contenidoFichero,null,configuracion));
        activarBotones();
        //TODO poner mensaje de error por si peta


    }

    private ArrayList<String> recibirDatos(){
        ArrayList<String> resultado;
        Bundle extras = getIntent().getExtras();
        resultado = extras.getStringArrayList("datos");
        return resultado;
    }

    @Override
    public void onBackPressed() {
        Intent i=new Intent(getApplicationContext(), AcHorarios.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    @Override
    public void cambiarDiaNoche(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.uiMode == MODO_CLARO) {
            setContentView(R.layout.activity_lista_claro);
        } else if (newConfig.uiMode == MODO_OSCURO){
            setContentView(R.layout.activity_lista_osc);
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
                        Uri.parse("https://raw.githubusercontent.com/moraloamg/pruebaAugustobriga/main/horarios/"+horario+".png"));
                startActivity(irFoto);
            }
        });
    }


}