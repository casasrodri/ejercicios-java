package trabajoPracticoEvaluable;

public class Cliente {

	private String nombre;
	private int dni;
	
	public Cliente(String nombre, int dni) {
		this.nombre = nombre;
		setDni(dni);
	}
	
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public int getDni() {
		return dni;
	}
	public void setDni(int dni) {
		if (dni > 0)
			this.dni = dni;
		else
			this.dni = 0;
	}
}
