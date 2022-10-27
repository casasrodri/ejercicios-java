package negocio.pago;

import negocio.factura.Factura;
import observer.Observador;
import observer.Sujeto;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public abstract class Pago implements Sujeto {

    private int id;
    private String fecha;
    private final String cuentaContable;
    private final String tipoMedioPago;
    private final Factura factura;
    private double importe;
    private boolean pendiente;
    private final List<Observador> observadorList;


    public Pago(String cuentaContable, String tipoMedioPago, Factura factura) {

        Calendar calendar = Calendar.getInstance();
        this.fecha = (calendar.get(Calendar.DATE) + "/" + calendar.get(Calendar.MONTH) + "/" + calendar.get(Calendar.YEAR));

        this.cuentaContable = cuentaContable;
        this.tipoMedioPago = tipoMedioPago;
        this.factura = factura;

        this.pendiente = true;
        this.observadorList = new ArrayList<Observador>();
    }


    //Overrides de interface - podría haberla hecho en cada subclase, pero prefería acá.
    @Override
    public void agregarObservador(Observador obs) {
        observadorList.add(obs);
    }

    @Override
    public void notificarObservadores() {
        for (Observador obs : observadorList){
            obs.actualizar(this);
        }

    }


    //Getters

    public double getImporte() {
        return importe;
    }

    public Factura getFactura() {
        return factura;
    }

    public String getCuentaContable() {
        return cuentaContable;
    }

    public int getId(){
        return this.id;
    }

    public String getFecha() {
        return fecha;
    }

    public String getTipoMedioPago() {
        return tipoMedioPago;
    }

    //Setters
    public void setImporte(double importe) {
        this.importe = importe;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public void setPendiente(boolean pendiente) {
        this.pendiente = pendiente;
    }

    //Métodos propios de la clase Pago
    public String visualizarDetalles(){

        return ("Fecha: " + fecha
                + " | Medio de Pago: " + tipoMedioPago
                + " | Importe: $" + String.format("%,.2f", importe));

    }

    public void confirmar(){
        this.pendiente = false;
        this.factura.reducirSaldo(importe);

        procesoOk();
        notificarObservadores();
    }

    public void procesoOk(){

        System.out.println("________________________________________________");
        System.out.println("PROCESO OK!");
        System.out.println(" Se procesó el pago cargado con los siguientes datos:");
        System.out.println("    " + "Fecha: " + fecha);
        System.out.println("    " + "Proveedor: " + factura.getProveedor().getNombre() + " (CUIT: " + factura.getProveedor().getCuit() + ")");
        System.out.println("    " + "Medio de Pago: " + tipoMedioPago);
        System.out.println("    " + "Importe: $" + String.format("%,.2f", importe));
        System.out.println("    " + "N° Factura: " + factura.getNumero());
        System.out.println("    " + "Estado: " + (pendiente?"Pendiente":"Realizado"));
        System.out.println("________________________________________________");
        System.out.println();

    }


    public static Pago crearPagoSegunTipo(int opcion, Factura facturaAsociada){
        return switch (opcion) {
            default -> new PagoEfectivo(facturaAsociada);
            case 2 -> new PagoBancario(facturaAsociada);
        };
    }


}
