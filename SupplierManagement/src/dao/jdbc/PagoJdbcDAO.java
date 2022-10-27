package dao.jdbc;

import dao.PagoDAO;
import negocio.factura.Factura;
import negocio.pago.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PagoJdbcDAO extends AccesoJdbcDAO implements PagoDAO {

    public static PagoJdbcDAO singleton = null;


    public static PagoJdbcDAO getInstance() {
        if (singleton == null)
            singleton = new PagoJdbcDAO();
        return singleton;
    }

    private PagoJdbcDAO() {
        super();
        TABLE_NAME = "Pagos";
        PRIMARY_KEY = "id";
        //Si no existe la tabla, la creo al generar el objeto.
        if (!existeTabla(TABLE_NAME))
            this.crearTabla();
    }

    @Override
    protected void crearTabla() {

        String sentencia = "CREATE TABLE " +

                // NOMBRE DE LA TABLA:
                TABLE_NAME.toUpperCase() + " (" +

                // DEFINICIÓN DE CAMPOS:
                "id INT NOT NULL" +
                ", " +
                "fecha VARCHAR" +
                ", " +
                "tipoMedioPago VARCHAR" +
                ", " +
                "cuentaContable VARCHAR" +
                ", " +
                "facturaId INT NOT NULL" +
                ", " +
                "importe DOUBLE" +
                ")";
        super.crearTabla(TABLE_NAME, sentencia);
    }

    @Override
    public int obtenerNuevoID() {
        return super.obtenerNuevoID(TABLE_NAME, PRIMARY_KEY);
    }

    @Override
    public void guardarPago(Pago pago) {

        Connection conexion;
        PreparedStatement preparedStatement;

        try {
            conexion = createConnection();

            preparedStatement = conexion.prepareStatement("INSERT INTO " + TABLE_NAME + " VALUES (?,?,?,?,?,?)");
            int nuevoIdDisponible = obtenerNuevoID();
            preparedStatement.setInt(1, nuevoIdDisponible);
            preparedStatement.setString(2, pago.getFecha());
            preparedStatement.setString(3,pago.getTipoMedioPago());
            preparedStatement.setString(4, pago.getCuentaContable());
            preparedStatement.setDouble(5, pago.getFactura().getId());
            preparedStatement.setDouble(6, pago.getImporte());

            int result = preparedStatement.executeUpdate();

            System.out.println("(!) Se guardó el pago bajo el ID # " + nuevoIdDisponible);
            pago.setId(nuevoIdDisponible);

            //Recursos a cerrar:
            recursosUsados.add(preparedStatement);

        } catch (SQLException ex) {
            System.err.println(" Error detallado [4]: " + ex);
        } finally {
            cerrarConexiones(recursosUsados);
        }
    }

    @Override
    public List<Pago> listarPagos() {

        Connection conexion;
        PreparedStatement preparedStatement;
        ResultSet rs;
        FacturaJdbcDAO facturaJdbcDAO = FacturaJdbcDAO.getInstance();

        Pago pago = null;
        List<Pago> pagoList = new ArrayList<>();

        try {
            conexion = createConnection();

            preparedStatement = conexion.prepareStatement("SELECT * FROM " + TABLE_NAME);
            rs = preparedStatement.executeQuery();

            while (rs.next()){


                int id = rs.getInt("id");
                String fecha = rs.getString("fecha");
                String tipoMedioPago = rs.getString("tipoMedioPago");
                Factura facturaAsociada = facturaJdbcDAO.obtenerFacturaPorId(rs.getInt("facturaId"));
                double importe = rs.getDouble("importe");

                if (tipoMedioPago.equals("Efectivo"))
                    pago = new PagoEfectivo(facturaAsociada);
                else if (tipoMedioPago.equals("Pattern's Bank - Cta.Cte. 2307"))
                    pago = new PagoBancario(facturaAsociada);

                pago.setId(id);
                pago.setFecha(fecha);
                pago.setImporte(importe);
                pago.setPendiente(false);

                pagoList.add(pago);
            }

            //Recursos a cerrar:
            recursosUsados.add(rs);
            recursosUsados.add(preparedStatement);
        }
        catch(SQLException e){
            System.err.println("  Excepción! No es posible listar los pagos...");
            System.err.println(e);
        } finally {
            cerrarConexiones(recursosUsados);
        }

        return pagoList;
    }
}

