package negocio.factura;

public class FacturaA extends Factura{

    private double ivaDiscriminado;

    public void setIvaDiscriminado(double ivaDiscriminado) {
        this.ivaDiscriminado = ivaDiscriminado;
    }

    public double getIvaDiscriminado() {
            return ivaDiscriminado;
    }

    //Override super métodos
    @Override
    public String toString() {

        return ("Proveedor: " + super.getProveedor().getNombre()
                + " | CUIT: " + super.getProveedor().getCuit()
                + " | Tipo: " + super.getTipo()
                + " | Número: " + super.getNumero()
                + " | IVA: " + ivaDiscriminado
                + " | Saldo: $" + String.format("%,.2f", super.getSaldo())
        );
    }

    @Override
    public String getDatosExtras() {
        return ("IVA discriminado: " + ivaDiscriminado);
    }



}
