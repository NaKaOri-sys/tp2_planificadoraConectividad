package tests.repository;

import model.entities.Localidad;
import model.repository.ILocalidadRepository;
import model.repository.LocalidadRepositoryJson;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;

public class LocalidadRepositoryJsonTest {

	private ILocalidadRepository repository;
	private static final String TEST_FILE = "test_localidades.json";

	@Before
	public void setUp() throws Exception {
		repository = new LocalidadRepositoryJson();
		File testFile = new File(TEST_FILE);
		if (testFile.exists()) {
			testFile.delete();
		}
	}

	@After
	public void tearDown() throws Exception {
		File testFile = new File(TEST_FILE);
		if (testFile.exists()) {
			testFile.delete();
		}
	}

	@Test
	public void testSaveAllYLuegoLoadAll() {
		Map<String, Localidad> localidades = new LinkedHashMap<>();
		Localidad loc1 = new Localidad("Buenos Aires", "Buenos Aires", -34.6037, -58.3816);
		Localidad loc2 = new Localidad("Córdoba", "Córdoba", -31.4135, -64.1889);
		
		localidades.put(loc1.buildKeyLocalidad(), loc1);
		localidades.put(loc2.buildKeyLocalidad(), loc2);
		
		repository.saveAll(localidades);
		
		Map<String, Localidad> cargadas = repository.loadAll();
		
		assertEquals("Debe tener 2 localidades", 2, cargadas.size());
	}

	@Test
	public void testCleanAllYLuegoLoadAll() {
		Map<String, Localidad> localidades = new LinkedHashMap<>();
		Localidad loc = new Localidad("Buenos Aires", "Buenos Aires", -34.6037, -58.3816);
		localidades.put(loc.buildKeyLocalidad(), loc);
		
		repository.saveAll(localidades);
		repository.cleanAll();
		
		Map<String, Localidad> vacias = repository.loadAll();
		assertNull("Después de limpiar debe estar vacío", vacias);
	}

	@Test
	public void testPersistenciaDatos() {
		Map<String, Localidad> datosOriginales = new LinkedHashMap<>();
		Localidad loc1 = new Localidad("La Plata", "Buenos Aires", -34.9205, -57.9557);
		Localidad loc2 = new Localidad("Rosario", "Santa Fe", -32.9443, -60.6558);
		Localidad loc3 = new Localidad("Mendoza", "Mendoza", -32.8895, -68.8458);
		
		datosOriginales.put(loc1.buildKeyLocalidad(), loc1);
		datosOriginales.put(loc2.buildKeyLocalidad(), loc2);
		datosOriginales.put(loc3.buildKeyLocalidad(), loc3);
		
		repository.saveAll(datosOriginales);
		
		LocalidadRepositoryJson repositorioNuevo = new LocalidadRepositoryJson();
		Map<String, Localidad> datosCargados = repositorioNuevo.loadAll();
		
		assertEquals("Debe cargar 3 localidades", 3, datosCargados.size());
	}

	@Test
	public void testLoadAllConMapaVacio() {
		repository.cleanAll();
		Map<String, Localidad> vacio = repository.loadAll();
		assertNull("No debe retornar null", vacio);
	}

}