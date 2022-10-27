package dao.jdbc;

import dao.FacturaDAO;
import exceptions.RegistroNoEncontradoEx;
import negocio.factura.*;
import negocio.proveedor.Proveedor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FacturaJdbcDAO extends AccesoJdbcDAO implements FacturaDAO {

    public static FacturaJdbcDAO singleton = null;


    public static FacturaJdbcDAO getInstance(){
        if (singleton == null)
            singleton = new FacturaJdbcDAO();
        return singleton;
    }

    private FacturaJdbcDAO(){
        super();
        TABLE_NAME = "Facturas";
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
                "numero VARCHAR(40)" +
                ", " +
                "tipo VARCHAR(1)" +
                ", " +
                "proveedorId INT NOT NULL" +
                ", " +
                "saldo DOUBLE" +
                ", " +
                "anulada INT NOT NULL" +
                ", " +
                "ivaDiscriminado DOUBLE" +
                ", " +
                "categoriaMonotributo VARCHAR(1)" +
                ", " +
                "numeroEmbarque VARCHAR(40)" +
                ")";
        super.crearTabla(TABLE_NAME, sentencia);
    }

    @Override
    public int obtenerNuevoID() {
        return super.obtenerNuevoID(TABLE_NAME,PRIMARY_KEY);
    }


    public void guardarFactura(Factura factura){

        Connection conexion;
        PreparedStatement preparedStatement;

        try {
            conexion = createConnection();

            preparedStatement = conexion.prepareStatement("INSERT INTO " + TABLE_NAME + " VALUES (?,?,?,?,?,?,?,?,?)");
            int nuevoIdDisponible = obtenerNuevoID();
            preparedStatement.setInt(1, nuevoIdDisponible);
            preparedStatement.setString(2, factura.getNumero());
            preparedStatement.setString(3,factura.getTipo());
            preparedStatement.setLong(4, factura.getProveedor().getCuit());
            preparedStatement.setDouble(5, factura.getSaldo());
            int anulado = factura.isAnulada()?1:0;
            preparedStatement.setInt(6, anulado);
            //Datos extras:

            switch (factura.getTipo()) {
                case "A" -> {
                    preparedStatement.setDouble(7, ((FacturaA) factura).getIvaDiscriminado());
                    preparedStatement.setString(8, "-");
                    preparedStatement.setString(9, "-");
                }
                case "C" -> {
                    preparedStatement.setDouble(7, 0);
                    String categoriaMonot = String.valueOf(((FacturaC) factura).getCategoriaMonotributo());
                    preparedStatement.setString(8, categoriaMonot);
                    preparedStatement.setString(9, "-");
                }
                case "M" -> {
                    preparedStatement.setDouble(7, 0);
                    preparedStatement.setString(8, "-");
                    preparedStatement.setString(9, ((FacturaM) factura).getNumeroEmbarque());
                }
            }

            int result = preparedStatement.executeUpdate();

            System.out.println("(!) Se guardó la factura bajo el ID # " + nuevoIdDisponible);

            //Recursos a cerrar:
            recursosUsados.add(preparedStatement);

        } catch (SQLException ex) {
            System.err.println(" Error detallado [guardarFactura]: " + ex);
        } finally {
            cerrarConexiones(recursosUsados);
        }
    }

    @Override
    public void actualizarFactura(Factura factura) {

        Connection conexion;
        PreparedStatement preparedStatement;

        try {
            conexion = createConnection();

            preparedStatement = conexion.prepareStatement("UPDATE " + TABLE_NAME + " SET saldo = ?, anulada = ? "
                                                            + "WHERE id = " + factura.getId());

            preparedStatement.setDouble(1, factura.getSaldo());
            int anulado = factura.isAnulada()?1:0;
            preparedStatement.setInt(2, anulado);

            int result = preparedStatement.executeUpdate();

            System.out.println("(!) Se actualizó la factura registrada bajo el ID # " + factura.getId());

            //Recursos a cerrar:
            recursosUsados.add(preparedStatement);

        } catch (SQLException ex) {
            System.err.println(" Error detallado [actualizarFactura]: " + ex);
        } finally {
            cerrarConexiones(recursosUsados);
        }
















    }

    @Override
    public Factura obtenerFacturaPorId(int idBuscado) {

        Connection conexion;
        PreparedStatement preparedStatement;
        ResultSet rs;

        Factura factura = null;

        try {
            conexion = createConnection();

            preparedStatement = conexion.prepareStatement("SELECT * FROM " + TABLE_NAME + " WHERE " + PRIMARY_KEY + " = ?");
            preparedStatement.setInt(1, idBuscado);
            rs = preparedStatement.executeQuery();

            while (rs.next()){

                int id = rs.getInt("id");
                String numero = rs.getString("numero");
                String tipo = rs.getString("tipo");
                double importe = rs.getDouble("saldo");

                long codProveedor = rs.getLong("proveedorId");
                Proveedor proveedor = ProveedorJdbcDAO.getInstance().buscarPorCuit(codProveedor);

                int anulada = rs.getInt("anulada");

                factura = Factura.crearFacturaTipo(proveedor);
                factura.setId(id);
                factura.setNumero(numero);
                factura.setSaldo(importe);
                factura.setAnulada(anulada == 1);

                switch (tipo) {
                    case "A" -> {
                        double ivaDiscriminado = rs.getDouble("ivaDiscriminado");
                        ((FacturaA) factura).setIvaDiscriminado(ivaDiscriminado);
                    }
                    case "C" -> {
                        String categoriaMonotributo = rs.getString("categoriaMonotributo");
                        ((FacturaC) factura).setCategoriaMonotributo(categoriaMonotributo.charAt(0));
                    }
                    case "M" -> {
                        String numeroEmbarque = rs.getString("numeroEmbarque");
                        ((FacturaM) factura).setNumeroEmbarque(numeroEmbarque);
                    }
                }
            }

            //Recursos a cerrar:
            recursosUsados.add(rs);
            recursosUsados.add(preparedStatement);
        }
        catch(SQLException | RegistroNoEncontradoEx e){
            System.err.println("  Excepción! No es posible listar las facturas...");
            System.err.println(e);
        } finally {
            cerrarConexiones(recursosUsados);
        }

        return factura;
    }


    @Override
    public List<Factura> listarFacturas() {

        Connection conexion;
        PreparedStatement preparedStatement;
        ResultSet rs;

        Factura factura;
        List<Factura> facturaList = new ArrayList<>();

        try {
            conexion = createConnection();

            preparedStatement = conexion.prepareStatement("SELECT * FROM " + TABLE_NAME);
            rs = preparedStatement.executeQuery();

            while (rs.next()){

                int id = rs.getInt("id");
                String numero = rs.getString("numero");
                String tipo = rs.getString("tipo");
                double importe = rs.getDouble("saldo");

                long codProveedor = rs.getLong("proveedorId");
                Proveedor proveedor = ProveedorJdbcDAO.getInstance().buscarPorCuit(codProveedor);

                int anulada = rs.getInt("anulada");

                factura = Factura.crearFacturaTipo(proveedor);
                factura.setId(id);
                factura.setNumero(numero);
                factura.setSaldo(importe);
                factura.setAnulada(anulada == 1);

                switch (tipo) {
                    case "A" -> {
                        double ivaDiscriminado = rs.getDouble("ivaDiscriminado");
                        ((FacturaA) factura).setIvaDiscriminado(ivaDiscriminado);
                    }
                    case "C" -> {
                        String categoriaMonotributo = rs.getString("categoriaMonotributo");
                        ((FacturaC) factura).setCategoriaMonotributo(categoriaMonotributo.charAt(0));
                    }
                    case "M" -> {
                        String numeroEmbarque = rs.getString("numeroEmbarque");
                        ((FacturaM) factura).setNumeroEmbarque(numeroEmbarque);
                    }
                }

                facturaList.add(factura);
            }

            //Recursos a cerrar:
            recursosUsados.add(rs);
            recursosUsados.add(preparedStatement);
        }
        catch(SQLException | RegistroNoEncontradoEx e){
            System.err.println("  Excepción! No es posible listar las facturas...");
            System.err.println(e);
        } finally {
            cerrarConexiones(recursosUsados);
        }

        return facturaList;

    }
}
