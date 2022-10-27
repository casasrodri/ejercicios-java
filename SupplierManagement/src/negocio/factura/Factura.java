package negocio.factura;

import observer.*;
import negocio.proveedor.*;

import java.util.ArrayList;
import java.util.List;

public abstract class Factura implements Sujeto{

    private int id;
    private String numero;
    private String tipo;
    private Proveedor proveedor;
    private double saldo;
    private boolean anulada;
    private final List<Observador> observadorList;

    protected Factura(){
        this.anulada = false;
        this.observadorList = new ArrayList<Observador>();
    }


    //Hago override de los métodos de la interfaz Sujeto directamente en la superclass:
    @Override
    public void agregarObservador(Observador obs) {
        this.observadorList.add(obs);
    }

    @Override
    public void notificarObservadores() {
        for (Observador obs: observadorList){
            obs.actualizar(this);
        }
    }

    //Getters
    public String getNumero() {
        return numero;
    }

    public String getTipo() {
        return tipo;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public double getSaldo() {
        return saldo;
    }

    public boolean isAnulada() {
        return anulada;
    }

    public int getId(){
        return this.id;
    }


    //Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public abstract String getDatosExtras();

    public void setAnulada(boolean anulada) {
        this.anulada = anulada;
        notificarObservadores();
    }

    public void reducirSaldo(double importe) {

        double nuevoSaldo;
        nuevoSaldo = saldo - importe;

        //Redondeo a 2 decimales
        nuevoSaldo = nuevoSaldo * 100;
        nuevoSaldo = Math.floor(nuevoSaldo);
        nuevoSaldo = nuevoSaldo / 100;

        this.saldo = nuevoSaldo;
    }

    //Métodos comunes a todos las clases:

    public abstract String toString();

    public void procesoOk(){

        System.out.println("________________________________________________");
        System.out.println("PROCESO OK!");
        System.out.println(" Se agregó la factura con los siguientes datos:");
        System.out.println("    " + "Proveedor: " + proveedor.getNombre() + " (CUIT: " + proveedor.getCuit() + ")");
        System.out.println("    " + "Tipo: " + tipo);
        System.out.println("    " + "Número: " + numero);
        System.out.println("    " + "Importe: $" + String.format("%,.2f", saldo));
        System.out.println("    " + getDatosExtras());
        System.out.println("________________________________________________");
        System.out.println();

    }

    public static Factura crearFacturaTipo(Proveedor proveedor){
        Factura nuevaFactura;

        int tipo = proveedor.getCategoriaImpositiva();
        switch (tipo) {
            default -> {
                nuevaFactura = new FacturaA();
                nuevaFactura.setTipo("A");
            }
            case 1, 3 -> {
                nuevaFactura = new FacturaC();
                nuevaFactura.setTipo("C");
            }
            case 4 -> {
                nuevaFactura = new FacturaM();
                nuevaFactura.setTipo("M");
            }
        }
        nuevaFactura.setProveedor(proveedor);
        return nuevaFactura;
    }

}
