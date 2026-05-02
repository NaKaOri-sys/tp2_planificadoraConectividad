package tests.localidad;

import static org.junit.Assert.*;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import model.entities.Localidad;

public class LocalidadTest {
	Localidad SanMiguel;
	Map<String, Localidad> localidades;

	@Before
	public void setUp() throws Exception {
		this.SanMiguel = new Localidad("Buenos Aires", "San Miguel", -34.603722, -58.381592);
		this.localidades = new LinkedHashMap<>();
	}

	@Test(expected = IllegalArgumentException.class)
	public void agregaLocalidadConTxtLocalidadVacio() {
		new Localidad("Buenos Aires", "", -34.603722, -58.381592);
	}

	@Test(expected = IllegalArgumentException.class)
	public void agregaLocalidadConTxtProvinciaVacio() {
		new Localidad("", "San Miguel", -34.603722, -58.381592);
	}

	@Test(expected = IllegalArgumentException.class)
	public void agregaLocalidadConTxtLatitudVacio() {
		new Localidad("Buenos Aires", "San Miguel", 0, -58.381592);
	}

	@Test(expected = IllegalArgumentException.class)
	public void agregaLocalidadConTxtLongitudVacio() {
		new Localidad("Buenos Aires", "San Miguel", -34.603722, 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void agregaLocalidadConTxtLatitudMayorA90() {
		new Localidad("Buenos Aires", "San Miguel", 91.0, -58.381592);
	}

	@Test(expected = IllegalArgumentException.class)
	public void agregaLocalidadConTxtLatitudMenorA90() {
		new Localidad("Buenos Aires", "San Miguel", -91.0, -58.381592);
	}

	@Test(expected = IllegalArgumentException.class)
	public void agregaLocalidadConTxtLongitudMayorA180() {
		new Localidad("Buenos Aires", "San Miguel", -34.603722, 181.0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void agregaLocalidadConTxtLongitudMenorA180() {
		new Localidad("Buenos Aires", "San Miguel", -34.603722, -181.0);
	}

	@Test
	public void testAgregaLocalidadValida() {
		Localidad localidadValida = new Localidad("Buenos Aires", "San Miguel", -34.603722, -58.381592);

		assertEquals(this.SanMiguel, localidadValida);
	}

	@Test
	public void testLocalidadEquals() {
		Localidad localidadValida = new Localidad("Buenos Aires", "San Miguel", -34.603722, -58.381592);

		assertTrue(this.SanMiguel.equals(localidadValida));
	}

	@Test
	public void testLocalidadNotEquals() {
		Localidad localidadDiferente = new Localidad("Manuel Alberti", "Buenos Aires", -34.45878, -58.77661);

		assertTrue(!this.SanMiguel.equals(localidadDiferente));
	}

	@Test
	public void testLocalidadMismoNombreDiferenteProvinciaCreaDosElementosEnLista() {
		Localidad villaMariaCBA = new Localidad("Villa María", "Córdoba", -32.4103, -63.2314);
		this.localidades.put(villaMariaCBA.getNombre() + " - " + villaMariaCBA.getProvincia(), villaMariaCBA);
		Localidad villaMariaBA = new Localidad("Villa María", "Buenos Aires", -35.0000, -60.0000);
		this.localidades.put(villaMariaBA.getNombre() + " - " + villaMariaBA.getProvincia(), villaMariaBA);
		
		assertEquals(2, this.localidades.size());
	}

	// TODO Agregar test para localidades con coordenadas iguales pero diferente
	// nombre
	// TODO Agregar test para coordenadas en límites válidos (±90.0 latitud, ±180.0
	// longitud)
}
