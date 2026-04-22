package model;

public class Conexion {
	private Localidad origen;
	private Localidad destino;
	private double costoTotal;

	public Conexion(Localidad origen, Localidad destino) {
		validateConexion(origen, destino);
		this.origen = origen;
		this.destino = destino;
	}
	
	private void validateConexion(Localidad origen, Localidad destino) {
		if (origen == null) {
			throw new IllegalArgumentException("Error: El origen no puede ser nulo.");
		}
		if (destino == null) {
			throw new IllegalArgumentException("Error: El destino no puede ser nulo.");
		}
		if (origen.equals(destino)) {
			throw new IllegalArgumentException("Error: El origen y el destino no pueden ser la misma localidad.");
		}
	}
	
	public Localidad getOrigen() {
		return origen;
	}

	public Localidad getDestino() {
		return destino;
	}
	
	public double getCostoTotal() {
		return costoTotal;
	}
	public void setCostoTotal(double costoTotal) {
		this.costoTotal = costoTotal;
	}
}
