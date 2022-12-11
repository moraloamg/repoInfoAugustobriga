package com.example.infoaugustobriga;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;


public class LecturaJson extends AsyncTask {

    private String url;
    AcMenuPrincipal menuPrincipal;
    String ficheroJson;

    public LecturaJson(String url){
        this.url = url;
    }


    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        menuPrincipal.lecturaFinalizada(ficheroJson);
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        URL urlFichero = null;
        try {

            urlFichero = new URL(url);
            BufferedReader fichero = new BufferedReader(new InputStreamReader(urlFichero.openStream()));
            StringBuffer buffer = new StringBuffer();
            String linea = "";
            while((linea = fichero.readLine()) != null){
                buffer.append(linea+"\n");
            }
            ficheroJson = buffer.toString();
            fichero.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
