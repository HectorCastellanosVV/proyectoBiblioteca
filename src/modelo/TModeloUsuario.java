/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Hector
 */
public class TModeloUsuario extends AbstractTableModel
{
    private List<Usuario> datos;
    private int nc;
    final private String columnas[]={"idUsuario","Nombre","numTelefono","correo"};
    public TModeloUsuario(List<Usuario> d)
    {
        datos=d;
    }

    
    @Override
    public int getRowCount() {
        return datos.size();
    }

    @Override
    public int getColumnCount() {
        return columnas.length;
    }

    @Override
    public Object getValueAt(int r, int c) {
        switch (c) {
            case 0:
                return datos.get(r).getIdUsuario();
            case 1:
                return datos.get(r).getNombre();
            case 2:
                return datos.get(r).getNumTelefono();
            case 3:
                return datos.get(r).getCorreo();
            default:
                throw new AssertionError();
        }
    }
    
}
