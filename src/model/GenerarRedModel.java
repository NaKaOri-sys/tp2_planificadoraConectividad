package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.entities.AGM;
import model.entities.Conexion;
import model.entities.CostCalculator;
import model.entities.DistanceCalculator;
import model.entities.Grafo;
import model.entities.Localidad;
import model.interfaces.IGenerarRed;

public class GenerarRedModel implements IGenerarRed {

	private double costoKm;
	private double recargo;
	private double costoDifProv;
	private Map<String, Localidad> localidades;
	private Grafo grafo;
	private CostCalculator costCalculator;
	private DistanceCalculator distanceCalculator;
	private AGM agm;
	
	public GenerarRedModel(AGM agm) {
		this.agm = agm;
		this.grafo = new Grafo();
	}

	private void generarGrafo() {
		this.localidades.values().forEach((localidad) -> this.grafo.agregarLocalidad(localidad));
		List<Localidad> localidadesList = new ArrayList<Localidad>(this.localidades.values());

		for (int i = 0; i < localidadesList.size(); i++) {
			for (int j = i + 1; j < localidadesList.size(); j++) {
				Localidad localidadOrigen = localidadesList.get(i);
				Localidad localidadDestino = localidadesList.get(j);

				double costoConexion = calcularCostoConexion(this.costoKm, this.recargo, this.costoDifProv,
						localidadOrigen, localidadDestino);
				Conexion conexion = new Conexion(localidadOrigen, localidadDestino, costoConexion);
				this.grafo.agregarConexion(conexion);
			}
		}
	}

	private double calcularCostoConexion(double costoKm, double recargo, double costoDifProv, Localidad origen,
			Localidad destino) {
		double km = this.distanceCalculator.calcularDistanciaHaversine(origen, destino);
		double costoConexion = this.costCalculator.calcularCosto(origen, destino, km);

		return costoConexion;
	}

	@Override
	public Grafo generarRed(double costoKM, double recargo, double costoDifProv, Map<String, Localidad> localidades) {
		initializeClass(costoKM, recargo, costoDifProv, localidades);
		generarGrafo();
		return agm.generarAGM(this.grafo);
	}

	// TODO Implementar: Métodos para soportar modificación de solución
	// - calcularCostoSiSeReemplazaConexion(Conexion actual, Conexion reemplazo): double
	// - verificarValidezDeLaSolucion(): boolean (asegurar que sigue siendo un árbol válido)
	// - obtenerAlternativasDeConexion(Conexion conexionAReemplazar): List<Conexion>
	
	public Grafo obtenerGrafoCompleto() {
		return this.grafo;
	}

	private void initializeClass(double costoKM, double recargo, double costoDifProv,
			Map<String, Localidad> localidades) {
		this.grafo = new Grafo();
		this.costoKm = costoKM;
		this.recargo = recargo;
		this.costoDifProv = costoDifProv;
		this.localidades = localidades;
		this.costCalculator = new CostCalculator(costoKM, recargo, costoDifProv);
		this.distanceCalculator = new DistanceCalculator();

	}

}