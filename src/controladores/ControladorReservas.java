package controladores;

import controladores.excepciones.PlazaOcupada;
import controladores.excepciones.ReservaInvalida;
import controladores.excepciones.SolicitudReservaInvalida;
import list.IList;
import modelo.gestoresplazas.GestorLocalidad;
import modelo.reservas.EstadoValidez;
import modelo.reservas.Reserva;
import modelo.reservas.Reservas;
import modelo.reservas.solicitudesreservas.SolicitudReserva;
import modelo.vehiculos.Vehiculo;


public class ControladorReservas {
	private Reservas registroReservas;
	private GestorLocalidad gestorLocalidad;

	//permite llamar a los atributos privados
	public GestorLocalidad getGestorLocalidad() {
		return gestorLocalidad;
	}

	public Reservas getRegistroReservas() {
		return registroReservas;
	}

	public boolean esValidaReserva(int i, int j, int numPlaza, int numReserva, String noMatricula) {
		Reserva reserva = this.registroReservas.obtenerReserva(numReserva);
		if (reserva == null)
			return false;
		reserva.validar(i, j, numPlaza, noMatricula, gestorLocalidad);
		return reserva.getEstadoValidez() == EstadoValidez.OK;
	}

	//TO-DO alumno obligatorio
	//constructor
	public ControladorReservas(int[][] plazas, double[][] precios) {
		this.gestorLocalidad=new GestorLocalidad(plazas,precios);
		this.registroReservas=new Reservas();
	}


	//PRE: la solicitud es vÃ¡lida
	//devuelve el orden de prioridad
	public int hacerReserva(SolicitudReserva solicitud) throws SolicitudReservaInvalida {
		int i=-1; //si hueco=null, i=-1 (valor por defecto)
		if(!solicitud.esValida(gestorLocalidad)) { 
			throw new SolicitudReservaInvalida("Solicitud invÃ¡lida"); //@throws solicitud invÃ¡lida
		}
		solicitud.gestionarSolicitudReserva(gestorLocalidad); 
		if(solicitud.getHueco()!=null) {
			i=registroReservas.registrarReserva(solicitud); //ordenado por jerarquÃ­a
		}
		return i;
	}

	//constructor
	public Reserva getReserva(int numReserva) {
		return registroReservas.obtenerReserva(numReserva);
	}

	//PRE: la plaza dada estÃ¡ libre y la reserva estÃ¡ validada
	public void ocuparPlaza(int i, int j, int numPlaza, int numReserva, Vehiculo vehiculo) throws PlazaOcupada, ReservaInvalida {
		registroReservas.obtenerReserva(numReserva).validar(i, j, numPlaza, vehiculo.getMatricula(), gestorLocalidad);
		if(registroReservas.obtenerReserva(numReserva).getEstadoValidez().equals(EstadoValidez.FAILED)) {
			throw new ReservaInvalida("La reserva no es validad"); //@throws reserva invÃ¡lida
		}
		if(registroReservas.obtenerReserva(numReserva).getHueco().getPlaza().getVehiculo()!=null) {
			throw new PlazaOcupada("Esta plaza ya esta ocupada"); //@throws plaza ya ocupada
		}
		registroReservas.obtenerReserva(numReserva).getHueco().getPlaza().setVehiculo(vehiculo); //se reserva el hueco con la informaciÃ³n
	}



	//TO-DO alumno opcional
	//elimina el vehículo de la plaza y se crea un hueco
	public void desocuparPlaza(int numReserva) {
		registroReservas.obtenerReserva(numReserva).getHueco().getPlaza().setVehiculo(null);
		registroReservas.obtenerReserva(numReserva).liberarHuecoReservado();
	}

	//borra la reserva y crea un hueco
	public void anularReserva(int numReserva) {
		registroReservas.obtenerReserva(numReserva).liberarHuecoReservado();
		registroReservas.borrarReserva(numReserva);
	}

	// PRE (no es necesario comprobar): todas las solicitudes atendidas son vÃ¡lidas.
	public IList<Integer> getReservasRegistradasDesdeListaEspera(int i, int j){
		return null;
	}
}
