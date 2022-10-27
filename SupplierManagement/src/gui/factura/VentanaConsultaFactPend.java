package gui.factura;

import dao.FacturaDAO;
import gui.VentanaMain;
import gui.factura.consultaFact.ModeloFacturas;
import negocio.factura.Factura;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.util.List;

public class VentanaConsultaFactPend extends JFrame {

    List<Factura> listaFacturas = VentanaMain.factory.getFacturaDAO().listarFacturas();
    ModeloFacturas modelo = new ModeloFacturas();
    JScrollPane scroll = new JScrollPane();
    JTable tabla = new JTable (modelo);
    JLabel labelTitulo = new JLabel("Facturas pendientes de pago:");
    double saldoTotal;
    JLabel labelSaldoTotal = new JLabel();

    public VentanaConsultaFactPend(){
        super();

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

        //Agrego el saldo total
        labelSaldoTotal.setText(String.format("Saldo total adeudado: $%,.2f", saldoTotal));
        panelPrincipal.add(labelSaldoTotal);

        //Agrego panelPrincipal al frame
        add(panelPrincipal);

        //Datos de la ventana:
        pack();
        setTitle("Consulta de facturas pendientes de pago");
        setSize(900,500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void agregarDatosModelo(){

        double saldo;

        if (listaFacturas.isEmpty()){
            JOptionPane.showMessageDialog(null, "No se han ingresado facturas hasta ahora!",
                    "Sin datos", JOptionPane.WARNING_MESSAGE);
        } else {

            for (Factura fact: listaFacturas){
                if (!fact.isAnulada() && fact.getSaldo() > 0){
                    modelo.agregarFactura(fact);
                    saldoTotal += fact.getSaldo();
                }
            }
        }
    }



    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new VentanaConsultaFactPend();
            }
        });

    }

}


