package gui.contabilidad;

import negocio.contabilidad.Asiento;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.ArrayList;

public class ModeloAsiento implements TableModel {

    private ArrayList<Asiento> listaLineaAsiento = new ArrayList<>();

    public int getRowCount() {
        return listaLineaAsiento.size();
    }

    public int getColumnCount() {
        //id, fecha, cuenta, debe, haber, observaciones
        return 6;
    }

    public String getColumnName(int columnIndex) {

        return switch (columnIndex) {
            case 0 -> "ID";
            case 1 -> "Fecha";
            case 2 -> "Cuenta contable";
            case 3 -> "Debe";
            case 4 -> "Haber";
            case 5 -> "Observaciones";
            default -> null;
        };
    }

    public Class<?> getColumnClass(int columnIndex) {
        //Devuelve que clase de tipo de dato es cada columna
        return switch (columnIndex) {
            case 0 -> Integer.class;
            case 1, 2, 5 -> String.class;
            case 3, 4 -> Double.class;
            default -> null;
        };
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false; //Ya que la idea es sólo mostrarlo por pantalla.
    }

    public Object getValueAt(int rowIndex, int columnIndex) {


        // Proveedor de la fila indicada
        Asiento aux = (listaLineaAsiento.get(rowIndex));

        // Se obtiene el campo apropiado según el valor de columnIndex
        return switch (columnIndex) {
            case 0 -> aux.getId();
            case 1 -> aux.getFecha();
            case 2 -> aux.getCuenta();
            case 3 -> aux.getDebe();
            case 4 -> aux.getHaber();
            case 5 -> aux.getObservaciones();
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

    public void agregarLineaAsiento(Asiento as){
        listaLineaAsiento.add(as);
    }
}