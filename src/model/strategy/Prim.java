package model.strategy;

import java.util.Set;

import model.entities.Conexion;
import model.entities.Grafo;
import model.entities.Localidad;

public class Prim implements IArbolGeneradorMinimo {
	private Grafo grafo;

	public Prim() {

	}

	public Prim(Grafo g) {
		validateGrafo(g);
		this.grafo = g;
	}

	private void validateGrafo(Grafo g) {
		if (g == null || g.getLocalidades().size() == 0) {
			throw new IllegalArgumentException("Error: El grafo no puede estar vacío.");
		}

		if (g.getTodasLasConexiones().isEmpty()) {
			throw new IllegalArgumentException("Error: El grafo debe tener al menos una conexión.");
		}
	}

	@Override
	public Grafo generarAGM() {
		Grafo agm = new Grafo();
		Set<Localidad> visitados = new java.util.HashSet<>();
		Localidad inicio = grafo.getLocalidades().iterator().next();
		visitados.add(inicio);
		agm.agregarLocalidad(inicio);

		while (visitados.size() < grafo.getLocalidades().size()) {
			Conexion conexionMinima = buscarConexionMinima(grafo, visitados);
			if (conexionMinima == null) {
				return agm; // No hay más conexiones disponibles, el grafo no es conexo y retorno el AGM
							// obtenido hasta ahora
			}
			Localidad destino = visitados.contains(conexionMinima.getOrigen()) ? conexionMinima.getDestino()
					: conexionMinima.getOrigen();

			agm.agregarLocalidad(destino);
			agm.agregarConexion(conexionMinima);
			visitados.add(destino);
		}
		return agm;
	}

	private Conexion buscarConexionMinima(Grafo g, Set<Localidad> visitados) {
		Conexion minima = null;
		double minCosto = Double.MAX_VALUE;

		for (Localidad visitado : visitados) {
			for (Conexion conexion : g.getConexiones(visitado)) {
				Localidad destino = conexion.getDestino().equals(visitado) ? conexion.getOrigen()
						: conexion.getDestino();

				if (!visitados.contains(destino) && conexion.getCosto() < minCosto) {
					minCosto = conexion.getCosto();
					minima = conexion;
				}
			}
		}
		return minima;
	}
}
// TODO Implementar tests unitarios para clase Prim:
// - Test para generarAGM con grafo de 3 localidades
// - Test para generarAGM con grafo desconexo (debe retornar árbol parcial)
// - Test para verificar que el costo del AGM es mínimo
// - Test para generarAGM con grafo completo (todas las aristas conectadas)
// - Test para grafo con ciclos (debe generar árbol sin ciclos)
