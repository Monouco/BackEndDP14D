package com.grupo4D.sag_system.model.reports;

public class ReporteUsoTipo {
    private int id;
    private String codigo;
    private double porcentajeGLP;
    private double porcentajePedido;

    public ReporteUsoTipo(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public double getPorcentajeGLP() {
        return porcentajeGLP;
    }

    public void setPorcentajeGLP(double porcentajeGLP) {
        this.porcentajeGLP = porcentajeGLP;
    }

    public double getPorcentajePedido() {
        return porcentajePedido;
    }

    public void setPorcentajePedido(double porcentajePedido) {
        this.porcentajePedido = porcentajePedido;
    }

}
