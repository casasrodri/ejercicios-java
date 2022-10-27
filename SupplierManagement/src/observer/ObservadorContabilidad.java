package observer;

import dao.LibroContableDAO;
import gui.contabilidad.VentanaMuestraAsiento;
import negocio.factura.Factura;
import negocio.pago.Pago;

import java.util.Calendar;

public class ObservadorContabilidad implements Observador{

    public final LibroContableDAO contableDAO;

    public ObservadorContabilidad(LibroContableDAO contableDAO){
        this.contableDAO = contableDAO;
    }

    @Override
    public void actualizar(Sujeto sujeto) {
        if (sujeto instanceof Factura) {
            Factura factRecibida = ((Factura)sujeto);
            if (factRecibida.isAnulada()) {
                registrarAnulacion(factRecibida);
            } else {
                registrarGasto(factRecibida);
            }
        } else if (sujeto instanceof Pago){
            Pago pagoRecibido = ((Pago)sujeto);
            registrarPago(pagoRecibido);
        }
    }

    private void registrarPago(Pago pago){

        System.out.println();
        System.out.println(" ---> Asiento contable registrado:");
        System.out.println("    - Cta. 210002 - Proveedores varios: " + pago.getImporte());
        System.out.println("    - Cta. " + pago.getCuentaContable() + ": (" + pago.getImporte() + ")");
        String tipoPago = pago.getFactura().getSaldo()>0?"Pago parcial":"Pago total";
        String obs = tipoPago + " factura n° " + pago.getFactura().getNumero() + " emitida por " + pago.getFactura().getProveedor().getNombre();
        System.out.println( "    Obs: " + obs);
        System.out.println();

        int id = contableDAO.obtenerNuevoID();
        contableDAO.postearAsientoContable(id,"210002 - Proveedores varios",pago.getImporte(),0,obs);
        contableDAO.postearAsientoContable(id,pago.getCuentaContable(), 0, pago.getImporte() * -1, obs);
        System.out.println("(!) Se contabilizó la operación bajo el ID # " + id);

        //Mostrar GUI:

        Calendar calendar = Calendar.getInstance();
        String fechaHoy = (calendar.get(Calendar.DATE) + "/" + calendar.get(Calendar.MONTH) + "/" + calendar.get(Calendar.YEAR));

        VentanaMuestraAsiento v = new VentanaMuestraAsiento();
        v.agregarDatosModelo(id, fechaHoy, "210002 - Proveedores varios",pago.getImporte(),0,obs);
        v.agregarDatosModelo(id, fechaHoy, pago.getCuentaContable(),0,pago.getImporte() * -1, obs);
        v.mostrarVentana();
    }

    private void registrarGasto(Factura factura){
        System.out.println();
        System.out.println(" ---> Asiento contable registrado:");
        System.out.println("    - Cta. 620001 - Gastos a imputar: " + factura.getSaldo());
        System.out.println("    - Cta. 210002 - Proveedores varios: (" + factura.getSaldo() + ")");
        String obs = "Registro factura n° " + factura.getNumero() + " emitida por " + factura.getProveedor().getNombre();
        System.out.println("    Obs: " + obs);
        System.out.println();

        int id = contableDAO.obtenerNuevoID();
        contableDAO.postearAsientoContable(id, "620001 - Gastos a imputar",factura.getSaldo(),0,obs);
        contableDAO.postearAsientoContable(id, "210002 - Proveedores varios",0,factura.getSaldo() * -1, obs);
        System.out.println("(!) Se contabilizó la operación bajo el ID # " + id);

        //Mostrar GUI:

        Calendar calendar = Calendar.getInstance();
        String fechaHoy = (calendar.get(Calendar.DATE) + "/" + calendar.get(Calendar.MONTH) + "/" + calendar.get(Calendar.YEAR));

        VentanaMuestraAsiento v = new VentanaMuestraAsiento();
        v.agregarDatosModelo(id, fechaHoy, "620001 - Gastos a imputar",factura.getSaldo(),0,obs);
        v.agregarDatosModelo(id, fechaHoy, "210002 - Proveedores varios",0,factura.getSaldo() * -1, obs);
        v.mostrarVentana();
    }

    private void registrarAnulacion(Factura factura){
        System.out.println();
        System.out.println(" ---> Asiento contable registrado:");
        System.out.println("    - Cta. 210002 - Proveedores varios: " + factura.getSaldo());
        System.out.println("    - Cta. 620001 - Gastos a imputar: (" + factura.getSaldo() + ")");
        String obs = "Anulación factura n° " + factura.getNumero() + " emitida por " + factura.getProveedor().getNombre();
        System.out.println("    Obs: " + obs);
        System.out.println();

        int id = contableDAO.obtenerNuevoID();
        contableDAO.postearAsientoContable(id, "210002 - Proveedores varios",factura.getSaldo(), 0, obs);
        contableDAO.postearAsientoContable(id, "620001 - Gastos a imputar", 0, factura.getSaldo() * -1, obs);
        System.out.println("(!) Se contabilizó la operación bajo el ID # "+ id);

        //Mostrar GUI:

        Calendar calendar = Calendar.getInstance();
        String fechaHoy = (calendar.get(Calendar.DATE) + "/" + calendar.get(Calendar.MONTH) + "/" + calendar.get(Calendar.YEAR));

        VentanaMuestraAsiento v = new VentanaMuestraAsiento();
        v.agregarDatosModelo(id, fechaHoy, "210002 - Proveedores varios",factura.getSaldo(),0,obs);
        v.agregarDatosModelo(id, fechaHoy, "620001 - Gastos a imputar",0,factura.getSaldo() * -1, obs);
        v.mostrarVentana();
    }


}
