package tests.agm;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import model.AGM;
import model.Conexion;
import model.Grafo;
import model.Localidad;

public class AGMTest {

	private Localidad SanMiguel;
	private Localidad ManuelAlberti;
	private Localidad Polvorines;
	private Localidad JoseCPaz;

	@Before
	public void setUp() throws Exception {
		this.SanMiguel = new Localidad("San Miguel", "Buenos Aires", -34.603722, -58.381592);
		this.ManuelAlberti = new Localidad("Manuel Alberti", "Buenos Aires", -34.45878, -58.77661);
		this.Polvorines = new Localidad("Polvorines", "Buenos Aires", -34.58333, -58.71667);
		this.JoseCPaz = new Localidad("Jose C. Paz", "Buenos Aires", -34.5166, -58.7666);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAGMConGrafoNulo() {
		new AGM(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAGMConGrafoVacio() {
		new AGM(new Grafo());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAGMConGrafoSinConexiones() {
		Grafo grafo = new Grafo();
		grafo.agregarLocalidad(SanMiguel);
		grafo.agregarLocalidad(ManuelAlberti);
		new AGM(grafo);
	}
	
	@Test
	public void testAGMConGrafoDesconectado() {
		Grafo grafo = new Grafo();
		grafo.agregarLocalidad(SanMiguel);
		grafo.agregarLocalidad(ManuelAlberti);
		grafo.agregarConexion(new Conexion(SanMiguel, ManuelAlberti, 10.0));
		grafo.agregarLocalidad(new Localidad("Otra Localidad", "Buenos Aires", -34.0, -58.0));
		Grafo agm = new AGM(grafo).generarAGM();
		assertNotNull(agm);
	}
	
	@Test
	public void testAGMConGrafoValido() {
		Grafo grafo = new Grafo();
		grafo.agregarLocalidad(SanMiguel);
		grafo.agregarLocalidad(ManuelAlberti);
		grafo.agregarConexion(new Conexion(SanMiguel, ManuelAlberti, 10.0));
		Grafo agm = new AGM(grafo).generarAGM();
		assertNotNull(agm);
	}
	
	@Test
	public void testAGMConGrafoConCiclo() {
		Grafo grafo = new Grafo();
		Localidad joseCPaz = new Localidad("Jose C. Paz", "Buenos Aires", -34.0, -58.0);
		grafo.agregarLocalidad(SanMiguel);
		grafo.agregarLocalidad(ManuelAlberti);
		grafo.agregarLocalidad(joseCPaz);
		grafo.agregarConexion(new Conexion(SanMiguel, ManuelAlberti, 10.0));
		grafo.agregarConexion(new Conexion(ManuelAlberti, joseCPaz, 5.0));
		grafo.agregarConexion(new Conexion(SanMiguel, joseCPaz, 15.0));
		Grafo agm = new AGM(grafo).generarAGM();
		assertNotNull(agm);
	}
	
	@Test
	public void testAGMConGrafoConCicloYConexionesAdicionales() {
		Grafo grafo = new Grafo();
		Localidad joseCPaz = new Localidad("Jose C. Paz", "Buenos Aires", -34.51, -58.76);
		Localidad otraLocalidad = new Localidad("Otra Localidad", "Buenos Aires", -34.0, -58.0);
		grafo.agregarLocalidad(SanMiguel);
		grafo.agregarLocalidad(ManuelAlberti);
		grafo.agregarLocalidad(joseCPaz);
		grafo.agregarLocalidad(otraLocalidad);
		grafo.agregarConexion(new Conexion(SanMiguel, ManuelAlberti, 10.0));
		grafo.agregarConexion(new Conexion(ManuelAlberti, joseCPaz, 5.0));
		grafo.agregarConexion(new Conexion(SanMiguel, joseCPaz, 15.0));
		grafo.agregarConexion(new Conexion(SanMiguel, otraLocalidad, 20.0));
		Grafo agm = new AGM(grafo).generarAGM();
		assertNotNull(agm);
	}
	
	@Test
	public void conteoDeAristasEnAGM() {
		Grafo grafo = new Grafo();
		grafo.agregarLocalidad(SanMiguel);
		grafo.agregarLocalidad(ManuelAlberti);
		grafo.agregarLocalidad(JoseCPaz);
		grafo.agregarConexion(new Conexion(SanMiguel, ManuelAlberti, 10.0));
		grafo.agregarConexion(new Conexion(ManuelAlberti, JoseCPaz, 5.0));
		grafo.agregarConexion(new Conexion(SanMiguel, JoseCPaz, 15.0));
		
		AGM agm = new AGM(grafo);
		Grafo grafoAGM = agm.generarAGM();
		
		int cantidadDeConexionesEnAGM = grafoAGM.getTodasLasConexiones().size();
		int cantidadDeLocalidadesEnAGM = grafoAGM.getLocalidades().size();
		
		assertEquals(cantidadDeLocalidadesEnAGM - 1, cantidadDeConexionesEnAGM);
	}
	
	
	@Test
	public void testCostoMinimoConCuatroLocalidades() {
		Grafo grafo = new Grafo();
		grafo.agregarLocalidad(SanMiguel);
		grafo.agregarLocalidad(ManuelAlberti);
		grafo.agregarLocalidad(JoseCPaz);
		grafo.agregarLocalidad(Polvorines);
		grafo.agregarConexion(new Conexion(SanMiguel, ManuelAlberti, 10.0));
		grafo.agregarConexion(new Conexion(ManuelAlberti, JoseCPaz, 5.0));
		grafo.agregarConexion(new Conexion(SanMiguel, JoseCPaz, 15.0));
		grafo.agregarConexion(new Conexion(SanMiguel, Polvorines, 20.0));
		grafo.agregarConexion(new Conexion(ManuelAlberti, Polvorines, 25.0));
		grafo.agregarConexion(new Conexion(JoseCPaz, Polvorines, 30.0));
		
		AGM agm = new AGM(grafo);
		Grafo grafoAGM = agm.generarAGM();
		
		double costoTotalAGM = agm.calcularCostoTotalAGM(grafoAGM);
		double costoEsperado = 10.0 + 5.0 + 20.0; // Conexiones
		
		assertEquals(costoEsperado, costoTotalAGM, 0.001);
	}

}
