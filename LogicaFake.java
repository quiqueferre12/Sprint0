package com.example.emjeplobeacon;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
// Clase Logica Fake
// Autor: Enrique Ferre Carbonell
//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
public class LogicaFake {

    public String URL;
    public Medida medida;
   public Context context;

    //Constructores--------------------------------
    public LogicaFake() {

    }
    public LogicaFake(String URL) {
        this.URL = URL;
    }
    public LogicaFake(String URL, Medida medida) {
        this.URL = URL;
        this.medida = medida;
    }
    public LogicaFake(String URL, Medida medida, Context context) {
        this.URL = URL;
        this.medida = medida;
        this.context= context;
    }
//Getters y setters----------------------------------------------------------
    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public Medida getMedida() {
        return medida;
    }

    public void setMedida(Medida medida) {
        this.medida = medida;
    }
    public void setContext(Context context){
        this.context = context;
    }
    //Metodos----------------------------------------------------------------------

    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Funcion  insertarMedida
    // Disenyo    Medida, URL, Contexto --> insertarMedida()
    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public void insertarMedida(Medida medida){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(context, "Operacion resuelta correctamente", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
            }

        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {// devuelven parametros post al servidor
                Map<String,String> parametros= new HashMap<String,String>();// creamos la tabla hash map con sus clave sus valores
                String valor = medida.getValor();
                String fecha = medida.getFecha();

                 int longitud = medida.getLongitud();
                 String longTxt = String.valueOf(longitud);

                parametros.put("valor",valor);// actualizamos el hashMap usando el put
                parametros.put("distancia", longTxt);
                parametros.put("fecha", fecha);

                return parametros;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(context); //creamos la cola de peticiones pasandole el contexto
        requestQueue.add(stringRequest);//anyadimos la peticion
    }//fin guardarMedida()

}
