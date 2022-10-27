package dao.jdbc;

import dao.ProveedorDAO;
import exceptions.RegistroDuplicadoEx;
import exceptions.RegistroNoEncontradoEx;
import negocio.factura.Factura;
import negocio.proveedor.Proveedor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProveedorJdbcDAO extends AccesoJdbcDAO implements ProveedorDAO {

    public static ProveedorJdbcDAO singleton= null;

    public static ProveedorJdbcDAO getInstance(){
        if (singleton == null)
            singleton = new ProveedorJdbcDAO();
        return singleton;
    }

    private ProveedorJdbcDAO(){
        super();
        TABLE_NAME = "Proveedores";
        PRIMARY_KEY = "cuit";
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
                "cuit BIGINT NOT NULL" +
                ", " +
                "nombre VARCHAR(60)" +
                ", " +
                "categoriaImpositiva INT" +
                ")";
        super.crearTabla(TABLE_NAME, sentencia);
    }

    @Override
    public int obtenerNuevoID() {
        return 0; //No utilizado para el caso de proveedores que tienen ID propio (externo).
    }

    public void guardarProveedor(Proveedor proveedor) throws RegistroDuplicadoEx {

        try {
            Proveedor encontrado = buscarPorCuit(proveedor.getCuit());
            if (encontrado.getCuit() == proveedor.getCuit()){

                throw new RegistroDuplicadoEx("En la BD ya existe el proveedor " +
                        encontrado.getNombre() + ", que posee el mismo CUIT que " + proveedor.getNombre()
                        + " (" + encontrado.getCuit() + ").\nNo se ha creado un nuevo registro.");
            }
        } catch (RegistroNoEncontradoEx e) {
            //Dado que no se encuentra, está habilitado a ser registrado en la DB.
        }


        Connection conexion;
        PreparedStatement preparedStatement;

        try {
            conexion = createConnection();

            preparedStatement = conexion.prepareStatement("INSERT INTO " + TABLE_NAME + " VALUES (?,?,?)");
            preparedStatement.setLong(1, proveedor.getCuit());
            preparedStatement.setString(2, proveedor.getNombre());
            preparedStatement.setInt(3, proveedor.getCategoriaImpositiva());

            int result = preparedStatement.executeUpdate();

            System.out.println("(!) Se guardó el proveedor " +  proveedor.getNombre() + " bajo el CUIT # " + proveedor.getCuit());

            //Recursos a cerrar:
            recursosUsados.add(preparedStatement);

        } catch (SQLException ex) {
            System.err.println(" Error detallado [4]: " + ex);
        } finally {
            cerrarConexiones(recursosUsados);
        }
    }

    @Override
    public double consultaSaldo(Proveedor proveedor) {

        double saldoProveedor = 0;

        List<Factura> listaFacturas = FacturaJdbcDAO.getInstance().listarFacturas();

        for (Factura fact: listaFacturas) {
            if (!fact.isAnulada() && fact.getProveedor().getCuit() == proveedor.getCuit()) {
                saldoProveedor += fact.getSaldo();
            }
        }
        return saldoProveedor;
    }

    @Override
    public List<Proveedor> listarProveedores() {

        Connection conexion;
        PreparedStatement preparedStatement;
        ResultSet rs;

        Proveedor proveedor;
        List<Proveedor> proveedorList = new ArrayList<>();

        try {
            conexion = createConnection();

            preparedStatement = conexion.prepareStatement("SELECT * FROM " + TABLE_NAME);
            rs = preparedStatement.executeQuery();

            while (rs.next()){
                proveedor = new Proveedor(rs.getString("nombre"),
                        rs.getLong("cuit"),
                        rs.getInt("categoriaImpositiva"));

                proveedorList.add(proveedor);
            }

            //Recursos a cerrar:
            recursosUsados.add(rs);
            recursosUsados.add(preparedStatement);
        }
        catch(SQLException e){
            System.err.println("  Excepción! No es posible listar los proveedores...");
            System.err.println(e);
        } finally {
            cerrarConexiones(recursosUsados);
        }

        return proveedorList;

    }

    @Override
    public Proveedor buscarPorCuit(long cuit) throws RegistroNoEncontradoEx {

        Proveedor proveedor = null;
        Connection conexion;
        PreparedStatement preparedStatement;
        ResultSet rs;

        try {
            conexion = createConnection();

            preparedStatement = conexion.prepareStatement("SELECT * FROM " + TABLE_NAME + " WHERE cuit = ?");
            preparedStatement.setLong(1, cuit);
            rs = preparedStatement.executeQuery();

            while (rs.next()){
                proveedor = new Proveedor(rs.getString("nombre"),
                                        rs.getLong("cuit"),
                                        rs.getInt("categoriaImpositiva"));
            }

            //Recursos a cerrar:
            recursosUsados.add(rs);
            recursosUsados.add(preparedStatement);
        }
        catch(SQLException e){
            System.err.println("  Excepción! No es posible crear el objeto proveedor...");
            System.err.println(e);
        } finally {
            cerrarConexiones(recursosUsados);
        }

        if (proveedor == null)
            throw new RegistroNoEncontradoEx("Proveedor no encontrado!");

        return proveedor;
    }

}
