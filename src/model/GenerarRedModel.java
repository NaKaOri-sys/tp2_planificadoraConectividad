package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.entities.Conexion;
import model.entities.CostCalculator;
import model.entities.DistanceCalculator;
import model.entities.Grafo;
import model.entities.Localidad;
import model.interfaces.IGenerarRed;
import model.strategy.Prim;

public class GenerarRedModel implements IGenerarRed {

	private double costoKm;
	private double recargo;
	private double costoDifProv;
	private Map<String, Localidad> localidades;
	private Grafo grafo = new Grafo();
	private CostCalculator costCalculator;
	private DistanceCalculator distanceCalculator;

	public GenerarRedModel() {
	}
	
	// TODO SOLID - DIP Violation: Crea instancias directamente (new Prim, new AGM, new CostCalculator, new DistanceCalculator)
	// Esto hace la clase difícil de testear y acopla con implementaciones concretas
	// SOLUCIÓN: Inyectar dependencias en el constructor:
	//   - IArbolGeneradorMinimo agmStrategy
	//   - ICostCalculator costCalculator (interfaz para CostCalculator)
	//   - IDistanceCalculator distanceCalculator (interfaz para DistanceCalculator)
	// También considerar inyectar el Grafo como dependencia

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
		AGM agm = new AGM();
		agm.setAGM(new Prim(this.grafo));
		return agm.generarAGM();
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