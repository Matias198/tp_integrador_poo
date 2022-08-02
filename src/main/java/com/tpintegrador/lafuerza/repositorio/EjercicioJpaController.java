/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tpintegrador.lafuerza.repositorio;

import com.tpintegrador.lafuerza.modelo.Ejercicio;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.tpintegrador.lafuerza.modelo.GrupoMuscular;
import com.tpintegrador.lafuerza.repositorio.exceptions.NonexistentEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author matia
 */
public class EjercicioJpaController implements Serializable {

    public EjercicioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EjercicioJpaController() {
        emf = Persistence.createEntityManagerFactory("com.tpintegrador_laFuerza_jar_1.0PU");
    }
    
    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Ejercicio ejercicio) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            GrupoMuscular musculo = ejercicio.getMusculo();
            if (musculo != null) {
                musculo = em.getReference(musculo.getClass(), musculo.getId());
                ejercicio.setMusculo(musculo);
            }
            em.persist(ejercicio);
            if (musculo != null) {
                musculo.getEjercicios().add(ejercicio);
                musculo = em.merge(musculo);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Ejercicio ejercicio) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Ejercicio persistentEjercicio = em.find(Ejercicio.class, ejercicio.getId());
            GrupoMuscular musculoOld = persistentEjercicio.getMusculo();
            GrupoMuscular musculoNew = ejercicio.getMusculo();
            if (musculoNew != null) {
                musculoNew = em.getReference(musculoNew.getClass(), musculoNew.getId());
                ejercicio.setMusculo(musculoNew);
            }
            ejercicio = em.merge(ejercicio);
            if (musculoOld != null && !musculoOld.equals(musculoNew)) {
                musculoOld.getEjercicios().remove(ejercicio);
                musculoOld = em.merge(musculoOld);
            }
            if (musculoNew != null && !musculoNew.equals(musculoOld)) {
                musculoNew.getEjercicios().add(ejercicio);
                musculoNew = em.merge(musculoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = ejercicio.getId();
                if (findEjercicio(id) == null) {
                    throw new NonexistentEntityException("The ejercicio with id " + id + " no longer exists.");
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
            Ejercicio ejercicio;
            try {
                ejercicio = em.getReference(Ejercicio.class, id);
                ejercicio.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ejercicio with id " + id + " no longer exists.", enfe);
            }
            GrupoMuscular musculo = ejercicio.getMusculo();
            if (musculo != null) {
                musculo.getEjercicios().remove(ejercicio);
                musculo = em.merge(musculo);
            }
            em.remove(ejercicio);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Ejercicio> findEjercicioEntities() {
        return findEjercicioEntities(true, -1, -1);
    }

    public List<Ejercicio> findEjercicioEntities(int maxResults, int firstResult) {
        return findEjercicioEntities(false, maxResults, firstResult);
    }

    private List<Ejercicio> findEjercicioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Ejercicio.class));
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

    public Ejercicio findEjercicio(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Ejercicio.class, id);
        } finally {
            em.close();
        }
    }

    public int getEjercicioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Ejercicio> rt = cq.from(Ejercicio.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
