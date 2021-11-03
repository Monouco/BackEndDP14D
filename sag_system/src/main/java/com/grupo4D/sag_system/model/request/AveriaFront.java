package com.grupo4D.sag_system.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;

public class AveriaFront {

        private int idCamion;

        @JsonFormat(pattern = "yyyy-MM-dd@HH:mm:ss")
        private String fecha;

        public AveriaFront() {
        }

        public AveriaFront(int idCamion, String fecha) {
                this.setIdCamion(idCamion);
                this.setFecha(fecha);
        }

        public int getIdCamion() {
                return idCamion;
        }

        public void setIdCamion(int idCamion) {
                this.idCamion = idCamion;
        }

        public String getFecha() {
                return fecha;
        }

        public void setFecha(String fecha) {
                this.fecha = fecha;
        }
}
