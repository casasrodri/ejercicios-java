package dao;

import negocio.pago.Pago;

import java.util.List;

public interface PagoDAO {

    int obtenerNuevoID();
    void guardarPago(Pago pago);
    List<Pago> listarPagos();

}
