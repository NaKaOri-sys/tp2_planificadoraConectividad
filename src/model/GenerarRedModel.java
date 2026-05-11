package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

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

	// - verificarValidezDeLaSolucion(): boolean (asegurar que sigue siendo un árbol válido)
	public boolean verificarValidezDeLaSolucion(Grafo grafoAValidar) {
		if (grafoAValidar == null || grafoAValidar.getLocalidades().isEmpty()) {
			return false;
		}
		int totalVertices = grafoAValidar.getLocalidades().size();
		int totalAristas = grafoAValidar.getTodasLasConexiones().size();

		if (totalAristas != totalVertices - 1) {
			return false; // Un árbol con n nodos debe tener exactamente n-1 conexiones
		}
		// Algoritmo BFS para verificar que todas las ciudades estén conectadas
		Set<Localidad> visitados = new HashSet<>();
		Queue<Localidad> cola = new LinkedList<>();

		Localidad nodoInicial = grafoAValidar.getLocalidades().getFirst();
		cola.add(nodoInicial);
		visitados.add(nodoInicial);

		while (!cola.isEmpty()) {
			Localidad actual = cola.poll();

			for (Conexion conexion : grafoAValidar.getConexiones(actual)) {
				Localidad vecino = conexion.getOrigen().getNombre().equals(actual.getNombre()) ? conexion.getDestino()
						: conexion.getOrigen();
				if (!visitados.contains(vecino)) {
					visitados.add(vecino);
					cola.add(vecino);
				}
			}
		}

		return visitados.size() == totalVertices;
	}
	
	// - obtenerAlternativasDeConexion(Conexion conexionAReemplazar): List<Conexion>
	public List<Conexion> obtenerAlternativasDeConexion(Conexion conexionAReemplazar, Grafo agmActual) {
		if (conexionAReemplazar == null || agmActual == null) return new ArrayList<>();

		// Descubrir la "Isla A" usando BFS desde el origen de la calle cortada
		Set<String> nombresIslaA = new HashSet<>();
		Queue<Localidad> cola = new LinkedList<>();
		
		Localidad inicio = conexionAReemplazar.getOrigen();
		cola.add(inicio);
		nombresIslaA.add(inicio.getNombre());

		while (!cola.isEmpty()) {
			Localidad actual = cola.poll();
			for (Conexion conexion : agmActual.getConexiones(actual)) {

				boolean esLaAristaBorrada = (conexion.getOrigen().getNombre().equals(conexionAReemplazar.getOrigen().getNombre()) && conexion.getDestino().getNombre().equals(conexionAReemplazar.getDestino().getNombre())) ||
										   (conexion.getOrigen().getNombre().equals(conexionAReemplazar.getDestino().getNombre()) && conexion.getDestino().getNombre().equals(conexionAReemplazar.getOrigen().getNombre()));
				if (esLaAristaBorrada) continue;

				Localidad vecino = conexion.getOrigen().getNombre().equals(actual.getNombre()) ? conexion.getDestino() : conexion.getOrigen();
				if (!nombresIslaA.contains(vecino.getNombre())) {
					nombresIslaA.add(vecino.getNombre());
					cola.add(vecino);
				}
			}
		}

		// Buscar en TODAS las rutas del país cuáles cruzan de la Isla A a la Isla B
		List<Conexion> alternativas = new ArrayList<>();
		for (Conexion conexion : this.grafo.getTodasLasConexiones()) {
			
			boolean esLaBorrada = (conexion.getOrigen().getNombre().equals(conexionAReemplazar.getOrigen().getNombre()) && conexion.getDestino().getNombre().equals(conexionAReemplazar.getDestino().getNombre())) ||
								  (conexion.getOrigen().getNombre().equals(conexionAReemplazar.getDestino().getNombre()) && conexion.getDestino().getNombre().equals(conexionAReemplazar.getOrigen().getNombre()));
			if (esLaBorrada) continue;
			
			boolean origenEnA = nombresIslaA.contains(conexion.getOrigen().getNombre());
			boolean destinoEnA = nombresIslaA.contains(conexion.getDestino().getNombre());

			if (origenEnA != destinoEnA) {
			    alternativas.add(conexion);
			}
		}
		return alternativas;
	}

	public Grafo repararArbolAlEliminar(Conexion borrada, Grafo agmActual) {
		if (borrada == null || agmActual == null) {
			return agmActual;
		}
		
		List<Conexion> alternativas = obtenerAlternativasDeConexion(borrada, agmActual);
		if (alternativas.isEmpty()) {
			return agmActual;
		}
		Conexion mejorReemplazo = alternativas.get(0);
		for (Conexion alternativa : alternativas) {
			if (alternativa.getCosto() < mejorReemplazo.getCosto()) {
				mejorReemplazo = alternativa;
			}
		}
		agmActual.eliminarConexion(borrada);
		agmActual.agregarConexion(mejorReemplazo);
		return agmActual;
	}
	
	public Conexion obtenerConexionMasCaraDelCiclo(Conexion nuevaConexion, Grafo agmActual) {
		Map<String, Conexion> conexionPrevia = new HashMap<>();
		Queue<Localidad> cola = new LinkedList<>();
		Set<String> visitados = new HashSet<>();
		
		Localidad origen = nuevaConexion.getOrigen();
		Localidad destino = nuevaConexion.getDestino();
		
		cola.add(origen);
		visitados.add(origen.getNombre());
		
		while (!cola.isEmpty()) {
			Localidad actual = cola.poll();
			if (actual.getNombre().equals(destino.getNombre())) break;
			
			for (Conexion conexion : agmActual.getConexiones(actual)) {
				Localidad vecino = conexion.getOrigen().getNombre().equals(actual.getNombre()) ? conexion.getDestino() : conexion.getOrigen();
				if (!visitados.contains(vecino.getNombre())) {
					visitados.add(vecino.getNombre());
					conexionPrevia.put(vecino.getNombre(), conexion);
					cola.add(vecino);
				}
			}
		}
		// Recorremos el camino de vuelta para encontrar la ruta más cara
		Conexion conexionMasCara = null;
		Localidad actual = destino;
		while (!actual.getNombre().equals(origen.getNombre())) {
			Conexion conexionCamino = conexionPrevia.get(actual.getNombre());
			if (conexionMasCara == null || conexionCamino.getCosto() > conexionMasCara.getCosto()) {
				conexionMasCara = conexionCamino;
			}
			// Retrocedemos al paso anterior
			actual = conexionCamino.getOrigen().getNombre().equals(actual.getNombre()) ? conexionCamino.getDestino() : conexionCamino.getOrigen();
		}
		
		return conexionMasCara;
	}
	
	@Override
	public Grafo recalcularRed(Conexion aEliminar, Conexion aAgregar, Grafo agmActual) {
		Grafo nuevoAgm = agmActual;
		if (aEliminar != null) {
			nuevoAgm = repararArbolAlEliminar(aEliminar, nuevoAgm);
		}
		if (aAgregar != null) {
			Conexion aBorrar = obtenerConexionMasCaraDelCiclo(aAgregar, nuevoAgm);						
			if (aBorrar != null) {
				nuevoAgm.eliminarConexion(aBorrar);
			}
			nuevoAgm.agregarConexion(aAgregar);
		}
		return nuevoAgm;
	}

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