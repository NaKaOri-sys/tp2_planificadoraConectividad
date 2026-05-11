package model.interfaces;

import java.util.Map;

import model.entities.Conexion;
import model.entities.Grafo;
import model.entities.Localidad;

public interface IGenerarRed {
	Grafo generarRed(double costoKM, double recargo, double costoDifProv, Map<String, Localidad> localidades);
	
	Grafo recalcularRed(Conexion aEliminar, Conexion aAgregar, Grafo agmActual);
}
