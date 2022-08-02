package com.tpintegrador.lafuerza.servicio;

import com.tpintegrador.lafuerza.modelo.Tutor;
import com.tpintegrador.lafuerza.repositorio.TutorJpaController;
import com.tpintegrador.lafuerza.repositorio.exceptions.NonexistentEntityException;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TutorService {

    private static final TutorJpaController TutorJPA = new TutorJpaController();

    public static void crearTutor(Tutor tutor) {
        try {
            TutorJPA.create(tutor);
        } catch (Exception ex) {
            Logger.getLogger(TutorService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void eliminarTutorById(Long idTutor) {
        try {
            TutorJPA.destroy(idTutor);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(TutorService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void modificarTutor(Tutor tutor) {
        try {
            TutorJPA.edit(tutor);
        } catch (Exception ex) {
            Logger.getLogger(TutorService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static List<Tutor> findTutores() {
        List<Tutor> listaTutores = TutorJPA.findTutorEntities();
        return listaTutores;
    }
    
    public static Tutor findTutorById(Long id) {
        return TutorJPA.findTutor(id);
    }
}
