package com.tpintegrador.lafuerza.servicio;

import com.tpintegrador.lafuerza.modelo.Persona;
import com.tpintegrador.lafuerza.repositorio.PersonaJpaController;
import com.tpintegrador.lafuerza.repositorio.exceptions.NonexistentEntityException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PersonaService {

    private static final PersonaJpaController PersonaJPA = new PersonaJpaController();

    public static void crearPersona(Persona persona) {
        try {
            PersonaJPA.create(persona);
        } catch (Exception ex) {
            Logger.getLogger(PersonaService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void eliminarPersonaById(Long idPersona) {
        try {
            PersonaJPA.destroy(idPersona);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(PersonaService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void modificarPersona(Persona persona) {
        try {
            PersonaJPA.edit(persona);
        } catch (Exception ex) {
            Logger.getLogger(PersonaService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static List<Persona> findPersonas() {
        List<Persona> listaPersonas = PersonaJPA.findPersonaEntities();
        return listaPersonas;
    }
    
    public static Persona findPersonaById(Long id) {
        Persona persona = PersonaJPA.findPersona(id);
        return persona;
    }
}
