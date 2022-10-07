package com.example.emjeplobeacon;

public class Medida {
    public String valor;
    public  String fecha;
    public double latitud;
    public int longitud;

    public Medida(){

    }

    public Medida(String medida, String fecha, double latitud, int longitud) {
        this.valor = medida;
        this.fecha = fecha;
        this.latitud = latitud;
        this.longitud = longitud;
    }



    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public int getLongitud() {
        return longitud;
    }

    public void setLongitud(int longitud) {
        this.longitud = longitud;
    }


    @Override
    public String toString() {
        return "Medida{" +
                "valor=" + valor +
                ", fecha=" + fecha +
                ", latitud=" + latitud +
                ", longitud=" + longitud +
                '}';
    }
}
