package gui.contabilidad;

import negocio.contabilidad.Asiento;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

public class VentanaMuestraAsiento extends JFrame {

    ModeloAsiento modelo = new ModeloAsiento();
    JScrollPane scroll = new JScrollPane();
    JTable tabla = new JTable (modelo);
    JLabel labelTitulo = new JLabel("Se registró el siguiente asiento:");



    public void mostrarVentana(){

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

        tabla.getColumnModel().getColumn(0).setMinWidth(50);
        tabla.getColumnModel().getColumn(0).setMaxWidth(50);
        tabla.getColumnModel().getColumn(0).setResizable(false);
        tabla.getColumnModel().getColumn(0).setCellRenderer(textoCentrado);

        tabla.getColumnModel().getColumn(1).setMinWidth(100);
        tabla.getColumnModel().getColumn(1).setMaxWidth(100);
        tabla.getColumnModel().getColumn(1).setResizable(false);
        tabla.getColumnModel().getColumn(1).setCellRenderer(textoCentrado);

        tabla.getColumnModel().getColumn(2).setMinWidth(200);
        tabla.getColumnModel().getColumn(2).setMaxWidth(200);
        tabla.getColumnModel().getColumn(2).setResizable(false);
        tabla.getColumnModel().getColumn(2).setCellRenderer(textoCentrado);

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
        setTitle("Asiento registrado!");
        setSize(800,120);
        setResizable(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }


    public void agregarDatosModelo(int id, String fecha, String cuenta, double debe, double haber, String obs){

        Asiento as = new Asiento();
        as.setId(id);
        as.setFecha(fecha);
        as.setCuenta(cuenta);
        as.setDebe(debe);
        as.setHaber(haber);
        as.setObservaciones(obs);

        modelo.agregarLineaAsiento(as);
    }

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                VentanaMuestraAsiento v = new VentanaMuestraAsiento();
                v.agregarDatosModelo(56, "5656", "2210002 - Proveedores varios",0,89,"Ayuda econo");
                v.agregarDatosModelo(56, "5656", "2210002 - Proveedores varios",-89,0,"Ayuda econo");

                v.mostrarVentana();
            }
        });

    }
}
