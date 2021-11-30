public class Proveedor {



    private String nombre;
    private int id;

    public Proveedor(int ID){
        this.id = ID;

        switch (this.id){
            case 1:
                this.nombre = "Terrabusi";
                break;
            case 2:
                this.nombre = "Nestle";
                break;
            case 3:
                this.nombre = "Arcor";
                break;
            case 4:
                this.nombre = "Granix";
                break;
            case 5:
                this.nombre = "Georgalos";
                break;
        }
    }

    public String getNombre() {
        return nombre;
    }

    public String toString(){
        return String.format("[Proveedor] {ID: %s, Nombre: %s}",
                this.id,
                this.nombre
        );
    }
}
