package com.grupo4D.sag_system.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.grupo4D.sag_system.model.response.NodoFront;

import java.time.LocalDateTime;

public class AveriaFront {

        private int idCamion;

        @JsonFormat(pattern = "yyyy-MM-dd@HH:mm:ss")
        private String fecha;

        private LocalDateTime startDate;

        private LocalDateTime endDate;

        private NodoFront node;

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

        public LocalDateTime getStartDate() {
                return startDate;
        }

        public void setStartDate(LocalDateTime startDate) {
                this.startDate = startDate;
        }

        public LocalDateTime getEndDate() {
                return endDate;
        }

        public void setEndDate(LocalDateTime endDate) {
                this.endDate = endDate;
        }

        public NodoFront getNode() {
                return node;
        }

        public void setNode(NodoFront node) {
                this.node = node;
        }
}
