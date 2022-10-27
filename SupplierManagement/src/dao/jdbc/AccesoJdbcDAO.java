package dao.jdbc;

import java.sql.*;
import java.util.ArrayList;

public abstract class AccesoJdbcDAO {

    protected static final String DRIVER_SQLITE = "org.sqlite.JDBC";
    protected static final String DBURL_SQLITE = "jdbc:sqlite:BD-SupplierManagement.sqlite";
    protected static Connection conexion = null;
    protected static ArrayList<AutoCloseable> recursosUsados;
    protected String TABLE_NAME;
    protected String PRIMARY_KEY;

    AccesoJdbcDAO(){
        //Lista de recursos, para luego cerrarlos.
        recursosUsados = new ArrayList<>();
    }

    public static Connection createConnection() {

        if (conexion == null){
            try {
                Class.forName(DRIVER_SQLITE);
                conexion = DriverManager.getConnection(DBURL_SQLITE);
            }
            catch (ClassNotFoundException ex) {
                System.err.println("  Excepción! No se pudo obtener la clase para efectuar la conexión!");
                System.err.println("Error detallado [AccesoJdbcDAO_1]: " + ex);
            }
            catch (SQLException ex) {
                System.err.println("  Excepción! Problema en la conexión SQL...");
                System.err.println("Error detallado [AccesoJdbcDAO_2]: " + ex);
            }
        }

        return conexion;
    }

    public boolean existeTabla(String nombreTabla){

        Connection conexion;
        Statement statement;
        ResultSet resultSet;

        try {
            conexion = createConnection();
            statement = conexion.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM " + nombreTabla);

            //Recursos a cerrar:
            recursosUsados.add(resultSet);
            recursosUsados.add(statement);
        }
        catch(SQLException e){
            return false;
        } finally {
            cerrarConexiones(recursosUsados);
        }

        return true;
    }

    //Defino métodos abstractos para que todas las clases lo implementen
    protected abstract void crearTabla();
    public abstract int obtenerNuevoID();

    //Defino el método sobrecargado para que lo reutilicen las sólo subclases
    protected void crearTabla(String nombreTabla, String sentenciaCreacion){

        Connection conexion;
        Statement statement;

        try {
            conexion = createConnection();
            statement = conexion.createStatement();

            int result = statement.executeUpdate(sentenciaCreacion);

            System.out.println("Tabla '" + nombreTabla + "' creada correctamente!");

            //Recursos a cerrar:
            recursosUsados.add(statement);

        } catch (SQLException ex) {
            System.err.println("  Excepción! Ocurrió un problema con la base de datos... ");
            System.err.println(ex);
        } finally {
            cerrarConexiones(recursosUsados);
        }
    }

    protected int obtenerNuevoID(String nombreTabla, String primaryKey){

        Connection conexion;
        Statement statement;
        ResultSet resultSet;

        try {
            conexion = createConnection();
            statement = conexion.createStatement();

            resultSet = statement.executeQuery("SELECT max(" + primaryKey + ") as 'Max' FROM " + nombreTabla);


            while (resultSet.next()){
                return (resultSet.getInt("Max") + 1);
            }

            //Recursos a cerrar:
            recursosUsados.add(resultSet);
            recursosUsados.add(statement);

        } catch (SQLException ex) {
            System.err.println("  Excepción! Ocurrió un problema al obtener el próximo ID disponible... ");
            System.err.println(ex);
        } finally {
            cerrarConexiones(recursosUsados);
        }
        return 0;
    }

    protected static void cerrarConexiones(ArrayList<AutoCloseable> recursosUsados) {

        for (Object recurso : recursosUsados){

            try {
                if (recurso != null){
                    ((AutoCloseable) recurso).close();
                    //System.out.println("  ::Se cerró la conexión: " + recurso.toString());
                }

            } catch (Exception e){
                System.err.println("  Excepción! No es posible liberar un recurso: " + recurso.toString());
                System.err.println(e);
            }
        }
    }
}
