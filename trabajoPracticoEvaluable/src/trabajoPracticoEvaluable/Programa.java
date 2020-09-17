package trabajoPracticoEvaluable;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Programa {
	
	static Vehiculo[] parqueVehicular = new Vehiculo[100];
	static Cliente[] baseClientes = new Cliente[50];
	static Scanner teclado = new Scanner(System.in);

	public static void main(String[] args) {
		
		int opcion = 999;
		int auto = 0, cliente = 0;
		
		//Datos alta cliente
		String nombre;
		int dni;
		
		//Datos alta cliente
		String marca, color, patente;
		int tipo;
		
		
		//Creo a modo de ejemplo 2 veh�culos de cada tipo:
		parqueVehicular[0] = new Moto("Yamaha", "Azul", "ASEF 2398", 250);
		parqueVehicular[1] = new Moto("Zanella", "Amarilla", "ZXOP 5669", 500);
		parqueVehicular[2] = new Auto("Volkswagen Up!", "Gris noche", "AE 569 FP", 5);
		parqueVehicular[3] = new Auto("Ford Ka", "Verde", "AC 897 RE", 4);
		parqueVehicular[4] = new Camion("Mercedez Benz", "Blanco", "AD 892 FJ", 2000);
		parqueVehicular[5] = new Camion("Ivecco", "Negro", "AA 126 UJ", 6000);
		
		//Creo a modo de ejemplo 4 clientes:
		baseClientes[0] = new Cliente("Rodrigo Casas", 36356168);
		baseClientes[1] = new Cliente("Paula Guzman", 23888920);
		baseClientes[2] = new Cliente("Antonio Perez", 6992548);
		baseClientes[3] = new Cliente("Maria L�pez", 29782003);
		
		
		// Alquilo un veh�culo
		parqueVehicular[2].setAlquiladoPor(baseClientes[0]);
		
		
		
		//Men� de opciones:
		
		do {
			
			opcion = mostrarMenu();
			
			switch (opcion) {
			
			case 1:
				
				for (int i = 0 ; i < parqueVehicular.length ; i++) {
					
					if (parqueVehicular[i] instanceof Moto || parqueVehicular[i] instanceof Auto || parqueVehicular[i] instanceof Camion) {
						if (parqueVehicular[i].getAlquiladoPor() == null) {
							
							System.out.printf("%s | %s%n", i, parqueVehicular[i].toString());
							
						}
					}
					
				}
				break;
			case 2:
				
				for (int i = 0 ; i < parqueVehicular.length ; i++) {
					
					if (parqueVehicular[i] instanceof Moto || parqueVehicular[i] instanceof Auto || parqueVehicular[i] instanceof Camion) {
						if (parqueVehicular[i].getAlquiladoPor() != null) {
							
							System.out.printf("%s | %s | Alquilado por: %s (DNI: %s)%n", i, 
									parqueVehicular[i].toString(),
									parqueVehicular[i].getAlquiladoPor().getNombre(),
									parqueVehicular[i].getAlquiladoPor().getDni());
							
						}
					}
					
				}
				break;
			case 3:
				
				
				System.out.println("Elija el cliente que est� por alquilar un veh�culo:");
				for (int i = 0 ; i < baseClientes.length ; i++) {
					
					if (baseClientes[i] instanceof Cliente) {
						System.out.printf("%s | Nombre: %s | DNI: %s%n", i, 
								baseClientes[i].getNombre(),
								baseClientes[i].getDni()
								);
						
					}
				}
				
				cliente = teclado.nextInt(); //Usuario ingresa valor real de la posici�n del array. Pend handle exceptions
				teclado.nextLine();
				
				
				
				System.out.println("Elija el veh�culo a alquilar:");
				for (int i = 0 ; i < parqueVehicular.length ; i++) {
					
					if (parqueVehicular[i] instanceof Moto || parqueVehicular[i] instanceof Auto || parqueVehicular[i] instanceof Camion) {
						if (parqueVehicular[i].getAlquiladoPor() == null) {
							
							System.out.printf("%s | %s%n", i, parqueVehicular[i].toString());
							
						}
					}
				}
				
				auto = teclado.nextInt(); //Usuario ingresa valor real de la posici�n del array. Pend handle exceptions
				teclado.nextLine();
				
				//Alquilo el veh�culo:
				parqueVehicular[auto].setAlquiladoPor(baseClientes[cliente]); 
				
				break;
			case 4:
				
				System.out.println("Elija el cliente que est� por devolver un veh�culo:");
				
				
				for (int i = 0 ; i < parqueVehicular.length ; i++) {
					
					if (parqueVehicular[i] instanceof Moto || parqueVehicular[i] instanceof Auto || parqueVehicular[i] instanceof Camion) {
						if (parqueVehicular[i].getAlquiladoPor() != null) {
							
							System.out.printf("%s | %s (DNI: %s)%n", i, 
									parqueVehicular[i].getAlquiladoPor().getNombre(),
									parqueVehicular[i].getAlquiladoPor().getDni());
						}
					}
					
				}
				
				cliente = teclado.nextInt(); //Usuario ingresa valor real de la posici�n del array. Pend handle exceptions
				teclado.nextLine();
				
				
				
				
				System.out.println("Elija el veh�culo que est� por devolver:");
				
				
				for (int i = 0 ; i < parqueVehicular.length ; i++) {
					
					if (parqueVehicular[i] instanceof Moto || parqueVehicular[i] instanceof Auto || parqueVehicular[i] instanceof Camion) {
						if (parqueVehicular[i].getAlquiladoPor() != null) {
							
							System.out.printf("%s | %s%n", i, 
									parqueVehicular[i].toString());
							
						}
					}
					
				}
				
				auto = teclado.nextInt(); //Usuario ingresa valor real de la posici�n del array. Pend handle exceptions
				teclado.nextLine();
				
				//Alquilo el veh�culo:
				parqueVehicular[auto].setAlquiladoPor(null); 
				
				
				break;
			case 5:

				
				System.out.print("Ingrese el nombre del nuevo cliente: ");
				nombre = teclado.nextLine();
				
				System.out.print("Ingrese el DNI del nuevo cliente: ");
				dni = teclado.nextInt();
				teclado.nextLine();
				
				baseClientes[primerPosicionLibre("Cliente")] = new Cliente(nombre, dni);				
				break;
				
				
			case 6:
				
				
				System.out.print("Ingrese la marca del veh�culo: ");
				marca = teclado.nextLine();
				
				System.out.print("Ingrese el color del veh�culo: ");
				color = teclado.nextLine();
								
				System.out.print("Ingrese la patente del veh�culo: ");
				patente = teclado.nextLine();
				
				System.out.println("Tipo de veh�culo:");
				System.out.println("1. Moto");
				System.out.println("2. Auto");
				System.out.println("3. Cami�n");
				System.out.print("  Opci�n > ");
				tipo = teclado.nextInt();
				teclado.nextLine();
				
				switch (tipo) {
				
				case 1:
					System.out.println("Ingrese la cilindrada de la moto: ");
					parqueVehicular[primerPosicionLibre("Vehiculo")] = new Moto(marca, color, patente, teclado.nextInt());
					teclado.nextLine();
					System.out.println("Moto guardada!");
					
					break;
				case 2:
					
					System.out.println("Ingrese la cantidad de asientos del auto: ");
					parqueVehicular[primerPosicionLibre("Vehiculo")] = new Auto(marca, color, patente, teclado.nextInt());
					teclado.nextLine();
					System.out.println("Auto guardado!");
					break;
				case 3:
					
					System.out.println("Ingrese la capacidad de carga del cami�n: ");
					parqueVehicular[primerPosicionLibre("Vehiculo")] = new Camion(marca, color, patente, teclado.nextInt());
					teclado.nextLine();
					System.out.println("Cami�n guardado!");
					break;
				default:
					;
				}
				
				
				
				break;
			default:
				;			
			}
			
			
			
			
		} while (opcion != 0);
		
		
		
		
		
		
		System.out.println("FIN DEL PROGRAMA.");
	}
	
	public static int mostrarMenu() {
		
		int opcion = 999;
		boolean done = false;
		
		System.out.println();
		System.out.println("ADMINISTRACI�N DE ALQUILERES - RENTCBA");
		System.out.println();
		System.out.println("Men� de opciones:");
		System.out.println(" 1| Consultar veh�culos disponibles");
		System.out.println(" 2| Consultar veh�culos alquilados");
		System.out.println(" 3| Alquilar veh�culo");
		System.out.println(" 4| Devolver veh�culo");
		System.out.println(" 5| Dar de alta un nuevo cliente");
		System.out.println(" 6| Dar de alta un nuevo veh�culo");
		System.out.println(" 0| Salir");
		System.out.println();
		
		
		do {
			
			try {
			System.out.print(" Opci�n > ");
			opcion = teclado.nextInt();
			
			
			if (opcion >= 0 && opcion <= 6)
				done = true;
			}
			catch (InputMismatchException e) {
				System.out.println("S�lo es posible ingresar n�meros!");
			}		
			
			teclado.nextLine();
			
		} while (!done);
		
		return opcion;
		
		
	}
	
	
	
	
	
	private static int primerPosicionLibre(String arreglo) {
		
		int count = 0;
		
		switch (arreglo) {
		
		
		case "Cliente":
			count = 0;
			for (int i = 0 ; i < baseClientes.length ; i++) {
				
				if (baseClientes[i] instanceof Cliente) {
					
					count++;
				}
				
			}
			
			break;
			
			
		case "Vehiculo":
			count = 0;
			for (int i = 0 ; i < parqueVehicular.length ; i++) {
				
				if (parqueVehicular[i] instanceof Moto || parqueVehicular[i] instanceof Auto || parqueVehicular[i] instanceof Camion) {
					count++;
				}
				
			}
			break;
		}
		
		return count;
	}

}
	

	