package model.entities;

public class CoordinateValidator {
	private double latitud;
	private double longitud;
	
	public CoordinateValidator(String latitud, String longitud) {
		initialize(latitud, longitud);
	}

	private void initialize(String latitud, String longitud) {
		if (latitud == null || latitud.trim().isEmpty() || longitud == null || longitud.trim().isEmpty()) {
			throw new IllegalArgumentException("Latitud y longitud no deben estar vacios.");
		}
		try {
			this.latitud = Double.parseDouble(latitud);
			this.longitud = Double.parseDouble(longitud);			
		} catch (NumberFormatException e) {
			throw new NumberFormatException("Latitud y longitud deben ser números válidos.");
		}
	}

	public double getLatitud() {
		return latitud;
	}

	public double getLongitud() {
		return longitud;
	}
	
}
