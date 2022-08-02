package com.tpintegrador.lafuerza.servicio;

import com.tpintegrador.lafuerza.modelo.Rutina;
import com.tpintegrador.lafuerza.repositorio.RutinaJpaController;
import com.tpintegrador.lafuerza.repositorio.exceptions.NonexistentEntityException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RutinaService {

    private static final RutinaJpaController RutinaJPA = new RutinaJpaController();

    public static void crearRutina(Rutina rutina) {
        try {
            RutinaJPA.create(rutina);
        } catch (Exception ex) {
            Logger.getLogger(RutinaService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void eliminarRutinaById(Long idRutina) {
        try {
            RutinaJPA.destroy(idRutina);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(RutinaService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void modificarRutina(Rutina rutina) {
        try {
            RutinaJPA.edit(rutina);
        } catch (Exception ex) {
            Logger.getLogger(RutinaService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static List<Rutina> findRutinas() {
        List<Rutina> listaRutinas = RutinaJPA.findRutinaEntities();
        return listaRutinas;
    }
    
    public static Rutina findRutinaById(Long Id) {
        Rutina rut = RutinaJPA.findRutina(Id);
        return rut;
    }
}
