package gui.contabilidad;

import gui.VentanaMain;
import negocio.contabilidad.Asiento;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.util.List;


public class VentanaConsultaAsientos extends JFrame {

    ModeloAsiento modelo = new ModeloAsiento();
    JScrollPane scroll = new JScrollPane();
    JTable tabla = new JTable (modelo);
    JLabel labelTitulo = new JLabel("Listado de asientos ingresados:");

    public VentanaConsultaAsientos(){

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

        tabla.getColumnModel().getColumn(2).setMinWidth(300);
        tabla.getColumnModel().getColumn(2).setMaxWidth(300);
        tabla.getColumnModel().getColumn(2).setResizable(false);
        tabla.getColumnModel().getColumn(2).setCellRenderer(textoIzquierda);

        tabla.getColumnModel().getColumn(3).setMinWidth(100);
        tabla.getColumnModel().getColumn(3).setMaxWidth(100);
        tabla.getColumnModel().getColumn(3).setResizable(false);

        tabla.getColumnModel().getColumn(4).setMinWidth(100);
        tabla.getColumnModel().getColumn(4).setMaxWidth(100);
        tabla.getColumnModel().getColumn(4).setResizable(false);



        //Agrego scroll dentro del panel
        panelPrincipal.add(scroll);


        //Agrego panelPrincipal al frame
        add(panelPrincipal);

        //Datos de la ventana:
        pack();
        setTitle("Consulta de asientos ingresados");
        setSize(1200,500);
        setResizable(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);

    }



    public void agregarDatosModelo(){

        List<Asiento> listadoAsientos = VentanaMain.factory.getLibroContableDAO().listarAsientosIngresados();

        for (Asiento as : listadoAsientos)
            modelo.agregarLineaAsiento(as);
    }




    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                VentanaConsultaAsientos v = new VentanaConsultaAsientos();
            }
        });

    }
}

