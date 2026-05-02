package model.entities;

public class DistanceCalculator {
	private final double RADIO_TIERRA = 6371; 
	public double calcularDistanciaHaversine(Localidad origen, Localidad destino) {
		// Convertir coordenadas de grados a radianes
		double lat1 = Math.toRadians(origen.getLatitud());
		double lat2 = Math.toRadians(destino.getLatitud());
		double deltaLat = Math.toRadians(destino.getLatitud() - origen.getLatitud());
		double deltaLon = Math.toRadians(destino.getLongitud() - origen.getLongitud());
		
		// Aplicar fórmula de Haversine
		double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
				Math.cos(lat1) * Math.cos(lat2) *
				Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);
		
		// Calcular ángulo central usando atan2 (más estable numéricamente que asin)
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		
		// Retornar distancia
		return RADIO_TIERRA * c;
	}
}
