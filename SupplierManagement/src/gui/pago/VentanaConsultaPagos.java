package gui.pago;

import gui.VentanaMain;
import negocio.factura.Factura;
import negocio.pago.Pago;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

public class VentanaConsultaPagos extends JFrame{

    List<Pago> listaPagos = VentanaMain.factory.getPagoDAO().listarPagos();
    List<Integer> facturasConPagos = new ArrayList<>();
    List<DefaultMutableTreeNode> nodos2doNivel = new ArrayList<>();

    JScrollPane scrollPane;
    JTree menuArbol;
    DefaultMutableTreeNode root = new DefaultMutableTreeNode("Pagos realizados");


    public VentanaConsultaPagos(){

        if (listaPagos.isEmpty()){
            JOptionPane.showMessageDialog(null, "No se han ingresado pagos hasta ahora!",
                    "Sin datos", JOptionPane.WARNING_MESSAGE);
            return;

        } else {


            //Recorro todos los pagos para ver a que facturas se asocian:
            for (Pago pago : listaPagos) {
                //Si la factura asociada a este pago no está en el listado, la agrego.
                int idAsociado = pago.getFactura().getId();
                if (!facturasConPagos.contains(idAsociado)) {
                    facturasConPagos.add(idAsociado);
                }
            }

            //Creo nodos de 2do nivel con cada factura que tiene pagos:
            for (Integer id : facturasConPagos){

                Factura factura = VentanaMain.factory.getFacturaDAO().obtenerFacturaPorId(id);

                String linea = "Proveedor: " + factura.getProveedor().getNombre() + " (" + factura.getProveedor().getCuit() + ")" +
                        " | Número: " + factura.getNumero() + " | Saldo pendiente: $";
                linea = linea + String.format("%,.2f",factura.getSaldo());
                System.out.println(linea);

                DefaultMutableTreeNode aux = new DefaultMutableTreeNode(linea);

                nodos2doNivel.add(aux);
            }

            //Agrego nodos al arbol principal
            for (DefaultMutableTreeNode nodo : nodos2doNivel){
                root.add(nodo);
            }


            //Recorro cada factura pagada y muestro a continuación los pagos asociados:
            for (Pago pago : listaPagos) {

                String nroFacturaPagada = pago.getFactura().getNumero();
                String detalles = pago.visualizarDetalles();

                for (DefaultMutableTreeNode nodo : nodos2doNivel){

                    String textoNodo = (String) nodo.getUserObject();
                    textoNodo = textoNodo.toLowerCase();

                    if (textoNodo.contains(nroFacturaPagada)){

                        DefaultMutableTreeNode aux = new DefaultMutableTreeNode(detalles);
                        nodo.add(aux);

                    }
                }
            }

            //Creo el arbol pasandole el nodo root y lo agrego al Frame
            menuArbol = new JTree(root);
            scrollPane = new JScrollPane(menuArbol);
            add(scrollPane);

            //Detalles de la ventana
            this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            this.setTitle("Consulta de pagos realizados");
            this.pack();
            this.setSize(800, 300);
            this.setLocationRelativeTo(null); //Para que aparezca al medio de la pantalla
            this.setVisible(true);
        }
    }


    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new VentanaConsultaPagos();
            }
        });

    }
}
