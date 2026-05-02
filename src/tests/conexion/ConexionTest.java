package tests.conexion;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import model.entities.Conexion;
import model.entities.Localidad;

public class ConexionTest {
	private Localidad SanMiguel;
	private Localidad ManuelAlberti;
	private Conexion conexionSanMiguelManuelAlberti;

	@Before
	public void setUp() {
		this.SanMiguel = new Localidad("San Miguel", "Buenos Aires", -34.603722, -58.381592);
		this.ManuelAlberti = new Localidad("Manuel Alberti", "Buenos Aires", -34.45878, -58.77661);
		this.conexionSanMiguelManuelAlberti = new Conexion(SanMiguel, ManuelAlberti, 10.0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConexionConOrigenNulo() {
		new Conexion(null, ManuelAlberti, 10.0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConexionConDestinoNulo() {
		new Conexion(SanMiguel, null, 10.0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConexionConMismaLocalidad() {
		new Conexion(SanMiguel, SanMiguel, 10.0);
	}

	@Test
	public void testConexionValida() {
		assertEquals(SanMiguel, this.conexionSanMiguelManuelAlberti.getOrigen());
		assertEquals(ManuelAlberti, this.conexionSanMiguelManuelAlberti.getDestino());
	}

	@Test
	public void testConexionObtenerCostoCorrecto() {
		double costo = this.conexionSanMiguelManuelAlberti.getCosto();

		assertEquals(10.0, costo, 0.01);
	}

	@Test
	public void testConexionAgregarConexionCostoCero() {
		Conexion conexion = new Conexion(ManuelAlberti, SanMiguel, 0.00);
		assertEquals(0.00, conexion.getCosto(), 0.01);
	}

	@Test
	public void testConexionEquals() {
		Conexion conexion = new Conexion(SanMiguel, ManuelAlberti, 10.00);

		assertTrue(conexion.equals(this.conexionSanMiguelManuelAlberti));
	}

	@Test
	public void testConexionNotEquals() {
		Conexion conexion = new Conexion(ManuelAlberti, SanMiguel, 10.00);

		assertTrue(!conexion.equals(this.conexionSanMiguelManuelAlberti));
	}

	// TODO Agregar test para conexión con costo muy grande
}
