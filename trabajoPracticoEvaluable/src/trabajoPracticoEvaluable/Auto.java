package trabajoPracticoEvaluable;

public class Auto extends Vehiculo{
	
	private int cantidadAsientos;

	
	
	//Constructor
	public Auto(String marca, String color, String patente, int cantidadAsientos) {
		super(marca,color,patente);
		this.cantidadAsientos = cantidadAsientos;
	}
	
	
	public int getCantidadAsientos() {
		return cantidadAsientos;
	}

	public void setCantidadAsientos(int cantidadAsientos) {
		
		if (cantidadAsientos > 0)
			this.cantidadAsientos = cantidadAsientos;
		else;
			this.cantidadAsientos = 0;
	}
	
	@Override
	public String toString() {
		return String.format("Vehículo: Auto | %s | Cantidad de Asientos: %s", super.toString(),getCantidadAsientos());
	}

}