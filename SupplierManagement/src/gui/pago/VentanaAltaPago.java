package gui.pago;

import gui.VentanaMain;
import negocio.factura.Factura;
import negocio.pago.Pago;
import observer.Observador;
import observer.ObservadorContabilidad;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class VentanaAltaPago extends JFrame {

    JLabel labelID1 = new JLabel("ID: ");
    JLabel labelID = new JLabel();

    JLabel lblIDFactura = new JLabel("ID Factura: ");
    JTextField txtIDFactura = new JTextField();
    JButton btnBuscarFact = new JButton("Elegir");

    JLabel lblMedioPago = new JLabel("Medio de pago: ");
    JComboBox comboMedioPago = new JComboBox();

    JLabel lblTipoPago = new JLabel("Pago: ");
    JComboBox comboTipoPago = new JComboBox();

    JLabel lblImporte= new JLabel("Importe: ");
    JTextField txtImporte = new JTextField();


    JButton btnGuardar = new JButton("Guardar");

    int idPago;

    int idFacturaAsociada;
    Factura facturaAsociada;

    String medioPago;
    double importe = 0;


    public VentanaAltaPago() {
        this.getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints restricciones = new GridBagConstraints();
        final int ANCHO_TEXTFIELDS = 18;



        //ID
        labelID1.setHorizontalAlignment(SwingConstants.LEFT);

            //Ubicación
        restricciones.gridx = 0;
        restricciones.gridy = 0;
        restricciones.gridwidth = 1;
        restricciones. gridheight = 1;
        restricciones.anchor = GridBagConstraints.WEST;
        this.getContentPane().add(labelID1, restricciones);


        idPago = VentanaMain.factory.getPagoDAO().obtenerNuevoID();
        labelID.setText(String.valueOf(idPago));

            //Ubicación
        restricciones.gridx = 1;
        restricciones.anchor = GridBagConstraints.WEST;
        this.getContentPane().add(labelID, restricciones);


        //ID Factura
        lblIDFactura.setHorizontalAlignment(SwingConstants.LEFT);

            //Ubicación
        restricciones.gridx = 0;
        restricciones.gridy = 1;
        restricciones.anchor = GridBagConstraints.WEST;
        this.getContentPane().add(lblIDFactura, restricciones);

        txtIDFactura.setColumns(ANCHO_TEXTFIELDS);

            //Ubicación
        restricciones.gridx = 1;
        restricciones.anchor = GridBagConstraints.WEST;
        this.getContentPane().add(txtIDFactura, restricciones);

        //Ubicación
        restricciones.gridx = 2;
        restricciones.anchor = GridBagConstraints.WEST;
        this.getContentPane().add(btnBuscarFact, restricciones);




        //Medio de pago
        lblMedioPago.setHorizontalAlignment(SwingConstants.LEFT);

            //Ubicación
        restricciones.gridx = 0;
        restricciones.gridy = 2;
        restricciones.anchor = GridBagConstraints.WEST;
        this.getContentPane().add(lblMedioPago, restricciones);

        comboMedioPago.addItem("Efectivo");
        comboMedioPago.addItem("Pattern's Bank - Cta.Cte. 2307");


            //Ubicación
        restricciones.gridx = 1;
        restricciones.anchor = GridBagConstraints.WEST;
        this.getContentPane().add(comboMedioPago, restricciones);



        //Tipo Pago

        lblTipoPago.setHorizontalAlignment(SwingConstants.LEFT);

        //Ubicación
        restricciones.gridx = 0;
        restricciones.gridy = 3;
        restricciones.anchor = GridBagConstraints.WEST;
        this.getContentPane().add(lblTipoPago, restricciones);



        comboTipoPago.addItem("Total");
        comboTipoPago.addItem("Parcial");

            //Ubicación
        restricciones.gridx = 1;
        restricciones.anchor = GridBagConstraints.WEST;
        this.getContentPane().add(comboTipoPago, restricciones);


        //Importe

        lblImporte.setHorizontalAlignment(SwingConstants.LEFT);

            //Ubicación
        restricciones.gridx = 0;
        restricciones.gridy = 4;
        restricciones.anchor = GridBagConstraints.WEST;
        this.getContentPane().add(lblImporte, restricciones);
        lblImporte.setVisible(false);


        txtImporte.setColumns(ANCHO_TEXTFIELDS);

        //Ubicación
        restricciones.gridx = 1;
        restricciones.anchor = GridBagConstraints.WEST;
        this.getContentPane().add(txtImporte, restricciones);
        txtImporte.setVisible(false);



        //Boton Guardar

            //Ubicación
        restricciones.gridx = 2;
        restricciones.gridy = 5;
        restricciones.anchor = GridBagConstraints.SOUTHEAST;
        this.getContentPane().add(btnGuardar, restricciones);

        //Listeners
        btnBuscarFact.addActionListener(new ClickBotonBuscaProveedores());
        btnGuardar.addActionListener(new ClickBtnGuardar());
        comboTipoPago.addItemListener(new ComboTipoPagoCambio());


        //Detalles de la ventana
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setTitle("Pagar factura");
        this.setResizable(false);
        this.pack();
        this.setSize(400, 190);
        this.setLocationRelativeTo(null); //Para que aparezca al medio de la pantalla
        this.setVisible(true);

    }


    class ClickBotonBuscaProveedores implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            SelectorBuscaFactura ventanaBuscador = new SelectorBuscaFactura();
            ventanaBuscador.addWindowListener(new WindowAdapter() {

                @Override
                public void windowClosed(WindowEvent e) {
                    idFacturaAsociada = ventanaBuscador.getId();
                    txtIDFactura.setText(String.valueOf(idFacturaAsociada));
                }});

        }
    }

    class ClickBtnGuardar implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            if (validarInformacion())
                guardarFactura();
        }
    }

    class ComboTipoPagoCambio implements ItemListener{

        @Override
        public void itemStateChanged(ItemEvent e) {
            if (comboTipoPago.getSelectedItem().equals("Parcial")){
                lblImporte.setVisible(true);
                txtImporte.setVisible(true);
            } else {
                lblImporte.setVisible(false);
                txtImporte.setVisible(false);
            }
        }
    }

    private boolean validarInformacion() {


        String campoIDFactura = txtIDFactura.getText();
        String campoMedioPago = txtIDFactura.getText();
        String campoImporte = txtImporte.getText();
        String campoTipoPago = (String) comboTipoPago.getSelectedItem();


        //Validación Proveedor
        try {
            int id = Integer.parseInt(campoIDFactura);
            facturaAsociada = VentanaMain.factory.getFacturaDAO().obtenerFacturaPorId(id);

            Observador obsContabilidad = new ObservadorContabilidad(VentanaMain.factory.getLibroContableDAO());
            facturaAsociada.agregarObservador(obsContabilidad);

        } catch (Exception e){
            JOptionPane.showMessageDialog(null, "Ingrese un número de identificación de factura válido!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (facturaAsociada.isAnulada()) {
            JOptionPane.showMessageDialog(null, "La factura elegida se encuentra anulada!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        //Validación Medio de Pago
        if (campoMedioPago == null || campoMedioPago.equalsIgnoreCase("")){
            JOptionPane.showMessageDialog(null, "Ingrese el medio de pago!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        } else {
                medioPago = campoMedioPago;
        }


        //Validación Importe

        if (campoTipoPago.equalsIgnoreCase("Total")) {
            importe = facturaAsociada.getSaldo();
        } else {

            if (campoImporte == null || campoImporte.equalsIgnoreCase("")){
                JOptionPane.showMessageDialog(null, "Ingrese el importe a abonar de la factura!", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            } else {
                try {
                    importe = Double.parseDouble(campoImporte);
                } catch (NumberFormatException e){
                    JOptionPane.showMessageDialog(null, "Ingrese un importe válido!", "Error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }

                if (importe > facturaAsociada.getSaldo()){
                    String mensaje = String.format("Ingrese un importe menor o igual al saldo de la factura: $%,.2f", facturaAsociada.getSaldo());
                    JOptionPane.showMessageDialog(null, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
        }



        return true;

    }

    private void guardarFactura() {

        //Creamos el pago
        Pago nuevoPago = Pago.crearPagoSegunTipo(medioPago == "Efectivo" ? 1 : 2, facturaAsociada);

        //Agrego observador
        Observador obsContabilidad = new ObservadorContabilidad(VentanaMain.factory.getLibroContableDAO());
        nuevoPago.agregarObservador(obsContabilidad);

        //Cargo importe
        nuevoPago.setImporte(importe);

        //Visualizar detalles y luego pedir confirmación:

        StringBuilder sb = new StringBuilder();
        sb.append("ID Factura: " + idFacturaAsociada);
        sb.append("\n");
        sb.append("Proveedor: " + facturaAsociada.getProveedor().getNombre() + " (" +
                "CUIT: " + facturaAsociada.getProveedor().getCuit() + ")");
        sb.append("\n");
        sb.append("Número factura: " + facturaAsociada.getNumero());
        sb.append("\n");
        sb.append("Medio de pago: " + medioPago);
        sb.append("\n");
        sb.append(String.format("Importe: $%,.2f", importe));
        sb.append("\n");
        sb.append("\n");
        sb.append("Desea confirmar el pago?");

        int opcion = JOptionPane.showConfirmDialog(null, sb, "Confirmación pago", JOptionPane.OK_CANCEL_OPTION);

        if (opcion == 0) {

            //Confirmo y guardo el pago:
            nuevoPago.confirmar();
            JOptionPane.showConfirmDialog(null, "Se ha guardado el pago bajo el ID # " + idPago,
                    "Guardado", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);

            VentanaMain.factory.getPagoDAO().guardarPago(nuevoPago);


            //actualizo saldo de la factura en la DB:
            VentanaMain.factory.getFacturaDAO().actualizarFactura(facturaAsociada);

            //Cierro ventana
            dispose();
        }


    }



    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new VentanaAltaPago();
            }
        });
    }
}
