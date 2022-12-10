package com.example.infoaugustobriga;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

public class AcMenuPrincipal extends AppCompatActivity {

    //LinearLayout de los contenedores de iconos
    LinearLayout lyCalendario, lyHorarios, lyMapa, lyNovedades, lyCentro, lyProfesorado, lyYoutube, lyRadio;
    //LinearLayout del titulo de la cabecera
    LinearLayout lyTitulo;
    //Animaciones
    Animation animCabecera, animSubcabecera, animContenedores;


    //Modo claro y oscuro
    //los modos de la clase configuration no funcionan bien, por eso los pongo manualmente
    final int MODO_CLARO = 17;
    final int MODO_OSCURO = 33;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //TODO OBTENER TAMBIEN LA CONFIGURACION DEL IDIOMA DEL MOVIL
        //TODO OBTENER ENLACES DEL FICHERO RAIZ

        //obtenemos la configuracion del dispositivo para el modo dia-noche
        Configuration configuracion = Resources.getSystem().getConfiguration();
        //cargamos el layout que corresponde segun el modo de iluminacion del movil
        cambiarDiaNoche(configuracion);
        //identificamos lo elementos de la interfaz
        identificarElementosInterfaz(configuracion);
        cargarAnimaciones();
        activarBotonesMenu();


        //TODO COMPROBAR QUE HAY CONEXION A INTERNET AL FINAL DE LA CARGA Y MOSTRAR UN MENSAJE DE ERROR
    }


    private void cambiarDiaNoche(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.uiMode == MODO_CLARO) {
            setContentView(R.layout.activity_menu_principal_claro);
        } else if (newConfig.uiMode == MODO_OSCURO){
            setContentView(R.layout.activity_menu_principal_osc);
        }
    }

    private void identificarElementosInterfaz(Configuration newConfig){
        if (newConfig.uiMode == MODO_CLARO) {
            lyCalendario = findViewById(R.id.ly_calendario_claro);
            lyHorarios = findViewById(R.id.ly_horario_claro);
            lyNovedades = findViewById(R.id.ly_novedades_claro);
            lyMapa = findViewById(R.id.ly_plano_claro);
            lyCentro = findViewById(R.id.ly_centro_claro);
            lyProfesorado = findViewById(R.id.ly_profesorado_claro);
            lyYoutube = findViewById(R.id.ly_youtube_claro);
            lyRadio = findViewById(R.id.ly_radio_claro);
            lyTitulo = findViewById(R.id.ly_titulo_claro);
        } else if (newConfig.uiMode == MODO_OSCURO){
            lyCalendario = findViewById(R.id.ly_calendario_osc);
            lyHorarios = findViewById(R.id.ly_horario_osc);
            lyNovedades = findViewById(R.id.ly_novedades_osc);
            lyMapa = findViewById(R.id.ly_plano_osc);
            lyCentro = findViewById(R.id.ly_centro_osc);
            lyProfesorado = findViewById(R.id.ly_profesorado_osc);
            lyYoutube = findViewById(R.id.ly_youtube_osc);
            lyRadio = findViewById(R.id.ly_radio_osc);
            lyTitulo = findViewById(R.id.ly_titulo_osc);
        }
    }

    private void cargarAnimaciones(){
        animContenedores = AnimationUtils.loadAnimation(this,R.anim.anim_contenedores);
        animCabecera = AnimationUtils.loadAnimation(this,R.anim.anim_cabecera);

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

    private void activarBotonesMenu(){
        lyNovedades.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent irNovedades=new Intent(Intent.ACTION_VIEW, Uri.parse("https://iesaugustobriga.educarex.es/"));
                startActivity(irNovedades);
            }
        });

        lyYoutube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent irYoutube=new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/channel/UCYqTO4pG-fD9KPyjvdI3f6w"));
                startActivity(irYoutube);
            }
        });
    }



    @Override
    public void onBackPressed() {
        this.finishAffinity();
    }

}