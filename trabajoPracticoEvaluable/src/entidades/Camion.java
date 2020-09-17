package entidades;

public class Camion extends Vehiculo{
	
	private int capacidadCarga;
	
	
	
	//Constructor
	public Camion(String marca, String color, String patente, int capacidadCarga) {
		super(marca,color,patente);
		this.capacidadCarga = capacidadCarga;
	}
	

	public int getCapacidadCarga() {
		return capacidadCarga;
	}

	public void setCapacidadCarga(int capacidadCarga) {
		
		if (capacidadCarga > 0)
			this.capacidadCarga = capacidadCarga;
		else;
			this.capacidadCarga = 0;
	}
	
	@Override
	public String toString() {
		return String.format("Vehículo: Camión | %s | Capacidad de carga: %s kg", super.toString(),getCapacidadCarga());
	}

}