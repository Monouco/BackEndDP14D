package com.grupo4D.sag_system.service;

import com.grupo4D.sag_system.model.*;
import com.grupo4D.sag_system.model.reports.ReporteCamionConsumoMensual;
import com.grupo4D.sag_system.model.request.FechaFront;
import com.grupo4D.sag_system.model.response.RepGLPEntregadoXCamionFront;
import com.grupo4D.sag_system.repository.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
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

    public InputStreamResource reporteConsumoMensual(LocalDateTime fechaInicio, LocalDateTime fechaFin) throws Exception{
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        XSSFWorkbook workbook = new XSSFWorkbook();

        XSSFSheet sheet = workbook.createSheet("Reporte Consumo Mensual ");
        sheet.setColumnWidth(0, 5 * 256);
        sheet.setColumnWidth(1, 30 * 256);
        sheet.setColumnWidth(2, 30 * 256);
        sheet.setColumnWidth(3, 30 * 256);

        String mesAnt = "";
        String mesAct;
        int k = 1;
        int rows = 0;

        ArrayList<ReporteCamionConsumoMensual> consumoMensual = camionRepository.generarReporteConsumoMensual(fechaInicio, fechaFin);

        for (ReporteCamionConsumoMensual linea:
             consumoMensual) {
            mesAct = linea.getMes();
            if(mesAct != mesAnt){
                k = rows +1;

                Row filaTitulo = sheet.createRow(k);
                Cell celda;
                for(int i = 1; i < 4; i++){
                    celda = filaTitulo.createCell(i);
                }

                sheet.addMergedRegion(new CellRangeAddress(k,k,1,3));
                filaTitulo.getCell(1).setCellValue(mesAct);

                Row filaHeader = sheet.createRow(k+1);
                Cell celdaCodigo = filaHeader.createCell(1);
                celdaCodigo.setCellValue("Codigo Camion");
                Cell celdaConsumo = filaHeader.createCell(2);
                celdaConsumo.setCellValue("Consumo Petroleo");
                Cell celdaDistancia = filaHeader.createCell(3);
                celdaDistancia.setCellValue("Distancia Recorrida");

                rows += 3;
            }
            Row filaContenido = sheet.createRow(rows);
            Cell celdaCodigoVal = filaContenido.createCell(1);
            celdaCodigoVal.setCellValue(linea.getCodigoCamion());
            Cell celdaConsumoVal = filaContenido.createCell(2);
            celdaConsumoVal.setCellValue(linea.getPetroleoConsumido());
            Cell celdaDistanciaVal = filaContenido.createCell(3);
            celdaDistanciaVal.setCellValue(linea.getDistancia());

            rows++;
            mesAnt = mesAct;
        }


        workbook.write(stream);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(stream.toByteArray());
        workbook.close();
        return new InputStreamResource(inputStream);
    }
}
