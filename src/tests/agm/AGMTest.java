package tests.agm;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import model.entities.AGM;
import model.entities.Conexion;
import model.entities.Grafo;
import model.entities.Localidad;
import model.strategy.IArbolGeneradorMinimo;
import model.strategy.Prim;

public class AGMTest {

	private Localidad SanMiguel;
	private Localidad ManuelAlberti;
	private Localidad Polvorines;
	private Localidad JoseCPaz;
	private IArbolGeneradorMinimo agmConPrim;
	private Grafo grafo;
	private AGM agm;

	@Before
	public void setUp() throws Exception {
		this.grafo = new Grafo();
		this.agm = new AGM();
		this.SanMiguel = new Localidad("San Miguel", "Buenos Aires", -34.603722, -58.381592);
		this.ManuelAlberti = new Localidad("Manuel Alberti", "Buenos Aires", -34.45878, -58.77661);
		this.Polvorines = new Localidad("Polvorines", "Buenos Aires", -34.58333, -58.71667);
		this.JoseCPaz = new Localidad("Jose C. Paz", "Buenos Aires", -34.5166, -58.7666);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAGMConGrafoNulo() {
		this.agmConPrim = new Prim();
		this.agm.setAGM(agmConPrim);
		this.agm.generarAGM(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAGMConGrafoVacio() {
		this.agmConPrim = new Prim();
		this.agm.setAGM(agmConPrim);
		this.agm.generarAGM(this.grafo);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAGMConGrafoSinConexiones() {
		this.grafo.agregarLocalidad(SanMiguel);
		this.grafo.agregarLocalidad(ManuelAlberti);
		this.agmConPrim = new Prim();
		this.agm.setAGM(agmConPrim);
		this.agm.generarAGM(this.grafo);
	}
	
	@Test
	public void testAGMConGrafoDesconectado() {
		grafo.agregarLocalidad(SanMiguel);
		grafo.agregarLocalidad(ManuelAlberti);
		grafo.agregarLocalidad(JoseCPaz);
		grafo.agregarConexion(new Conexion(SanMiguel, ManuelAlberti, 10.0));
		this.agmConPrim = new Prim();
		this.agm.setAGM(agmConPrim);
		
		Grafo agm = this.agm.generarAGM(this.grafo);
		
		assertEquals(10, agm.calcularCostoTotal(), 0.001);
	}
	
	@Test
	public void testCostoMinimoConCuatroLocalidades() {
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
		this.agmConPrim = new Prim();
		this.agm.setAGM(agmConPrim);
		double costoEsperado = 10.0 + 5.0 + 20.0;
		Grafo grafoAGM = this.agm.generarAGM(this.grafo);
		
		
		double costoTotalAGM = grafoAGM.calcularCostoTotal();
		
		assertEquals(costoEsperado, costoTotalAGM, 0.001);
	}

	@Test
	public void testAGMConGrafoCompleto() {
		grafo.agregarLocalidad(SanMiguel);
		grafo.agregarLocalidad(ManuelAlberti);
		grafo.agregarLocalidad(JoseCPaz);
		grafo.agregarConexion(new Conexion(SanMiguel, ManuelAlberti, 10.0));
		grafo.agregarConexion(new Conexion(ManuelAlberti, JoseCPaz, 5.0));
		grafo.agregarConexion(new Conexion(SanMiguel, JoseCPaz, 15.0));
		this.agmConPrim = new Prim();
		this.agm.setAGM(agmConPrim);

		Grafo agm = this.agm.generarAGM(this.grafo);
		
		assertEquals("AGM debe tener 3 localidades", 3, agm.getLocalidades().size());
		assertEquals("AGM debe tener 2 conexiones", 2, agm.getTodasLasConexiones().size());
	}

	@Test
	public void testAGMNoTieneCiclos() {
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
		this.agmConPrim = new Prim();
		this.agm.setAGM(agmConPrim);
		
		Grafo agm = this.agm.generarAGM(this.grafo);
		
		int numNodos = agm.getLocalidades().size();
		int numAristas = agm.getTodasLasConexiones().size();
		assertEquals("Un árbol debe tener N-1 aristas", numNodos - 1, numAristas);
	}

}