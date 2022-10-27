package dao;

import exceptions.*;
import negocio.proveedor.*;

import java.util.List;

public interface ProveedorDAO {


    Proveedor buscarPorCuit(long cuit) throws RegistroNoEncontradoEx;
    void guardarProveedor(Proveedor proveedor) throws RegistroDuplicadoEx;
    double consultaSaldo(Proveedor proveedor) throws RegistroNoEncontradoEx;
    List<Proveedor> listarProveedores();


}
