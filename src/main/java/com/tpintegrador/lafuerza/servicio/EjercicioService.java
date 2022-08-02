package com.tpintegrador.lafuerza.servicio;

import com.tpintegrador.lafuerza.modelo.Ejercicio;
import com.tpintegrador.lafuerza.modelo.Entrenamiento;
import com.tpintegrador.lafuerza.modelo.GrupoMuscular;
import com.tpintegrador.lafuerza.repositorio.EjercicioJpaController;
import com.tpintegrador.lafuerza.repositorio.exceptions.NonexistentEntityException;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EjercicioService {

    private static final EjercicioJpaController EjercicioJPA = new EjercicioJpaController();

    public static void crearEjercicio(Ejercicio ejercicio) {
        try {
            EjercicioJPA.create(ejercicio);
        } catch (Exception ex) {
            Logger.getLogger(EjercicioService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void eliminarEjercicioById(Long idEjercicio) {
        try {
            EjercicioJPA.destroy(idEjercicio);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(EjercicioService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void modificarEjercicio(Ejercicio ejercicio) {
        try {
            EjercicioJPA.edit(ejercicio);
        } catch (Exception ex) {
            Logger.getLogger(EjercicioService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static List<Ejercicio> findEjercicios() {
        List<Ejercicio> listaEjercicios = EjercicioJPA.findEjercicioEntities();
        return listaEjercicios;
    }
    
    public static Ejercicio findEjercicioById(Long id) {
        Ejercicio ejercicio = EjercicioJPA.findEjercicio(id);
        return ejercicio;
    }

    public static Ejercicio agregarDatosEjercicio(String nombre, String detalle, GrupoMuscular musculo, Set<Entrenamiento> entrenamientos) {
        Ejercicio ejercicio = new Ejercicio(nombre, detalle, musculo, entrenamientos);
        return ejercicio;
    }
}
