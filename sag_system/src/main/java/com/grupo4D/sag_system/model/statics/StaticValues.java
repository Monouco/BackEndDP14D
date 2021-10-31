package com.grupo4D.sag_system.model.statics;

import java.time.LocalDateTime;
import java.time.ZoneId;

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
}
