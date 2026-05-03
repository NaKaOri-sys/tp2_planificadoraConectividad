package model.entities;

import java.io.Serializable;
import java.util.Objects;

import model.dtos.LocalidadDto;

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

	public String buildKeyLocalidad() {
		return this.getNombre() + " - " + this.getProvincia();
	}

	@Override
	public int hashCode() {
		return Objects.hash(Double.valueOf(latitud), Double.valueOf(longitud), nombre, provincia);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Localidad)) {
			return false;
		}
		Localidad other = (Localidad) obj;
		return Double.doubleToLongBits(latitud) == Double.doubleToLongBits(other.latitud)
				&& Double.doubleToLongBits(longitud) == Double.doubleToLongBits(other.longitud)
				&& Objects.equals(nombre, other.nombre) && Objects.equals(provincia, other.provincia);
	}

	public LocalidadDto toDto() {
		return new LocalidadDto(nombre, provincia, String.valueOf(this.latitud), String.valueOf(this.longitud));
	}
}
