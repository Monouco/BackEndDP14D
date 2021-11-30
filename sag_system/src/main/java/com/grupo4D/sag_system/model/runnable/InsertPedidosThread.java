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

        if(type ==2){
            this.orders = StaticValues.ordersSim;
        }
        else{
            this.orders = StaticValues.ordersCollapse;
        }

        System.out.println("Pedidos por insertar " + type + ": " + this.orders.size());

        pedidoRepository.saveAll(this.orders);
    }
}
