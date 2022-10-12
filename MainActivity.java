package com.example.emjeplobeacon;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;


//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
// Clase Main Activity
// Autor: Enrique Ferre Carbonell
//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

public class MainActivity extends AppCompatActivity {
    // --------------------------------------------------------------
    // --------------------------------------------------------------
    private TextView textoMuestra;//texto para visualizar cada medida que se realiza en el xml
    private boolean emitiendo =true;//varaible para alternar entre medidas
    String URL ="http://192.168.0.20/develop/insertar_medida.php";//cambiar ip para cuando se use
    Context context;//contexto de mainActivity
    // --------------------------------------------------------------
    // --------------------------------------------------------------
    private static final String ETIQUETA_LOG = ">>>>";

    private static final int CODIGO_PETICION_PERMISOS = 11223344;

    // --------------------------------------------------------------
    // --------------------------------------------------------------
    private BluetoothLeScanner elEscanner;

    private ScanCallback callbackDelEscaneo = null;

    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Funcion buscar todos los dispositivos
    // Disenyo     buscarTodosLosDispositivosBTLE() --> Resultado Scaneo
    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    @SuppressLint("MissingPermission")
    private void buscarTodosLosDispositivosBTLE() {
        Log.d(ETIQUETA_LOG, " buscarTodosLosDispositivosBTL(): empieza ");

        Log.d(ETIQUETA_LOG, " buscarTodosLosDispositivosBTL(): instalamos scan callback ");

        this.callbackDelEscaneo = new ScanCallback() {
            @Override
            public void onScanResult( int callbackType, ScanResult resultado ) {//Devolución de llamada cuando se ha encontrado un BTLE.
                super.onScanResult(callbackType, resultado);
                Log.d(ETIQUETA_LOG, " buscarTodosLosDispositivosBTL(): onScanResult() ");

                mostrarInformacionDispositivoBTLE( resultado );
            }

            @Override
            public void onBatchScanResults(List<ScanResult> results) {//Devolución de llamada cuando se entregan los resultados del lote
                super.onBatchScanResults(results);
                Log.d(ETIQUETA_LOG, " buscarTodosLosDispositivosBTL(): onBatchScanResults() ");

            }

            @Override
            public void onScanFailed(int errorCode) {//Devolución de llamada cuando no se pudo iniciar el escaneo
                super.onScanFailed(errorCode);
                Log.d(ETIQUETA_LOG, " buscarTodosLosDispositivosBTL(): onScanFailed() ");

            }
        };

        Log.d(ETIQUETA_LOG, " buscarTodosLosDispositivosBTL(): empezamos a escanear ");

        this.elEscanner.startScan( this.callbackDelEscaneo);

    } // fin buscarDispositivosBTLE()

    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Funcion mostrar informacion dispositivio btle
    // Disenyo    ScanResult --> mostrarInformacionDispositivoBTLE()
    //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    @SuppressLint("MissingPermission")
    private void mostrarInformacionDispositivoBTLE(ScanResult resultado ) {

        BluetoothDevice bluetoothDevice = resultado.getDevice();
        byte[] bytes = resultado.getScanRecord().getBytes();
        int rssi = resultado.getRssi();

        Log.d(ETIQUETA_LOG, " ****************************************************");
        Log.d(ETIQUETA_LOG, " ****** DISPOSITIVO DETECTADO BTLE ****************** ");
        Log.d(ETIQUETA_LOG, " ****************************************************");
        Log.d(ETIQUETA_LOG, " nombre = " + bluetoothDevice.getName());
        Log.d(ETIQUETA_LOG, " toString = " + bluetoothDevice.toString());

        /*
        ParcelUuid[] puuids = bluetoothDevice.getUuids();
        if ( puuids.length >= 1 ) {
            //Log.d(ETIQUETA_LOG, " uuid = " + puuids[0].getUuid());
           // Log.d(ETIQUETA_LOG, " uuid = " + puuids[0].toString());
        }*/

        Log.d(ETIQUETA_LOG, " dirección = " + bluetoothDevice.getAddress());
        Log.d(ETIQUETA_LOG, " rssi = " + rssi );

        Log.d(ETIQUETA_LOG, " bytes = " + new String(bytes));
        Log.d(ETIQUETA_LOG, " bytes (" + bytes.length + ") = " + Utilidades.bytesToHexString(bytes));

        TramaIBeacon tib = new TramaIBeacon(bytes);



        Log.d(ETIQUETA_LOG, " ----------------------------------------------------");
        Log.d(ETIQUETA_LOG, " prefijo  = " + Utilidades.bytesToHexString(tib.getPrefijo()));
        Log.d(ETIQUETA_LOG, "          advFlags = " + Utilidades.bytesToHexString(tib.getAdvFlags()));
        Log.d(ETIQUETA_LOG, "          advHeader = " + Utilidades.bytesToHexString(tib.getAdvHeader()));
        Log.d(ETIQUETA_LOG, "          companyID = " + Utilidades.bytesToHexString(tib.getCompanyID()));
        Log.d(ETIQUETA_LOG, "          iBeacon type = " + Integer.toHexString(tib.getiBeaconType()));
        Log.d(ETIQUETA_LOG, "          iBeacon length 0x = " + Integer.toHexString(tib.getiBeaconLength()) + " ( "+ tib.getiBeaconLength() + " ) ");
        Log.d(ETIQUETA_LOG, " uuid  = " + Utilidades.bytesToHexString(tib.getUUID()));
        Log.d(ETIQUETA_LOG, " uuid  = " + Utilidades.bytesToString(tib.getUUID()));
        Log.d(ETIQUETA_LOG, " major  = " + Utilidades.bytesToHexString(tib.getMajor()) + "( " + Utilidades.bytesToInt(tib.getMajor()) + " ) ");
        Log.d(ETIQUETA_LOG, " minor  = " + Utilidades.bytesToHexString(tib.getMinor()) + "( " + Utilidades.bytesToInt(tib.getMinor()) + " ) ");
        Log.d(ETIQUETA_LOG, " txPower  = " + Integer.toHexString(tib.getTxPower()) + " ( " + tib.getTxPower() + " )");
        Log.d(ETIQUETA_LOG, " ****************************************************");

         /*if(cont==0){
            //pruebas para ver si va

           DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            dtf.format(LocalDateTime.now());
            medida.fecha = dtf.format(LocalDateTime.now());
            medida.medida= "PEPE";
            medida.latitud = rssi;
            medida.longitud = rssi;
            String res = medida.getMedida() + medida.getFecha();
            textoMuestra.setText(res);
            cont ++;
            LogicaFake envio = new LogicaFake(URL,medida);
            context=getApplicationContext();
            envio.setContext(context);
            envio.guardarMedida();

            //enviarMedida(URL,medida);
            //medida = obtenerMedida(resultado);
        }*/


    } // fin mostrarInformacionDispositivoBTLE()

    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Funcion buscar este dispositivo BTLE
    // Disenyo    String --> buscarEsteDispositivoBTLE() --> Resultado Scaneo
    //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    @SuppressLint("MissingPermission")
    private void buscarEsteDispositivoBTLE(String dispositivoBuscado ) {
        Log.d(ETIQUETA_LOG, " buscarEsteDispositivoBTLE(): empieza ");

        Log.d(ETIQUETA_LOG, "  buscarEsteDispositivoBTLE(): instalamos scan callback ");


        // super.onScanResult(ScanSettings.SCAN_MODE_LOW_LATENCY, result); para ahorro de energía

        this.callbackDelEscaneo = new ScanCallback() {//Devoluciones de llamada de exploración de Bluetooth LE
            @Override
            public void onScanResult( int callbackType, ScanResult resultado ) {//Devolución de llamada cuando se ha encontrado un BTLE.
                super.onScanResult(callbackType, resultado);

                // hacemos la conversion del ScanResult
                byte[] bytes = resultado.getScanRecord().getBytes();
                //gastamos la clase TramaBeacon para obtener el uuid
                TramaIBeacon tib = new TramaIBeacon(bytes);
                String nombre = resultado.getDevice().getName();
                Log.d(ETIQUETA_LOG, "  buscarEsteDispositivoBTLE(): Buscando: "+dispositivoBuscado+" y se ha encontrado: " + nombre);


                if(Objects.equals(nombre, dispositivoBuscado) && emitiendo ==true){//Si han pasado 5 seg y coincide con la uuid que buscabamos en la variable dispositivoBuscado
                    obtenerMedida( resultado );//llamamos a la funcion obtener medida
                    emitiendo = false;//cambiamos esta variable a flaso para que no vuelva a entrar en la condicion if hasta que no acabe la cuenta atras
                    long num = 5000;// indicamos el tiempo en milisegundos que tardara hasta la siguiente medida
                    iniciarCuentaAtras(num);//llamamos a la funcion para que cuando el tiempo llegue a 0 la variable emitiendo cambie a true
                }

            }


            @Override
            public void onScanFailed(int errorCode) {
                super.onScanFailed(errorCode);
                Log.d(ETIQUETA_LOG, "  Error al intentar buscar dispositivo");

            }
        };


        this.elEscanner.startScan( this.callbackDelEscaneo );
    } // ()

    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Funcion detener busqueda dispositivo BTLE
    // Disenyo    detenerBusquedaDispositivosBTLE() --> Resultado Scaneo
    //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    @SuppressLint("MissingPermission")
    private void detenerBusquedaDispositivosBTLE() {

        if ( this.callbackDelEscaneo == null ) {
            return;
        }
        emitiendo=false;// Ponemos la variable en falso para evitar que entre en el bucle dado que entre dentro de buscar dispositivo asi no emitira mas
        this.elEscanner.stopScan( this.callbackDelEscaneo );
        this.callbackDelEscaneo = null;

    } //fin detenerBusquedaDispositivosBTLE()

