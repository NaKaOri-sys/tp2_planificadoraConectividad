package tests.entities;

import static org.junit.Assert.*;

import org.junit.Test;

import model.entities.CoordinateValidator;

public class CoordinateValidatorTest {

	@Test(expected = NumberFormatException.class)
	public void testCoordinateValidatorConLatitudNoNumerica() {
		new CoordinateValidator("abc", "58.381592");
	}

	@Test(expected = NumberFormatException.class)
	public void testCoordinateValidatorConLongitudNoNumerica() {
		new CoordinateValidator("-34.603722", "xyz");
	}

	@Test(expected = NumberFormatException.class)
	public void testCoordinateValidatorConAmbasNoNumericas() {
		new CoordinateValidator("abc", "xyz");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCoordinateValidatorConLongNull(){
		new CoordinateValidator("-34.603722", null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCoordinateValidatorConLatNull(){
		new CoordinateValidator(null, "58.381592");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCoordinateValidatorConLongVacia(){
		new CoordinateValidator("-34.603722", "");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCoordinateValidatorConLatVacia(){
		new CoordinateValidator("", "58.381592");
	}

	@Test
	public void testCoordinateValidatorConValoresValidos() {
		CoordinateValidator validator = new CoordinateValidator("-34.603722", "-58.381592");
		assertEquals(-34.603722, validator.getLatitud(), 0.001);
		assertEquals(-58.381592, validator.getLongitud(), 0.001);
	}

	@Test
	public void testCoordinateValidatorConValoresPositivos() {
		CoordinateValidator validator = new CoordinateValidator("34.603722", "58.381592");
		assertEquals(34.603722, validator.getLatitud(), 0.001);
		assertEquals(58.381592, validator.getLongitud(), 0.001);
	}

	@Test
	public void testCoordinateValidatorConCeros() {
		CoordinateValidator validator = new CoordinateValidator("0", "0");
		assertEquals(0.0, validator.getLatitud(), 0.001);
		assertEquals(0.0, validator.getLongitud(), 0.001);
	}

}
