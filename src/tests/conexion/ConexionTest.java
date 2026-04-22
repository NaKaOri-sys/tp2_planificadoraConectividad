package tests.conexion;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import model.Conexion;
import model.Localidad;

public class ConexionTest {
	private Localidad SanMiguel;
	private Localidad ManuelAlberti;
	
	@Before
	public void setUp() {
		this.SanMiguel = new Localidad("San Miguel", "Buenos Aires", -34.603722, -58.381592);
		this.ManuelAlberti = new Localidad("Manuel Alberti", "Buenos Aires", -34.45878 , -58.77661);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testConexionConOrigenNulo() {
		new Conexion(null, ManuelAlberti);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testConexionConDestinoNulo() {
		new Conexion(SanMiguel, null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testConexionConMismaLocalidad() {
		new Conexion(SanMiguel, SanMiguel);
	}
	
	@Test
	public void testConexionValida() {
		Conexion conexion = new Conexion(SanMiguel, ManuelAlberti);
		assertEquals(SanMiguel, conexion.getOrigen());
		assertEquals(ManuelAlberti, conexion.getDestino());
	}
	
	
}
