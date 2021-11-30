package com.grupo4D.sag_system.model.statics;

import com.grupo4D.sag_system.model.Pedido;
import com.grupo4D.sag_system.model.response.ColapsoFront;
import org.springframework.core.io.InputStreamResource;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;

public class StaticValues {
    public static int mult;
    public static LocalDateTime start;
    public static LocalDateTime virtualDate;
    public static int simulationType;
    public static LocalDateTime end;
    public static boolean collapseFlag;
    public static boolean endSimulationFlag;
    public static boolean collapseSimulationFlag;
    public static boolean fullCollapseFlag;
    public static ZoneId zoneId = ZoneId.of("America/Lima");
    public static int idCamion;
    public static boolean comCollapseFlag;
    public static boolean comSimCollapseFlag;
    public static boolean comFullCollapseFlag;
    public static boolean comEndSimFlag;
    public static boolean comEndCollapseFlag;
    public static int numCamion;
    public static ColapsoFront collapseStatus = new ColapsoFront();
    public static ColapsoFront collapseSimStatus = new ColapsoFront();
    public static ColapsoFront fullCollapseStatus = new ColapsoFront();
    public static ArrayList<Pedido> ordersSim = new ArrayList<>();
    public static ArrayList<Pedido> ordersCollapse = new ArrayList<>();
    public static InputStreamResource reportCapacity;
    public static InputStreamResource simReportCapacity;
    public static InputStreamResource collapseReportCapacity;
}
