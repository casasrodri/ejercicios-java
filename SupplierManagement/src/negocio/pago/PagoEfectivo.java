package negocio.pago;

import negocio.factura.Factura;

public class PagoEfectivo extends Pago {

    public PagoEfectivo(Factura facturaAsociada) {
        super("100001 - Efectivo en caja", "Efectivo", facturaAsociada);
    }

}
