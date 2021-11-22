package com.grupo4D.sag_system.controller;

import com.grupo4D.sag_system.model.Camion;
import com.grupo4D.sag_system.model.Fecha;
import com.grupo4D.sag_system.model.TipoCamion;
import com.grupo4D.sag_system.model.request.CamionRegistrarFront;
import com.grupo4D.sag_system.model.response.CamionListarFront;
import com.grupo4D.sag_system.model.response.TipoSimulacionFront;
import com.grupo4D.sag_system.repository.CamionRepository;
import com.grupo4D.sag_system.service.CamionService;
import com.grupo4D.sag_system.service.TipoCamionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/camion")
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.PUT,RequestMethod.POST})
public class CamionController {
    @Autowired
    private CamionService camionService;

    @Autowired
    private  TipoCamionService tipoCamionService;

    @PostMapping("/guardarCamion")
    public Camion guardarCamion(@RequestBody Camion camionModel){
        return camionService.guardarCamion(camionModel);
    }

    @PostMapping("/registrarCamionNuevo")
    public Camion registrarCamionNuevo(@RequestBody CamionRegistrarFront camion){
        Camion camionModel = new Camion();
        TipoCamion tipoCamion = new TipoCamion();
        tipoCamion.setId(camion.getTipoCamion());


        camionModel.setTipoCamion(tipoCamion);
        camionModel.setCodigo(String.format("%02d",camion.getCodigoCamion()));
        camionModel.setEstadoSimulacion("Operativo");
        camionModel.setEstadoColapso("Operativo");
        camionModel.setEstado("Operativo");
        camionModel.setVelocidad(camion.getVelocidadCamion());
        camionModel.setKilometraje(camion.getKilometraje());

        return camionService.guardarCamionNuevo(camionModel);
    }

    @GetMapping("/listarCamiones")
    public List<CamionListarFront> listarCamiones(){
        ArrayList<CamionListarFront> camiones = new ArrayList<>();
        ArrayList<Camion> lista = camionService.listarCamiones();
        //TipoCamionService tipoCamionService = new TipoCamionService();

        for (Camion c:lista) {
            CamionListarFront camion = new CamionListarFront();
            camion.setId(c.getId());
            TipoCamion tipoCamion = new TipoCamion();
            tipoCamion = tipoCamionService.obtenerDatosTipoCamion(c.getId());
            camion.setTaraCamion(tipoCamion.getPesoTara());
            camion.setEstadoCamion(c.getEstado());

            camion.setTipoCamion(camionService.buscarCodigo1Camion(c.getId()));
            camion.setCapacidadGLP(tipoCamion.getCapacidadGLP());
            camion.setCapacidadPetroleo(tipoCamion.getCapacidadPetroleo());

            camiones.add(camion);
        }
        return camiones;

    }

    @PostMapping("/listarCamionesAveriados")
    public ArrayList<CamionListarFront> listarCamionesAveriados(@RequestBody TipoSimulacionFront t){
        ArrayList<CamionListarFront> camiones = new ArrayList<>();
        ArrayList<Camion> lista = camionService.listarCamionesAveriados(t.getTipo());

        for (Camion c:lista) {
            CamionListarFront camion = new CamionListarFront();
            camion.setId(c.getId());
            TipoCamion tipoCamion = new TipoCamion();
            tipoCamion = tipoCamionService.obtenerDatosTipoCamion(c.getId());
            camion.setTaraCamion(tipoCamion.getPesoTara());
            switch (t.getTipo()){
                case 1:{
                    camion.setEstadoCamion(c.getEstado());
                    break;
                }
                case 2:{
                    camion.setEstadoCamion(c.getEstadoSimulacion());
                    break;
                }
                case 3:{
                    camion.setEstadoCamion(c.getEstadoColapso());
                    break;
                }
            }

            camion.setTipoCamion(camionService.buscarCodigo1Camion(c.getId()));
            camion.setCapacidadGLP(tipoCamion.getCapacidadGLP());
            camion.setCapacidadPetroleo(tipoCamion.getCapacidadPetroleo());

            camiones.add(camion);
        }
        return camiones;
    }
}

