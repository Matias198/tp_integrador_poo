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
import com.tpintegrador.lafuerza.modelo.Tutor;
import com.tpintegrador.lafuerza.repositorio.exceptions.NonexistentEntityException;
import com.tpintegrador.lafuerza.repositorio.exceptions.PreexistingEntityException;
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
public class TutorJpaController implements Serializable {

    public TutorJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public TutorJpaController() {
        emf = Persistence.createEntityManagerFactory("com.tpintegrador_laFuerza_jar_1.0PU");
    }
    
    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Tutor tutor) throws PreexistingEntityException, Exception {
        if (tutor.getRutinas() == null) {
            tutor.setRutinas(new HashSet<Rutina>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Set<Rutina> attachedRutinas = new HashSet<Rutina>();
            for (Rutina rutinasRutinaToAttach : tutor.getRutinas()) {
                rutinasRutinaToAttach = em.getReference(rutinasRutinaToAttach.getClass(), rutinasRutinaToAttach.getId());
                attachedRutinas.add(rutinasRutinaToAttach);
            }
            tutor.setRutinas(attachedRutinas);
            em.persist(tutor);
            for (Rutina rutinasRutina : tutor.getRutinas()) {
                Tutor oldTutorOfRutinasRutina = rutinasRutina.getTutor();
                rutinasRutina.setTutor(tutor);
                rutinasRutina = em.merge(rutinasRutina);
                if (oldTutorOfRutinasRutina != null) {
                    oldTutorOfRutinasRutina.getRutinas().remove(rutinasRutina);
                    oldTutorOfRutinasRutina = em.merge(oldTutorOfRutinasRutina);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTutor(tutor.getDni()) != null) {
                throw new PreexistingEntityException("Tutor " + tutor + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tutor tutor) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tutor persistentTutor = em.find(Tutor.class, tutor.getDni());
            Set<Rutina> rutinasOld = persistentTutor.getRutinas();
            Set<Rutina> rutinasNew = tutor.getRutinas();
            Set<Rutina> attachedRutinasNew = new HashSet<Rutina>();
            for (Rutina rutinasNewRutinaToAttach : rutinasNew) {
                rutinasNewRutinaToAttach = em.getReference(rutinasNewRutinaToAttach.getClass(), rutinasNewRutinaToAttach.getId());
                attachedRutinasNew.add(rutinasNewRutinaToAttach);
            }
            rutinasNew = attachedRutinasNew;
            tutor.setRutinas(rutinasNew);
            tutor = em.merge(tutor);
            for (Rutina rutinasOldRutina : rutinasOld) {
                if (!rutinasNew.contains(rutinasOldRutina)) {
                    rutinasOldRutina.setTutor(null);
                    rutinasOldRutina = em.merge(rutinasOldRutina);
                }
            }
            for (Rutina rutinasNewRutina : rutinasNew) {
                if (!rutinasOld.contains(rutinasNewRutina)) {
                    Tutor oldTutorOfRutinasNewRutina = rutinasNewRutina.getTutor();
                    rutinasNewRutina.setTutor(tutor);
                    rutinasNewRutina = em.merge(rutinasNewRutina);
                    if (oldTutorOfRutinasNewRutina != null && !oldTutorOfRutinasNewRutina.equals(tutor)) {
                        oldTutorOfRutinasNewRutina.getRutinas().remove(rutinasNewRutina);
                        oldTutorOfRutinasNewRutina = em.merge(oldTutorOfRutinasNewRutina);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = tutor.getDni();
                if (findTutor(id) == null) {
                    throw new NonexistentEntityException("The tutor with id " + id + " no longer exists.");
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
            Tutor tutor;
            try {
                tutor = em.getReference(Tutor.class, id);
                tutor.getDni();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tutor with id " + id + " no longer exists.", enfe);
            }
            Set<Rutina> rutinas = tutor.getRutinas();
            for (Rutina rutinasRutina : rutinas) {
                rutinasRutina.setTutor(null);
                rutinasRutina = em.merge(rutinasRutina);
            }
            em.remove(tutor);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Tutor> findTutorEntities() {
        return findTutorEntities(true, -1, -1);
    }

    public List<Tutor> findTutorEntities(int maxResults, int firstResult) {
        return findTutorEntities(false, maxResults, firstResult);
    }

    private List<Tutor> findTutorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tutor.class));
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

    public Tutor findTutor(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tutor.class, id);
        } finally {
            em.close();
        }
    }

    public int getTutorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tutor> rt = cq.from(Tutor.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
