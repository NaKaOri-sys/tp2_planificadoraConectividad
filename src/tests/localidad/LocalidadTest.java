package tests.localidad;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import model.Localidad;

public class LocalidadTest {
	Localidad localidad;

	@Before
	public void setUp() throws Exception {
		this.localidad = new Localidad("Buenos Aires", "San Miguel", -34.603722, -58.381592);
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
	public void agregaLocalidadValida() {
		Localidad localidadValida = new Localidad("Buenos Aires", "San Miguel", -34.603722, -58.381592);

		assertEquals(this.localidad, localidadValida);
	}
}
