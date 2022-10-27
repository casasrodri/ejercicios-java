package gui.proveedor;

import dao.ProveedorDAO;
import exceptions.RegistroNoEncontradoEx;
import gui.VentanaMain;
import gui.proveedor.consultaSaldo.*;
import negocio.proveedor.Proveedor;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.util.List;

public class VentanaConsultaSaldoProv extends JFrame {
    ModeloTabla modelo = new ModeloTabla();
    JScrollPane scroll = new JScrollPane();
    JTable tabla = new JTable (modelo);
    JLabel labelTitulo = new JLabel("Saldo por proveedor:");
    double saldoTotal;
    JLabel labelSaldoTotal = new JLabel();

    public VentanaConsultaSaldoProv(){
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

        tabla.getColumnModel().getColumn(0).setMinWidth(150);

        tabla.getColumnModel().getColumn(1).setMinWidth(100);
        tabla.getColumnModel().getColumn(1).setMaxWidth(100);
        tabla.getColumnModel().getColumn(1).setResizable(false);
        tabla.getColumnModel().getColumn(1).setCellRenderer(textoCentrado);

        tabla.getColumnModel().getColumn(2).setMinWidth(250);
        tabla.getColumnModel().getColumn(2).setMaxWidth(250);
        tabla.getColumnModel().getColumn(2).setResizable(false);
        tabla.getColumnModel().getColumn(2).setCellRenderer(textoCentrado);

        tabla.getColumnModel().getColumn(3).setMinWidth(100);
        tabla.getColumnModel().getColumn(3).setMaxWidth(100);
        tabla.getColumnModel().getColumn(3).setResizable(false);

        //Agrego scroll dentro del panel
        panelPrincipal.add(scroll);

        //Agrego el saldo total
        labelSaldoTotal.setText(String.format("Saldo total adeudado: $%,.2f", saldoTotal));
        panelPrincipal.add(labelSaldoTotal);

        //Agrego panelPrincipal al frame
        add(panelPrincipal);

        //Datos de la ventana:
        pack();
        setTitle("Consulta de saldos de proveedores");
        setSize(800,300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void agregarDatosModelo(){

        ProveedorDAO proveedorDAO = VentanaMain.factory.getProveedorDAO();
        List<Proveedor> listaProveedores = proveedorDAO.listarProveedores();
        double saldo;
        List<Proveedor> listaProveedoresConSaldo = proveedorDAO.listarProveedores();

        if (listaProveedores.isEmpty()){
            JOptionPane.showMessageDialog(null, "No se han ingresado proveedores hasta ahora!",
                    "Sin datos", JOptionPane.WARNING_MESSAGE);
        } else {

            for (Proveedor prov: listaProveedores){

                try {
                    saldo = proveedorDAO.consultaSaldo(prov);
                } catch (RegistroNoEncontradoEx registroNoEncontradoEx) {
                    saldo = 0;
                }

                ProveedorConSaldo provConSaldo = new ProveedorConSaldo(prov.getNombre(),
                        prov.getCuit(),
                        prov.getCategoriaImpositiva(),
                        saldo);

                modelo.agregarProveedor(provConSaldo);
                saldoTotal += saldo;
            }
        }
    }



    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new VentanaConsultaSaldoProv();
            }
        });
    }
}