    // --------------------------------------------------------------
    // --------------------------------------------------------------
    public void botonBuscarDispositivosBTLEPulsado( View v ) {
        Log.d(ETIQUETA_LOG, " boton buscar dispositivos BTLE Pulsado" );
        this.buscarTodosLosDispositivosBTLE();
    } // ()

    // --------------------------------------------------------------
    // --------------------------------------------------------------
    public void botonBuscarNuestroDispositivoBTLEPulsado( View v ) {
        Log.d(ETIQUETA_LOG, " boton nuestro dispositivo BTLE Pulsado" );
        //cuando se pulsa el boton
        // se busca el dispositivo con el uuid que hemos introducido
        String nombre= "GTI-3AQuique";//el nombre
        emitiendo= true;
        buscarEsteDispositivoBTLE( nombre );//llamamos a la función

    } // ()

    // --------------------------------------------------------------
    // --------------------------------------------------------------
    public void botonDetenerBusquedaDispositivosBTLEPulsado( View v ) {
        Log.d(ETIQUETA_LOG, " boton detener busqueda dispositivos BTLE Pulsado" );
        detenerBusquedaDispositivosBTLE();
    } // ()

    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Funcion obtener Meida
    // Disenyo   inicializarBluetooth() -->  ScanResult
    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    @SuppressLint("MissingPermission")
    private void inicializarBlueTooth() {//funcion para iniciar el escaneo
        Log.d(ETIQUETA_LOG, " inicializarBlueTooth(): obtenemos adaptador BT ");

        BluetoothAdapter bta = BluetoothAdapter.getDefaultAdapter();// para iniciar  el bluetooth hace falta el adaptador

        Log.d(ETIQUETA_LOG, " inicializarBlueTooth(): habilitamos adaptador BT ");

        bta.enable();// se activa

        Log.d(ETIQUETA_LOG, " inicializarBlueTooth(): habilitado =  " + bta.isEnabled() );

        Log.d(ETIQUETA_LOG, " inicializarBlueTooth(): estado =  " + bta.getState() );

        Log.d(ETIQUETA_LOG, " inicializarBlueTooth(): obtenemos escaner btle ");

        this.elEscanner = bta.getBluetoothLeScanner();//se inicia el escaneo asociando al objeto bta el escaner

        if ( this.elEscanner == null ) {
            Log.d(ETIQUETA_LOG, " inicializarBlueTooth(): Socorro: NO hemos obtenido escaner btle  !!!!");

        }

        Log.d(ETIQUETA_LOG, " inicializarBlueTooth(): voy a perdir permisos (si no los tuviera) !!!!");
        //Pedimos los permisos comprobando si no se han dado
        if (
                ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        )
        {//si no se han dado se hace la peticion de pedir permisos
            ActivityCompat.requestPermissions(
                    MainActivity.this,
                    new String[]{Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.ACCESS_FINE_LOCATION},
                    CODIGO_PETICION_PERMISOS);
        }
        else {//si ya se han dado
            Log.d(ETIQUETA_LOG, " inicializarBlueTooth(): parece que YA tengo los permisos necesarios !!!!");

        }
    } // fin inicializarBlueTooth()


