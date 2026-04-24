package tests.grafo;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import model.Conexion;
import model.Grafo;
import model.Localidad;

public class GrafoTest {
	private Grafo grafo;
	private Localidad SanMiguel;
	private Localidad ManuelAlberti;
	private Localidad Polvorines;
	
	@Before
	public void setUp() {
		
		this.grafo = new Grafo();
		this.SanMiguel = new Localidad("San Miguel", "Buenos Aires", -34.603722, -58.381592);
		this.ManuelAlberti = new Localidad("Manuel Alberti", "Buenos Aires", -34.45878 , -58.77661);
		this.Polvorines = new Localidad("Polvorines", "Buenos Aires", -34.50316 , -58.70596);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testEliminarLocalidadNoExistente() {
		grafo.eliminarLocalidad(SanMiguel);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testObtenerConexionesDeLocalidadInexistente() {
		grafo.getConexiones(SanMiguel);
	}
	
	@Test
	public void testAgregarLocalidadNueva() {
		grafo.agregarLocalidad(SanMiguel);
		assertTrue(grafo.getLocalidades().contains(SanMiguel));
	}
	
	@Test
	public void testAgregarLocalidadExistente() {
		grafo.agregarLocalidad(ManuelAlberti);
		grafo.agregarLocalidad(ManuelAlberti);
		assertEquals(1, grafo.getLocalidades().size());
	}
	
	@Test
	public void testAgregarConexionBidireccional() {
		Conexion conexion = new Conexion(SanMiguel, ManuelAlberti, 10.0);
		
		grafo.agregarConexion(conexion);
		
		assertEquals(1, grafo.getConexiones(SanMiguel).size());
		assertEquals(1, grafo.getConexiones(ManuelAlberti).size());
		
		Conexion conexionInverso = grafo.getConexiones(ManuelAlberti).get(0);
		assertEquals(SanMiguel, conexionInverso.getDestino());
	}
	
	@Test
	public void testAgregarConexionConLocalidadesNoExistentes() {
		Conexion conexion = new Conexion(SanMiguel, Polvorines, 15.0);
		
		grafo.agregarConexion(conexion);
		
		assertTrue(grafo.getLocalidades().contains(SanMiguel));
		assertTrue(grafo.getLocalidades().contains(Polvorines));
	}
	
	@Test
	public void testEliminarLocalidadLimpiandoConexionesVecinos() {
		grafo.agregarConexion(new Conexion(SanMiguel, ManuelAlberti, 10.0));
		grafo.agregarConexion(new Conexion(SanMiguel, Polvorines, 15.0));
		grafo.agregarConexion(new Conexion(ManuelAlberti, Polvorines, 5.0));
		
		grafo.eliminarLocalidad(Polvorines);
		
		assertFalse(grafo.getLocalidades().contains(Polvorines));
		assertEquals(1, grafo.getConexiones(SanMiguel).size());
		assertEquals(1, grafo.getConexiones(ManuelAlberti).size());
	}
	
	@Test
	public void testEliminarLocalidadSinConexion() {
		
		grafo.agregarLocalidad(SanMiguel);
		grafo.eliminarLocalidad(SanMiguel);
		
		assertFalse(grafo.getLocalidades().contains(SanMiguel));
	}
	
	
	@Test
	public void testObtenerConexionesDeLocalidadSinConexiones() {
		grafo.agregarLocalidad(ManuelAlberti);
		assertTrue(grafo.getConexiones(ManuelAlberti).isEmpty());
	}
	
}
