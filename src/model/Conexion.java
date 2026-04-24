package model;

public class Conexion {
	private Localidad origen;
	private Localidad destino;
	private final double costo;

	public Conexion(Localidad origen, Localidad destino, double costo) {
		validateConexion(origen, destino, costo);
		this.origen = origen;
		this.destino = destino;
		this.costo = costo;
	}

	private void validateConexion(Localidad origen, Localidad destino, double costo) {
		if (origen == null) {
			throw new IllegalArgumentException("Error: El origen no puede ser nulo.");
		}
		if (destino == null) {
			throw new IllegalArgumentException("Error: El destino no puede ser nulo.");
		}
		if (origen.equals(destino)) {
			throw new IllegalArgumentException("Error: El origen y el destino no pueden ser la misma localidad.");
		}
		if (costo < 0) {
			throw new IllegalArgumentException("Error: El costo total no puede ser negativo.");
		}
	}

	public Localidad getOrigen() {
		return origen;
	}

	public Localidad getDestino() {
		return destino;
	}

	public double getCosto() {
		return costo;
	}
}
