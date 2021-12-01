package com.grupo4D.sag_system.model.reports;

public class ReporteCapacidadAtencion {
    private double indicador;
    private String mes;
    private int agno;

    public ReporteCapacidadAtencion(){

    }

    public double getIndicador() {
        return indicador;
    }

    public void setIndicador(double indicador) {
        this.indicador = indicador;
    }

    public String getMes() {
        String rMes;
        switch (mes){
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

    public void setMes(String mes) {
        this.mes = mes;
    }

    public int getAgno() {
        return agno;
    }

    public void setAgno(int agno) {
        this.agno = agno;
    }

}
