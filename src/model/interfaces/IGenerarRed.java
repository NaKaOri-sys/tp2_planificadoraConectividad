package model.interfaces;

import java.util.Map;

import model.entities.Grafo;
import model.entities.Localidad;

public interface IGenerarRed {
	Grafo generarRed(double costoKM, double recargo, double costoDifProv, Map<String, Localidad> localidades);
}
