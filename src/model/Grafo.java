package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class Grafo {
	private Map<Localidad, List<Conexion>> mapaDeRedes;
	
	public Grafo() {
		this.mapaDeRedes = new HashMap<>();
	}
	
	public void agregarLocalidad(Localidad localidad) {
		if (!mapaDeRedes.containsKey(localidad)) {
			mapaDeRedes.put(localidad, new ArrayList<>());
		}
	}
	
	public void eliminarLocalidad(Localidad localidad) {
		if (!mapaDeRedes.containsKey(localidad)) {
			throw new IllegalArgumentException("Error: La localidad no existe en el grafo.");
		} 
		mapaDeRedes.remove(localidad);
		
		for (List<Conexion> conexionesVecinos : mapaDeRedes.values()) {
			Iterator<Conexion> iterator = conexionesVecinos.iterator();
			while(iterator.hasNext()) {
				Conexion cable = iterator.next();
				if(cable.getOrigen().equals(localidad) || cable.getDestino().equals(localidad)) {
					iterator.remove();
				}
			}
		}
	}

	public void agregarConexion(Conexion conexion) {
		Localidad origen = conexion.getOrigen();
		Localidad destino = conexion.getDestino();
		
		agregarLocalidad(origen);
		agregarLocalidad(destino);
		
		mapaDeRedes.get(origen).add(conexion);
		
		Conexion conexionInversa = new Conexion(destino, origen);
		mapaDeRedes.get(destino).add(conexionInversa);
	}
	
	public List<Conexion> getConexiones(Localidad localidad) {
		if(mapaDeRedes.containsKey(localidad)) {
			return mapaDeRedes.get(localidad);
		}else {
			throw new IllegalArgumentException("Error: La localidad no existe en el grafo.");
		}
	}
	
	public Set<Localidad> getLocalidades(){
		return mapaDeRedes.keySet();
	}
}
