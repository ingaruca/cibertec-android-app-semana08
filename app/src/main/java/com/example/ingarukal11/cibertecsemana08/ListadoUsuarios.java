package com.example.ingarukal11.cibertecsemana08;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ListadoUsuarios extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_usuarios);

        Thread tr = new Thread(){
            public void run(){
                final String resul = consumirJSON();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        llenarLista(objDatos(resul));
                    }
                });
            }
        };

        tr.start();
    }

    public String consumirJSON(){
        URL url             = null;
        String linea        = "";
        int respuesta       = 0;
        StringBuilder resul = null;

        try{
            url                        = new URL("http://xamarin.addictphones.com/listado.php");
            HttpURLConnection conexion = (HttpURLConnection)url.openConnection();
            respuesta                  = conexion.getResponseCode();

            resul = new StringBuilder();

            if(respuesta == HttpURLConnection.HTTP_OK){
                InputStream in        = new BufferedInputStream(conexion.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                while ((linea = reader.readLine()) != null){
                    resul.append(linea);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return resul.toString();
    }

    public ArrayList<String> objDatos(String resul){
        ArrayList<String> lista = new ArrayList<String>();

        try{
            JSONArray json = new JSONArray(resul);
            String texto   = "";

            for (int i = 0; i < json.length(); i ++){
                texto = json.getJSONObject(i).getInt("codigo") + "-" +
                        json.getJSONObject(i).getString("nombre") + "-" +
                        json.getJSONObject(i).getString("correo");
                lista.add(texto);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return lista;
    }

    public void llenarLista(ArrayList<String> lista){
        ArrayAdapter<String> adap = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lista);
        ListView listUsuarios     = (ListView)findViewById(R.id.listUsuarios);
        listUsuarios.setAdapter(adap);
    }
}
