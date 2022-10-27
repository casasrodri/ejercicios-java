package negocio.proveedor;

import java.util.Scanner;

public class Proveedor {
    public static final int CANTIDAD_CATEGORIAS = 4;
    private final String nombre;
    private final long cuit;
    private final int categoriaImpositiva;


    public Proveedor(String nombre, long cuit, int categoria){
        this.nombre = nombre;
        this.cuit = cuit;
        this.categoriaImpositiva = categoria;
    }

    public String getNombre() {
        return nombre;
    }

    public long getCuit() {
        return cuit;
    }

    public int getCategoriaImpositiva(){
        return categoriaImpositiva;
    }


    @Override
    public String toString(){
        return ("Denominación: " + nombre + " (CUIT: " + cuit + ") " + "Categoría Impositiva: " + consultarCategoria(categoriaImpositiva));
    }


    public static int asignarCategorias(){

        Scanner teclado = new Scanner(System.in);
        boolean flag = false;
        int opcion = 0;

        System.out.println();
        System.out.println("Seleccionar una categoría de proveedor:");
        System.out.println(" 1. " + consultarCategoria(1));
        System.out.println(" 2. " + consultarCategoria(2));
        System.out.println(" 3. " + consultarCategoria(3));
        System.out.println(" 4. " + consultarCategoria(4));
        System.out.print("   Elección: ");

        do{
            try {
                opcion = teclado.nextInt();
                if (opcion < 1 || opcion > 4){
                    throw new Exception();
                }
            } catch (Exception e){
                System.err.println(" Excepción, ingrese un número válido! ");
            } finally {
                teclado.nextLine();
                if (opcion >= 1 && opcion <= 4){
                    flag = true;
                }

            }
        } while (!flag);

        return opcion;
    }

    public static String consultarCategoria(int categoria){

        return switch (categoria) {
            case 1 -> "Monotributista";
            case 2 -> "Responsable inscripto";
            case 3 -> "Exento";
            case 4 -> "Importador de bienes (RG AFIP 2390/89)";
            default -> "";
        };
    }
}
