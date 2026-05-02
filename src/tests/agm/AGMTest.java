package tests.agm;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

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
	private IArbolGeneradorMinimo agmConPrimGrafoVacio;
	private IArbolGeneradorMinimo agmConPrimGrafoNull;
	private IArbolGeneradorMinimo agmConPrim;
	private Grafo grafo;

	@Before
	public void setUp() throws Exception {
		this.grafo = new Grafo();
		this.SanMiguel = new Localidad("San Miguel", "Buenos Aires", -34.603722, -58.381592);
		this.ManuelAlberti = new Localidad("Manuel Alberti", "Buenos Aires", -34.45878, -58.77661);
		this.Polvorines = new Localidad("Polvorines", "Buenos Aires", -34.58333, -58.71667);
		this.JoseCPaz = new Localidad("Jose C. Paz", "Buenos Aires", -34.5166, -58.7666);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAGMConGrafoNulo() {
		this.agmConPrimGrafoNull = new Prim(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAGMConGrafoVacio() {
		this.agmConPrimGrafoVacio = new Prim(this.grafo);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAGMConGrafoSinConexiones() {
		this.grafo.agregarLocalidad(SanMiguel);
		this.grafo.agregarLocalidad(ManuelAlberti);
		this.agmConPrim = new Prim(this.grafo);
		this.agmConPrim.generarAGM();
	}
	
	@Test
	public void testAGMConGrafoDesconectado() {
		grafo.agregarLocalidad(SanMiguel);
		grafo.agregarLocalidad(ManuelAlberti);
		grafo.agregarLocalidad(JoseCPaz);
		grafo.agregarConexion(new Conexion(SanMiguel, ManuelAlberti, 10.0));
		this.agmConPrim = new Prim(this.grafo);
		Grafo agm = this.agmConPrim.generarAGM();
		
		assertEquals(10, agm.calcularCostoTotal(), 0.001);
	}
	
	@Test
	public void testAGMConGrafoConCiclo() {
		Localidad joseCPaz = new Localidad("Jose C. Paz", "Buenos Aires", -34.0, -58.0);
		grafo.agregarLocalidad(SanMiguel);
		grafo.agregarLocalidad(ManuelAlberti);
		grafo.agregarLocalidad(joseCPaz);
		grafo.agregarConexion(new Conexion(SanMiguel, ManuelAlberti, 10.0));
		grafo.agregarConexion(new Conexion(ManuelAlberti, joseCPaz, 5.0));
		grafo.agregarConexion(new Conexion(SanMiguel, joseCPaz, 15.0));
		this.agmConPrim = new Prim(this.grafo);
		Grafo agm = this.agmConPrim.generarAGM();
		int cantidadDeConexionesEnAGM = agm.getTodasLasConexiones().size();
		int cantidadDeLocalidadesEnAGM = agm.getLocalidades().size();
		
		assertEquals(cantidadDeLocalidadesEnAGM - 1, cantidadDeConexionesEnAGM);
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
		this.agmConPrim = new Prim(this.grafo);
		Grafo grafoAGM = this.agmConPrim.generarAGM();
		
		double costoTotalAGM = grafoAGM.calcularCostoTotal();
		double costoEsperado = 10.0 + 5.0 + 20.0;
		
		assertEquals(costoEsperado, costoTotalAGM, 0.001);
	}

	// TODO Agregar test para AGM con grafo completo (todas las aristas conectadas)
	// TODO Agregar test para AGM con grafo donde hay múltiples AGM posibles (verificar que se elige uno)
	// TODO Agregar test para AGM con grafo de 5+ localidades
	// TODO Agregar test para verificar que el AGM tiene exactamente N-1 aristas (donde N es cantidad de nodos)
	// TODO Agregar test para verificar que el AGM es un árbol (no tiene ciclos)

}
