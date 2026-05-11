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

	public boolean verificarValidezDeLaSolucion(Grafo grafoAValidar) {
		if (grafoAValidar == null || grafoAValidar.getLocalidades().isEmpty()) {
			return false;
		}
		int totalVertices = grafoAValidar.getLocalidades().size();
		int totalAristas = grafoAValidar.getTodasLasConexiones().size();

		// Un árbol con n nodos debe tener exactamente n-1 conexiones
		if (totalAristas != totalVertices - 1) {
			return false;
		}
		
		// Verificar conectividad con BFS
		return verificarConectividadConBFS(grafoAValidar, totalVertices);
	}
	
	private boolean verificarConectividadConBFS(Grafo grafo, int totalVertices) {
		Set<String> visitados = new HashSet<>();
		Queue<Localidad> cola = new LinkedList<>();

		Localidad nodoInicial = grafo.getLocalidades().getFirst();
		cola.add(nodoInicial);
		visitados.add(nodoInicial.getNombre());

		while (!cola.isEmpty()) {
			Localidad actual = cola.poll();
			for (Conexion conexion : grafo.getConexiones(actual)) {
				Localidad vecino = obtenerVecinoDesdeConexion(conexion, actual);
				if (!visitados.contains(vecino.getNombre())) {
					visitados.add(vecino.getNombre());
					cola.add(vecino);
				}
			}
		}

		return visitados.size() == totalVertices;
	}
	
	public List<Conexion> obtenerAlternativasDeConexion(Conexion conexionAReemplazar, Grafo agmActual) {
		if (conexionAReemplazar == null || agmActual == null) {
			return new ArrayList<>();
		}

		// Descubrir la "Isla A" usando BFS desde el origen de la calle cortada
		Set<String> nombresIslaA = identificarIslaDesdeConexion(conexionAReemplazar, agmActual);

		// Buscar en todas las rutas del pais cuales cruzan de la Isla A a la Isla B
		List<Conexion> alternativas = new ArrayList<>();
		for (Conexion conexion : this.grafo.getTodasLasConexiones()) {
			if (!esConexionIgual(conexion, conexionAReemplazar) && cruzaEntreIslas(conexion, nombresIslaA)) {
				alternativas.add(conexion);
			}
		}
		return alternativas;
	}
	
	private Set<String> identificarIslaDesdeConexion(Conexion conexionAReemplazar, Grafo agmActual) {
		Set<String> isla = new HashSet<>();
		Queue<Localidad> cola = new LinkedList<>();
		
		Localidad inicio = conexionAReemplazar.getOrigen();
		cola.add(inicio);
		isla.add(inicio.getNombre());

		while (!cola.isEmpty()) {
			Localidad actual = cola.poll();
			for (Conexion conexion : agmActual.getConexiones(actual)) {
				if (!esConexionIgual(conexion, conexionAReemplazar)) {
					Localidad vecino = obtenerVecinoDesdeConexion(conexion, actual);
					if (!isla.contains(vecino.getNombre())) {
						isla.add(vecino.getNombre());
						cola.add(vecino);
					}
				}
			}
		}
		return isla;
	}
	
	private boolean esConexionIgual(Conexion conexion1, Conexion conexion2) {
		String ori1 = conexion1.getOrigen().getNombre();
		String des1 = conexion1.getDestino().getNombre();
		String ori2 = conexion2.getOrigen().getNombre();
		String des2 = conexion2.getDestino().getNombre();
		
		return (ori1.equals(ori2) && des1.equals(des2)) || 
		       (ori1.equals(des2) && des1.equals(ori2));
	}
	
	private boolean cruzaEntreIslas(Conexion conexion, Set<String> isla) {
		boolean origenEnIsla = isla.contains(conexion.getOrigen().getNombre());
		boolean destinoEnIsla = isla.contains(conexion.getDestino().getNombre());
		return origenEnIsla != destinoEnIsla;
	}
	
	private Localidad obtenerVecinoDesdeConexion(Conexion conexion, Localidad nodoCentral) {
		String nombreActual = nodoCentral.getNombre();
		return conexion.getOrigen().getNombre().equals(nombreActual) ? 
			conexion.getDestino() : conexion.getOrigen();
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
		Map<String, String> padre = new HashMap<>();
		
		// BFS para encontrar el camino desde origen a destino
		encontrarCaminoBFS(nuevaConexion, agmActual, conexionPrevia, padre);
		
		// Reconstruir el camino y encontrar la conexión más cara
		return obtenerMasCaraDelCamino(nuevaConexion, conexionPrevia, padre);
	}
	
	/**
	 * Realiza BFS para encontrar el camino desde origen a destino
	 */
	private void encontrarCaminoBFS(Conexion nuevaConexion, Grafo agmActual, 
			Map<String, Conexion> conexionPrevia, Map<String, String> padre) {
		Queue<String> cola = new LinkedList<>();
		Set<String> visitados = new HashSet<>();
		
		String origen = nuevaConexion.getOrigen().getNombre();
		String destino = nuevaConexion.getDestino().getNombre();
		
		cola.add(origen);
		visitados.add(origen);
		padre.put(origen, null);
		
		while (!cola.isEmpty()) {
			String nombreActual = cola.poll();
			
			if (!nombreActual.equals(destino)) {
				Localidad actual = obtenerLocalidadPorNombre(agmActual, nombreActual);
				
				if (actual != null) {
					for (Conexion conexion : agmActual.getConexiones(actual)) {
						Localidad vecino = obtenerVecinoDesdeConexion(conexion, actual);
						String nombreVecino = vecino.getNombre();
						
						if (!visitados.contains(nombreVecino)) {
							visitados.add(nombreVecino);
							padre.put(nombreVecino, nombreActual);
							conexionPrevia.put(nombreVecino, conexion);
							cola.add(nombreVecino);
						}
					}
				}
			}
		}
	}
	
	private Localidad obtenerLocalidadPorNombre(Grafo grafo, String nombre) {
		for (Localidad localidad : grafo.getLocalidades()) {
			if (localidad.getNombre().equals(nombre)) {
				return localidad;
			}
		}
		return null;
	}
	
	private Conexion obtenerMasCaraDelCamino(Conexion nuevaConexion, 
			Map<String, Conexion> conexionPrevia, Map<String, String> padre) {
		Conexion conexionMasCara = null;
		String actual = nuevaConexion.getDestino().getNombre();
		String origen = nuevaConexion.getOrigen().getNombre();
		
		while (!actual.equals(origen) && padre.containsKey(actual)) {
			Conexion conexionCamino = conexionPrevia.get(actual);
			if (conexionCamino != null && (conexionMasCara == null || conexionCamino.getCosto() > conexionMasCara.getCosto())) {
				conexionMasCara = conexionCamino;
			}
			// Retroceder al nodo anterior
			actual = padre.get(actual);
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