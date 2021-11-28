package com.grupo4D.sag_system.service;

import com.grupo4D.sag_system.model.*;
import com.grupo4D.sag_system.model.request.Fecha1TipoFront;
import com.grupo4D.sag_system.model.request.Fecha2TipoFront;
import com.grupo4D.sag_system.model.request.FechaFront;
import com.grupo4D.sag_system.model.response.ConsumoPetroleoFront;
import com.grupo4D.sag_system.model.response.ConsumoPetroleoNodoFront;
import com.grupo4D.sag_system.model.response.RepGLPEntregadoXCamionFront;
import com.grupo4D.sag_system.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
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

    @Autowired
    RutaXNodoRepository rutaXNodoRepository;

    @Autowired
    TipoCamionRepository tipoCamionRepository;

    public ArrayList<RepGLPEntregadoXCamionFront> glpXCamionXFecha(Fecha1TipoFront fecha){
        String f = fecha.getF().getYear()+"-"+ fecha.getF().getMonthValue()+"-"+fecha.getF().getDayOfMonth()+"%";
        String fe = fecha.getF().getYear()+"-"+ fecha.getF().getMonthValue()+"-"+fecha.getF().getDayOfMonth();
        ArrayList<RepGLPEntregadoXCamionFront> rep = new ArrayList<>();
//        ArrayList<Camion> camiones = (ArrayList<Camion>) camionRepository.findAll();
//        ArrayList<Pedido> pedidos = pedidoRepository.pedidosXFecha(f);
//        ArrayList<RutaXPedido> rutasXPedido = rutaXPedidoRepository.rutasXFecha(f);
        ArrayList<Ruta> rutas = rutaRepository.rutasCompletadasPorFechaYTipo(f,fecha.getTipo());
        //System.out.println(rutas.size()+ " tamaño rutas");
        double [] glpCamiones = new double[30];

        for (Ruta r: rutas ) {
            //System.out.println(r.getCamion().getId()+ " Camion");
            //selecciona la rutaxpedido con el idRuta e idCamion
            ArrayList<RutaXPedido> rxps = rutaXPedidoRepository.rutasXIdRuta(r.getId());
            //System.out.println(rxps.size()+ " tamaño rutasXPEDIDO");
            //System.out.println(r.getId()+ " idRuta============");
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


    public ConsumoPetroleoFront consumoPetroleoDiario(Fecha2TipoFront req){
        ConsumoPetroleoFront rep = new ConsumoPetroleoFront();
        ArrayList<ConsumoPetroleoNodoFront> lista = new ArrayList<>();
        FechaFront fInicio = new FechaFront(req.getFechaInicio());
        FechaFront fFin = new FechaFront(req.getFechaFin());
        String fI = fInicio.getF().getYear()+"-"+ fInicio.getF().getMonthValue()+"-"+fInicio.getF().getDayOfMonth();
        String fF = fFin.getF().getYear()+"-"+ fFin.getF().getMonthValue()+"-"+fFin.getF().getDayOfMonth();

        ArrayList<Ruta> rutas = rutaRepository.rutasCompletadasEntreFechasPorTipo(fI,fF, req.getTipo());
        double consumo =0;

        if (rutas.size()>0) {
            LocalDateTime fechaActual = rutas.get(0).getFechaFin();
            String dia = String.format("%02d", fechaActual.getDayOfMonth());
            String fecha = fechaActual.getYear() +"-"+ fechaActual.getMonthValue()+"-"+dia;
            for (Ruta r : rutas) {
                String fechaComparar = r.getFechaFin().getYear()+"-"+r.getFechaFin().getMonthValue()+"-"+r.getFechaFin().getDayOfMonth();
                String fechaCompararActual = fechaActual.getYear()+"-"+fechaActual.getMonthValue()+"-"+fechaActual.getDayOfMonth();
                if(!(fechaComparar.equals(fechaCompararActual))){
                    ConsumoPetroleoNodoFront nodo = new ConsumoPetroleoNodoFront();
                    nodo.setFecha(fecha);
                    nodo.setConsumo(consumo);
                    lista.add(nodo);
                    consumo =0;
                    fechaActual = r.getFechaFin();
                    dia = String.format("%02d", fechaActual.getDayOfMonth());
                    fecha = fechaActual.getYear() +"-"+ fechaActual.getMonthValue()+"-"+dia;
                }
                consumo += r.getCostoOperacion();
            }
        }



        rep.setConsumoPetroleoNodoFront(lista);
        return rep;
    }

}
