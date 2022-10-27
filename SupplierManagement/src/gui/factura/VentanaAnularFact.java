package gui.factura;

import dao.FacturaDAO;
import gui.VentanaMain;
import gui.factura.consultaFact.ModeloFacturas;
import negocio.factura.Factura;
import negocio.pago.Pago;
import observer.Observador;
import observer.ObservadorContabilidad;
import observer.ObservadorSupervisor;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class VentanaAnularFact extends JFrame {
    ModeloFacturas modelo = new ModeloFacturas();
    JScrollPane scroll = new JScrollPane();
    JTable tabla = new JTable (modelo);
    JLabel labelTitulo = new JLabel("Seleccione la factura a anular:");
    JButton btnAnular = new JButton("Anular");

    public VentanaAnularFact(){
        super();

        //Layout
        getContentPane().setLayout(new BorderLayout());

        //Agrego datos al modelo
        agregarDatosModelo();

        //Panel para poner la info
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));


        //Agrego titulo
        panelPrincipal.add(labelTitulo);

        //Agrego tabla dentro del scroll
        scroll.setViewportView(tabla);
        scroll.setColumnHeaderView (tabla.getTableHeader());

        //Modifico ancho y centrado de las columnas
        DefaultTableCellRenderer textoCentrado = new DefaultTableCellRenderer();
        textoCentrado.setHorizontalAlignment(JLabel.CENTER);

        DefaultTableCellRenderer textoIzquierda = new DefaultTableCellRenderer();
        textoIzquierda.setHorizontalAlignment(JLabel.LEFT);

        tabla.getColumnModel().getColumn(0).setMinWidth(50);
        tabla.getColumnModel().getColumn(0).setMaxWidth(50);
        tabla.getColumnModel().getColumn(0).setResizable(false);
        tabla.getColumnModel().getColumn(0).setCellRenderer(textoCentrado);

        tabla.getColumnModel().getColumn(1).setMinWidth(100);
        tabla.getColumnModel().getColumn(1).setMaxWidth(100);
        tabla.getColumnModel().getColumn(1).setResizable(false);
        tabla.getColumnModel().getColumn(1).setCellRenderer(textoCentrado);

        tabla.getColumnModel().getColumn(2).setMinWidth(40);
        tabla.getColumnModel().getColumn(2).setMaxWidth(40);
        tabla.getColumnModel().getColumn(2).setResizable(false);
        tabla.getColumnModel().getColumn(2).setCellRenderer(textoCentrado);

        tabla.getColumnModel().getColumn(3).setMinWidth(200);
        tabla.getColumnModel().getColumn(3).setCellRenderer(textoIzquierda);

        tabla.getColumnModel().getColumn(4).setMinWidth(100);
        tabla.getColumnModel().getColumn(4).setMaxWidth(100);
        tabla.getColumnModel().getColumn(4).setResizable(false);

        tabla.getColumnModel().getColumn(5).setMinWidth(100);
        tabla.getColumnModel().getColumn(5).setMaxWidth(100);
        tabla.getColumnModel().getColumn(5).setResizable(false);

        tabla.getColumnModel().getColumn(6).setMinWidth(150);
        tabla.getColumnModel().getColumn(6).setMaxWidth(150);
        tabla.getColumnModel().getColumn(6).setResizable(false);
        tabla.getColumnModel().getColumn(6).setCellRenderer(textoCentrado);


        tabla.getColumnModel().getColumn(7).setMinWidth(150);
        tabla.getColumnModel().getColumn(7).setMaxWidth(150);
        tabla.getColumnModel().getColumn(7).setResizable(false);
        tabla.getColumnModel().getColumn(7).setCellRenderer(textoCentrado);

        //Agrego scroll dentro del panel
        panelPrincipal.add(scroll);

        //Agrego el botón
        JPanel panelAbajo = new JPanel();
        panelAbajo.setLayout(new BorderLayout());
        panelAbajo.add(btnAnular, BorderLayout.EAST);

        //Agrego panelPrincipal al frame
        add(panelPrincipal, BorderLayout.NORTH);
        add(panelAbajo, BorderLayout.SOUTH);

        //Listener del botón
        btnAnular.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int id = (int) tabla.getValueAt(tabla.getSelectedRow(), 0);
                Factura facturaAAnular = VentanaMain.factory.getFacturaDAO().obtenerFacturaPorId(id);



                //Agrego observadores
                Observador obsContabilidad = new ObservadorContabilidad(VentanaMain.factory.getLibroContableDAO());
                Observador obsSupervisor = new ObservadorSupervisor();

                facturaAAnular.agregarObservador(obsSupervisor);
                facturaAAnular.agregarObservador(obsContabilidad);


                //Anulo y guardo, antes pregunto
                int opcion = JOptionPane.showConfirmDialog(null,"Está seguro que desea anular la factura?", "Confirma anulación", JOptionPane.OK_CANCEL_OPTION);

                if (opcion == 0) {
                    facturaAAnular.setAnulada(true);
                    //Guardo en la DB:
                    VentanaMain.factory.getFacturaDAO().actualizarFactura(facturaAAnular);

                    dispose();
                }
            }
        });

        //Datos de la ventana:
        pack();
        setTitle("Anulación de facturas");
        setSize(900,500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void agregarDatosModelo(){

        FacturaDAO facturaDAO = VentanaMain.factory.getFacturaDAO();

        List<Pago> listaPagos = VentanaMain.factory.getPagoDAO().listarPagos();
        List<Factura> listaFacturas = facturaDAO.listarFacturas();
        double saldo;

        if (listaFacturas.isEmpty()){
            JOptionPane.showMessageDialog(null, "No se han ingresado facturas hasta ahora!",
                    "Sin datos", JOptionPane.WARNING_MESSAGE);
        } else {

            for (Factura fact: listaFacturas){

                boolean noPoseePagos = true;
                for (Pago pago: listaPagos) {

                    if (pago.getFactura().getId() == fact.getId()) {
                        noPoseePagos = false;
                        break;
                    }
                }

                boolean noAnulada = !fact.isAnulada();

                if (noAnulada && noPoseePagos){
                    modelo.agregarFactura(fact);
                }

            }
        }
    }



    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new VentanaAnularFact();
            }
        });

    }
}


