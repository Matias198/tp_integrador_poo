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
import com.tpintegrador.lafuerza.modelo.Ejercicio;
import com.tpintegrador.lafuerza.modelo.GrupoMuscular;
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
public class GrupoMuscularJpaController implements Serializable {

    public GrupoMuscularJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public GrupoMuscularJpaController() {
        emf = Persistence.createEntityManagerFactory("com.tpintegrador_laFuerza_jar_1.0PU");
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(GrupoMuscular grupoMuscular) {
        if (grupoMuscular.getEjercicios() == null) {
            grupoMuscular.setEjercicios(new HashSet<Ejercicio>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Set<Ejercicio> attachedEjercicios = new HashSet<Ejercicio>();
            for (Ejercicio ejerciciosEjercicioToAttach : grupoMuscular.getEjercicios()) {
                ejerciciosEjercicioToAttach = em.getReference(ejerciciosEjercicioToAttach.getClass(), ejerciciosEjercicioToAttach.getId());
                attachedEjercicios.add(ejerciciosEjercicioToAttach);
            }
            grupoMuscular.setEjercicios(attachedEjercicios);
            em.persist(grupoMuscular);
            for (Ejercicio ejerciciosEjercicio : grupoMuscular.getEjercicios()) {
                GrupoMuscular oldMusculoOfEjerciciosEjercicio = ejerciciosEjercicio.getMusculo();
                ejerciciosEjercicio.setMusculo(grupoMuscular);
                ejerciciosEjercicio = em.merge(ejerciciosEjercicio);
                if (oldMusculoOfEjerciciosEjercicio != null) {
                    oldMusculoOfEjerciciosEjercicio.getEjercicios().remove(ejerciciosEjercicio);
                    oldMusculoOfEjerciciosEjercicio = em.merge(oldMusculoOfEjerciciosEjercicio);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(GrupoMuscular grupoMuscular) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            GrupoMuscular persistentGrupoMuscular = em.find(GrupoMuscular.class, grupoMuscular.getId());
            Set<Ejercicio> ejerciciosOld = persistentGrupoMuscular.getEjercicios();
            Set<Ejercicio> ejerciciosNew = grupoMuscular.getEjercicios();
            Set<Ejercicio> attachedEjerciciosNew = new HashSet<Ejercicio>();
            for (Ejercicio ejerciciosNewEjercicioToAttach : ejerciciosNew) {
                ejerciciosNewEjercicioToAttach = em.getReference(ejerciciosNewEjercicioToAttach.getClass(), ejerciciosNewEjercicioToAttach.getId());
                attachedEjerciciosNew.add(ejerciciosNewEjercicioToAttach);
            }
            ejerciciosNew = attachedEjerciciosNew;
            grupoMuscular.setEjercicios(ejerciciosNew);
            grupoMuscular = em.merge(grupoMuscular);
            for (Ejercicio ejerciciosOldEjercicio : ejerciciosOld) {
                if (!ejerciciosNew.contains(ejerciciosOldEjercicio)) {
                    ejerciciosOldEjercicio.setMusculo(null);
                    ejerciciosOldEjercicio = em.merge(ejerciciosOldEjercicio);
                }
            }
            for (Ejercicio ejerciciosNewEjercicio : ejerciciosNew) {
                if (!ejerciciosOld.contains(ejerciciosNewEjercicio)) {
                    GrupoMuscular oldMusculoOfEjerciciosNewEjercicio = ejerciciosNewEjercicio.getMusculo();
                    ejerciciosNewEjercicio.setMusculo(grupoMuscular);
                    ejerciciosNewEjercicio = em.merge(ejerciciosNewEjercicio);
                    if (oldMusculoOfEjerciciosNewEjercicio != null && !oldMusculoOfEjerciciosNewEjercicio.equals(grupoMuscular)) {
                        oldMusculoOfEjerciciosNewEjercicio.getEjercicios().remove(ejerciciosNewEjercicio);
                        oldMusculoOfEjerciciosNewEjercicio = em.merge(oldMusculoOfEjerciciosNewEjercicio);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = grupoMuscular.getId();
                if (findGrupoMuscular(id) == null) {
                    throw new NonexistentEntityException("The grupoMuscular with id " + id + " no longer exists.");
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
            GrupoMuscular grupoMuscular;
            try {
                grupoMuscular = em.getReference(GrupoMuscular.class, id);
                grupoMuscular.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The grupoMuscular with id " + id + " no longer exists.", enfe);
            }
            Set<Ejercicio> ejercicios = grupoMuscular.getEjercicios();
            for (Ejercicio ejerciciosEjercicio : ejercicios) {
                ejerciciosEjercicio.setMusculo(null);
                ejerciciosEjercicio = em.merge(ejerciciosEjercicio);
            }
            em.remove(grupoMuscular);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<GrupoMuscular> findGrupoMuscularEntities() {
        return findGrupoMuscularEntities(true, -1, -1);
    }

    public List<GrupoMuscular> findGrupoMuscularEntities(int maxResults, int firstResult) {
        return findGrupoMuscularEntities(false, maxResults, firstResult);
    }

    private List<GrupoMuscular> findGrupoMuscularEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(GrupoMuscular.class));
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

    public GrupoMuscular findGrupoMuscular(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(GrupoMuscular.class, id);
        } finally {
            em.close();
        }
    }

    public int getGrupoMuscularCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<GrupoMuscular> rt = cq.from(GrupoMuscular.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
