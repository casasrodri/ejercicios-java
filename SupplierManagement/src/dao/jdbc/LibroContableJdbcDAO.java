package dao.jdbc;

import dao.LibroContableDAO;
import negocio.contabilidad.Asiento;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class LibroContableJdbcDAO extends AccesoJdbcDAO implements LibroContableDAO {

    public static LibroContableJdbcDAO singleton = null;


    public static LibroContableJdbcDAO getInstance() {
        if (singleton == null)
            singleton = new LibroContableJdbcDAO();
        return singleton;
    }

    private LibroContableJdbcDAO() {
        super();
        TABLE_NAME = "Contabilidad";
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
                "cuentaContable VARCHAR" +
                ", " +
                "importeDebe DOUBLE" +
                ", " +
                "importeHaber DOUBLE" +
                ", " +
                "observaciones VARCHAR" +
                ")";
        super.crearTabla(TABLE_NAME, sentencia);
    }

    @Override
    public int obtenerNuevoID() {
        return super.obtenerNuevoID(TABLE_NAME, PRIMARY_KEY);
    }

    @Override
    public void postearAsientoContable(int id, String cuentaContable, double debe, double haber, String observaciones) {

        Connection conexion;
        PreparedStatement preparedStatement;

        try {
            conexion = createConnection();

            preparedStatement = conexion.prepareStatement("INSERT INTO " + TABLE_NAME + " VALUES (?,?,?,?,?,?)");

            preparedStatement.setInt(1, id);

            Calendar calendar = Calendar.getInstance();
            String fechaHoy = (calendar.get(Calendar.DATE) + "/" + calendar.get(Calendar.MONTH) + "/" + calendar.get(Calendar.YEAR));
            preparedStatement.setString(2, fechaHoy);

            preparedStatement.setString(3, cuentaContable);

            preparedStatement.setDouble(4, debe);
            preparedStatement.setDouble(5, haber);
            preparedStatement.setString(6, observaciones);

            int result = preparedStatement.executeUpdate();

            //Recursos a cerrar:
            recursosUsados.add(preparedStatement);

        } catch (SQLException ex) {
            System.err.println(" Error detallado [postearAsientoContable]: " + ex);
        } finally {
            cerrarConexiones(recursosUsados);
        }
    }

    @Override
    public List<Asiento> listarAsientosIngresados() {

        Connection conexion;
        PreparedStatement preparedStatement;
        ResultSet rs;

        List<Asiento> listadoAsientos = new ArrayList<>();
        Asiento as = null;

        try {
            conexion = createConnection();

            preparedStatement = conexion.prepareStatement("SELECT * FROM " + TABLE_NAME);
            rs = preparedStatement.executeQuery();

            System.out.println("Listado de asientos ingresados en el módulo contable:");

            while (rs.next()){

                int id = rs.getInt("id");
                String fecha = rs.getString("fecha");
                String cuentaContable = rs.getString("cuentaContable");
                double debe = rs.getDouble("importeDebe");
                double haber = rs.getDouble("importeHaber");
                String observaciones = rs.getString("observaciones");


                as = new Asiento();

                as.setId(id);
                as.setFecha(fecha);
                as.setCuenta(cuentaContable);
                as.setDebe(debe);
                as.setHaber(haber);
                as.setObservaciones(observaciones);

                listadoAsientos.add(as);
            }

            //Recursos a cerrar:
            recursosUsados.add(rs);
            recursosUsados.add(preparedStatement);
        }
        catch(SQLException e){
            System.err.println("  Excepción! No es posible listar los asientos ingresados...");
            System.err.println(e);
        } finally {
            cerrarConexiones(recursosUsados);
        }

        return listadoAsientos;
    }
}
