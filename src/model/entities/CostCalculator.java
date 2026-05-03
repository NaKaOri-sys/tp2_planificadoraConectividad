package model.entities;

public class CostCalculator{
	private double costoKm;
	private double recargo;
	private double costoDifProv;
	public CostCalculator(double costoKm, double recargo, double costoDifProv) {
		this.costoKm = costoKm;
        this.recargo = recargo;
        this.costoDifProv = costoDifProv;
	}
	
	public double calcularCosto(Localidad origen, Localidad destino, double distanciaKm) {
		double base = (distanciaKm * costoKm);
		double adicionalDistancia = distanciaKm > 300 ? (base * recargo) / 100.0 : 0;
 		double adicionalDifProvincia = !origen.getProvincia().trim().equalsIgnoreCase(destino.getProvincia().trim()) ? costoDifProv : 0;
 		return base + adicionalDistancia + adicionalDifProvincia;
	}
}
