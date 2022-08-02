/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tpintegrador.lafuerza.repositorio;

import com.tpintegrador.lafuerza.modelo.Cliente;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.tpintegrador.lafuerza.modelo.Rutina;
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
public class ClienteJpaController implements Serializable {

    public ClienteJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public ClienteJpaController() {
        emf = Persistence.createEntityManagerFactory("com.tpintegrador_laFuerza_jar_1.0PU");
    }
    
    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Cliente cliente) throws PreexistingEntityException, Exception {
        if (cliente.getRutinas() == null) {
            cliente.setRutinas(new HashSet<Rutina>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Set<Rutina> attachedRutinas = new HashSet<Rutina>();
            for (Rutina rutinasRutinaToAttach : cliente.getRutinas()) {
                rutinasRutinaToAttach = em.getReference(rutinasRutinaToAttach.getClass(), rutinasRutinaToAttach.getId());
                attachedRutinas.add(rutinasRutinaToAttach);
            }
            cliente.setRutinas(attachedRutinas);
            em.persist(cliente);
            for (Rutina rutinasRutina : cliente.getRutinas()) {
                Cliente oldClienteOfRutinasRutina = rutinasRutina.getCliente();
                rutinasRutina.setCliente(cliente);
                rutinasRutina = em.merge(rutinasRutina);
                if (oldClienteOfRutinasRutina != null) {
                    oldClienteOfRutinasRutina.getRutinas().remove(rutinasRutina);
                    oldClienteOfRutinasRutina = em.merge(oldClienteOfRutinasRutina);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCliente(cliente.getDni()) != null) {
                throw new PreexistingEntityException("Cliente " + cliente + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Cliente cliente) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cliente persistentCliente = em.find(Cliente.class, cliente.getDni());
            Set<Rutina> rutinasOld = persistentCliente.getRutinas();
            Set<Rutina> rutinasNew = cliente.getRutinas();
            Set<Rutina> attachedRutinasNew = new HashSet<Rutina>();
            for (Rutina rutinasNewRutinaToAttach : rutinasNew) {
                rutinasNewRutinaToAttach = em.getReference(rutinasNewRutinaToAttach.getClass(), rutinasNewRutinaToAttach.getId());
                attachedRutinasNew.add(rutinasNewRutinaToAttach);
            }
            rutinasNew = attachedRutinasNew;
            cliente.setRutinas(rutinasNew);
            cliente = em.merge(cliente);
            for (Rutina rutinasOldRutina : rutinasOld) {
                if (!rutinasNew.contains(rutinasOldRutina)) {
                    rutinasOldRutina.setCliente(null);
                    rutinasOldRutina = em.merge(rutinasOldRutina);
                }
            }
            for (Rutina rutinasNewRutina : rutinasNew) {
                if (!rutinasOld.contains(rutinasNewRutina)) {
                    Cliente oldClienteOfRutinasNewRutina = rutinasNewRutina.getCliente();
                    rutinasNewRutina.setCliente(cliente);
                    rutinasNewRutina = em.merge(rutinasNewRutina);
                    if (oldClienteOfRutinasNewRutina != null && !oldClienteOfRutinasNewRutina.equals(cliente)) {
                        oldClienteOfRutinasNewRutina.getRutinas().remove(rutinasNewRutina);
                        oldClienteOfRutinasNewRutina = em.merge(oldClienteOfRutinasNewRutina);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = cliente.getDni();
                if (findCliente(id) == null) {
                    throw new NonexistentEntityException("The cliente with id " + id + " no longer exists.");
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
            Cliente cliente;
            try {
                cliente = em.getReference(Cliente.class, id);
                cliente.getDni();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cliente with id " + id + " no longer exists.", enfe);
            }
            Set<Rutina> rutinas = cliente.getRutinas();
            for (Rutina rutinasRutina : rutinas) {
                rutinasRutina.setCliente(null);
                rutinasRutina = em.merge(rutinasRutina);
            }
            em.remove(cliente);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Cliente> findClienteEntities() {
        return findClienteEntities(true, -1, -1);
    }

    public List<Cliente> findClienteEntities(int maxResults, int firstResult) {
        return findClienteEntities(false, maxResults, firstResult);
    }

    private List<Cliente> findClienteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cliente.class));
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

    public Cliente findCliente(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cliente.class, id);
        } finally {
            em.close();
        }
    }

    public int getClienteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cliente> rt = cq.from(Cliente.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
