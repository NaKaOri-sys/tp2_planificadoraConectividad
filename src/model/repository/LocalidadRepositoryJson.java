package model.repository;

import java.util.Map;

import model.dao.LocalidadDao;
import model.entities.Localidad;

public class LocalidadRepositoryJson implements ILocalidadRepository {
	
	@Override
	public Map<String, Localidad> loadAll() {
		try {
			return LocalidadDao.cargarDesdeJson(LocalidadDao.FILE_PATH);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	public void saveAll(Map<String, Localidad> localidades) {
		try {
			LocalidadDao.generarJsonLocalidad(LocalidadDao.FILE_PATH, localidades);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	public void cleanAll() {
		try {
			LocalidadDao.limpiarArchivoJson(LocalidadDao.FILE_PATH);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

}
