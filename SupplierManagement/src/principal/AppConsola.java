package principal;

import dao.*;
import exceptions.*;
import factoryDAO.DAOFactory;
import negocio.contabilidad.Asiento;
import negocio.factura.*;
import negocio.pago.*;
import negocio.proveedor.*;
import observer.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AppConsola {

    //Input
    private final static Scanner teclado = new Scanner(System.in);
    //Conexion con BD (patrón DAO)
    private final static DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.JDBC);
    private final static ProveedorDAO proveedorDAO = factory.getProveedorDAO();
    private final static FacturaDAO facturaDAO = factory.getFacturaDAO();
    private final static PagoDAO pagoDAO = factory.getPagoDAO();
    private final static LibroContableDAO contableDAO = factory.getLibroContableDAO();
    //Observers
    private final static Observador obsContabilidad = new ObservadorContabilidad(contableDAO);
    private final static Observador obsSupervisor = new ObservadorSupervisor();

    public static void main(String[] args){

        boolean salirMenu = false;

        do {
            switch (menuPrincipal()) {
                case 1 -> agregarProveedor();
                case 2 -> consultaSaldoProveedor();
                case 3 -> cargaFacturas();
                case 4 -> consultarFacturasPendientes();
                case 5 -> pagarFacturas();
                case 6 -> anularFacturas();
                case 7 -> listarPagos();
                case 8 -> listarContabilidad();
                default -> System.err.println("Opción pendiente de implementación! :'(");
                case 0 -> {
                    System.out.println("Gracias por usar nuestros servicios! Hasta luego ~ ");
                    salirMenu = true;
                }
            }
        } while (!salirMenu);

    }

    private static int menuPrincipal(){
        
        int opcion = -1;
        boolean flag = false;

        do {
            try {
                System.out.println();
                System.out.println("::::::::::::::::::::::::::::::::::::");
                System.out.println(":: PROGRAMA DE PAGO A PROVEEDORES ::");
                System.out.println("::::::::::::::::::::::::::::::::::::");
                System.out.println();
                System.out.println("Menú de opciones:");
                System.out.println();
                System.out.println(" 1| Carga de proveedores"); //LISTO
                System.out.println(" 2| Consulta de saldo de proveedor"); //PENDIENTE
                System.out.println(" 3| Carga de facturas"); //LISTO
                System.out.println(" 4| Consultar facturas pendientes de pago");
                System.out.println(" 5| Pagar una factura");
                System.out.println(" 6| Anular una factura");
                System.out.println(" 7| Listar pagos por factura");
                System.out.println(" 8| Visualizar libro contable");
                int cantOpciones = 8;
                System.out.println(" 0| Salir");
                System.out.println();
                System.out.print("  Opción: ");

                opcion = teclado.nextInt();

                if (opcion < 0 || opcion > cantOpciones) {
                    throw new Exception();
                } else {
                    flag = true;
                }

            } catch (Exception e) {
                System.err.println("  Por favor, ingresé una opción válida para continuar.");
            } finally {
                teclado.nextLine();
                System.out.println();
            }

        } while (!flag);

        return opcion;
    }

    private static void agregarProveedor(){

        String nombre;
        long cuit = 0;
        int categoria;
        boolean flag = false;

        //Denominación
        System.out.print("Ingrese la denominación del proveedor: ");
        nombre = teclado.nextLine();

        //CUIT
        do {
            try {
                System.out.print("Ingrese el CUIT del proveedor: ");
                cuit = teclado.nextLong();
            } catch (Exception e) {
                System.err.println("  Por favor, ingresé un número de CUIT válido.");
            } finally {
                teclado.nextLine();
                if (cuit > 0) {
                    flag = true;
                }
            }
        } while (!flag);

        //Categoría
        System.out.print("Ingrese la categoría fiscal del proveedor: ");
        categoria = Proveedor.asignarCategorias();
        System.out.println();

        //Guardo en DB:
        Proveedor nuevo = new Proveedor(nombre, cuit, categoria);
        try {
            proveedorDAO.guardarProveedor(nuevo);
        } catch (Exception e) {
            System.err.println(e);
        }

    }

    private static void consultaSaldoProveedor(){

        List<Proveedor> listaProveedores = proveedorDAO.listarProveedores();

        if (listaProveedores.isEmpty()){
            System.out.println();
            System.err.println("  No se han ingresado proveedores hasta ahora!");
        } else {
            for (Proveedor prov: listaProveedores){

                StringBuilder sb = new StringBuilder();
                sb.append(listaProveedores.indexOf(prov) + 1);
                sb.append(". ");
                sb.append(prov);
                sb.append(" - Saldo: $");

                try {
                    double saldo = proveedorDAO.consultaSaldo(prov);
                    sb.append(String.format("%,.2f", saldo));
                } catch (RegistroNoEncontradoEx registroNoEncontradoEx) {
                    sb.append("Error");
                }

                System.out.println(sb);
            }
        }
    }

    private static void cargaFacturas(){

        int id;
        String numero;
        double importe = 0;
        boolean flag;
        Proveedor proveedorAsociado;
        Factura nuevaFactura;

        List<Proveedor> listaProveedores = proveedorDAO.listarProveedores();

        if (listaProveedores.isEmpty()){
            System.out.println();
            System.err.println("  No se han ingresado proveedores hasta ahora!");

        } else {

            //ID
            id = facturaDAO.obtenerNuevoID();

            //Número
            System.out.print("Ingrese el número de la factura: ");
            numero = teclado.nextLine();

            //Proveedor y tipo:
            System.out.println("Seleccione un proveedor de la lista: ");

            int orden = 0;
            int opcion = -1;
            flag = false;

            //  muestro proveedores
            for (Proveedor prov: listaProveedores){
                orden = listaProveedores.indexOf(prov) + 1;
                System.out.println("  " + orden + ". " + prov);
            }

            //  pido se seleccione uno y asigno
            do {
                try {
                    System.out.print("   Elección: ");
                    opcion = teclado.nextInt();

                    if (opcion <= 0 || opcion > orden){
                        throw new Exception();
                    }
                } catch (Exception e) {
                    if (orden == 1) {
                        System.err.println("  Sólo puede seleccionar al proveedor 1.");
                    } else {
                        System.err.println("  Por favor, ingresé un proveedor del 1 al " + orden + ".");
                    }

                } finally {
                    teclado.nextLine();
                    if (opcion > 0 && opcion <= orden) {
                        flag = true;
                        opcion--;
                    }
                }
            } while (!flag);

            //  determino el proveedor a asociar:
            proveedorAsociado = listaProveedores.get(opcion);

            //Saldo inicial (importe):
            flag = false;
            do {
                try {
                    System.out.print("Ingrese el importe total de la factura (IVA incluido): ");
                    importe = teclado.nextDouble();
                } catch (Exception e) {
                    System.err.println("  Por favor, ingresé un importe válido.");
                } finally {
                    teclado.nextLine();
                    if (importe > 0) {
                        flag = true;
                    }
                }
            } while (!flag);

            //Creamos la factura y asocio datos
            nuevaFactura = Factura.crearFacturaTipo(proveedorAsociado);
            nuevaFactura.setId(id);
            nuevaFactura.setNumero(numero);
            nuevaFactura.setSaldo(importe);

            //Datos adicionales según tipo de factura:
            switch (nuevaFactura.getTipo()) {
                case "A" -> {
                    double ivaDiscriminado = 0;
                    flag = false;
                    do {
                        try {
                            System.out.print("Ingrese el importe del IVA incluido en la factura: ");
                            ivaDiscriminado = teclado.nextDouble();
                        } catch (Exception e) {
                            System.err.println("  Por favor, ingresé un importe válido.");
                        } finally {
                            teclado.nextLine();
                            if (ivaDiscriminado > 0) {
                                flag = true;
                            }
                        }
                    } while (!flag);

                    ((FacturaA) nuevaFactura).setIvaDiscriminado(ivaDiscriminado);
                }
                case "C" -> {
                    System.out.print("Ingrese la categoría de monotributo del proveedor: ");
                    ((FacturaC) nuevaFactura).setCategoriaMonotributo(teclado.nextLine().charAt(0));
                }
                case "M" -> {
                    System.out.print("Ingrese el número de embarque internacional del producto: ");
                    ((FacturaM) nuevaFactura).setNumeroEmbarque(teclado.nextLine());
                }
            }

            //Agrego observadores
            nuevaFactura.agregarObservador(obsSupervisor);
            nuevaFactura.agregarObservador(obsContabilidad);

            //Guardo factura en DB:
            facturaDAO.guardarFactura(nuevaFactura);

            //Muestro proceso ok:
            nuevaFactura.procesoOk();

            //Notifico observadores
            nuevaFactura.notificarObservadores();
        }
    }

    public static void consultarFacturasPendientes(){
        int orden;
        double totalAdeudado = 0;
        List<Factura> listaFacturas = facturaDAO.listarFacturas();

        if (listaFacturas.isEmpty()){
            System.out.println();
            System.err.println("  No se han ingresado facturas hasta ahora!");

        } else {

            for (Factura fact: listaFacturas){

                if (!fact.isAnulada() && fact.getSaldo() > 0){
                    orden = listaFacturas.indexOf(fact) + 1;
                    System.out.println("  " + orden + ". " + fact);
                    totalAdeudado += fact.getSaldo();
                }
            }
            System.out.println();
            System.out.println("Total adeudado: $" + String.format("%,.2f", totalAdeudado));
        }

    }

    public static void pagarFacturas(){

        int orden;
        int opcion = -1;
        String opcion2 = "N";
        ArrayList<Integer> opcionesDisponibles = new ArrayList<Integer>();
        boolean flag = false;
        boolean flag2 = false;


        Pago nuevoPago;
        Factura facturaElegida;
        List<Factura> listaFacturas = facturaDAO.listarFacturas();

        if (listaFacturas.isEmpty()){
            System.out.println();
            System.err.println("  No se han ingresado facturas hasta ahora!");

        } else {

            for (Factura fact: listaFacturas) {
                if (!fact.isAnulada() && fact.getSaldo() > 0) {
                    orden = listaFacturas.indexOf(fact) + 1;
                    opcionesDisponibles.add(orden);
                    System.out.println("  " + orden + ". " + fact);
                }
            }

            do {

                try{
                    System.out.print("  Factura que desea pagar: ");
                    opcion = teclado.nextInt();

                    //Compruebo si la opcion ingresada es una de las mostradas en pantalla
                    for (Integer opcionDisp: opcionesDisponibles) {
                        if (opcionDisp == opcion){
                            flag2 = true;
                            break;
                        }
                    }

                    if (!flag2){
                        throw new Exception();
                    } else {
                        flag = true;
                        opcion--;
                    }

                } catch (Exception e) {
                    System.err.println(" Excepción! Elija un de los números mostrados en pantalla!");
                } finally {
                    teclado.nextLine();
                }


            } while (!flag);

            //Asigno factura elegida
            facturaElegida = listaFacturas.get(opcion);

            //Agrego observadores:
            facturaElegida.agregarObservador(obsContabilidad);
            facturaElegida.agregarObservador(obsSupervisor);


            //Creo pago
            opcion = -1;
            flag = false;

            System.out.println("Ingrese el medio de pago:");
            System.out.println(" 1. Efectivo");
            System.out.println(" 2. Pattern's Bank - Cta.Cte. 2307");
            System.out.print(" Medio elegido: ");

            do{
                try {
                    opcion = teclado.nextInt();
                    if (opcion != 1 && opcion != 2){
                        throw new Exception();
                    }
                } catch (Exception e){
                    System.err.println(" Excepción! Ingrese uno de los medios de pago habilitados!");
                } finally {
                    teclado.nextLine();
                    if (opcion == 1 || opcion == 2) {
                        flag = true;
                    }
                }
            } while(!flag);

            nuevoPago = Pago.crearPagoSegunTipo(opcion, facturaElegida);

            //Agrego observadores al pago
            nuevoPago.agregarObservador(obsContabilidad);

            //Determino el importe
            double importe = 0;
            flag = false;

            do {
                try {
                    System.out.print("Ingrese el importe que va a abonar: ");
                    importe = teclado.nextDouble();
                    if (importe > 0 && importe <= facturaElegida.getSaldo()) {
                        flag = true;
                    } else {
                        throw new Exception();
                    }
                } catch (Exception e){
                    System.err.println(" Excepción! Ingrese un importe de hasta $" + String.format("%,.2f", facturaElegida.getSaldo()));
                } finally {
                    teclado.nextLine();
                }
            } while (!flag);

            nuevoPago.setImporte(importe);

            //Visualizar detalles y luego pedir confirmación:
            System.out.println(" Verifique los datos del pago que ha cargado:");
            System.out.println(nuevoPago.visualizarDetalles());

            flag = false;
            do {
                try {
                    System.out.print(" Desea confirmar el pago? (S/N): ");
                    opcion2 = teclado.nextLine();
                    if (opcion2.equalsIgnoreCase("S") || opcion2.equalsIgnoreCase("N")) {
                        flag = true;
                    } else {
                        throw new Exception();
                    }
                } catch (Exception e){
                    System.err.println(" Excepción! Ingrese sólo 'S' para SI o 'N' para NO");
                }
            } while (!flag);

            //En caso de que el usuario acepte, se confirma el pago y se muestra mensaje:
            if (opcion2.equalsIgnoreCase("S")){


                //Confirmo y guardo el pago:
                nuevoPago.confirmar();
                pagoDAO.guardarPago(nuevoPago);


                //actualizo saldo de la factura en la DB:
                facturaDAO.actualizarFactura(facturaElegida);

            } else {
                System.out.println("El pago cargado no ha sido procesado!.");
            }
        }

   }

    public static void anularFacturas(){

        int orden;
        int opcion = -1;
        ArrayList<Integer> opcionesDisponibles = new ArrayList<Integer>();
        boolean flag = false;
        boolean flag2 = false;

        List<Factura> listaFacturas = facturaDAO.listarFacturas();
        List<Pago> listaPagos = pagoDAO.listarPagos();
        Factura facturaAAnular;

        if (listaFacturas.isEmpty()){
            System.out.println();
            System.err.println("  No se han ingresado facturas hasta ahora!");

        } else {

            for (Factura fact: listaFacturas) {

                boolean noPoseePagos = true;
                for (Pago pago: listaPagos) {

                    if (pago.getFactura().getId() == fact.getId()) {
                        noPoseePagos = false;
                        break;
                    }

                }
                boolean noAnulada = !fact.isAnulada();

                if (noAnulada && noPoseePagos) {
                    orden = listaFacturas.indexOf(fact) + 1;
                    opcionesDisponibles.add(orden);
                    System.out.println("  " + orden + ". " + fact);
                }
            }

            System.out.print("  Factura que desea anular: ");

            do {

                try{
                    opcion = teclado.nextInt();

                    //Compruebo si la opcion ingresada es una de las mostradas en pantalla
                    for (Integer opcionDisp: opcionesDisponibles) {
                        if (opcionDisp == opcion){
                            flag2 = true;
                            break;
                        }
                    }

                    if (!flag2){
                        throw new Exception();
                    } else {
                        flag = true;
                        opcion--;
                    }

                } catch (Exception e) {
                    System.err.println(" Excepción! Elija un de los números mostrados en pantalla!");
                } finally {
                    teclado.nextLine();
                }


            } while (!flag);

            facturaAAnular = listaFacturas.get(opcion);
            facturaAAnular.agregarObservador(obsSupervisor);

            facturaAAnular.setAnulada(true);

            //Guardo en la DB:
            facturaDAO.actualizarFactura(facturaAAnular);
        }

    }

    public static void listarPagos(){

        List<Pago> listaPagos = pagoDAO.listarPagos();
        List<Factura> listarFacturas = facturaDAO.listarFacturas();
        List<Integer> facturasConPagos = new ArrayList<>();

        if (listaPagos.isEmpty()){
            System.out.println();
            System.err.println("  No se han ingresado pagos hasta ahora!");

        } else {


            //Recorro todos los pagos para ver a que facturas se asocian:
            for (Pago pago : listaPagos) {
                //Si la factura asociada a este pago no está en el listado, la agrego.
                int idAsociado = pago.getFactura().getId();
                if (!facturasConPagos.contains(idAsociado)) {
                    facturasConPagos.add(idAsociado);
                }
            }

            //Recorro cada factura pagada, la muestro y a continuación, los pagos asociados:
            for (Factura fact : listarFacturas) {

                if (facturasConPagos.contains(fact.getId())){
                    System.out.println(fact);

                    for (Pago pago : listaPagos) {
                        if (pago.getFactura().getId() == fact.getId()) {
                            System.out.println("  - " + pago.visualizarDetalles());
                        }
                    }
                    System.out.println();
                }
            }
        }
    }

    public static void listarContabilidad(){

        List<Asiento> listadoAsientos = contableDAO.listarAsientosIngresados();


        for (Asiento as : listadoAsientos){
            System.out.println("ID: #" + as.getId() + " Fecha: " + as.getFecha() + " Cuenta: " + as.getCuenta()
                    + " Debe: " + String.format("%,.2f", as.getDebe())
                    + " Haber: " + String.format("%,.2f", as.getHaber())
                    + " Observ.: " + as.getObservaciones());

        }


        System.out.println();
    }
}

