package com.grupo4D.sag_system.model.response;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ConsumoPetroleoFront {
    private ArrayList<ConsumoPetroleoNodoFront> consumoPetroleoNodoFront;

    public ConsumoPetroleoFront() {
    }

    public ConsumoPetroleoFront(ArrayList<ConsumoPetroleoNodoFront> consumoPetroleoNodoFront) {
        this.consumoPetroleoNodoFront = consumoPetroleoNodoFront;
    }

    public ArrayList<ConsumoPetroleoNodoFront> getConsumoPetroleoNodoFront() {
        return consumoPetroleoNodoFront;
    }

    public void setConsumoPetroleoNodoFront(ArrayList<ConsumoPetroleoNodoFront> consumoPetroleoNodoFront) {
        this.consumoPetroleoNodoFront = consumoPetroleoNodoFront;
    }
}
