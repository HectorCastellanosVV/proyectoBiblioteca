/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package control;

import control.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import modelo.Prestamo;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modelo.Usuario;
import modelo.Visita;

/**
 *
 * @author Hector
 */
public class UsuarioJpaController implements Serializable {

    public UsuarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuario usuario) {
        if (usuario.getPrestamoList() == null) {
            usuario.setPrestamoList(new ArrayList<Prestamo>());
        }
        if (usuario.getVisitaList() == null) {
            usuario.setVisitaList(new ArrayList<Visita>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Prestamo> attachedPrestamoList = new ArrayList<Prestamo>();
            for (Prestamo prestamoListPrestamoToAttach : usuario.getPrestamoList()) {
                prestamoListPrestamoToAttach = em.getReference(prestamoListPrestamoToAttach.getClass(), prestamoListPrestamoToAttach.getIdPrestamo());
                attachedPrestamoList.add(prestamoListPrestamoToAttach);
            }
            usuario.setPrestamoList(attachedPrestamoList);
            List<Visita> attachedVisitaList = new ArrayList<Visita>();
            for (Visita visitaListVisitaToAttach : usuario.getVisitaList()) {
                visitaListVisitaToAttach = em.getReference(visitaListVisitaToAttach.getClass(), visitaListVisitaToAttach.getIdVisita());
                attachedVisitaList.add(visitaListVisitaToAttach);
            }
            usuario.setVisitaList(attachedVisitaList);
            em.persist(usuario);
            for (Prestamo prestamoListPrestamo : usuario.getPrestamoList()) {
                Usuario oldIdUsuarioOfPrestamoListPrestamo = prestamoListPrestamo.getIdUsuario();
                prestamoListPrestamo.setIdUsuario(usuario);
                prestamoListPrestamo = em.merge(prestamoListPrestamo);
                if (oldIdUsuarioOfPrestamoListPrestamo != null) {
                    oldIdUsuarioOfPrestamoListPrestamo.getPrestamoList().remove(prestamoListPrestamo);
                    oldIdUsuarioOfPrestamoListPrestamo = em.merge(oldIdUsuarioOfPrestamoListPrestamo);
                }
            }
            for (Visita visitaListVisita : usuario.getVisitaList()) {
                Usuario oldIdUsuarioOfVisitaListVisita = visitaListVisita.getIdUsuario();
                visitaListVisita.setIdUsuario(usuario);
                visitaListVisita = em.merge(visitaListVisita);
                if (oldIdUsuarioOfVisitaListVisita != null) {
                    oldIdUsuarioOfVisitaListVisita.getVisitaList().remove(visitaListVisita);
                    oldIdUsuarioOfVisitaListVisita = em.merge(oldIdUsuarioOfVisitaListVisita);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuario usuario) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario persistentUsuario = em.find(Usuario.class, usuario.getIdUsuario());
            List<Prestamo> prestamoListOld = persistentUsuario.getPrestamoList();
            List<Prestamo> prestamoListNew = usuario.getPrestamoList();
            List<Visita> visitaListOld = persistentUsuario.getVisitaList();
            List<Visita> visitaListNew = usuario.getVisitaList();
            List<Prestamo> attachedPrestamoListNew = new ArrayList<Prestamo>();
            for (Prestamo prestamoListNewPrestamoToAttach : prestamoListNew) {
                prestamoListNewPrestamoToAttach = em.getReference(prestamoListNewPrestamoToAttach.getClass(), prestamoListNewPrestamoToAttach.getIdPrestamo());
                attachedPrestamoListNew.add(prestamoListNewPrestamoToAttach);
            }
            prestamoListNew = attachedPrestamoListNew;
            usuario.setPrestamoList(prestamoListNew);
            List<Visita> attachedVisitaListNew = new ArrayList<Visita>();
            for (Visita visitaListNewVisitaToAttach : visitaListNew) {
                visitaListNewVisitaToAttach = em.getReference(visitaListNewVisitaToAttach.getClass(), visitaListNewVisitaToAttach.getIdVisita());
                attachedVisitaListNew.add(visitaListNewVisitaToAttach);
            }
            visitaListNew = attachedVisitaListNew;
            usuario.setVisitaList(visitaListNew);
            usuario = em.merge(usuario);
            for (Prestamo prestamoListOldPrestamo : prestamoListOld) {
                if (!prestamoListNew.contains(prestamoListOldPrestamo)) {
                    prestamoListOldPrestamo.setIdUsuario(null);
                    prestamoListOldPrestamo = em.merge(prestamoListOldPrestamo);
                }
            }
            for (Prestamo prestamoListNewPrestamo : prestamoListNew) {
                if (!prestamoListOld.contains(prestamoListNewPrestamo)) {
                    Usuario oldIdUsuarioOfPrestamoListNewPrestamo = prestamoListNewPrestamo.getIdUsuario();
                    prestamoListNewPrestamo.setIdUsuario(usuario);
                    prestamoListNewPrestamo = em.merge(prestamoListNewPrestamo);
                    if (oldIdUsuarioOfPrestamoListNewPrestamo != null && !oldIdUsuarioOfPrestamoListNewPrestamo.equals(usuario)) {
                        oldIdUsuarioOfPrestamoListNewPrestamo.getPrestamoList().remove(prestamoListNewPrestamo);
                        oldIdUsuarioOfPrestamoListNewPrestamo = em.merge(oldIdUsuarioOfPrestamoListNewPrestamo);
                    }
                }
            }
            for (Visita visitaListOldVisita : visitaListOld) {
                if (!visitaListNew.contains(visitaListOldVisita)) {
                    visitaListOldVisita.setIdUsuario(null);
                    visitaListOldVisita = em.merge(visitaListOldVisita);
                }
            }
            for (Visita visitaListNewVisita : visitaListNew) {
                if (!visitaListOld.contains(visitaListNewVisita)) {
                    Usuario oldIdUsuarioOfVisitaListNewVisita = visitaListNewVisita.getIdUsuario();
                    visitaListNewVisita.setIdUsuario(usuario);
                    visitaListNewVisita = em.merge(visitaListNewVisita);
                    if (oldIdUsuarioOfVisitaListNewVisita != null && !oldIdUsuarioOfVisitaListNewVisita.equals(usuario)) {
                        oldIdUsuarioOfVisitaListNewVisita.getVisitaList().remove(visitaListNewVisita);
                        oldIdUsuarioOfVisitaListNewVisita = em.merge(oldIdUsuarioOfVisitaListNewVisita);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = usuario.getIdUsuario();
                if (findUsuario(id) == null) {
                    throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario usuario;
            try {
                usuario = em.getReference(Usuario.class, id);
                usuario.getIdUsuario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.", enfe);
            }
            List<Prestamo> prestamoList = usuario.getPrestamoList();
            for (Prestamo prestamoListPrestamo : prestamoList) {
                prestamoListPrestamo.setIdUsuario(null);
                prestamoListPrestamo = em.merge(prestamoListPrestamo);
            }
            List<Visita> visitaList = usuario.getVisitaList();
            for (Visita visitaListVisita : visitaList) {
                visitaListVisita.setIdUsuario(null);
                visitaListVisita = em.merge(visitaListVisita);
            }
            em.remove(usuario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Usuario> findUsuarioEntities() {
        return findUsuarioEntities(true, -1, -1);
    }

    public List<Usuario> findUsuarioEntities(int maxResults, int firstResult) {
        return findUsuarioEntities(false, maxResults, firstResult);
    }

    private List<Usuario> findUsuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuario.class));
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

    public Usuario findUsuario(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuario.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuario> rt = cq.from(Usuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
