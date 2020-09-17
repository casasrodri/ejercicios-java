package entidades;

public abstract class Vehiculo {

	private String marca, color, patente;
	private Cliente alquiladoPor;
	

	//Constructor
	public Vehiculo(String marca, String color, String patente) {
		this.marca = marca;
		this.color = color;
		this.patente = patente;
	}
	
	
	
	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getPatente() {
		return patente;
	}

	public void setPatente(String patente) {
		this.patente = patente;
	}
	
	@Override
	public String toString() {
		
		return String.format("Marca: %s | Color: %s | Patente: %s", getMarca(), getColor(), getPatente());
		
	}

	public Cliente getAlquiladoPor() {
		return alquiladoPor;
	}

	public void setAlquiladoPor(Cliente alquiladoPor) {
		this.alquiladoPor = alquiladoPor;
	}
}
