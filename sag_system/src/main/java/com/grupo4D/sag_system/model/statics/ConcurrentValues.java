package com.grupo4D.sag_system.model.statics;

import java.util.concurrent.Semaphore;

public class ConcurrentValues {
    public static Semaphore updateVal = new Semaphore(1);
}
