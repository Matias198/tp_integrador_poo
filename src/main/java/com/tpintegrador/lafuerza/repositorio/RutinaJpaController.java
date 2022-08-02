/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tpintegrador.lafuerza.repositorio;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.tpintegrador.lafuerza.modelo.Cliente;
import com.tpintegrador.lafuerza.modelo.Tutor;
import com.tpintegrador.lafuerza.modelo.Entrenamiento;
import com.tpintegrador.lafuerza.modelo.Rutina;
import com.tpintegrador.lafuerza.repositorio.exceptions.NonexistentEntityException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author matia
 */
public class RutinaJpaController implements Serializable {

    public RutinaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public RutinaJpaController() {
        emf = Persistence.createEntityManagerFactory("com.tpintegrador_laFuerza_jar_1.0PU");
    }
    
    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Rutina rutina) {
        if (rutina.getEntrenamientos() == null) {
            rutina.setEntrenamientos(new HashSet<Entrenamiento>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cliente cliente = rutina.getCliente();
            if (cliente != null) {
                cliente = em.getReference(cliente.getClass(), cliente.getDni());
                rutina.setCliente(cliente);
            }
            Tutor tutor = rutina.getTutor();
            if (tutor != null) {
                tutor = em.getReference(tutor.getClass(), tutor.getDni());
                rutina.setTutor(tutor);
            }
            Set<Entrenamiento> attachedEntrenamientos = new HashSet<Entrenamiento>();
            for (Entrenamiento entrenamientosEntrenamientoToAttach : rutina.getEntrenamientos()) {
                entrenamientosEntrenamientoToAttach = em.getReference(entrenamientosEntrenamientoToAttach.getClass(), entrenamientosEntrenamientoToAttach.getId());
                attachedEntrenamientos.add(entrenamientosEntrenamientoToAttach);
            }
            rutina.setEntrenamientos(attachedEntrenamientos);
            em.persist(rutina);
            if (cliente != null) {
                cliente.getRutinas().add(rutina);
                cliente = em.merge(cliente);
            }
            if (tutor != null) {
                tutor.getRutinas().add(rutina);
                tutor = em.merge(tutor);
            }
            for (Entrenamiento entrenamientosEntrenamiento : rutina.getEntrenamientos()) {
                Rutina oldRutinaOfEntrenamientosEntrenamiento = entrenamientosEntrenamiento.getRutina();
                entrenamientosEntrenamiento.setRutina(rutina);
                entrenamientosEntrenamiento = em.merge(entrenamientosEntrenamiento);
                if (oldRutinaOfEntrenamientosEntrenamiento != null) {
                    oldRutinaOfEntrenamientosEntrenamiento.getEntrenamientos().remove(entrenamientosEntrenamiento);
                    oldRutinaOfEntrenamientosEntrenamiento = em.merge(oldRutinaOfEntrenamientosEntrenamiento);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Rutina rutina) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Rutina persistentRutina = em.find(Rutina.class, rutina.getId());
            Cliente clienteOld = persistentRutina.getCliente();
            Cliente clienteNew = rutina.getCliente();
            Tutor tutorOld = persistentRutina.getTutor();
            Tutor tutorNew = rutina.getTutor();
            Set<Entrenamiento> entrenamientosOld = persistentRutina.getEntrenamientos();
            Set<Entrenamiento> entrenamientosNew = rutina.getEntrenamientos();
            if (clienteNew != null) {
                clienteNew = em.getReference(clienteNew.getClass(), clienteNew.getDni());
                rutina.setCliente(clienteNew);
            }
            if (tutorNew != null) {
                tutorNew = em.getReference(tutorNew.getClass(), tutorNew.getDni());
                rutina.setTutor(tutorNew);
            }
            Set<Entrenamiento> attachedEntrenamientosNew = new HashSet<Entrenamiento>();
            for (Entrenamiento entrenamientosNewEntrenamientoToAttach : entrenamientosNew) {
                entrenamientosNewEntrenamientoToAttach = em.getReference(entrenamientosNewEntrenamientoToAttach.getClass(), entrenamientosNewEntrenamientoToAttach.getId());
                attachedEntrenamientosNew.add(entrenamientosNewEntrenamientoToAttach);
            }
            entrenamientosNew = attachedEntrenamientosNew;
            rutina.setEntrenamientos(entrenamientosNew);
            rutina = em.merge(rutina);
            if (clienteOld != null && !clienteOld.equals(clienteNew)) {
                clienteOld.getRutinas().remove(rutina);
                clienteOld = em.merge(clienteOld);
            }
            if (clienteNew != null && !clienteNew.equals(clienteOld)) {
                clienteNew.getRutinas().add(rutina);
                clienteNew = em.merge(clienteNew);
            }
            if (tutorOld != null && !tutorOld.equals(tutorNew)) {
                tutorOld.getRutinas().remove(rutina);
                tutorOld = em.merge(tutorOld);
            }
            if (tutorNew != null && !tutorNew.equals(tutorOld)) {
                tutorNew.getRutinas().add(rutina);
                tutorNew = em.merge(tutorNew);
            }
            for (Entrenamiento entrenamientosOldEntrenamiento : entrenamientosOld) {
                if (!entrenamientosNew.contains(entrenamientosOldEntrenamiento)) {
                    entrenamientosOldEntrenamiento.setRutina(null);
                    entrenamientosOldEntrenamiento = em.merge(entrenamientosOldEntrenamiento);
                }
            }
            for (Entrenamiento entrenamientosNewEntrenamiento : entrenamientosNew) {
                if (!entrenamientosOld.contains(entrenamientosNewEntrenamiento)) {
                    Rutina oldRutinaOfEntrenamientosNewEntrenamiento = entrenamientosNewEntrenamiento.getRutina();
                    entrenamientosNewEntrenamiento.setRutina(rutina);
                    entrenamientosNewEntrenamiento = em.merge(entrenamientosNewEntrenamiento);
                    if (oldRutinaOfEntrenamientosNewEntrenamiento != null && !oldRutinaOfEntrenamientosNewEntrenamiento.equals(rutina)) {
                        oldRutinaOfEntrenamientosNewEntrenamiento.getEntrenamientos().remove(entrenamientosNewEntrenamiento);
                        oldRutinaOfEntrenamientosNewEntrenamiento = em.merge(oldRutinaOfEntrenamientosNewEntrenamiento);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = rutina.getId();
                if (findRutina(id) == null) {
                    throw new NonexistentEntityException("The rutina with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Rutina rutina;
            try {
                rutina = em.getReference(Rutina.class, id);
                rutina.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The rutina with id " + id + " no longer exists.", enfe);
            }
            Cliente cliente = rutina.getCliente();
            if (cliente != null) {
                cliente.getRutinas().remove(rutina);
                cliente = em.merge(cliente);
            }
            Tutor tutor = rutina.getTutor();
            if (tutor != null) {
                tutor.getRutinas().remove(rutina);
                tutor = em.merge(tutor);
            }
            Set<Entrenamiento> entrenamientos = rutina.getEntrenamientos();
            for (Entrenamiento entrenamientosEntrenamiento : entrenamientos) {
                entrenamientosEntrenamiento.setRutina(null);
                entrenamientosEntrenamiento = em.merge(entrenamientosEntrenamiento);
            }
            em.remove(rutina);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Rutina> findRutinaEntities() {
        return findRutinaEntities(true, -1, -1);
    }

    public List<Rutina> findRutinaEntities(int maxResults, int firstResult) {
        return findRutinaEntities(false, maxResults, firstResult);
    }

    private List<Rutina> findRutinaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Rutina.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Rutina findRutina(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Rutina.class, id);
        } finally {
            em.close();
        }
    }

    public int getRutinaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Rutina> rt = cq.from(Rutina.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
