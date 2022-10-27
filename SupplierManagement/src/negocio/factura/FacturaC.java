package negocio.factura;

public class FacturaC extends Factura{

    private char categoriaMonotributo;

    public char getCategoriaMonotributo() {
        return categoriaMonotributo;
    }

    public void setCategoriaMonotributo(char categoriaMonotributo) {
        this.categoriaMonotributo = categoriaMonotributo;
    }

    //Override super métodos
    @Override
    public String toString() {

        return ("Proveedor: " + super.getProveedor().getNombre()
                + " | CUIT: " + super.getProveedor().getCuit()
                + " | Tipo: " + super.getTipo()
                + " | Número: " + super.getNumero()
                + " | Cat. Monotr.: " + categoriaMonotributo
                + " | Saldo: $" + String.format("%,.2f", super.getSaldo())
        );
    }

    @Override
    public String getDatosExtras() {
        return ("Categoría Monotributo: " + categoriaMonotributo);
    }
}
