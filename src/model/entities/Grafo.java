package model.entities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Grafo {
	private Map<Localidad, List<Conexion>> mapaDeRedes;
	private Map<Localidad, Set<Localidad>> vecinos;
	private Set<Conexion> conexionesUnicas;

	public Grafo() {
		this.mapaDeRedes = new LinkedHashMap<>();
		this.vecinos = new LinkedHashMap<>();
		this.conexionesUnicas = new HashSet<Conexion>();
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
		this.conexionesUnicas.add(conexion);
	}

	public List<Conexion> getConexiones(Localidad localidad) {
		if (!mapaDeRedes.containsKey(localidad))
			throw new IllegalArgumentException("Error: La localidad no existe en el grafo.");
		return mapaDeRedes.get(localidad);
	}

	public List<Localidad> getLocalidades() {
		return new ArrayList<>(mapaDeRedes.keySet());
	}

	public Set<Conexion> getTodasLasConexiones() {
		return new HashSet<>(this.conexionesUnicas);
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
