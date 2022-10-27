package gui;


import factoryDAO.DAOFactory;
import gui.contabilidad.VentanaConsultaAsientos;
import gui.factura.VentanaAltaFactura;
import gui.factura.VentanaAnularFact;
import gui.factura.VentanaConsultaFactPend;
import gui.pago.VentanaAltaPago;
import gui.pago.VentanaConsultaPagos;
import gui.proveedor.*;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.event.*;

public class VentanaMain extends JFrame
{
    JTree menuArbol;
    public final static DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.JDBC);


    public VentanaMain()
    {
        //Creo el nodo principal
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Menú");

        //Creo los menú de primer nivel
        DefaultMutableTreeNode nodoProveedores = new DefaultMutableTreeNode("Proveedores");
        DefaultMutableTreeNode nodoFacturas = new DefaultMutableTreeNode("Facturas");
        DefaultMutableTreeNode nodoPagos = new DefaultMutableTreeNode("Pagos");
        DefaultMutableTreeNode nodoContabilidad = new DefaultMutableTreeNode("Contabilidad");

        //Agrego los menú de primer nivel abajo del nodo principal
        root.add(nodoProveedores);
        root.add(nodoFacturas);
        root.add(nodoPagos);
        root.add(nodoContabilidad);

        //agrego opciones dentro de cada clasificación
            //Proveedores
        DefaultMutableTreeNode opcionAltaProveedor = new DefaultMutableTreeNode("Alta de proveedor");
        DefaultMutableTreeNode opcionConsultaSaldos = new DefaultMutableTreeNode("Consulta de saldos");
        nodoProveedores.add(opcionAltaProveedor);
        nodoProveedores.add(opcionConsultaSaldos);

            //Facturas
        DefaultMutableTreeNode opcionNuevaFactura = new DefaultMutableTreeNode("Nueva factura");
        DefaultMutableTreeNode opcionConsultaFacturas = new DefaultMutableTreeNode("Consulta de facturas impagas");
        DefaultMutableTreeNode opcionAnulaFactura = new DefaultMutableTreeNode("Anular factura");
        nodoFacturas.add(opcionNuevaFactura);
        nodoFacturas.add(opcionConsultaFacturas);
        nodoFacturas.add(opcionAnulaFactura);

            //Pagos
        DefaultMutableTreeNode opcionNuevoPago = new DefaultMutableTreeNode("Nuevo pago");
        DefaultMutableTreeNode opcionConsultaPago = new DefaultMutableTreeNode("Consulta de pagos realizados");
        nodoPagos.add(opcionNuevoPago);
        nodoPagos.add(opcionConsultaPago);

            //Contabilidad
        DefaultMutableTreeNode opcionConsultaContabilidad = new DefaultMutableTreeNode("Visualizar imputaciones contables");
        nodoContabilidad.add(opcionConsultaContabilidad);

        //Creo el arbol pasandole el nodo Menú y lo agrego al Frame
        menuArbol = new JTree(root);
        add(menuArbol);

        //Agrego el mouse listener
        MouseListener mouseListener = new ListenerMouseArbol();
        menuArbol.addMouseListener(mouseListener);

        //Detalles de la ventana
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Supplier Management");
        this.pack();
        this.setSize(350, 300);
        this.setLocationRelativeTo(null); //Para que aparezca al medio de la pantalla
        this.setVisible(true);
    }

    class ListenerMouseArbol extends MouseAdapter {
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                DefaultMutableTreeNode opcionSeleccionada = (DefaultMutableTreeNode) menuArbol.getLastSelectedPathComponent();

                if (opcionSeleccionada == null) return;

                //Opciones:
                String nombreMenu = opcionSeleccionada.getUserObject().toString();
                if (nombreMenu.equalsIgnoreCase("Alta de proveedor")) new VentanaAltaProveedor();
                if (nombreMenu.equalsIgnoreCase("Consulta de saldos")) new VentanaConsultaSaldoProv();

                if (nombreMenu.equalsIgnoreCase("Nueva factura")) new VentanaAltaFactura();
                if (nombreMenu.equalsIgnoreCase("Consulta de facturas impagas")) new VentanaConsultaFactPend();
                if (nombreMenu.equalsIgnoreCase("Anular factura")) new VentanaAnularFact();

                if (nombreMenu.equalsIgnoreCase("Nuevo pago")) new VentanaAltaPago();
                if (nombreMenu.equalsIgnoreCase("Consulta de pagos realizados")) new VentanaConsultaPagos();

                if (nombreMenu.equalsIgnoreCase("Visualizar imputaciones contables")) new VentanaConsultaAsientos();
            }
        }
    }

















    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new VentanaMain();
            }
        });

    }
}