    // --------------------------------------------------------------
    // --------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textoMuestra = (TextView)findViewById(R.id.medidaTxt);
        Log.d(ETIQUETA_LOG, " onCreate(): empieza ");

        inicializarBlueTooth();

        Log.d(ETIQUETA_LOG, " onCreate(): termina ");

    } // fin onCreate()

    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Funcion obtener permisos
    // Disenyo    R, Txt, R--> obtenerMeida()
    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult( requestCode, permissions, grantResults);

        switch (requestCode) {
            case CODIGO_PETICION_PERMISOS:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Log.d(ETIQUETA_LOG, " onRequestPermissionResult(): permisos concedidos  !!!!");
                    // Permission is granted. Continue the action or workflow
                    // in your app.
                }  else {

                    Log.d(ETIQUETA_LOG, " onRequestPermissionResult(): Socorro: permisos NO concedidos  !!!!");

                }
                return;
        }
        // Other 'case' lines to check for other
        // permissions this app might request.
    } // fin onRequestPermissionsResult()

    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Funcion obtener Meida
    // Disenyo    ScanResult--> obtenerMeida() --> Medida(Logica Fake)
    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public void obtenerMedida(ScanResult resultado){
        //definimos las variables
        Medida medida= new Medida();
        byte[] bytes = resultado.getScanRecord().getBytes();
        TramaIBeacon tib = new TramaIBeacon(bytes);

        //la fecha siempre obtener fecha
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");//definimos un objeto de tipo date time formatter para establecer fecha e hora
        medida.setFecha(dtf.format(LocalDateTime.now()));

        //obtener medida
        /*
        int numero = (int)(Math.random()*(75-25+1)+25);
        String valorTxt = String.valueOf(numero);
        medida.setValor(valorTxt);*/
        medida.setValor(Utilidades.bytesToString(tib.getUUID()));

        //longitud
        medida.longitud = resultado.getRssi();
        int longitud = medida.getLongitud();


        //Esto es para llenar el text view que estan bajo de los botones
        String res = medida.getValor()+ longitud + medida.getFecha();
        textoMuestra.setText(res);
        //cont ++;

        LogicaFake envio = new LogicaFake(URL,medida);//creamos el objeto de tipo Logica Fake
        context=getApplicationContext();
        envio.setContext(context);
        envio.guardarMedida();



    }//fin obtenerMedida()

    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Funcion Iniciar cuenta atras
    // Disenyo   Z --> iniciarCuentaAtras() --> TRUE
    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    private void iniciarCuentaAtras(long numero){
        new CountDownTimer(numero,1000){

            @Override
            public void onTick(long l) {//durante el transcurso del tiempo que esta en marcha el CountDownTimer que tiene que hacer

            }

            @Override
            public void onFinish() {// que tiene que hcer cuando acabe
                emitiendo = true;
            }
        }.start();
    }//fin iniciarCuentaAtras()


    /*private void enviarMedida(String URL, Medida measure){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "Operacion resuelta correctamente", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }



        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros= new HashMap<String,String>();
                String id = measure.getMedida();
                String numero = measure.getFecha();
                parametros.put("id",id);
                parametros.put("numero", numero);
                return parametros;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }*/

} // class
// --------------------------------------------------------------
// --------------------------------------------------------------
// --------------------------------------------------------------
// --------------------------------------------------------------