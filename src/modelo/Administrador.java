/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Hector
 */
@Entity
@Table(name = "administrador")
@NamedQueries({
    @NamedQuery(name = "Administrador.findAll", query = "SELECT a FROM Administrador a"),
    @NamedQuery(name = "Administrador.findByIdAdmi", query = "SELECT a FROM Administrador a WHERE a.idAdmi = :idAdmi"),
    @NamedQuery(name = "Administrador.findByNombreUsuario", query = "SELECT a FROM Administrador a WHERE a.nombreUsuario = :nombreUsuario"),
    @NamedQuery(name = "Administrador.findByContrasenia", query = "SELECT a FROM Administrador a WHERE a.contrasenia = :contrasenia")})
public class Administrador implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idAdmi")
    private Integer idAdmi;
    @Column(name = "nombreUsuario")
    private String nombreUsuario;
    @Column(name = "contrasenia")
    private String contrasenia;
    @OneToMany(mappedBy = "idAdministrador")
    private List<Prestamo> prestamoList;

    public Administrador() {
    }

    public Administrador(Integer idAdmi) {
        this.idAdmi = idAdmi;
    }

    public Integer getIdAdmi() {
        return idAdmi;
    }

    public void setIdAdmi(Integer idAdmi) {
        this.idAdmi = idAdmi;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public List<Prestamo> getPrestamoList() {
        return prestamoList;
    }

    public void setPrestamoList(List<Prestamo> prestamoList) {
        this.prestamoList = prestamoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idAdmi != null ? idAdmi.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Administrador)) {
            return false;
        }
        Administrador other = (Administrador) object;
        if ((this.idAdmi == null && other.idAdmi != null) || (this.idAdmi != null && !this.idAdmi.equals(other.idAdmi))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.Administrador[ idAdmi=" + idAdmi + " ]";
    }
    
}
