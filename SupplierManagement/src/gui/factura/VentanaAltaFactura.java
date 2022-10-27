package gui.factura;

import gui.VentanaMain;
import negocio.factura.Factura;
import negocio.factura.FacturaA;
import negocio.factura.FacturaC;
import negocio.factura.FacturaM;
import negocio.proveedor.Proveedor;
import observer.Observador;
import observer.ObservadorContabilidad;
import observer.ObservadorSupervisor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class VentanaAltaFactura extends JFrame {

    JLabel labelID1 = new JLabel("ID: ");
    JLabel labelID = new JLabel();

    JLabel lblNumero = new JLabel("Número de factura:                             ");
    JTextField txtNumero = new JTextField();

    JLabel lblProveedor = new JLabel("Proveedor: ");
    JTextField txtProveedor = new JTextField();
    JButton btnBuscarProv = new JButton("Elegir");

    JLabel lblImporte = new JLabel("Importe: ");
    JLabel lblImporte2 = new JLabel(" (IVA incluido)");
    JTextField txtImporte = new JTextField();

    JLabel lblDatoExtra= new JLabel("");
    JTextField txtDatoExtra = new JTextField();


    JButton btnGuardar = new JButton("Guardar");

    int id;
    String numero;
    double importe = 0;
    long cuit;
    double importeIVA;
    String datoExtra;
    Proveedor proveedorAsociado;
    Factura nuevaFactura;


    public VentanaAltaFactura() {
        this.getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints restricciones = new GridBagConstraints();
        final int ANCHO_TEXTFIELDS = 23;



        //ID
        labelID1.setHorizontalAlignment(SwingConstants.LEFT);

            //Ubicación
        restricciones.gridx = 0;
        restricciones.gridy = 0;
        restricciones.gridwidth = 1;
        restricciones. gridheight = 1;
        restricciones.anchor = GridBagConstraints.WEST;
        this.getContentPane().add(labelID1, restricciones);


        id = VentanaMain.factory.getFacturaDAO().obtenerNuevoID();
        labelID.setText(String.valueOf(id));

            //Ubicación
        restricciones.gridx = 1;
        restricciones.anchor = GridBagConstraints.WEST;
        this.getContentPane().add(labelID, restricciones);


        //Número
        lblNumero.setHorizontalAlignment(SwingConstants.LEFT);

            //Ubicación
        restricciones.gridx = 0;
        restricciones.gridy = 1;
        restricciones.anchor = GridBagConstraints.WEST;
        this.getContentPane().add(lblNumero, restricciones);

        txtNumero.setColumns(ANCHO_TEXTFIELDS);

            //Ubicación
        restricciones.gridx = 1;
        restricciones.anchor = GridBagConstraints.WEST;
        this.getContentPane().add(txtNumero, restricciones);


        //Proveedor
        lblProveedor.setHorizontalAlignment(SwingConstants.LEFT);

            //Ubicación
        restricciones.gridx = 0;
        restricciones.gridy = 2;
        restricciones.anchor = GridBagConstraints.WEST;
        this.getContentPane().add(lblProveedor, restricciones);

        txtProveedor.setColumns(ANCHO_TEXTFIELDS);

            //Ubicación
        restricciones.gridx = 1;
        restricciones.anchor = GridBagConstraints.WEST;
        this.getContentPane().add(txtProveedor, restricciones);


            //Ubicación
        restricciones.gridx = 2;
        restricciones.anchor = GridBagConstraints.WEST;
        this.getContentPane().add(btnBuscarProv, restricciones);


        //Importe

        lblImporte.setHorizontalAlignment(SwingConstants.LEFT);


            //Ubicación
        restricciones.gridx = 0;
        restricciones.gridy = 3;
        restricciones.anchor = GridBagConstraints.WEST;
        this.getContentPane().add(lblImporte, restricciones);


        txtImporte.setColumns(ANCHO_TEXTFIELDS);

        //Ubicación
        restricciones.gridx = 1;
        restricciones.anchor = GridBagConstraints.WEST;
        this.getContentPane().add(txtImporte, restricciones);




        lblImporte2.setHorizontalAlignment(SwingConstants.LEFT);

            //Ubicación
        restricciones.gridx = 2;
        restricciones.anchor = GridBagConstraints.WEST;
        this.getContentPane().add(lblImporte2, restricciones);



        //Dato Extra
        lblDatoExtra.setHorizontalAlignment(SwingConstants.LEFT);

            //Ubicación
        restricciones.gridx = 0;
        restricciones.gridy = 4;
        restricciones.anchor = GridBagConstraints.WEST;
        this.getContentPane().add(lblDatoExtra, restricciones);

        txtDatoExtra.setColumns(ANCHO_TEXTFIELDS);

            //Ubicación
        restricciones.gridx = 1;
        restricciones.anchor = GridBagConstraints.WEST;
        this.getContentPane().add(txtDatoExtra, restricciones);
        txtDatoExtra.setVisible(false);


        //Boton Guardar

            //Ubicación
        restricciones.gridx = 2;
        restricciones.gridy = 5;
        restricciones.anchor = GridBagConstraints.SOUTHEAST;
        this.getContentPane().add(btnGuardar, restricciones);

        //Listeners

        txtProveedor.addActionListener(new CambioCampoProveedor());
        txtProveedor.addKeyListener(new CambioCampoProveedor());

        btnBuscarProv.addActionListener(new ClickBotonBuscaProveedores());
        btnGuardar.addActionListener(new ClickBtnGuardar());


        //Detalles de la ventana
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setTitle("Carga de factura");
        this.setResizable(false);
        this.pack();
        this.setSize(656, 175);
        this.setLocationRelativeTo(null); //Para que aparezca al medio de la pantalla
        this.setVisible(true);

    }


    class ClickBotonBuscaProveedores implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            SelectorBuscaProveedor ventanaBuscador = new SelectorBuscaProveedor();
            ventanaBuscador.addWindowListener(new WindowAdapter() {

                @Override
                public void windowClosed(WindowEvent e) {
                    cuit = ventanaBuscador.getCuitElegido();
                    txtProveedor.setText(String.valueOf(cuit));
                    cambioProveedor();
                }});

        }
    }

    class CambioCampoProveedor implements ActionListener, KeyListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            cambioProveedor();
        }

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {

        }

        @Override
        public void keyReleased(KeyEvent e) {
            cambioProveedor();
        }
    }

    private void cambioProveedor() {

        proveedorAsociado = null;
        cuit = 0;
        nuevaFactura = null;

        try {
            cuit = Long.parseLong(txtProveedor.getText());
            proveedorAsociado = VentanaMain.factory.getProveedorDAO().buscarPorCuit(cuit);
        } catch (Exception e){;}

        if (proveedorAsociado != null) {

            nuevaFactura = Factura.crearFacturaTipo(proveedorAsociado);


            lblDatoExtra.setText(
                    switch (nuevaFactura.getTipo()) {
                    case "A" -> "IVA incluido en la factura: ";
                    case "C" -> "Categoría de monotributo del proveedor: ";
                    case "M" -> "Número de embarque internacional del producto: ";
                        default -> "";
                    });

            txtDatoExtra.setVisible(true);

        }
    }



    class ClickBtnGuardar implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            if (validarInformacion())
                guardarFactura();
        }
    }


    private boolean validarInformacion() {


        String campoNumero = txtNumero.getText();
        String campoProveedor = txtProveedor.getText();
        String campoImporte = txtImporte.getText();
        String campoExtra = txtDatoExtra.getText();

        //Validación Número
        if (campoNumero == null || campoNumero.equalsIgnoreCase("")){
            JOptionPane.showMessageDialog(null, "Ingrese el número de factura!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        } else {
            numero = campoNumero;
        }


        //Validación Proveedor
        try {
            cuit = Long.parseLong(campoProveedor);
            proveedorAsociado = VentanaMain.factory.getProveedorDAO().buscarPorCuit(cuit);
        } catch (Exception e){
            JOptionPane.showMessageDialog(null, "Ingrese el CUIT de un proveedor existente!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        //Validación Importe
        if (campoImporte == null || campoImporte.equalsIgnoreCase("")){
            JOptionPane.showMessageDialog(null, "Ingrese el importe total de la factura!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        } else {
            try {
                importe = Double.parseDouble(campoImporte);
            } catch (NumberFormatException e){
                JOptionPane.showMessageDialog(null, "Ingrese un importe válido!", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        //Validación Dato Extra
        if (proveedorAsociado.getCategoriaImpositiva() == 2) {
            if (campoExtra == null || campoExtra.equalsIgnoreCase("")) {
                JOptionPane.showMessageDialog(null, "Ingrese el importe de IVA de la factura!", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            } else {
                try {
                    importeIVA = Double.parseDouble(campoExtra);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Ingrese un importe de IVA válido!", "Error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
        } else {
            if (campoExtra == null || campoExtra.equalsIgnoreCase("")) {
                JOptionPane.showMessageDialog(null, "Ingrese todos los datos requeridos!", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            } else {
                datoExtra = campoExtra;
            }
        }



        return true;

    }

    private void guardarFactura(){

    //Creamos la factura y asocio datos

        nuevaFactura = Factura.crearFacturaTipo(proveedorAsociado);
        nuevaFactura.setId(id);
        nuevaFactura.setNumero(numero);
        nuevaFactura.setSaldo(importe);

        //Datos adicionales según tipo de factura:
        switch (nuevaFactura.getTipo()) {
            case "A" -> ((FacturaA) nuevaFactura).setIvaDiscriminado(importeIVA);
            case "C" -> ((FacturaC) nuevaFactura).setCategoriaMonotributo(datoExtra.charAt(0));
            case "M" -> ((FacturaM) nuevaFactura).setNumeroEmbarque(datoExtra);
        }

        //Agrego observadores
        Observador obsContabilidad = new ObservadorContabilidad(VentanaMain.factory.getLibroContableDAO());
        Observador obsSupervisor = new ObservadorSupervisor();

        nuevaFactura.agregarObservador(obsSupervisor);
        nuevaFactura.agregarObservador(obsContabilidad);

        //Guardo factura en DB:
        VentanaMain.factory.getFacturaDAO().guardarFactura(nuevaFactura);

        //Muestro proceso ok:
        nuevaFactura.procesoOk();
        JOptionPane.showConfirmDialog(null,"Se ha guardado la factura bajo el ID # " + id,
                "Guardado" , JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
        this.dispose();

        //Notifico observadores
        nuevaFactura.notificarObservadores();
    }











/*
    private boolean validarInformacion(){


        String campoNumero = txtNumero.getText();
        String campoProveedor = txtProveedor.getText();
        String campoImporte = txtImporte.getText();
        String campoExtra = txtDatoExtra.getText();

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

*/






    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new VentanaAltaFactura();
            }
        });
    }
}
