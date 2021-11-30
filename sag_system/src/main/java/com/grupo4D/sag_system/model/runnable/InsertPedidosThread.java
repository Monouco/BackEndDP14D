package com.grupo4D.sag_system.model.runnable;

import com.grupo4D.sag_system.model.Pedido;
import com.grupo4D.sag_system.model.statics.StaticValues;
import com.grupo4D.sag_system.repository.CamionRepository;
import com.grupo4D.sag_system.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@Scope("prototype")
public class InsertPedidosThread implements Runnable{

    @Autowired
    PedidoRepository pedidoRepository;

    private ArrayList<Pedido> orders;
    private int type;

    @Override
    public void run(){
        this.type = StaticValues.simulationType;
        this.orders = (type == 2) ? StaticValues.ordersSim : StaticValues.ordersCollapse;

        pedidoRepository.saveAll(this.orders);
    }
}
