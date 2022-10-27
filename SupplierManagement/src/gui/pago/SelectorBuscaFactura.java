package gui.pago;

import gui.VentanaMain;
import negocio.factura.Factura;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class SelectorBuscaFactura extends JFrame {

    JList listaElegirFactura = new JList();;
    JScrollPane scroll = new JScrollPane();

    DefaultListModel modelo = new DefaultListModel();
    List<Factura> listadoFacturas = new ArrayList<>();

    int id = 0;

    public SelectorBuscaFactura(){
        
        //Defino layout de toda la ventana
        getContentPane().setLayout(new BorderLayout());

        //Cargo el modelo inicialmente
        cargarModelo("");


        JTextField txtBuscado = new JTextField();
        txtBuscado.setColumns(30);
        txtBuscado.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                cargarModelo(txtBuscado.getText());

            }
        });




        //Contenedor para texto a buscar
        JPanel contTextoBuscar = new JPanel();
        contTextoBuscar.setLayout(new FlowLayout());
        contTextoBuscar.add(new JLabel("Buscar:"));
        contTextoBuscar.add(txtBuscado);

        //Contenedor y botón para devolver dato
        JPanel contBotonSeleccionar = new JPanel();
        JButton btnElegirSeleccionado = new JButton("Elegir");
        contBotonSeleccionar.setLayout(new BorderLayout());
        contBotonSeleccionar.add(btnElegirSeleccionado, BorderLayout.EAST);

        //Determino nombre elegido
        btnElegirSeleccionado.addActionListener(new botonAccionado());



        //Agrego los contenedores al frame

        add(contTextoBuscar,BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(contBotonSeleccionar, BorderLayout.SOUTH);

        setSize(700,400);
        setTitle("Elegir factura...");
        setVisible(true);
        setResizable(false);
        this.setLocationRelativeTo(null); //Para que aparezca al medio de la pantalla

    }


    private void cargarModelo(String buscado){

        //Cargo lista de facturas
        listadoFacturas = VentanaMain.factory.getFacturaDAO().listarFacturas();

        //Limpio modelo anterior
        modelo.clear();

        //Si no se buscó nada, muestro todos las facturas
        if (buscado.equalsIgnoreCase("")) {
            for (Factura fact : listadoFacturas){

                String linea = fact.getId() + " | " + fact.getProveedor().getNombre() + " (" +
                        fact.getProveedor().getCuit() + ")" +
                        " | Tipo: " + fact.getTipo() +
                        " | Número: " + fact.getNumero() +
                        " | Saldo: $";
                linea = linea + String.format("%,.2f", fact.getSaldo());

                if (!fact.isAnulada() && fact.getSaldo() > 0){
                    modelo.addElement(linea);
                }

            }
        } else { //En caso de que si, muestro los que coinciden:

            for (Factura fact : listadoFacturas){

                String linea = fact.getId() + " | " + fact.getProveedor().getNombre() + " (" +
                        fact.getProveedor().getCuit() + ")" +
                        " | Tipo: " + fact.getTipo() +
                        " | Número: " + fact.getNumero() +
                        " | Saldo: $";
                linea = linea + String.format("%,.2f", fact.getSaldo());


                String lineaFactura = linea.toLowerCase();

                buscado = buscado.toLowerCase();
                if (lineaFactura.contains(buscado) && !fact.isAnulada() && fact.getSaldo() > 0){
                    modelo.addElement(linea);
                }

            }
        }

        listaElegirFactura.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaElegirFactura.setModel(modelo);

        scroll.setViewportView(listaElegirFactura);

    }



    class botonAccionado implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {

            String elegido = (String) listaElegirFactura.getSelectedValue();
            String[] partes = elegido.split(" ");

            id = Integer.parseInt(partes[0]);
            dispose();
        }
    }


    public int getId(){
        return id;
    }






    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                SelectorBuscaFactura ventanita = new SelectorBuscaFactura();
                //ventanita.elegirProveedor();

                ventanita.addWindowListener(new WindowAdapter() {

                    @Override
                    public void windowClosed(WindowEvent e) {
                        int idSeleccionado = ventanita.getId();
                        System.out.println("El cuit es: " + idSeleccionado);
                    }
                });

            }
        });
    }


}
