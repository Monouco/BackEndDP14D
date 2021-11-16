package com.grupo4D.sag_system.model.statics;

import java.util.concurrent.Semaphore;

public class ConcurrentValues {
    public static Semaphore updateValSimulation = new Semaphore(1);
    public static Semaphore updateValCollapse = new Semaphore(1);
    public static Semaphore updateVal = new Semaphore(1);
}
