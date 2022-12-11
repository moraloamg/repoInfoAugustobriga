package com.example.infoaugustobriga;

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
import android.widget.LinearLayout;

import com.example.infoaugustobriga.Interfaces.IConfigurarActividad;
import com.example.infoaugustobriga.horarios.AcHorarios;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import java.util.ArrayList;

public class AcMenuPrincipal extends AppCompatActivity implements IConfigurarActividad {

    //LinearLayout de los contenedores de iconos
    LinearLayout lyCalendario, lyHorarios, lyMapa, lyNovedades, lyCentro, lyProfesorado, lyYoutube, lyRadio;
    //LinearLayout del titulo de la cabecera
    LinearLayout lyTitulo;
    //Animaciones
    Animation animCabecera, animSubcabecera, animContenedores;
    //fichero .json con todos los enlaces
    JSONObject json;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //TODO OBTENER TAMBIEN LA CONFIGURACION DEL IDIOMA DEL MOVIL

        //obtenemos la configuracion del dispositivo para el modo dia-noche
        Configuration configuracion = Resources.getSystem().getConfiguration();
        //cargamos el layout que corresponde segun el modo de iluminacion del movil
        cambiarDiaNoche(configuracion);
        //leemos el fichero raiz del cual obtendremos todos los enlaces
        leerFicheroRaiz("https://raw.githubusercontent.com/moraloamg/pruebaAugustobriga/main/fichero_raiz.json");
        //identificamos lo elementos de la interfaz
        identificarElementosInterfaz(configuracion);
        cargarAnimaciones();
        activarBotones();

    }

    public void leerFicheroRaiz(String url){
        //TODO conectar primero para saber si hay conexion a internet o si el fichero existe
        LecturaJson fr= new LecturaJson(url);
        fr.menuPrincipal = this;
        fr.execute();
    }

    public void lecturaFinalizada(String ficheroJson){
        try {
            json = new JSONObject(ficheroJson);
            //TODO Anadir aqui mensaje de error por si el fichero esta corrupto
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void cambiarDiaNoche(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.uiMode == MODO_CLARO) {
            setContentView(R.layout.activity_menu_principal_claro);
        } else if (newConfig.uiMode == MODO_OSCURO){
            setContentView(R.layout.activity_menu_principal_osc);
        }
    }

    @Override
    public void identificarElementosInterfaz(Configuration newConfig){
        lyTitulo = findViewById(R.id.ly_titulo_claro);
        if (newConfig.uiMode == MODO_CLARO) {
            lyCalendario = findViewById(R.id.ly_calendario_claro);
            lyHorarios = findViewById(R.id.ly_horario_claro);
            lyNovedades = findViewById(R.id.ly_novedades_claro);
            lyMapa = findViewById(R.id.ly_plano_claro);
            lyCentro = findViewById(R.id.ly_centro_claro);
            lyProfesorado = findViewById(R.id.ly_profesorado_claro);
            lyYoutube = findViewById(R.id.ly_youtube_claro);
            lyRadio = findViewById(R.id.ly_radio_claro);
        } else if (newConfig.uiMode == MODO_OSCURO){
            lyCalendario = findViewById(R.id.ly_calendario_osc);
            lyHorarios = findViewById(R.id.ly_horario_osc);
            lyNovedades = findViewById(R.id.ly_novedades_osc);
            lyMapa = findViewById(R.id.ly_plano_osc);
            lyCentro = findViewById(R.id.ly_centro_osc);
            lyProfesorado = findViewById(R.id.ly_profesorado_osc);
            lyYoutube = findViewById(R.id.ly_youtube_osc);
            lyRadio = findViewById(R.id.ly_radio_osc);
        }
    }

    public void cargarAnimaciones(){
        animContenedores = AnimationUtils.loadAnimation(this,R.anim.anim_contenedores);
        animCabecera = AnimationUtils.loadAnimation(this, R.anim.anim_cabecera);

        lyTitulo.startAnimation(animCabecera);

        lyCalendario.startAnimation(animContenedores);
        lyHorarios.startAnimation(animContenedores);
        lyNovedades.startAnimation(animContenedores);
        lyMapa.startAnimation(animContenedores);
        lyCentro.startAnimation(animContenedores);
        lyProfesorado.startAnimation(animContenedores);
        lyYoutube.startAnimation(animContenedores);
        lyRadio.startAnimation(animContenedores);
    }

    @Override
    public void activarBotones(){
        //TODO anadir mensaje de error en cada uno de los listeners
        lyCentro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        lyNovedades.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent irNovedades= null;
                try {
                    irNovedades = new Intent(Intent.ACTION_VIEW, Uri.parse(json.getString("Novedades")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                startActivity(irNovedades);
            }
        });
        lyMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent irPlano= null;
                try {
                    irPlano = new Intent(Intent.ACTION_VIEW, Uri.parse(json.getString("Plano del centro")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                startActivity(irPlano);
            }
        });
        lyProfesorado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        lyHorarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent irHorarios=new Intent(AcMenuPrincipal.this, AcHorarios.class);
                try {
                    //pasamos el array como string y lo volvemos a convertir en la actividad de destino
                    irHorarios.putExtra("listaModalidades",json.getJSONArray("Horarios").toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                startActivity(irHorarios);
            }
        });
        lyCalendario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        lyYoutube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent irYoutube= null;
                try {
                    irYoutube = new Intent(Intent.ACTION_VIEW, Uri.parse(json.getString("Youtube")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                startActivity(irYoutube);
            }
        });
        lyRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent irRadio= null;
                try {
                    irRadio = new Intent(Intent.ACTION_VIEW, Uri.parse(json.getString("Radio")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                startActivity(irRadio);
            }
        });
    }



    @Override
    public void onBackPressed() {
        this.finishAffinity();
    }

}