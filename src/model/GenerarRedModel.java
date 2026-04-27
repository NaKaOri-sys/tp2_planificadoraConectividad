package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GenerarRedModel {
	private double costoKm;
	private double recargo;
	private double costoDifProv;
	private Map<String, Localidad> localidades;
	private Grafo grafo = new Grafo();

	public GenerarRedModel(double costoKM, double recargo, double costoDifProv, Map<String, Localidad> localidades) {
		this.costoKm = costoKM;
		this.recargo = recargo;
		this.costoDifProv = costoDifProv;
		this.localidades = localidades;
		generarGrafo();
	}

	private void generarGrafo() {
		this.localidades.values().forEach((localidad) -> this.grafo.agregarLocalidad(localidad));
		List<Localidad> localidadesList = new ArrayList<Localidad>(this.localidades.values());
		
		for (int i = 0; i < localidadesList.size(); i++) {
			for (int j = i + 1; j < localidadesList.size(); j++) {
				Localidad localidadOrigen = localidadesList.get(i);
				Localidad localidadDestino = localidadesList.get(j);
				
				double costoConexion = calcularCostoConexion(this.costoKm, this.recargo, this.costoDifProv, localidadOrigen, localidadDestino);
				Conexion conexion = new Conexion(localidadOrigen, localidadDestino, costoConexion);
				this.grafo.agregarConexion(conexion);
			}
		}
	}

	private double calcularCostoConexion(double costoKm, double recargo, double costoDifProv, Localidad origen, Localidad destino) {
		double km = distanciaEnKm(origen, destino);
		
		double base = (km * costoKm);
		double adicionalDistancia = km > 300 ? (base * recargo) / 100.0 : 0;
		
		double adicionalDifProvincia = !origen.getProvincia().equalsIgnoreCase(destino.getProvincia()) ? costoDifProv : 1;
		return base + adicionalDistancia + adicionalDifProvincia;
	}
	
	private double distanciaEnKm(Localidad origen, Localidad destino) {
		final int RADIO_TIERRA = 6371; // Radio de la Tierra en km
		double dLat = Math.toRadians(destino.getLatitud() - origen.getLatitud());
		double dLon = Math.toRadians(destino.getLongitud() - origen.getLongitud());
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
				Math.cos(Math.toRadians(origen.getLatitud())) * Math.cos(Math.toRadians(destino.getLatitud())) *
				Math.sin(dLon / 2) * Math.sin(dLon / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		return RADIO_TIERRA * c;
	}
	
	public Grafo generarRed() {
		AGM agm = new AGM(this.grafo);
		Grafo resultado = agm.generarAGM();
		return resultado;
	} 

}
