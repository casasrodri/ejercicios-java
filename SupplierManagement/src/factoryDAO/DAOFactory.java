package factoryDAO;

import dao.FacturaDAO;
import dao.LibroContableDAO;
import dao.PagoDAO;
import dao.ProveedorDAO;

public abstract class DAOFactory {

    public static final int JDBC = 1;
    //public static final int PROPERTIES_FILE = 2;

    public abstract ProveedorDAO getProveedorDAO();
    public abstract FacturaDAO getFacturaDAO();
    public abstract PagoDAO getPagoDAO();
    public abstract LibroContableDAO getLibroContableDAO();

    public static DAOFactory getDAOFactory(int factory) {
        return switch (factory) {
            case JDBC -> new JdbcDAOFactory();
            default -> null;
        };
    }

}
