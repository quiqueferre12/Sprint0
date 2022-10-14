package com.example.emjeplobeacon;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
// Clase Medida
// Autor: Enrique Ferre Carbonell
//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
public class Medida {
    //Atributos----------------------------------------------
    public String valor;
    public  String fecha;
    public int longitud;
    //Constructores----------------------------------------------------
    public Medida(){

    }

    public Medida(String medida, String fecha, int longitud) {
        this.valor = medida;
        this.fecha = fecha;
        this.longitud = longitud;
    }


    //Getters y setters----------------------------------------------------------------------
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



    public int getLongitud() {
        return longitud;
    }

    public void setLongitud(int longitud) {
        this.longitud = longitud;
    }

    //metodos--------------------------------------------------------------------
    @Override
    public String toString() {
        return "Medida{" +
                "valor=" + valor +
                ", fecha=" + fecha +
                ", longitud=" + longitud +
                '}';
    }
}
