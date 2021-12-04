package com.grupo4D.sag_system.model.reports;

public class ReporteCantidadPedidos {
    private int cantidadPedidos;
    private String fecha;

    public int getCantidadPedidos() {
        return cantidadPedidos;
    }

    public void setCantidadPedidos(int cantidadPedidos) {
        this.cantidadPedidos = cantidadPedidos;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
    public String getMes() {
        String rMes;
        switch (fecha){
            case "January":{
                rMes = "Enero";
                break;
            }
            case "February":{
                rMes = "Febrero";
                break;
            }
            case "March":{
                rMes = "Marzo";
                break;
            }
            case "April":{
                rMes = "Abril";
                break;
            }
            case "May":{
                rMes = "Mayo";
                break;
            }
            case "June":{
                rMes = "Junio";
                break;
            }
            case "July":{
                rMes = "Julio";
                break;
            }
            case "August":{
                rMes = "Agosto";
                break;
            }
            case "September":{
                rMes = "Septiembre";
                break;
            }
            case "October":{
                rMes = "Octubre";
                break;
            }
            case "November":{
                rMes = "Noviembre";
                break;
            }
            case "December":{
                rMes = "Diciembre";
                break;
            }
            default:{
                rMes = "None";
            }
        }
        return rMes;
    }
}
