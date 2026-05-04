package model.repository;

import java.util.Map;

import model.dao.LocalidadDao;
import model.entities.Localidad;

public class LocalidadRepositoryJson implements ILocalidadRepository {
	private String FILE_PATH;
	public LocalidadRepositoryJson(String FILE_PATH) {
		this.FILE_PATH = FILE_PATH;
	}
	
	@Override
	public Map<String, Localidad> loadAll() {
		try {
			return LocalidadDao.cargarDesdeJson(this.FILE_PATH);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	public void saveAll(Map<String, Localidad> localidades) {
		try {
			LocalidadDao.generarJsonLocalidad(this.FILE_PATH, localidades);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	public void cleanAll() {
		try {
			LocalidadDao.limpiarArchivoJson(this.FILE_PATH);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

}
