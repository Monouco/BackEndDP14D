package com.grupo4D.sag_system.service;

import com.grupo4D.sag_system.model.*;
import com.grupo4D.sag_system.model.reports.ReporteCamionConsumoMensual;
import com.grupo4D.sag_system.model.reports.ReporteCapacidadAtencion;
import com.grupo4D.sag_system.model.reports.ReporteUsoTipo;
import com.grupo4D.sag_system.model.request.Fecha1TipoFront;
import com.grupo4D.sag_system.model.request.Fecha2TipoFront;
import com.grupo4D.sag_system.model.request.FechaFront;
import com.grupo4D.sag_system.model.response.ConsumoPetroleoFront;
import com.grupo4D.sag_system.model.response.ConsumoPetroleoNodoFront;
import com.grupo4D.sag_system.model.response.RepGLPEntregadoXCamionFront;
import com.grupo4D.sag_system.repository.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
                //System.out.println(rxp.getCantidadGLPEnviado()+" GLP");
                glpCamiones[r.getCamion().getId()] += rxp.getCantidadGLPEnviado();
            }
        }


        for (int i=0;i<30;i++){
            //System.out.println(glpCamiones[i]);
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


    public InputStreamResource reporteConsumoMensual(LocalDateTime fechaInicio, LocalDateTime fechaFin, int tipo) throws Exception{
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        XSSFWorkbook workbook = new XSSFWorkbook();

        XSSFSheet sheet = workbook.createSheet("Reporte Consumo Mensual ");
        sheet.setColumnWidth(0, 5 * 256);
        sheet.setColumnWidth(1, 30 * 256);
        sheet.setColumnWidth(2, 30 * 256);
        sheet.setColumnWidth(3, 30 * 256);

        Font tituloFont = workbook.createFont();
        tituloFont.setBold(true);
        tituloFont.setFontHeight((short)(tituloFont.getFontHeight() + 30));

        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeight((short)(headerFont.getFontHeight() + 6));

        Font codigoFont = workbook.createFont();
        codigoFont.setBold(true);

        CellStyle estiloTitulo = workbook.createCellStyle();
        estiloTitulo.setAlignment(HorizontalAlignment.CENTER);
        estiloTitulo.setVerticalAlignment(VerticalAlignment.CENTER);
        estiloTitulo.setBorderBottom(BorderStyle.MEDIUM);
        estiloTitulo.setBorderLeft(BorderStyle.MEDIUM);
        estiloTitulo.setBorderRight(BorderStyle.MEDIUM);
        estiloTitulo.setBorderTop(BorderStyle.MEDIUM);

        estiloTitulo.setFont(tituloFont);

        CellStyle estiloHeader = workbook.createCellStyle();
        estiloHeader.setAlignment(HorizontalAlignment.CENTER);
        estiloHeader.setVerticalAlignment(VerticalAlignment.CENTER);
        estiloHeader.setBorderBottom(BorderStyle.MEDIUM);
        estiloHeader.setBorderLeft(BorderStyle.MEDIUM);
        estiloHeader.setBorderRight(BorderStyle.MEDIUM);
        estiloHeader.setBorderTop(BorderStyle.MEDIUM);

        estiloHeader.setFont(headerFont);

        CellStyle estiloCodigo = workbook.createCellStyle();
        estiloCodigo.setVerticalAlignment(VerticalAlignment.CENTER);
        estiloCodigo.setBorderBottom(BorderStyle.MEDIUM);
        estiloCodigo.setBorderLeft(BorderStyle.MEDIUM);
        estiloCodigo.setBorderRight(BorderStyle.MEDIUM);
        estiloCodigo.setBorderTop(BorderStyle.MEDIUM);

        estiloHeader.setFont(codigoFont);

        CellStyle estiloCelda = workbook.createCellStyle();
        estiloCelda.setVerticalAlignment(VerticalAlignment.CENTER);
        estiloCelda.setBorderBottom(BorderStyle.MEDIUM);
        estiloCelda.setBorderLeft(BorderStyle.MEDIUM);
        estiloCelda.setBorderRight(BorderStyle.MEDIUM);
        estiloCelda.setBorderTop(BorderStyle.MEDIUM);



        String mesAnt = "";
        String mesAct;
        int k = 1;
        int rows = 0;

        ArrayList<ReporteCamionConsumoMensual> consumoMensual = new ArrayList<>();
        List<Object[]> temp = camionRepository.generarReporteConsumoMensual(fechaInicio, fechaFin, tipo);
        for (Object[] t:
             temp) {
            ReporteCamionConsumoMensual consumo = new ReporteCamionConsumoMensual();
            consumo.setIdCamion((int)t[0]);
            consumo.setCodigoCamion((String)t[1]);
            //System.out.println("Esta clase es : "+t[2].getClass());
            consumo.setPetroleoConsumido((double) t[2]);
            BigDecimal dTemp = (BigDecimal) t[3];
            consumo.setDistancia(dTemp.doubleValue());
            consumo.setMes((String)t[4]);
            consumo.setAgno((int)t[5]);
            consumoMensual.add(consumo);
        }

        for (ReporteCamionConsumoMensual linea:
                consumoMensual) {
            mesAct = linea.getMes() + "-" + linea.getAgno();
            if(mesAct != mesAnt){
                k = rows +1;

                Row filaTitulo = sheet.createRow(k);
                Cell celda;
                for(int i = 1; i < 4; i++){
                    celda = filaTitulo.createCell(i);
                    celda.setCellStyle(estiloTitulo);
                }

                sheet.addMergedRegion(new CellRangeAddress(k,k,1,3));
                filaTitulo.getCell(1).setCellValue(mesAct);

                Row filaHeader = sheet.createRow(k+1);
                Cell celdaCodigo = filaHeader.createCell(1);
                celdaCodigo.setCellValue("Código Camión");
                Cell celdaConsumo = filaHeader.createCell(2);
                celdaConsumo.setCellValue("Consumo Petróleo (m^3)");
                Cell celdaDistancia = filaHeader.createCell(3);
                celdaDistancia.setCellValue("Distancia Recorrida (km)");

                celdaCodigo.setCellStyle(estiloHeader);
                celdaConsumo.setCellStyle(estiloHeader);
                celdaDistancia.setCellStyle(estiloHeader);

                rows += 3;
            }
            Row filaContenido = sheet.createRow(rows);
            Cell celdaCodigoVal = filaContenido.createCell(1);
            celdaCodigoVal.setCellValue(linea.getCodigoCamion());
            Cell celdaConsumoVal = filaContenido.createCell(2);
            celdaConsumoVal.setCellValue(String.valueOf(Math.round(linea.getPetroleoConsumido()*100)/100));
            Cell celdaDistanciaVal = filaContenido.createCell(3);
            celdaDistanciaVal.setCellValue(linea.getDistancia());

            celdaCodigoVal.setCellStyle(estiloCodigo);
            celdaConsumoVal.setCellStyle(estiloCelda);
            celdaDistanciaVal.setCellStyle(estiloCelda);

            rows++;
            mesAnt = mesAct;
        }


        workbook.write(stream);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(stream.toByteArray());
        workbook.close();
        return new InputStreamResource(inputStream);
    }


    public InputStreamResource reporteCapacidadAtencion( int tipo) throws Exception{
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        XSSFWorkbook workbook = new XSSFWorkbook();

        XSSFSheet sheet = workbook.createSheet("Reporte Capacidad Atencion ");
        sheet.setColumnWidth(0, 5 * 256);
        sheet.setColumnWidth(1, 30 * 256);
        sheet.setColumnWidth(2, 30 * 256);
        sheet.setColumnWidth(3, 30 * 256);
        sheet.setColumnWidth(5, 30 * 256);
        sheet.setColumnWidth(6, 30 * 256);
        sheet.setColumnWidth(7, 30 * 256);

        Font tituloFont = workbook.createFont();
        tituloFont.setBold(true);
        tituloFont.setFontHeight((short)(tituloFont.getFontHeight() + 30));

        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeight((short)(headerFont.getFontHeight() + 6));

        Font codigoFont = workbook.createFont();
        codigoFont.setBold(true);

        CellStyle estiloTitulo = workbook.createCellStyle();
        estiloTitulo.setAlignment(HorizontalAlignment.CENTER);
        estiloTitulo.setVerticalAlignment(VerticalAlignment.CENTER);
        estiloTitulo.setBorderBottom(BorderStyle.MEDIUM);
        estiloTitulo.setBorderLeft(BorderStyle.MEDIUM);
        estiloTitulo.setBorderRight(BorderStyle.MEDIUM);
        estiloTitulo.setBorderTop(BorderStyle.MEDIUM);

        estiloTitulo.setFont(tituloFont);

        CellStyle estiloHeader = workbook.createCellStyle();
        estiloHeader.setAlignment(HorizontalAlignment.CENTER);
        estiloHeader.setVerticalAlignment(VerticalAlignment.CENTER);
        estiloHeader.setBorderBottom(BorderStyle.MEDIUM);
        estiloHeader.setBorderLeft(BorderStyle.MEDIUM);
        estiloHeader.setBorderRight(BorderStyle.MEDIUM);
        estiloHeader.setBorderTop(BorderStyle.MEDIUM);

        estiloHeader.setFont(headerFont);

        CellStyle estiloCodigo = workbook.createCellStyle();
        estiloCodigo.setVerticalAlignment(VerticalAlignment.CENTER);
        estiloCodigo.setBorderBottom(BorderStyle.MEDIUM);
        estiloCodigo.setBorderLeft(BorderStyle.MEDIUM);
        estiloCodigo.setBorderRight(BorderStyle.MEDIUM);
        estiloCodigo.setBorderTop(BorderStyle.MEDIUM);

        estiloHeader.setFont(codigoFont);

        CellStyle estiloCelda = workbook.createCellStyle();
        estiloCelda.setVerticalAlignment(VerticalAlignment.CENTER);
        estiloCelda.setBorderBottom(BorderStyle.MEDIUM);
        estiloCelda.setBorderLeft(BorderStyle.MEDIUM);
        estiloCelda.setBorderRight(BorderStyle.MEDIUM);
        estiloCelda.setBorderTop(BorderStyle.MEDIUM);

        int rows = 0;

        ArrayList<ReporteCapacidadAtencion> lineas = new ArrayList<>();
        List<Object[]> temp = pedidoRepository.capacidadAtencionReporte(tipo);
        for (Object[] t:
                temp) {
            ReporteCapacidadAtencion capacidad = new ReporteCapacidadAtencion();
            BigDecimal dTemp = (BigDecimal) t[0];
            capacidad.setIndicador(dTemp.doubleValue());
            capacidad.setMes((String)t[1]);
            //System.out.println("Esta clase es : "+t[2].getClass());
            capacidad.setAgno((int) t[2]);
            lineas.add(capacidad);
        }

        //System.out.println("Se esta ejecutando la capacidad de atencion con " + temp.size());
        List<Object[]> tempA = tipoCamionRepository.porcentajeUsoTipo(tipo);
        ArrayList<ReporteUsoTipo> lineaTipo = new ArrayList<>();
        for (Object[] t:
                tempA) {
            ReporteUsoTipo uso = new ReporteUsoTipo();
            uso.setId((int)t[0]);
            uso.setCodigo((String)t[1]);
            Double dTemp = (Double) t[2];
            uso.setPorcentajeGLP(dTemp.doubleValue());
            BigDecimal dTemp1 = (BigDecimal) t[3] ;
            uso.setPorcentajePedido(dTemp1.doubleValue());
            //System.out.println("Esta clase es : "+t[2].getClass());
            lineaTipo.add(uso);
        }

        Row filaHeader = sheet.createRow(1);
        Cell celdaCodigo = filaHeader.createCell(1);
        celdaCodigo.setCellValue("Año");
        Cell celdaConsumo = filaHeader.createCell(2);
        celdaConsumo.setCellValue("Mes");
        Cell celdaDistancia = filaHeader.createCell(3);
        celdaDistancia.setCellValue("Porcentaje Capacidad Atención");

        Cell celdaTipo = filaHeader.createCell(5);
        celdaTipo.setCellValue("Tipo Camión");
        Cell celdaPGLP = filaHeader.createCell(6);
        celdaPGLP.setCellValue("Porcentaje GLP Transportado");
        Cell celdaPPed = filaHeader.createCell(7);
        celdaPPed.setCellValue("Porcentaje Pedidos Atendidos");

        celdaCodigo.setCellStyle(estiloHeader);
        celdaConsumo.setCellStyle(estiloHeader);
        celdaDistancia.setCellStyle(estiloHeader);

        celdaTipo.setCellStyle(estiloHeader);
        celdaPGLP.setCellStyle(estiloHeader);
        celdaPPed.setCellStyle(estiloHeader);

        rows = 2;
        int cont = 0;
        int maxIter = (lineas.size() > lineaTipo.size()) ? lineas.size() : lineaTipo.size();
        System.out.println("Cantidad de iteraciones " + maxIter);
        for (cont =0 ; cont < maxIter; cont++) {

            Row filaContenido = sheet.createRow(rows);

            if(cont < lineas.size()) {
                ReporteCapacidadAtencion linea = lineas.get(cont);
                Cell celdaAgno = filaContenido.createCell(1);
                celdaAgno.setCellValue(linea.getAgno());
                Cell celdaMes = filaContenido.createCell(2);
                celdaMes.setCellValue(linea.getMes());
                Cell celdaIndicador = filaContenido.createCell(3);
                //celdaIndicador.setCellValue(String.valueOf(Math.round(linea.getIndicador() * 100) / 100));
                celdaIndicador.setCellValue(String.valueOf(Math.round(linea.getIndicador() * 10000) / 100));

                celdaAgno.setCellStyle(estiloCelda);
                celdaMes.setCellStyle(estiloCelda);
                celdaIndicador.setCellStyle(estiloCelda);
            }

            if(cont < lineaTipo.size()){
                Cell celdaCodigoAbv = filaContenido.createCell(5);
                celdaCodigoAbv.setCellValue(lineaTipo.get(cont).getCodigo());
                Cell celdaValGlp = filaContenido.createCell(6);
                celdaValGlp.setCellValue(String.valueOf(Math.round(lineaTipo.get(cont).getPorcentajeGLP()*100)/100));
                Cell celdaValPedido = filaContenido.createCell(7);
                celdaValPedido.setCellValue(String.valueOf(Math.round(lineaTipo.get(cont).getPorcentajePedido()*100)/100));

                celdaCodigoAbv.setCellStyle(estiloCelda);
                celdaValGlp.setCellStyle(estiloCelda);
                celdaValPedido.setCellStyle(estiloCelda);
            }

            rows++;
        }


        workbook.write(stream);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(stream.toByteArray());
        workbook.close();
        return new InputStreamResource(inputStream);
    }


    public ReporteCapacidadAtencion capacidadAtencion (int tipo){
        double porcentaje = pedidoRepository.capacidadAtencion(tipo);
        ReporteCapacidadAtencion response = new ReporteCapacidadAtencion();
        response.setIndicador(porcentaje);
        response.setMes("");
        response.setAgno(0);
        return response;
    }
}



