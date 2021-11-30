import java.util.GregorianCalendar;

public class Lote {

    private final int CANTIDAD_MIN = 500;
    private final int CANTIDAD_MAX = 1000;
    private final int DEFECTUOSOS_MAX = 200;
    private final int ANO_INICIO = 2015;
    private final int ANO_FIN = 2021;
    private final int PROVEEDORES_MAX = 5;
    private final int PRODUCTOS_MAX = 6;

    private int id;
    private String fecha;
    private long cantidad;
    private long cantidadError;
    private int producto;
    private Proveedor proveedor;
    private double porcentajeRechazo;
    private String estado;

    public Lote(int ID){
        this.id = ID;

        //Datos aleatorios
        fecha = fechaRandom(ANO_INICIO, ANO_FIN);
        cantidad = aleatorioEntre(CANTIDAD_MIN, CANTIDAD_MAX);
        cantidadError = aleatorioEntre(0,DEFECTUOSOS_MAX);
        producto = aleatorioEntre(1,PRODUCTOS_MAX);
        proveedor = new Proveedor(aleatorioEntre(1,PROVEEDORES_MAX));
    }

    public double evaluarRendimiento(){
        porcentajeRechazo = ((double) cantidadError) / ((double) cantidad);
        estado = porcentajeRechazo < 0.2 ? "Aceptado" : "Rechazado";
        return porcentajeRechazo;
    }

    public double getPorcentajeRechazo() {
        return porcentajeRechazo;
    }

    public String toString(){
        return String.format("[Lote] {ID: %s, Fecha: %s, Cantidad: %d, CantidadError: %d, Producto: %d, Proveedor: %s, %%Rechazo: %.2f%%, Estado: %s}",
                            this.id,
                            this.fecha,
                            this.cantidad,
                            this.cantidadError,
                            this.producto,
                            this.proveedor.getNombre(),
                            this.porcentajeRechazo * 100,
                            this.estado
        );
    }




    private int aleatorioEntre(int start, int end) {
        return start + (int)Math.round(Math.random() * (end - start));
    }

    private String fechaRandom(int añoMin, int añoMax){
        GregorianCalendar calendario = new GregorianCalendar();

        //Seteamos año
        calendario.set(calendario.YEAR, aleatorioEntre(añoMin, añoMax));
        //Seteamos un día random del año
        int dayOfYear = aleatorioEntre(1, calendario.getActualMaximum(calendario.DAY_OF_YEAR));
        calendario.set(calendario.DAY_OF_YEAR, dayOfYear);
        //Devolvemos
        int año = calendario.get(calendario.YEAR);
        int mes = (calendario.get(calendario.MONTH) + 1);
        int dia = calendario.get(calendario.DAY_OF_MONTH);
        return dia + "/" + mes + "/" + año;

    }


}
