package modelo.gestoresplazas;

import java.time.LocalDateTime;
import java.util.Arrays;

import list.IList;
import modelo.gestoresplazas.huecos.GestorHuecos;
import modelo.gestoresplazas.huecos.Hueco;
import modelo.gestoresplazas.huecos.Plaza;
import modelo.reservas.solicitudesreservas.SolicitudReservaAnticipada;

public class GestorZona {
	private int iZona; //coordenada i
	private int jZona; //coordenada j
	private Plaza[] plazas; //array de nº plazas
	private double precio; //precio de cada plaza
	private IList<SolicitudReservaAnticipada> listaEspera; //array lista de espera
	private GestorHuecos gestorHuecos; //huecos asociados a una zona
	private IList<Hueco> huecosReservados; //lista de huecos reservados
	
	public int getI() {
		return iZona;
	}
	
	public int getJ() {
		return jZona;
	}
	
	public double getPrecio() {
		return precio;
	}
	
	public String getId() {
		return "z" + iZona + ":" + jZona;
	}
	
	public String getEstadoHuecosLibres() {
		return this.gestorHuecos.toString();
	}
	
	public String getEstadoHuecosReservados() {
		return this.huecosReservados.toString();
	}
	
	public String getListaEspera() {
		return this.listaEspera.toString();
	}
	
	public String getPlazas() {
		return Arrays.toString(this.plazas);
	}
	
	public String toString() {
		return getId() + ": " + getEstadoHuecosReservados();
	}
	
	//TO-DO alumno obligatorios
	//constructor
	public GestorZona(int i, int j, int noPlazas, double precio) {
		this.iZona=i;
		this.jZona=j;
		this.plazas=new Plaza[noPlazas];
		for (int e=0;e<noPlazas;e++) {
			plazas[e]=new Plaza(e);
		}
		//inicializar los atributos
		this.precio=precio;
		this.gestorHuecos= new GestorHuecos(this.plazas);
		this.huecosReservados=new ArrayList<Hueco>();
		this.listaEspera=new ArrayList<SolicitudReservaAnticipada>();
		
		
	}
	
	//los huecos se reservan introduciendo el intervalo de tiempo que serán ocupados
	public Hueco reservarHueco(LocalDateTime tI, LocalDateTime tF) {
		Hueco hueco=gestorHuecos.reservarHueco(tI, tF);
		if (hueco!=null) { //si existe el hueco se añade al final
			huecosReservados.add(huecosReservados.size(), hueco);
		}
		return hueco;
	}
	
	//la solicitud se ordena dependiendo de su prioridad
	public void meterEnListaEspera(SolicitudReservaAnticipada solicitud) {
        TEnumPrioridad baja = TEnumPrioridad.BAJA;
        TEnumPrioridad media = TEnumPrioridad.MEDIA;
        TEnumPrioridad alta = TEnumPrioridad.ALTA;
		int i=listaEspera.size();
		if(solicitud.getPrioridad().equals(baja)) { //añadir al final
			i=listaEspera.size();
		}
		if(solicitud.getPrioridad().equals(media)) { //añadir justo antes de la primera prioridad baja
			for(i=listaEspera.size(); i>0 && (listaEspera.get(i-1).getPrioridad().equals(baja));i--);
		}
		if(solicitud.getPrioridad().equals(alta)) { //añadir justo antes de la primera prioridad media
		for(i=0;i<listaEspera.size() && (listaEspera.get(i).getPrioridad().equals(alta));i--);
		}
		listaEspera.add(i, solicitud);
	}
	
	//buscar si existe el hueco en el array huecosReservados
	public boolean existeHueco(LocalDateTime tI, LocalDateTime tF) {
		int i;
		for (i=0;i<huecosReservados.size()&&
				!((huecosReservados.get(i).gettI()==tI)&&(huecosReservados.get(i).gettF()==tF));i++);
		return (i<huecosReservados.size()); //sale del bucle antes si ha encontrado el hueco
	}
	
	//busca si el hueco está reservado
	public boolean existeHuecoReservado(Hueco hueco) { 
		int i;
		for (i=0;i<huecosReservados.size()&&
				!(huecosReservados.get(i).getPlaza().equals(hueco.getPlaza()));i++);
		return i<huecosReservados.size()&&existeHueco(hueco.gettI(),hueco.gettF());
	}
	
	//TO-DO alumno opcionales
	
	public void liberarHueco(Hueco hueco) {
		huecosReservados.remove(hueco);
		gestorHuecos.liberarHueco(hueco);
	}

	//PRE (no es necesario comprobar): las solicitudes de la lista de espera son válidas
	public IList<SolicitudReservaAnticipada> getSolicitudesAtendidasListaEspera() {
		IList<SolicitudReservaAnticipada> lista=new ArrayList<SolicitudReservaAnticipada>();
		Hueco hueco=null;
		for(int i=0;i<listaEspera.size();i++) {
			hueco=reservarHueco(listaEspera.get(i).getTInicial(),listaEspera.get(i).getTFinal());
		if(hueco!=null) {
			lista.add(lista.size(), listaEspera.get(i));
			listaEspera.removeElementAt(i);
		}
		}
		return lista;
	}

}
