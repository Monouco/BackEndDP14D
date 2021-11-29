package com.grupo4D.sag_system.model.reports;

public class ReporteCamionConsumoMensual {
    private int idCamion;
    private String codigoCamion;
    private double petroleoConsumido;
    private long distancia;
    private String mes;

    public ReporteCamionConsumoMensual(){

    }

    public ReporteCamionConsumoMensual(int idCamion, String codigoCamion, double petroleoConsumido, long distancia, String mes) {
        this.idCamion = idCamion;
        this.codigoCamion = codigoCamion;
        this.petroleoConsumido = petroleoConsumido;
        this.distancia = distancia;
        this.mes = mes;
    }

    public int getIdCamion() {
        return idCamion;
    }

    public void setIdCamion(int idCamion) {
        this.idCamion = idCamion;
    }

    public String getCodigoCamion() {
        return codigoCamion;
    }

    public void setCodigoCamion(String codigoCamion) {
        this.codigoCamion = codigoCamion;
    }

    public double getPetroleoConsumido() {
        return petroleoConsumido;
    }

    public void setPetroleoConsumido(double petroleoConsumido) {
        this.petroleoConsumido = petroleoConsumido;
    }

    public long getDistancia() {
        return distancia;
    }

    public void setDistancia(long distancia) {
        this.distancia = distancia;
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
}
