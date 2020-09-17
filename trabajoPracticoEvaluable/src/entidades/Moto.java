package entidades;

public class Moto extends Vehiculo{
	
	private int cilindrada;
	
	
	//Constructor
		public Moto(String marca, String color, String patente, int cilindrada) {
			super(marca,color,patente);
			this.cilindrada = cilindrada;
		}

	public int getCilindrada() {
		return cilindrada;
	}

	public void setCilindrada(int cilindrada) {
		
		if (cilindrada > 0)
			this.cilindrada = cilindrada;
		else;
			this.cilindrada = 0;
	}
	
	@Override
	public String toString() {
		return String.format("Vehículo: Moto | %s | Cilindrada: %s cc", super.toString(),getCilindrada());
	}

}
