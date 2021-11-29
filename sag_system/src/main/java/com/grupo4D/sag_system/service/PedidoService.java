package com.grupo4D.sag_system.service;


import com.grupo4D.sag_system.model.Fecha;
import com.grupo4D.sag_system.model.Nodo;
import com.grupo4D.sag_system.model.Pedido;
import com.grupo4D.sag_system.model.RutaXPedido;
import com.grupo4D.sag_system.model.request.FechaFront;
import com.grupo4D.sag_system.model.statics.StaticValues;
import com.grupo4D.sag_system.repository.PedidoRepository;
import com.grupo4D.sag_system.repository.NodoRepository;
import com.grupo4D.sag_system.repository.RutaXPedidoRepository;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Month;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;

@Service
public class PedidoService {
    @Autowired
    PedidoRepository pedidoRepository;
    @Autowired
    NodoRepository nodoRepository;
    @Autowired
    RutaXPedidoRepository rutaXPedidoRepository;

    @RequestMapping(value = "/generarPedidosColapso", produces="application/zip", method= RequestMethod.GET)
    public ResponseEntity<ByteArrayResource> generarPedidosColapso(HttpServletRequest request){//
        FechaFront fecha = new FechaFront();
        LocalDateTime l =LocalDateTime.of(2021, Month.NOVEMBER, 16, 00, 00, 01);
        fecha.setF(l);
        //funcion exponencial para registrar pedidos
        int limit =1200;//40 meses aprox
        double base = 5;
        double k = 5;
        int a=10;
        double b=1.58;
        int x;
        double n=1.2;
        //double f = k*Math.pow(base,exp); //y = a+ b*x^n
        double cantGLP;
        int plazoEntrega;
        LocalDateTime fechaPedido=fecha.getF();
        int y;
        int segundos;
        int mes=fecha.getF().getMonthValue();
        int anio=fecha.getF().getYear();
        int mesAnt =fecha.getF().getMonthValue();
        ArrayList<String> nombresArchivos = new ArrayList<>();
        String path = "ventas"+Integer.toString(anio)+String.format("%02d",mes)+".txt";
        nombresArchivos.add(path);
        File arch = new File(path);
        try{
            for (int i=1;i<limit;i++){
                FileWriter writer = new FileWriter(path, true);
                double formula = a+b*Math.pow(i,n);
                //System.out.print(Math.ceil(formula)+" aqui\n");
                segundos = (int)Math.floor((24*60*60)/(formula+1));

                for(int w=0;w<formula;w++){
                    Pedido p = new Pedido();
                    cantGLP = new Random().nextInt(30);
                    x = new Random().nextInt(70);
                    y = new Random().nextInt(50);
                    Nodo nodo = new Nodo(x, y);
                    plazoEntrega = 4 + new Random().nextInt(37);

                    fechaPedido = fechaPedido.plusSeconds(segundos);
//                    System.out.println(segundos*w);
//                    System.out.println(fechaPedido);

                    p.setCantidadGLP(cantGLP);
                    p.setNodo(nodo);
                    p.setPlazoEntrega(plazoEntrega);
                    p.setFechaPedido(fechaPedido);

                    DateTimeFormatter fo =  DateTimeFormatter.ofPattern("dd:HH:mm");

                    writer.write(fo.format(fechaPedido));
                    writer.write(",");
                    writer.write(Integer.toString(x));
                    writer.write(",");
                    writer.write(Integer.toString(y));
                    writer.write(",");
                    writer.write(Integer.toString((int)cantGLP));
                    writer.write(",");
                    writer.write(Integer.toString(plazoEntrega));
                    writer.write("\n");
                }
                fechaPedido = fechaPedido.plusDays(1).withHour(0).withMinute(0).withSecond(1);
                if (mes < fechaPedido.getMonthValue()){
                    mes = mes+1;
                    if (anio < fechaPedido.getYear()){
                        anio = anio+1;
                    }
                    writer.close();

                    path = "ventas"+Integer.toString(anio)+String.format("%02d",mes)+".txt";
                    nombresArchivos.add(path);
                }else if (anio < fechaPedido.getYear()){
                    anio = anio+1;
                    mes = 1;
                    writer.close();
                    path = "ventas"+Integer.toString(anio)+String.format("%02d",mes)+".txt";
                    nombresArchivos.add(path);
                }
                writer.close();
            }

            try
            {
                //Source files

                //Zipped file
                String zipFilename = "Archivos.zip";
                File zipFile = new File(zipFilename);
                FileOutputStream fos  = new FileOutputStream(zipFile);
                ZipOutputStream zos = new ZipOutputStream(fos);
                for(String s:nombresArchivos) {
                    zipFile(s,zos);
                }


                zos.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
                System.out.println("Error en compresión de archivos");
            }
            byte[] data = Files.readAllBytes(Path.of("Archivos.zip"));
            ByteArrayResource resource = new ByteArrayResource(data);

            try{
                for (String s: nombresArchivos){
                    Files.deleteIfExists(Path.of(s));
                }

            }catch (IOException ex){
                ex.printStackTrace();
                System.out.println("Error en el borrado de archivos");
            }

            return ResponseEntity.ok()
                    // Content-Disposition
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + "Archivos.zip")
                    // Content-Type
                    .contentType(MediaType.parseMediaType("application/zip")) //
                    // Content-Lengh
                    .contentLength(data.length) //
                    .body(resource);

        }catch (IOException ex){
            System.out.println("Error en el archivo de "+path);
        }
        return null;
    }



    private static void zipFile(String fileName, ZipOutputStream zos) throws IOException
    {
        final int BUFFER = 1024;
        BufferedInputStream bis = null;
        try
        {
            File file = new File(fileName);
            FileInputStream fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis, BUFFER);

            // ZipEntry --- Here file name can be created using the source file
            ZipEntry zipEntry = new ZipEntry(file.getName());
            zos.putNextEntry(zipEntry);
            byte data[] = new byte[BUFFER];
            int count;
            while((count = bis.read(data, 0, BUFFER)) != -1)
            {
                zos.write(data, 0, count);
            }
            // close entry every time
            zos.closeEntry();
        }
        finally
        {
            try
            {
                bis.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }


public Pedido guardarPedido(Pedido pedido){
        return pedidoRepository.save(pedido);
    }

    public Pedido guardarPedidoNuevo(Pedido pedido){
        pedido.setEstadoPedido("Nuevo");
        Nodo nodo = nodoRepository.findIdNodoByCoordenadaXAndCoordenadaYAndActivoTrue(pedido.getNodo().getCoordenadaX(), pedido.getNodo().getCoordenadaY());
        pedido.getNodo().setId(nodo.getId());
        pedido.setTipo(1); // 1 es simulacion dia a dia
        return pedidoRepository.save(pedido);
    }

    public ArrayList<Pedido> listarPedidos() {
        return (ArrayList<Pedido>) pedidoRepository.listarPedidos();
    }

    public ArrayList<Pedido> listarPedidosSinAsignar() {
        String estado = "Nuevo";
        return (ArrayList<Pedido>) pedidoRepository.findPedidosByEstadoPedido(estado);
    }

    public ArrayList<Pedido> listarPedidosTemp(){
        return pedidoRepository.listarPedidosDisponibles(LocalDateTime.now(StaticValues.zoneId), 1);
    }

    public ArrayList<Pedido> listarPedidosDisponibles(LocalDateTime date, int tipo){
        ArrayList<Pedido> pedidos = pedidoRepository.listarPedidosDisponibles(date, tipo);
        ArrayList<RutaXPedido> rutaXPedidos ;
        int i;
        double glp;
        for(i = 0; i<pedidos.size(); i++){
            Pedido p = pedidos.get(i);
            glp = 0;
            rutaXPedidos = rutaXPedidoRepository.listarRutaXPedido(p.getId());
            for (RutaXPedido rp:
                 rutaXPedidos) {
                glp = glp + rp.getCantidadGLPEnviado();
            }
            p.setGlpProgramado(p.getGlpProgramado() - glp);
        }
        return pedidos;
    }

}