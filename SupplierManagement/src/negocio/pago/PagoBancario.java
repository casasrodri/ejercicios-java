package negocio.pago;

import negocio.factura.Factura;

public class PagoBancario extends Pago {

    public PagoBancario(Factura facturaAsociada) {
        super("100002 - Pattern's Bank - Cuenta Corriente N° 2307", "Pattern's Bank - Cta.Cte. 2307", facturaAsociada);
    }


}
