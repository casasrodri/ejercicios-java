package gui.proveedor.consultaSaldo;

import negocio.proveedor.Proveedor;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.ArrayList;

public class ModeloTabla implements TableModel {

    private ArrayList<Proveedor> listaProveedores = new ArrayList<>();

    public int getRowCount() {
        return listaProveedores.size();
    }

    public int getColumnCount() {
        //Nombre, CUIT, Categoria, Saldo
        return 4;
    }

    public String getColumnName(int columnIndex) {
        return switch (columnIndex) {
            case 0 -> "Nombre";
            case 1 -> "CUIT";
            case 2 -> "Categoría impositiva";
            case 3 -> "Saldo en $";
            default -> null;
        };
    }

    public Class<?> getColumnClass(int columnIndex) {
        //Devuelve que clase de tipo de dato es cada columna
        return switch (columnIndex) {
            case 0, 2 -> String.class;
            case 1 -> Long.class;
            case 3 -> Double.class;
            default -> null;
        };
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false; //Ya que la idea es sólo mostrarlo por pantalla.
    }

    public Object getValueAt(int rowIndex, int columnIndex) {

        // Proveedor de la fila indicada
        Proveedor aux = (listaProveedores.get(rowIndex));

        // Se obtiene el campo apropiado según el valor de columnIndex
        return switch (columnIndex) {
            case 0 -> aux.getNombre();
            case 1 -> aux.getCuit();
            case 2 -> Proveedor.consultarCategoria(aux.getCategoriaImpositiva());
            case 3 -> ((ProveedorConSaldo) aux).getSaldo();
            default -> null;
        };
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        //Como no voy a agregar proveedores, no lo implemento.
    }

    public void addTableModelListener(TableModelListener l) {

    }

    public void removeTableModelListener(TableModelListener l) {
        //No lo considero necesario para este proyecto.
    }

    public void agregarProveedor(ProveedorConSaldo prov){
        listaProveedores.add(prov);
    }
}
