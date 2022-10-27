package gui.factura.consultaFact;

import negocio.factura.*;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.ArrayList;

public class ModeloFacturas implements TableModel {

    private ArrayList<Factura> listaFacturas = new ArrayList<>();

    public int getRowCount() {
        return listaFacturas.size();
    }

    public int getColumnCount() {
        //id, numero, tipo, proveedor, saldo, ivadiscriminado, categMonot, nroEmbarque
        return 8;
    }

    public String getColumnName(int columnIndex) {

        return switch (columnIndex) {
            case 0 -> "ID";
            case 1 -> "Numero";
            case 2 -> "Tipo";
            case 3 -> "Proveedor";
            case 4 -> "Saldo en $";
            case 5 -> "IVA discriminado";
            case 6 -> "Categoría monotributo";
            case 7 -> "Número de embarque";
            default -> null;
        };
    }

    public Class<?> getColumnClass(int columnIndex) {
        //Devuelve que clase de tipo de dato es cada columna
        return switch (columnIndex) {
            case 0 -> Integer.class;
            case 1, 2, 6, 7 -> String.class;
            case 3 -> Long.class;
            case 4, 5  -> Double.class;
            default -> null;
        };
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false; //Ya que la idea es sólo mostrarlo por pantalla.
    }

    public Object getValueAt(int rowIndex, int columnIndex) {


        // Factura de la fila indicada
        Factura aux = (listaFacturas.get(rowIndex));

        // Se obtiene el campo apropiado según el valor de columnIndex




        return switch (columnIndex) {
            case 0 -> aux.getId();
            case 1 -> aux.getNumero();
            case 2 -> aux.getTipo();
            case 3 -> aux.getProveedor().getNombre();
            case 4 -> aux.getSaldo();
            case 5 -> aux instanceof FacturaA ? ((FacturaA) aux).getIvaDiscriminado() : 0;
            case 6 -> aux instanceof FacturaC ? ((FacturaC) aux).getCategoriaMonotributo() : "-";
            case 7 -> aux instanceof FacturaM ? ((FacturaM) aux).getNumeroEmbarque() : "-";
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

    public void agregarFactura(Factura fact){
        listaFacturas.add(fact);
    }
}