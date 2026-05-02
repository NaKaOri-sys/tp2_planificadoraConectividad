package model.entities;

public class CoordinateValidator {
	private double latitud;
	private double longitud;
	
	public CoordinateValidator(String latitud, String longitud) {
		initialize(latitud, longitud);
	}

	private void initialize(String latitud, String longitud) {
		try {
			this.latitud = Double.parseDouble(latitud);
			this.longitud = Double.parseDouble(longitud);			
		} catch (NumberFormatException e) {
			throw new NumberFormatException("Latitud y longitud deben ser números válidos.");
		} catch(NullPointerException e) {
			throw new IllegalArgumentException("Latitud y longitud no deben estar vacios.");
		}
		
	}

	public double getLatitud() {
		return latitud;
	}

	public double getLongitud() {
		return longitud;
	}
	
}
