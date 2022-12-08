package com.example.infoaugustobriga;

import androidx.appcompat.app.AppCompatActivity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.ImageView;

public class AcMenuPrincipal extends AppCompatActivity {

    ImageView icCalendario, icHorarios, icMapa, icNovedades, icCentro, icProfesorado, idRed;

    //Modo claro y oscuro
    //los modos de la clase configuration no funcionan bien, por eso los pongo manualmente
    final int MODO_CLARO = 17;
    final int MODO_OSCURO = 33;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        //prueba modo oscuro
        cambiarDiaNoche(Resources.getSystem().getConfiguration());

    }


    public void cambiarDiaNoche(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);


        if (newConfig.uiMode == MODO_CLARO) {

        } else if (newConfig.uiMode == MODO_OSCURO){

        }
    }
}