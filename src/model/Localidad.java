package model;

import java.io.Serializable;

public class Localidad implements Serializable {
	/** 
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String nombre;
	private String provincia;
	private double latitud;
	private double longitud;

	public Localidad(String nombre, String provincia, double latitud, double longitud) throws IllegalArgumentException {
		validateLocalidad(nombre, provincia, latitud, longitud);
		this.nombre = nombre;
		this.provincia = provincia;
		this.latitud = latitud;
		this.longitud = longitud;
	}

	private void validateLocalidad(String nombre, String provincia, double latitud, double longitud)
			throws IllegalArgumentException {
		if (nombre == null || nombre.trim().isEmpty()) {
			throw new IllegalArgumentException("El nombre de la localidad no puede ser nulo o vacío.");
		}
		if (provincia == null || provincia.trim().isEmpty()) {
			throw new IllegalArgumentException("La provincia de la localidad no puede ser nula o vacía.");
		}

		if (latitud == 0) {
			throw new IllegalArgumentException("La latitud no puede ser cero.");
		}

		if (longitud == 0) {
			throw new IllegalArgumentException("La longitud no puede ser cero.");
		}

		if (latitud < -90 || latitud > 90) {
			throw new IllegalArgumentException("La latitud debe estar entre -90 y 90 grados.");
		}
		if (longitud < -180 || longitud > 180) {
			throw new IllegalArgumentException("La longitud debe estar entre -180 y 180 grados.");
		}
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getProvincia() {
		return provincia;
	}

	public double getLatitud() {
		return latitud;
	}

	public double getLongitud() {
		return longitud;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Localidad other = (Localidad) obj;
		if (nombre == null) {
			if (other.nombre != null)
				return false;
		} else if (!nombre.equals(other.nombre))
			return false;
		if (provincia == null) {
			if (other.provincia != null)
				return false;
		} else if (!provincia.equals(other.provincia))
			return false;
		if (Double.doubleToLongBits(latitud) != Double.doubleToLongBits(other.latitud))
			return false;
		if (Double.doubleToLongBits(longitud) != Double.doubleToLongBits(other.longitud))
			return false;
		return true;
	}
}
