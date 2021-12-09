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
import java.util.HashMap;
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

    @RequestMapping(value = "/generarPedidosColapsoMetodoGLP", produces="application/zip", method= RequestMethod.GET)
    public String generarPedidosColapsoMetodoGLP(HttpServletRequest request){//
        FechaFront fecha = new FechaFront();
        LocalDateTime l =LocalDateTime.of(2021, Month.NOVEMBER, 16, 00, 00, 01);
        fecha.setF(l);
        //funcion exponencial para registrar pedidos
        int limit =1200;//40 meses aprox

        //Para controlar los pedidos
        int a=5;
        double b=240;
        int x;
        double n=1.223;
        //y es volumen total de GLP en m3 (max_GLP_dia) //y = a*x^n  + b
                                            // y = 5*x^1.223 +240

        //controlar plazo de entrega
//        10% en 4 horas
//        40% entre 5 y 12 horas
//        40% entre 12 y 18 horas
//        10% entre 18 y 36 horas.

//        cantidad de GLP por camión cisterna se ha usado el siguiente esquema:
//        T+ = 1         = 4.8% (1/21)        genera de 25m3 a más
//                TA = 2         = 9.5% (2/21)        genera de 15 a menos de 25m3
//                TB= 4         =19.05% (4/21)      genera de 10 a menos de 15m3
//                TC = 4        = 19.05 (4/21)        genera de 5 a menos de 10m3
//                TD = 10      = 47.6% (10/21)     genera de 1 a menos de 5m3

//        La determinación de la cantidad de GLP por pedido está influenciada por el tipo de camión (TA,..,TD, T+). Pero se usó una función aleatoria.


        double cantGLP;
        int plazoEntrega;
        LocalDateTime fechaPedido=fecha.getF();
        int y;
        int segundos;
        int mes=fecha.getF().getMonthValue();
        int anio=fecha.getF().getYear();
        ArrayList<String> nombresArchivos = new ArrayList<>();
        String path = "ventas"+Integer.toString(anio)+String.format("%02d",mes)+".txt";
        nombresArchivos.add(path);
        File arch = new File(path);
        try{
            ArrayList<Pedido> pedidos = new ArrayList<>();
            int cantPedidos = 0;
            for (int i=1;i<limit;i++){  //por dia
                FileWriter writer = new FileWriter(path, true);
                double formula = b+a*Math.pow(i,n); //total de GLP a enviar en el dia i
                //System.out.print(Math.ceil(formula)+" aqui\n");


                double cantidadActualGLP = 0;

                while(cantidadActualGLP<formula){
                    Pedido p = new Pedido();
                    double rand =  new Random().nextDouble();
                    if (rand < 0.048){ //si es T+ (mayor a 25m3)
                        cantGLP = new Random().nextInt() + 25;
                    }else if (rand < 0.143 && rand>=0.048){ //si TA (mayor a 15 menor a 25)
                        cantGLP = new Random().nextInt(10)+15;
                    }else if (rand <0.3335 && rand>= 0.143){ //si TB (mayor a 10 menor a 15)
                        cantGLP = new Random().nextInt(5)+10;
                    }else if (rand < 0.524 && rand>= 0.3335){//si TC (mayor a 5 menor a 10)
                        cantGLP = new Random().nextInt(5)+5;
                    }else{ // si TD (mayor a 1 menor a 5)
                        cantGLP = new Random().nextInt(5)+1;
                    }
                    cantGLP = new Random().nextInt(30);

                    cantidadActualGLP +=cantGLP;
                    x = new Random().nextInt(70);
                    y = new Random().nextInt(50);
                    Nodo nodo = new Nodo(x, y);
                    //        10% en 4 horas
//        40% entre 5 y 12 horas
//        40% entre 12 y 18 horas
//        10% entre 18 y 36 horas.
                    double randPlazo = new Random().nextDouble();
                    if (rand < 0.1){ //si es T+ (mayor a 25m3)
                        plazoEntrega = 4;
                    }else if (rand < 0.5 && rand>=0.1){ //si TA (mayor a 15 menor a 25)
                        plazoEntrega = new Random().nextInt(8)+5;
                    }else if (rand <0.9&& rand>= 0.5){ //si TB (mayor a 10 menor a 15)
                        plazoEntrega = new Random().nextInt(7)+12;
                    }else{ // si TD (mayor a 1 menor a 5)
                        plazoEntrega = new Random().nextInt(19)+18;
                    }

                    p.setCantidadGLP(cantGLP);
                    p.setNodo(nodo);
                    p.setPlazoEntrega(plazoEntrega);

                    pedidos.add(p);
                    cantPedidos +=1;
                }

                segundos = (int)Math.floor((24*60*60)/(cantPedidos+1));
                for (Pedido p: pedidos) {
                    fechaPedido = fechaPedido.plusSeconds(segundos);
                    p.setFechaPedido(fechaPedido);

                    DateTimeFormatter fo =  DateTimeFormatter.ofPattern("dd:HH:mm");

                    writer.write(fo.format(fechaPedido));
                    writer.write(",");
                    writer.write(Integer.toString(p.getNodo().getCoordenadaX()));
                    writer.write(",");
                    writer.write(Integer.toString(p.getNodo().getCoordenadaY()));
                    writer.write(",");
                    writer.write(Integer.toString((int)p.getCantidadGLP()));
                    writer.write(",");
                    writer.write(Integer.toString(p.getPlazoEntrega()));
                    writer.write("\n");
                }

//                for(int w=0;w<formula;w++){
//                    Pedido p = new Pedido();
//                    cantGLP = new Random().nextInt(30);
//                    x = new Random().nextInt(70);
//                    y = new Random().nextInt(50);
//                    Nodo nodo = new Nodo(x, y);
//                    plazoEntrega = 4 + new Random().nextInt(37);
//
//                    fechaPedido = fechaPedido.plusSeconds(segundos);
////                    System.out.println(segundos*w);
////                    System.out.println(fechaPedido);
//
//                    p.setCantidadGLP(cantGLP);
//                    p.setNodo(nodo);
//                    p.setPlazoEntrega(plazoEntrega);
//                    p.setFechaPedido(fechaPedido);
//
//                    DateTimeFormatter fo =  DateTimeFormatter.ofPattern("dd:HH:mm");
//
//                    writer.write(fo.format(fechaPedido));
//                    writer.write(",");
//                    writer.write(Integer.toString(x));
//                    writer.write(",");
//                    writer.write(Integer.toString(y));
//                    writer.write(",");
//                    writer.write(Integer.toString((int)cantGLP));
//                    writer.write(",");
//                    writer.write(Integer.toString(plazoEntrega));
//                    writer.write("\n");
//                }
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
//            byte[] data = Files.readAllBytes(Path.of("Archivos.zip"));
//            ByteArrayResource resource = new ByteArrayResource(data);
//
            try{
                for (String s: nombresArchivos){
                    Files.deleteIfExists(Path.of(s));
                }

            }catch (IOException ex){
                ex.printStackTrace();
                System.out.println("Error en el borrado de archivos");
            }
            String msg= "Se ha generado pedidos para " + limit+ " días.";
            return msg;
//
//            return ResponseEntity.ok()
//                    // Content-Disposition
//                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" +   "Archivos.zip" + "\"")
//                    // Content-Type
//                    .contentType(MediaType.parseMediaType("application/zip")) //
//                    // Content-Lengh
//                    .contentLength(data.length) //
//                    .body(resource);

        }catch (IOException ex){
            System.out.println("Error en el archivo de "+path);
        }
        String msg= "Error en el proceso de generación de pedidos";
        return msg;
    }

    @RequestMapping(value = "/generarPedidosColapso", method= RequestMethod.GET)
    public String generarPedidosColapso(HttpServletRequest request){//
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
//            byte[] data = Files.readAllBytes(Path.of("Archivos.zip"));
//            ByteArrayResource resource = new ByteArrayResource(data);
//
            try{
                for (String s: nombresArchivos){
                    Files.deleteIfExists(Path.of(s));
                }

            }catch (IOException ex){
                ex.printStackTrace();
                System.out.println("Error en el borrado de archivos");
            }
            String msg= "Se ha generado pedidos para " + limit+ " días.";
            return msg;
//
//            return ResponseEntity.ok()
//                    // Content-Disposition
//                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" +   "Archivos.zip" + "\"")
//                    // Content-Type
//                    .contentType(MediaType.parseMediaType("application/zip")) //
//                    // Content-Lengh
//                    .contentLength(data.length) //
//                    .body(resource);

        }catch (IOException ex){
            System.out.println("Error en el archivo de "+path);
        }
        String msg= "Error en el proceso de generación de pedidos";
        return msg;
    }

    @RequestMapping(value = "/descargarPedidosColapso", produces="application/zip",  method= RequestMethod.GET)
    public ResponseEntity<ByteArrayResource> descargarPedidosColapso(HttpServletRequest request){
        try{
            byte[] data = Files.readAllBytes(Path.of("Archivos.zip"));
            ByteArrayResource resource = new ByteArrayResource(data);
            return ResponseEntity.ok()
                    // Content-Disposition
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" +   "Archivos.zip" + "\"")
                    // Content-Type
                    .contentType(MediaType.parseMediaType("application/zip")) //
                    // Content-Lengh
                    .contentLength(data.length) //
                    .body(resource);
        }catch (IOException e){
            e.printStackTrace();
            System.out.println("Error en la descarga de archivos");
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

    public ArrayList<Pedido> guardarListaPedidos(ArrayList<Pedido> lista){
        HashMap<String,Nodo> listaNodos = new HashMap<>();
        ArrayList<Nodo> nodosAct = nodoRepository.listarNodos();

        for (Nodo n:
                nodosAct) {
            listaNodos.put(n.getCoor(),n);
        }
        ArrayList<Pedido> listaNueva = new ArrayList<>();
        try{
            for (Pedido p : lista ) {
                p.setEstadoPedido("Nuevo");
                Nodo nodo = nodoRepository.findIdNodoByCoordenadaXAndCoordenadaYAndActivoTrue(p.getNodo().getCoordenadaX(), p.getNodo().getCoordenadaY());
                p.getNodo().setId(nodo.getId());
                p.setTipo(1); // 1 es simulacion dia a dia
                listaNueva.add(p);
            }
            pedidoRepository.saveAll(listaNueva);
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
        return null;
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