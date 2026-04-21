package tests.localidad;

import static org.junit.Assert.*;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dao.LocalidadDao;
import model.Localidad;

public class LocalidadDaoTest {
	private Map<String, Localidad> localidades = new LinkedHashMap<>();

	@Before
	public void setUp() throws Exception {
		this.localidades.put("Buenos Aires", new Localidad("Buenos Aires", "San Miguel", -34.603722, -58.381592));
		this.localidades.put("Córdoba", new Localidad("Córdoba", "Córdoba", -31.420083, -64.188776));
		this.localidades.put("Rosario", new Localidad("Rosario", "Santa Fe", -32.946819, -60.639321));
	}

	@Test(expected = IllegalArgumentException.class)
	public void generarJsonLocalidadConRutaVaciaTest() {
		String filePath = "";
		LocalidadDao.generarJsonLocalidad(filePath, this.localidades);
	}

	@Test(expected = IllegalArgumentException.class)
	public void generarJsonLocalidadConRutaNulaTest() {
		String filePath = null;
		LocalidadDao.generarJsonLocalidad(filePath, this.localidades);
	}

	@Test(expected = IllegalArgumentException.class)
	public void generarJsonLocalidadConLocalidadesNulasTest() {
		String filePath = "test_localidades.json";
		LocalidadDao.generarJsonLocalidad(filePath, null);
	}
	
	public void cargarDesdeJsonRutaInvalidaDevuelveNullTest() {
		String filePath = "ruta_invalida.json";
		String path = "localidades.json";
		LocalidadDao.generarJsonLocalidad(path, localidades);
		Map<String, Localidad> localidades =  LocalidadDao.cargarDesdeJson(filePath);
		assertNull(localidades);
	}

	@Test
	public void generarJsonLocalidadCorrectamenteTest() {
		String filePath = "test_localidades.json";
		LocalidadDao.generarJsonLocalidad(filePath, this.localidades);
	}

	@Test
	public void cargarDesdeJsonTresLocalidadesTest() {
		String filePath = "test_localidades.json";
		LocalidadDao.generarJsonLocalidad(filePath, this.localidades);
		Map<String, Localidad> localidadesCargadas = LocalidadDao.cargarDesdeJson(filePath);
		assertEquals(3, localidadesCargadas.size());
	}

	@After
	public void tearDown() throws Exception {
		File file = new File("test_localidades.json");
		if (file.exists()) {
			file.delete();
		}
	}

}
