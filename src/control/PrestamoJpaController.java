/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package control;

import control.exceptions.NonexistentEntityException;
import control.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import modelo.Administrador;
import modelo.Libro;
import modelo.Usuario;
import modelo.Multa;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modelo.Prestamo;

/**
 *
 * @author Hector
 */
public class PrestamoJpaController implements Serializable {

    public PrestamoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Prestamo prestamo) throws PreexistingEntityException, Exception {
        if (prestamo.getMultaList() == null) {
            prestamo.setMultaList(new ArrayList<Multa>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Administrador idAdministrador = prestamo.getIdAdministrador();
            if (idAdministrador != null) {
                idAdministrador = em.getReference(idAdministrador.getClass(), idAdministrador.getIdAdmi());
                prestamo.setIdAdministrador(idAdministrador);
            }
            Libro idLibro = prestamo.getIdLibro();
            if (idLibro != null) {
                idLibro = em.getReference(idLibro.getClass(), idLibro.getIdlibro());
                prestamo.setIdLibro(idLibro);
            }
            Usuario idUsuario = prestamo.getIdUsuario();
            if (idUsuario != null) {
                idUsuario = em.getReference(idUsuario.getClass(), idUsuario.getIdUsuario());
                prestamo.setIdUsuario(idUsuario);
            }
            List<Multa> attachedMultaList = new ArrayList<Multa>();
            for (Multa multaListMultaToAttach : prestamo.getMultaList()) {
                multaListMultaToAttach = em.getReference(multaListMultaToAttach.getClass(), multaListMultaToAttach.getIdmulta());
                attachedMultaList.add(multaListMultaToAttach);
            }
            prestamo.setMultaList(attachedMultaList);
            em.persist(prestamo);
            if (idAdministrador != null) {
                idAdministrador.getPrestamoList().add(prestamo);
                idAdministrador = em.merge(idAdministrador);
            }
            if (idLibro != null) {
                idLibro.getPrestamoList().add(prestamo);
                idLibro = em.merge(idLibro);
            }
            if (idUsuario != null) {
                idUsuario.getPrestamoList().add(prestamo);
                idUsuario = em.merge(idUsuario);
            }
            for (Multa multaListMulta : prestamo.getMultaList()) {
                Prestamo oldIdPrestamoOfMultaListMulta = multaListMulta.getIdPrestamo();
                multaListMulta.setIdPrestamo(prestamo);
                multaListMulta = em.merge(multaListMulta);
                if (oldIdPrestamoOfMultaListMulta != null) {
                    oldIdPrestamoOfMultaListMulta.getMultaList().remove(multaListMulta);
                    oldIdPrestamoOfMultaListMulta = em.merge(oldIdPrestamoOfMultaListMulta);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPrestamo(prestamo.getIdPrestamo()) != null) {
                throw new PreexistingEntityException("Prestamo " + prestamo + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Prestamo prestamo) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Prestamo persistentPrestamo = em.find(Prestamo.class, prestamo.getIdPrestamo());
            Administrador idAdministradorOld = persistentPrestamo.getIdAdministrador();
            Administrador idAdministradorNew = prestamo.getIdAdministrador();
            Libro idLibroOld = persistentPrestamo.getIdLibro();
            Libro idLibroNew = prestamo.getIdLibro();
            Usuario idUsuarioOld = persistentPrestamo.getIdUsuario();
            Usuario idUsuarioNew = prestamo.getIdUsuario();
            List<Multa> multaListOld = persistentPrestamo.getMultaList();
            List<Multa> multaListNew = prestamo.getMultaList();
            if (idAdministradorNew != null) {
                idAdministradorNew = em.getReference(idAdministradorNew.getClass(), idAdministradorNew.getIdAdmi());
                prestamo.setIdAdministrador(idAdministradorNew);
            }
            if (idLibroNew != null) {
                idLibroNew = em.getReference(idLibroNew.getClass(), idLibroNew.getIdlibro());
                prestamo.setIdLibro(idLibroNew);
            }
            if (idUsuarioNew != null) {
                idUsuarioNew = em.getReference(idUsuarioNew.getClass(), idUsuarioNew.getIdUsuario());
                prestamo.setIdUsuario(idUsuarioNew);
            }
            List<Multa> attachedMultaListNew = new ArrayList<Multa>();
            for (Multa multaListNewMultaToAttach : multaListNew) {
                multaListNewMultaToAttach = em.getReference(multaListNewMultaToAttach.getClass(), multaListNewMultaToAttach.getIdmulta());
                attachedMultaListNew.add(multaListNewMultaToAttach);
            }
            multaListNew = attachedMultaListNew;
            prestamo.setMultaList(multaListNew);
            prestamo = em.merge(prestamo);
            if (idAdministradorOld != null && !idAdministradorOld.equals(idAdministradorNew)) {
                idAdministradorOld.getPrestamoList().remove(prestamo);
                idAdministradorOld = em.merge(idAdministradorOld);
            }
            if (idAdministradorNew != null && !idAdministradorNew.equals(idAdministradorOld)) {
                idAdministradorNew.getPrestamoList().add(prestamo);
                idAdministradorNew = em.merge(idAdministradorNew);
            }
            if (idLibroOld != null && !idLibroOld.equals(idLibroNew)) {
                idLibroOld.getPrestamoList().remove(prestamo);
                idLibroOld = em.merge(idLibroOld);
            }
            if (idLibroNew != null && !idLibroNew.equals(idLibroOld)) {
                idLibroNew.getPrestamoList().add(prestamo);
                idLibroNew = em.merge(idLibroNew);
            }
            if (idUsuarioOld != null && !idUsuarioOld.equals(idUsuarioNew)) {
                idUsuarioOld.getPrestamoList().remove(prestamo);
                idUsuarioOld = em.merge(idUsuarioOld);
            }
            if (idUsuarioNew != null && !idUsuarioNew.equals(idUsuarioOld)) {
                idUsuarioNew.getPrestamoList().add(prestamo);
                idUsuarioNew = em.merge(idUsuarioNew);
            }
            for (Multa multaListOldMulta : multaListOld) {
                if (!multaListNew.contains(multaListOldMulta)) {
                    multaListOldMulta.setIdPrestamo(null);
                    multaListOldMulta = em.merge(multaListOldMulta);
                }
            }
            for (Multa multaListNewMulta : multaListNew) {
                if (!multaListOld.contains(multaListNewMulta)) {
                    Prestamo oldIdPrestamoOfMultaListNewMulta = multaListNewMulta.getIdPrestamo();
                    multaListNewMulta.setIdPrestamo(prestamo);
                    multaListNewMulta = em.merge(multaListNewMulta);
                    if (oldIdPrestamoOfMultaListNewMulta != null && !oldIdPrestamoOfMultaListNewMulta.equals(prestamo)) {
                        oldIdPrestamoOfMultaListNewMulta.getMultaList().remove(multaListNewMulta);
                        oldIdPrestamoOfMultaListNewMulta = em.merge(oldIdPrestamoOfMultaListNewMulta);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = prestamo.getIdPrestamo();
                if (findPrestamo(id) == null) {
                    throw new NonexistentEntityException("The prestamo with id " + id + " no longer exists.");
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
            Prestamo prestamo;
            try {
                prestamo = em.getReference(Prestamo.class, id);
                prestamo.getIdPrestamo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The prestamo with id " + id + " no longer exists.", enfe);
            }
            Administrador idAdministrador = prestamo.getIdAdministrador();
            if (idAdministrador != null) {
                idAdministrador.getPrestamoList().remove(prestamo);
                idAdministrador = em.merge(idAdministrador);
            }
            Libro idLibro = prestamo.getIdLibro();
            if (idLibro != null) {
                idLibro.getPrestamoList().remove(prestamo);
                idLibro = em.merge(idLibro);
            }
            Usuario idUsuario = prestamo.getIdUsuario();
            if (idUsuario != null) {
                idUsuario.getPrestamoList().remove(prestamo);
                idUsuario = em.merge(idUsuario);
            }
            List<Multa> multaList = prestamo.getMultaList();
            for (Multa multaListMulta : multaList) {
                multaListMulta.setIdPrestamo(null);
                multaListMulta = em.merge(multaListMulta);
            }
            em.remove(prestamo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Prestamo> findPrestamoEntities() {
        return findPrestamoEntities(true, -1, -1);
    }

    public List<Prestamo> findPrestamoEntities(int maxResults, int firstResult) {
        return findPrestamoEntities(false, maxResults, firstResult);
    }

    private List<Prestamo> findPrestamoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Prestamo.class));
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

    public Prestamo findPrestamo(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Prestamo.class, id);
        } finally {
            em.close();
        }
    }

    public int getPrestamoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Prestamo> rt = cq.from(Prestamo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
