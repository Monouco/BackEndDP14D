package com.grupo4D.sag_system.model.statics;

import java.util.concurrent.Semaphore;

public class ConcurrentValues {
    public static Semaphore updateValSimulation = new Semaphore(0);
    public static Semaphore updateValCollapse = new Semaphore(0);
    public static Semaphore updateVal = new Semaphore(0);
    public static Semaphore freeUpdateValSimulation = new Semaphore(0);
    public static Semaphore freeUpdateValCollapse = new Semaphore(0);
    public static Semaphore freeUpdateVal = new Semaphore(0);
    public static Semaphore newSimulationDay = new Semaphore(0);
    public static Semaphore newCollapseDay = new Semaphore(0);
}
