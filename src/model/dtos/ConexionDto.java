package model.dtos;

/**
 * DTO para transferencia de datos de conexiones entre capas.
 * Permite visualizar y manipular información de conexiones de forma desacoplada.
 */
public class ConexionDto {
	private String origen;
	private String destino;
	private double costo;
	
	public ConexionDto(String origen, String destino, double costo) {
		this.origen = origen;
		this.destino = destino;
		this.costo = costo;
	}
	
	public String getOrigen() {
		return origen;
	}
	
	public void setOrigen(String origen) {
		this.origen = origen;
	}
	
	public String getDestino() {
		return destino;
	}
	
	public void setDestino(String destino) {
		this.destino = destino;
	}
	
	public double getCosto() {
		return costo;
	}
	
	public void setCosto(double costo) {
		this.costo = costo;
	}
	
	@Override
	public String toString() {
		return origen + " → " + destino + " ($" + String.format("%.2f", costo) + ")";
	}
}
