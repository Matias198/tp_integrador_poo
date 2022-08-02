package com.tpintegrador.lafuerza.servicio;

import com.tpintegrador.lafuerza.modelo.Entrenamiento;
import com.tpintegrador.lafuerza.repositorio.EntrenamientoJpaController;
import com.tpintegrador.lafuerza.repositorio.exceptions.NonexistentEntityException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EntrenamientoService {
    
    private static final EntrenamientoJpaController EntrenamientoJPA = new EntrenamientoJpaController();

    public static void crearEntrenamiento(Entrenamiento entrenamiento) {
        try {
            EntrenamientoJPA.create(entrenamiento);
        } catch (Exception ex) {
            Logger.getLogger(EntrenamientoService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void eliminarEntrenamientoById(Long idEntrenamiento) {
        try {
            EntrenamientoJPA.destroy(idEntrenamiento);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(EntrenamientoService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void modificarEntrenamiento(Entrenamiento entrenamiento) {
        try {
            EntrenamientoJPA.edit(entrenamiento);
        } catch (Exception ex) {
            Logger.getLogger(EntrenamientoService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static Entrenamiento findEntrenamientoById(Long id){
        return EntrenamientoJPA.findEntrenamiento(id);
    }

    public static List<Entrenamiento> findEntrenamientos() {
        List<Entrenamiento> listaEntrenamientos = EntrenamientoJPA.findEntrenamientoEntities();
        return listaEntrenamientos;
    }
}
