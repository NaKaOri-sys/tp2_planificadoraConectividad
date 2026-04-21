package model.dtos;

public class LocalidadDto {
	private String nombre;
	private String provincia;
	private String latitud;
	private String longitud;

	public LocalidadDto(String nombre, String provincia, String latitud, String longitud) {
		this.nombre = nombre;
		this.provincia = provincia;
		this.latitud = latitud;
		this.longitud = longitud;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public String getProvincia() {
		return provincia;
	}
	
	public String getLatitud() {
		return latitud;
	}
	
	public String getLongitud() {
		return longitud;
	}
}
