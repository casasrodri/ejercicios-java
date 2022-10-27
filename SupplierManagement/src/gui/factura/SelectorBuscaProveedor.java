package gui.factura;

import gui.VentanaMain;
import negocio.proveedor.Proveedor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class SelectorBuscaProveedor extends JFrame {

    JList listaElegirProveedor = new JList();;
    JScrollPane scroll = new JScrollPane();

    DefaultListModel modelo = new DefaultListModel();
    List<Proveedor> listadoProveedores = new ArrayList<>();

    long cuitElegido = 0;

    public SelectorBuscaProveedor(){
        
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

        setSize(450,400);
        setTitle("Elegir proveedor...");
        setVisible(true);
        setResizable(false);
        this.setLocationRelativeTo(null); //Para que aparezca al medio de la pantalla

    }


    private void cargarModelo(String buscado){

        //Cargo lista de proveedores
        listadoProveedores = VentanaMain.factory.getProveedorDAO().listarProveedores();

        //Limpio modelo anterior
        modelo.clear();

        //Si no se buscó nada, muestro todos los proveedores
        if (buscado.equalsIgnoreCase("")) {
            for (Proveedor prov : listadoProveedores){
                String linea = "[" + prov.getCuit() + "] " + prov.getNombre();
                modelo.addElement(linea);
            }
        } else { //En caso de que si, muestro los que coinciden:

            for (Proveedor prov : listadoProveedores){

                String nombreProv = prov.getNombre().toLowerCase() + prov.getCuit();
                buscado = buscado.toLowerCase();
                if (nombreProv.contains(buscado)){
                    String linea = "[" + prov.getCuit() + "] " + prov.getNombre();
                    modelo.addElement(linea);
                }

            }
        }

        listaElegirProveedor.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaElegirProveedor.setModel(modelo);

        scroll.setViewportView(listaElegirProveedor);

    }



    class botonAccionado implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {

            String elegido = (String) listaElegirProveedor.getSelectedValue();
            String[] partes = elegido.split("]");

            elegido = partes[0].substring(1);
            cuitElegido = Long.parseLong(elegido);
            dispose();
        }
    }


    public long getCuitElegido(){
        return cuitElegido;
    }






    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                SelectorBuscaProveedor ventanita = new SelectorBuscaProveedor();
                //ventanita.elegirProveedor();

                ventanita.addWindowListener(new WindowAdapter() {

                    @Override
                    public void windowClosed(WindowEvent e) {
                        long cuit = ventanita.getCuitElegido();
                        System.out.println("El cuit es: " + cuit);
                    }
                });

            }
        });
    }


}
