package com.grupo4D.sag_system.service;

import com.grupo4D.sag_system.model.*;
import com.grupo4D.sag_system.model.request.FechaFront;
import com.grupo4D.sag_system.model.response.RepGLPEntregadoXCamionFront;
import com.grupo4D.sag_system.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;
@Service
public class ReportesService {

    @Autowired
    PedidoRepository pedidoRepository;

    @Autowired
    CamionRepository camionRepository;

    @Autowired
    RutaRepository rutaRepository;

    @Autowired
    RutaXPedidoRepository rutaXPedidoRepository;

    public ArrayList<RepGLPEntregadoXCamionFront> glpXCamionXFecha(FechaFront fecha){
        String f = fecha.getF().getYear()+"-"+ fecha.getF().getMonthValue()+"-"+fecha.getF().getDayOfMonth()+"%";
        String fe = fecha.getF().getYear()+"-"+ fecha.getF().getMonthValue()+"-"+fecha.getF().getDayOfMonth();
        ArrayList<RepGLPEntregadoXCamionFront> rep = new ArrayList<>();
//        ArrayList<Camion> camiones = (ArrayList<Camion>) camionRepository.findAll();
//        ArrayList<Pedido> pedidos = pedidoRepository.pedidosXFecha(f);
//        ArrayList<RutaXPedido> rutasXPedido = rutaXPedidoRepository.rutasXFecha(f);
        ArrayList<Ruta> rutas = rutaRepository.rutasCompletadasPorFechaYTipo(f,1);
        System.out.println(rutas.size()+ " tamaño rutas");
        double [] glpCamiones = new double[30];

        for (Ruta r: rutas ) {
            System.out.println(r.getCamion().getId()+ " Camion");
            //selecciona la rutaxpedido con el idRuta e idCamion
            ArrayList<RutaXPedido> rxps = rutaXPedidoRepository.rutasXIdRuta(r.getId());
            System.out.println(rxps.size()+ " tamaño rutasXPEDIDO");
            System.out.println(r.getId()+ " idRuta============");
            for (RutaXPedido rxp: rxps ) {
                System.out.println(rxp.getCantidadGLPEnviado()+" GLP");
                glpCamiones[r.getCamion().getId()] += rxp.getCantidadGLPEnviado();
            }
        }


        for (int i=0;i<30;i++){
            System.out.println(glpCamiones[i]);
            if (glpCamiones[i]!=0){
                RepGLPEntregadoXCamionFront repCamion = new RepGLPEntregadoXCamionFront();
                repCamion.setIdCamion(i);
                repCamion.setCantidadGLP(glpCamiones[i]);
                repCamion.setFecha(fe);
                String p = camionRepository.listarCodigo1Camion(repCamion.getIdCamion());
                repCamion.setPlaca(p);
                rep.add(repCamion);
            }
        }
        return rep;
    }
}
