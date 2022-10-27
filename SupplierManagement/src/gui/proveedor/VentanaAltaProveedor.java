package gui.proveedor;

import dao.ProveedorDAO;
import exceptions.RegistroDuplicadoEx;
import gui.VentanaMain;
import negocio.proveedor.Proveedor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaAltaProveedor extends JFrame {

    JTextField txtDenominacion;
    JTextField txtCUIT;
    JComboBox comboCatFiscal;
    JButton btnGuardar;

    String denominacion = "";
    long cuit = 0;
    int categoria = -1;

    public VentanaAltaProveedor(){
        this.getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints restricciones = new GridBagConstraints();
        final int ANCHO_TEXTFIELDS = 23;


        //Denominación
        JLabel labelDenominacion = new JLabel("Denominación:");
        labelDenominacion.setHorizontalAlignment(SwingConstants.LEFT);

            //Ubicación
        restricciones.gridx = 0;
        restricciones.gridy = 0;
        restricciones.gridwidth = 1;
        restricciones. gridheight = 1;
        restricciones.anchor = GridBagConstraints.WEST;
        this.getContentPane().add(labelDenominacion, restricciones);

        txtDenominacion = new JTextField();
        txtDenominacion.setColumns(ANCHO_TEXTFIELDS);

            //Ubicación
        restricciones.gridx = 1;
        restricciones.anchor = GridBagConstraints.WEST;
        this.getContentPane().add(txtDenominacion, restricciones);


        //CUIT
        JLabel labelCUIT = new JLabel("CUIT:");
        labelCUIT.setHorizontalAlignment(SwingConstants.LEFT);

            //Ubicación
        restricciones.gridx = 0;
        restricciones.gridy = 1;
        restricciones.anchor = GridBagConstraints.WEST;
        this.getContentPane().add(labelCUIT, restricciones);

        txtCUIT = new JTextField();
        txtCUIT.setColumns(ANCHO_TEXTFIELDS);

            //Ubicación
        restricciones.gridx = 1;
        restricciones.anchor = GridBagConstraints.WEST;
        this.getContentPane().add(txtCUIT, restricciones);


        //Categoria Fiscal
        JLabel labelCatFiscal = new JLabel("Categoría Fiscal:  ");
        labelCatFiscal.setHorizontalAlignment(SwingConstants.LEFT);

            //Ubicación
        restricciones.gridx = 0;
        restricciones.gridy = 2;
        restricciones.anchor = GridBagConstraints.WEST;
        this.getContentPane().add(labelCatFiscal, restricciones);

        comboCatFiscal = new JComboBox();
        for (int i = 0 ; i <= Proveedor.CANTIDAD_CATEGORIAS ; i++){
            comboCatFiscal.addItem(Proveedor.consultarCategoria(i));
        }


            //Ubicación
        restricciones.gridx = 1;
        restricciones.anchor = GridBagConstraints.WEST;
        this.getContentPane().add(comboCatFiscal, restricciones);


        JLabel separador = new JLabel(" ");

        //Ubicación
        restricciones.gridx = 1;
        restricciones.gridy = 3;
        restricciones.anchor = GridBagConstraints.SOUTHEAST;
        this.getContentPane().add(separador, restricciones);


        //Boton Guardar
        btnGuardar = new JButton("Guardar");
        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validarInformacion()) {
                    guardarNuevoProveedor();
                };
            }
        });

        //Ubicación
        restricciones.gridx = 1;
        restricciones.gridy = 4;
        restricciones.anchor = GridBagConstraints.SOUTHEAST;
        this.getContentPane().add(btnGuardar, restricciones);





        //Detalles de la ventana
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setTitle("Alta de proveedor");
        this.setResizable(false);
        this.pack();
        this.setSize(400, 150);
        this.setLocationRelativeTo(null); //Para que aparezca al medio de la pantalla
        this.setVisible(true);



    }

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new VentanaAltaProveedor();
            }
        });
    }

    private boolean validarInformacion(){

        String campoDenominacion = txtDenominacion.getText();
        String campoCUIT = txtCUIT.getText();
        String campoCategoria = (String) comboCatFiscal.getSelectedItem();

        //Validaciones Denominacion
        if (campoDenominacion == null || campoDenominacion.equalsIgnoreCase("")){
            JOptionPane.showMessageDialog(null, "Ingrese una denominación para el nuevo proveedor", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        } else {
            denominacion = campoDenominacion;
        }

        //Validaciones CUIT
        if (campoCUIT == null || campoCUIT.equalsIgnoreCase("")){
            JOptionPane.showMessageDialog(null, "Ingrese el número de CUIT para el nuevo proveedor", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        } else {
            try {
                cuit = Long.parseLong(campoCUIT);
            } catch (NumberFormatException e){
                JOptionPane.showMessageDialog(null, "Ingrese un número de CUIT válido!", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        //Validaciones Categoria
        if (campoCategoria == null || campoCategoria.equalsIgnoreCase("")){
            JOptionPane.showMessageDialog(null, "Seleccione una categoría fiscal para el nuevo proveedor", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        } else {
            for (int i = 0 ; i <= Proveedor.CANTIDAD_CATEGORIAS ; i++){
                if (campoCategoria.equalsIgnoreCase(Proveedor.consultarCategoria(i))){
                    categoria = i;
                }
            }
        }

        return true;
    }

    private void guardarNuevoProveedor(){
        ProveedorDAO proveedorDAO = VentanaMain.factory.getProveedorDAO();
        Proveedor nuevo = new Proveedor(denominacion, cuit, categoria);
        try {
            proveedorDAO.guardarProveedor(nuevo);
            JOptionPane.showConfirmDialog(null,"Se ha guardado con éxito el proveedor!",
                    "Guardado" , JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
            this.dispose();

        } catch (RegistroDuplicadoEx e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Registro ya existente", JOptionPane.WARNING_MESSAGE);
        }
    }
}
