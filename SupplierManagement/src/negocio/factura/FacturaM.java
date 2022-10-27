package negocio.factura;

public class FacturaM extends Factura{

    private String numeroEmbarque;

    public String getNumeroEmbarque() {
        return numeroEmbarque;
    }

    public void setNumeroEmbarque(String numeroEmbarque) {
        this.numeroEmbarque = numeroEmbarque;
    }

    //Override super métodos
    @Override
    public String toString() {

        return ("Proveedor: " + super.getProveedor().getNombre()
                + " | CUIT: " + super.getProveedor().getCuit()
                + " | Tipo: " + super.getTipo()
                + " | Número: " + super.getNumero()
                + " | N° Embarque: " + numeroEmbarque
                + " | Saldo: $" + String.format("%,.2f", super.getSaldo())
        );
    }

    @Override
    public String getDatosExtras() {
        return ("Número de embarque: " + numeroEmbarque);
    }
}
