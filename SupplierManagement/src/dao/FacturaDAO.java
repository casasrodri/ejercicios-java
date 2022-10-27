package dao;

import negocio.factura.*;

import java.util.List;

public interface FacturaDAO {

    void guardarFactura(Factura factura);
    void actualizarFactura(Factura factura);
    Factura obtenerFacturaPorId(int id);
    int obtenerNuevoID();
    List<Factura> listarFacturas();
}
