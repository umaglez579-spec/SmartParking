package modelo.reservas.solicitudesreservas;

import java.time.LocalDateTime;

import modelo.gestoresplazas.GestorLocalidad;
import modelo.gestoresplazas.GestorZona;
import modelo.gestoresplazas.huecos.Hueco;
import modelo.vehiculos.Vehiculo;



public class SolicitudReserva {
	private int iZona; //coordenada i
	private int jZona; //coordenada j
	private LocalDateTime tInicial; //inicio
	private LocalDateTime tFinal; //final
	private Vehiculo vehiculo; //vehículo para el que se solicita
	private GestorZona gestorZona; // se inicializa al gestionar la solicitud
	private Hueco hueco; // se deja a null hasta que se completa la reserva

	//constructor
	protected SolicitudReserva(int i, int j, LocalDateTime tI, 
			LocalDateTime tF, Vehiculo vehiculo) {
		this.iZona = i;
		this.jZona = j;
		this.tInicial = tI;
		this.tFinal = tF;
		this.vehiculo = vehiculo;
		this.hueco=null;
	}

	public String toString() {
		return "(Sol:" + iZona + " " + jZona + " " + tInicial.toLocalTime() + " " + tFinal.toLocalTime() 
		+ " " + this.vehiculo.getMatricula() +  ")";
	}
	
	public void setHueco(Hueco hueco) {
		this.hueco = hueco;		
	}

	public Hueco getHueco() {
		return this.hueco;
	}
	
	public void setGestorZona(GestorZona gestor) {
		this.gestorZona = gestor;		
	}
	
	public GestorZona getGestorZona() {
		return this.gestorZona;
	}
	
	public int getIZona() {
		return iZona;
	}

	public int getJZona() {
		return jZona;
	}

	public LocalDateTime getTInicial() {
		return tInicial;
	}

	public LocalDateTime getTFinal() {
		return tFinal;
	}

	public Vehiculo getVehiculo() {
		return vehiculo;
	}
	
	//TO-DO alumno obligatorio
	//tFinal>tInicial, existen coordenadas (i,j), el vehículo no está sancionado
	public boolean esValida(GestorLocalidad gestorLocalidad) {
		return (this.tFinal.isAfter(this.tInicial) && gestorLocalidad.existeZona(iZona, jZona) && !vehiculo.getSancionado());
	}
	
	//el hueco se ocupa desde tInicial hasta tFinal
	public void gestionarSolicitudReserva(GestorLocalidad gestor) {
		this.gestorZona=gestor.getGestorZona(this.iZona, this.jZona);
		this.hueco=this.gestorZona.reservarHueco(this.tInicial, this.tFinal);
	}

}
