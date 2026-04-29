package model.repository;

import java.util.Map;

import model.entities.Localidad;

public interface ILocalidadRepository {
	Map<String, Localidad> loadAll();
    void saveAll(Map<String, Localidad> localidades);
    void cleanAll();
}
