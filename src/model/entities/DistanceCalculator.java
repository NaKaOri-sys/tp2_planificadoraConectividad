package model.entities;

public class DistanceCalculator {
	private final int RADIO_TIERRA = 6371; // Radio de la Tierra en km
	public double calcularDistanciaHaversine(Localidad origen, Localidad destino) {
		double dLat = Math.toRadians(destino.getLatitud() - origen.getLatitud());
		double dLon = Math.toRadians(destino.getLongitud() - origen.getLongitud());
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
				Math.cos(Math.toRadians(origen.getLatitud())) * Math.cos(Math.toRadians(destino.getLatitud())) *
				Math.sin(dLon / 2) * Math.sin(dLon / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		return RADIO_TIERRA * c;
	}
}
