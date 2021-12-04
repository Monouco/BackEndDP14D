package com.grupo4D.sag_system.model.response;

public class RepGLPEntregadoXCamionFront implements Comparable<RepGLPEntregadoXCamionFront> {
    private int idCamion;
    private String placa;
    private double cantidadGLP;
    private String fecha;

    public RepGLPEntregadoXCamionFront() {
    }

    public RepGLPEntregadoXCamionFront(int idCamion, String placa, double cantidadGLP, String fecha) {
        this.idCamion = idCamion;
        this.placa = placa;
        this.cantidadGLP = cantidadGLP;
        this.fecha = fecha;
    }

    @Override
    public int compareTo(RepGLPEntregadoXCamionFront o) {
        if (this.getFecha().compareTo(o.getFecha())!=0){
            return this.getFecha().compareTo(o.getFecha());
        }else{
            return this.getIdCamion()-o.getIdCamion();
        }
    }

    public int getIdCamion() {
        return idCamion;
    }

    public void setIdCamion(int idCamion) {
        this.idCamion = idCamion;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public double getCantidadGLP() {
        return cantidadGLP;
    }

    public void setCantidadGLP(double cantidadGLP) {
        this.cantidadGLP = cantidadGLP;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
