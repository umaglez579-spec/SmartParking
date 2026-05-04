package modelo.gestoresplazas;

import list.IList;
import modelo.gestoresplazas.huecos.Hueco;
import modelo.reservas.solicitudesreservas.SolicitudReservaAnticipada;

//TO-DO alumno obligatorio
//constructor
public class GestorLocalidad {
	private GestorZona[][] gestoresZonas; //matriz de plazas y sus precios

	public GestorLocalidad(int[][] plazas, double[][] precios) {
		this.gestoresZonas=new GestorZona[plazas.length][plazas[0].length];
		for (int i=0;i<plazas.length;i++) {
			for(int j=0; j<plazas[i].length ;j++) {
				this.gestoresZonas[i][j]=new GestorZona(i,j,plazas[i][j],precios[i][j]);
			}
		}
	}
	
	//radios de coordenadas i y j
	public int getRadioMaxI() {
		return gestoresZonas.length-1;
	}
	
	public int getRadioMaxJ() {
		return  gestoresZonas[0].length-1;
	}
	
	//existen zona y hueco
	public boolean existeZona(int i, int j) {
		return (i<this.getRadioMaxI()+1&&j<this.getRadioMaxJ()+1&&i>=0&&j>=0);
	}

	public boolean existeHuecoReservado(Hueco hueco, int i, int j) {
		return this.gestoresZonas[i][j].existeHuecoReservado(hueco);
	}

	//info de la zona en la posición (i,j)
	public GestorZona getGestorZona(int i, int j) {
		return this.gestoresZonas[i][j];
	}
	
	//TO-DO alumno opcional
	//array de reservas que pueden ser atendidas en (i,j)
	public IList<SolicitudReservaAnticipada> getSolicitudesAtendidasListaEspera(int i, int j) {
		IList<SolicitudReservaAnticipada> lista=new ArrayList<SolicitudReservaAnticipada>();
		lista=this.gestoresZonas[i][j].getSolicitudesAtendidasListaEspera(); //añadimos a las que pueden ser atendidas
		return lista;
	}
	
}
