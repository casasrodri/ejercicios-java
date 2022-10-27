package factoryDAO;

import dao.FacturaDAO;
import dao.LibroContableDAO;
import dao.PagoDAO;
import dao.ProveedorDAO;
import dao.jdbc.FacturaJdbcDAO;
import dao.jdbc.LibroContableJdbcDAO;
import dao.jdbc.PagoJdbcDAO;
import dao.jdbc.ProveedorJdbcDAO;

public class JdbcDAOFactory extends DAOFactory{
    @Override
    public ProveedorDAO getProveedorDAO() {
        return ProveedorJdbcDAO.getInstance();
    }

    @Override
    public FacturaDAO getFacturaDAO() {
        return FacturaJdbcDAO.getInstance();
    }

    @Override
    public PagoDAO getPagoDAO() {
        return PagoJdbcDAO.getInstance();
    }

    @Override
    public LibroContableDAO getLibroContableDAO() {
        return LibroContableJdbcDAO.getInstance();
    }
}
