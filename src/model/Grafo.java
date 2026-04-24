package model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Grafo {
	private Map<Localidad, List<Conexion>> mapaDeRedes;
	private Map<Localidad, Set<Localidad>> vecinos;

	public Grafo() {
		this.mapaDeRedes = new LinkedHashMap<>();
		this.vecinos = new LinkedHashMap<>();
	}

	public void agregarLocalidad(Localidad localidad) {
		if (mapaDeRedes.containsKey(localidad)) {
			return; // La localidad ya existe, no se agrega nuevamente
		}
		mapaDeRedes.put(localidad, new ArrayList<>());
		vecinos.put(localidad, new HashSet<>());
	}

	public void eliminarLocalidad(Localidad localidad) {
		if (!mapaDeRedes.containsKey(localidad)) {
			throw new IllegalArgumentException("Error: La localidad no existe en el grafo.");
		}

		List<Conexion> conexionesABorrar = mapaDeRedes.get(localidad);
		for (Conexion conexionesVecinos : conexionesABorrar) {
			mapaDeRedes.get(conexionesVecinos.getOrigen()).remove(conexionesVecinos);
			mapaDeRedes.get(conexionesVecinos.getDestino()).remove(conexionesVecinos);
		}
		mapaDeRedes.remove(localidad);
	}

	public boolean sonVecinos(Localidad localidad, Localidad vecino) {
		return vecinos.containsKey(localidad) && vecinos.get(localidad).contains(vecino);
	}

	public void agregarConexion(Conexion conexion) {
		if (sonVecinos(conexion.getOrigen(), conexion.getDestino())) {
			return; // La conexión ya existe, no se agrega nuevamente
		}
		Localidad origen = conexion.getOrigen();
		Localidad destino = conexion.getDestino();

		agregarLocalidad(origen);
		agregarLocalidad(destino);

		mapaDeRedes.get(origen).add(conexion);
		mapaDeRedes.get(destino).add(conexion);

		vecinos.get(conexion.getOrigen()).add(conexion.getDestino());
		vecinos.get(conexion.getDestino()).add(conexion.getOrigen());
	}

	public List<Conexion> getConexiones(Localidad localidad) {
		if (!mapaDeRedes.containsKey(localidad))
			throw new IllegalArgumentException("Error: La localidad no existe en el grafo.");
		return mapaDeRedes.get(localidad);
	}

	public List<Localidad> getLocalidades() {
		return new ArrayList<>(mapaDeRedes.keySet());
	}

	public List<Conexion> getTodasLasConexiones() {
		Set<Conexion> conexionesUnicas = new HashSet<>();
		for (List<Conexion> conexiones : mapaDeRedes.values()) {
			conexionesUnicas.addAll(conexiones);
		}
		return new ArrayList<>(conexionesUnicas);
	}

	public boolean existeLocalidad(Localidad localidad) {
		return mapaDeRedes.containsKey(localidad);
	}
	
	public double calcularCostoTotal() {
		double costoTotal = 0.0;
		for (Conexion conexion : this.getTodasLasConexiones()) {
			costoTotal += conexion.getCosto();
		}
		return costoTotal;
	}
}
