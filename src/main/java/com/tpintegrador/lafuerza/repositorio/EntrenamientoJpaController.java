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
import com.tpintegrador.lafuerza.modelo.Rutina;
import com.tpintegrador.lafuerza.modelo.Ejercicio;
import com.tpintegrador.lafuerza.modelo.Entrenamiento;
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
public class EntrenamientoJpaController implements Serializable {

    public EntrenamientoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntrenamientoJpaController() {
        emf = Persistence.createEntityManagerFactory("com.tpintegrador_laFuerza_jar_1.0PU");
    }
    
    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Entrenamiento entrenamiento) {
        if (entrenamiento.getEjercicios() == null) {
            entrenamiento.setEjercicios(new HashSet<Ejercicio>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Rutina rutina = entrenamiento.getRutina();
            if (rutina != null) {
                rutina = em.getReference(rutina.getClass(), rutina.getId());
                entrenamiento.setRutina(rutina);
            }
            Set<Ejercicio> attachedEjercicios = new HashSet<Ejercicio>();
            for (Ejercicio ejerciciosEjercicioToAttach : entrenamiento.getEjercicios()) {
                ejerciciosEjercicioToAttach = em.getReference(ejerciciosEjercicioToAttach.getClass(), ejerciciosEjercicioToAttach.getId());
                attachedEjercicios.add(ejerciciosEjercicioToAttach);
            }
            entrenamiento.setEjercicios(attachedEjercicios);
            em.persist(entrenamiento);
            if (rutina != null) {
                rutina.getEntrenamientos().add(entrenamiento);
                rutina = em.merge(rutina);
            }
            for (Ejercicio ejerciciosEjercicio : entrenamiento.getEjercicios()) {
                ejerciciosEjercicio.getEntrenamientos().add(entrenamiento);
                ejerciciosEjercicio = em.merge(ejerciciosEjercicio);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Entrenamiento entrenamiento) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Entrenamiento persistentEntrenamiento = em.find(Entrenamiento.class, entrenamiento.getId());
            Rutina rutinaOld = persistentEntrenamiento.getRutina();
            Rutina rutinaNew = entrenamiento.getRutina();
            Set<Ejercicio> ejerciciosOld = persistentEntrenamiento.getEjercicios();
            Set<Ejercicio> ejerciciosNew = entrenamiento.getEjercicios();
            if (rutinaNew != null) {
                rutinaNew = em.getReference(rutinaNew.getClass(), rutinaNew.getId());
                entrenamiento.setRutina(rutinaNew);
            }
            Set<Ejercicio> attachedEjerciciosNew = new HashSet<Ejercicio>();
            for (Ejercicio ejerciciosNewEjercicioToAttach : ejerciciosNew) {
                ejerciciosNewEjercicioToAttach = em.getReference(ejerciciosNewEjercicioToAttach.getClass(), ejerciciosNewEjercicioToAttach.getId());
                attachedEjerciciosNew.add(ejerciciosNewEjercicioToAttach);
            }
            ejerciciosNew = attachedEjerciciosNew;
            entrenamiento.setEjercicios(ejerciciosNew);
            entrenamiento = em.merge(entrenamiento);
            if (rutinaOld != null && !rutinaOld.equals(rutinaNew)) {
                rutinaOld.getEntrenamientos().remove(entrenamiento);
                rutinaOld = em.merge(rutinaOld);
            }
            if (rutinaNew != null && !rutinaNew.equals(rutinaOld)) {
                rutinaNew.getEntrenamientos().add(entrenamiento);
                rutinaNew = em.merge(rutinaNew);
            }
            for (Ejercicio ejerciciosOldEjercicio : ejerciciosOld) {
                if (!ejerciciosNew.contains(ejerciciosOldEjercicio)) {
                    ejerciciosOldEjercicio.getEntrenamientos().remove(entrenamiento);
                    ejerciciosOldEjercicio = em.merge(ejerciciosOldEjercicio);
                }
            }
            for (Ejercicio ejerciciosNewEjercicio : ejerciciosNew) {
                if (!ejerciciosOld.contains(ejerciciosNewEjercicio)) {
                    ejerciciosNewEjercicio.getEntrenamientos().add(entrenamiento);
                    ejerciciosNewEjercicio = em.merge(ejerciciosNewEjercicio);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = entrenamiento.getId();
                if (findEntrenamiento(id) == null) {
                    throw new NonexistentEntityException("The entrenamiento with id " + id + " no longer exists.");
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
            Entrenamiento entrenamiento;
            try {
                entrenamiento = em.getReference(Entrenamiento.class, id);
                entrenamiento.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The entrenamiento with id " + id + " no longer exists.", enfe);
            }
            Rutina rutina = entrenamiento.getRutina();
            if (rutina != null) {
                rutina.getEntrenamientos().remove(entrenamiento);
                rutina = em.merge(rutina);
            }
            Set<Ejercicio> ejercicios = entrenamiento.getEjercicios();
            for (Ejercicio ejerciciosEjercicio : ejercicios) {
                ejerciciosEjercicio.getEntrenamientos().remove(entrenamiento);
                ejerciciosEjercicio = em.merge(ejerciciosEjercicio);
            }
            em.remove(entrenamiento);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Entrenamiento> findEntrenamientoEntities() {
        return findEntrenamientoEntities(true, -1, -1);
    }

    public List<Entrenamiento> findEntrenamientoEntities(int maxResults, int firstResult) {
        return findEntrenamientoEntities(false, maxResults, firstResult);
    }

    private List<Entrenamiento> findEntrenamientoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Entrenamiento.class));
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

    public Entrenamiento findEntrenamiento(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Entrenamiento.class, id);
        } finally {
            em.close();
        }
    }

    public int getEntrenamientoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Entrenamiento> rt = cq.from(Entrenamiento.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
