package com.tpintegrador.lafuerza.servicio;

import com.tpintegrador.lafuerza.modelo.Ejercicio;
import com.tpintegrador.lafuerza.modelo.GrupoMuscular;
import com.tpintegrador.lafuerza.repositorio.GrupoMuscularJpaController;
import com.tpintegrador.lafuerza.repositorio.exceptions.NonexistentEntityException;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GrupoMuscularService {

    private static final GrupoMuscularJpaController GrupoMuscularJPA = new GrupoMuscularJpaController();

    public static void crearGrupoMuscular(GrupoMuscular grupoMuscular) {
        try {
            GrupoMuscularJPA.create(grupoMuscular);
        } catch (Exception ex) {
            Logger.getLogger(GrupoMuscularService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void eliminarGrupoMuscular(Long idGrupoMuscular) {
        try {
            GrupoMuscularJPA.destroy(idGrupoMuscular);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(GrupoMuscularService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void modificarGrupoMuscular(GrupoMuscular grupoMuscular) {
        try {
            GrupoMuscularJPA.edit(grupoMuscular);
        } catch (Exception ex) {
            Logger.getLogger(GrupoMuscularService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static List<GrupoMuscular> findGruposMusculares() {
        List<GrupoMuscular> listaGruposMusculares = GrupoMuscularJPA.findGrupoMuscularEntities();
        return listaGruposMusculares;
    }
    
    public static GrupoMuscular findGrupoMuscularById(Long id){
        return GrupoMuscularJPA.findGrupoMuscular(id);
    }

    public static GrupoMuscular agregarDatosGrupoMuscular(String musculo, String tamanioGrupo, Set<Ejercicio> ejercicios) {
        GrupoMuscular grupoMuscular = new GrupoMuscular(musculo, tamanioGrupo, ejercicios);
        return grupoMuscular;
    }
}
