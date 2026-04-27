package model.dtos;

public class ConfigurationDto {
	private String costoKm;
	private String recargo;
	private String costoDifProv;

	public ConfigurationDto(String costoKm, String recargo, String costoDifProv) {
		this.costoKm = costoKm;
		this.costoDifProv = costoDifProv;
		this.recargo = recargo;
	}

	public String getCostoKm() {
		return costoKm;
	}

	public String getRecargo() {
		return recargo;
	}

	public String getCostoDifProv() {
		return costoDifProv;
	}
}
