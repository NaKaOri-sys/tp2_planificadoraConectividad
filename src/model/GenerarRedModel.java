package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.entities.Conexion;
import model.entities.CostCalculator;
import model.entities.DistanceCalculator;
import model.entities.Grafo;
import model.entities.Localidad;

public class GenerarRedModel {
	
	private double costoKm;
	private double recargo;
	private double costoDifProv;
	private Map<String, Localidad> localidades;
	private Grafo grafo = new Grafo();
	private final CostCalculator costCalculator;
	private final DistanceCalculator distanceCalculator;

	public GenerarRedModel(double costoKM, double recargo, double costoDifProv, Map<String, Localidad> localidades) {
		this.grafo = new Grafo();
		this.localidades = localidades;
		this.costCalculator = new CostCalculator(costoKM, recargo, costoDifProv);
		this.distanceCalculator = new DistanceCalculator();
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
		double km = this.distanceCalculator.calcularDistanciaHaversine(origen, destino);
		double costoConexion = this.costCalculator.calcularCosto(origen, destino, km);
		
		return costoConexion;
	}
	
	public Grafo generarRed() {
		AGM agm = new AGM(this.grafo);
		return agm.generarAGM();
	} 

}