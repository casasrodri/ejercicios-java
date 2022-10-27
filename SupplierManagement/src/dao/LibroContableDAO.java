package dao;

import negocio.contabilidad.Asiento;

import java.util.List;

public interface LibroContableDAO {

    void postearAsientoContable(int id, String cuentaContable, double debe, double haber, String observaciones);
    List<Asiento> listarAsientosIngresados();
    int obtenerNuevoID();
}
