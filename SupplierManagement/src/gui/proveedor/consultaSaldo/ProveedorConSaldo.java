package gui.proveedor.consultaSaldo;

import negocio.proveedor.Proveedor;

public class ProveedorConSaldo extends Proveedor {

    double saldo = 0;

    public ProveedorConSaldo(String nombre, long cuit, int categoria, double saldo) {
        super(nombre, cuit, categoria);
        this.saldo = saldo;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }
}
