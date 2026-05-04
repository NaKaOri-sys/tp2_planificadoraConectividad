package tests.agm;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import model.entities.Conexion;
import model.entities.Grafo;
import model.entities.Localidad;
import model.strategy.Prim;

public class PrimTest {

	private Prim prim;
	private Grafo grafo;
	private Localidad caba;
	private Localidad laPlata;
	private Localidad rosario;
	private Localidad cordoba;
	private Localidad mendoza;

	@Before
	public void setUp() throws Exception {
		this.prim = new Prim();
		this.grafo = new Grafo();
		this.caba = new Localidad("Buenos Aires", "Buenos Aires", -34.6037, -58.3816);
		this.laPlata = new Localidad("La Plata", "Buenos Aires", -34.9205, -57.9545);
		this.rosario = new Localidad("Rosario", "Santa Fe", -32.9468, -60.6393);
		this.cordoba = new Localidad("Córdoba", "Córdoba", -31.4135, -64.1889);
		this.mendoza = new Localidad("Mendoza", "Mendoza", -32.8895, -68.8458);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGenerarAGMConGrafoNulo() {
		prim.generarAGM(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGenerarAGMConGrafoVacio() {
		prim.generarAGM(grafo);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGenerarAGMConGrafoSinConexiones() {
		grafo.agregarLocalidad(caba);
		grafo.agregarLocalidad(laPlata);
		prim.generarAGM(grafo);
	}

	@Test
	public void testGenerarAGMConTresLocalidades() {
		grafo.agregarLocalidad(caba);
		grafo.agregarLocalidad(laPlata);
		grafo.agregarLocalidad(rosario);

		grafo.agregarConexion(new Conexion(caba, laPlata, 100.0));
		grafo.agregarConexion(new Conexion(laPlata, rosario, 150.0));
		grafo.agregarConexion(new Conexion(caba, rosario, 200.0));

		Grafo agm = prim.generarAGM(grafo);

		assertEquals("AGM debe contener 3 localidades", 3, agm.getLocalidades().size());
		assertEquals("AGM con 3 nodos debe tener 2 aristas", 2, agm.getTodasLasConexiones().size());
	}

	@Test
	public void testGenerarAGMConGrafoDesconexo() {
		grafo.agregarLocalidad(caba);
		grafo.agregarLocalidad(laPlata);
		grafo.agregarLocalidad(rosario);
		grafo.agregarLocalidad(cordoba);

		grafo.agregarConexion(new Conexion(caba, laPlata, 50.0));

		grafo.agregarConexion(new Conexion(rosario, cordoba, 75.0));

		Grafo agm = prim.generarAGM(grafo);

		assertTrue("AGM debe contener algunas localidades", agm.getLocalidades().size() > 0);
		assertTrue("AGM debe contener menos de 4 localidades (no todas)", agm.getLocalidades().size() < 4);

		int numNodos = agm.getLocalidades().size();
		int numAristas = agm.getTodasLasConexiones().size();
		assertEquals("AGM debe ser un árbol (n-1 aristas)", numNodos - 1, numAristas);
	}

	@Test
	public void testVerificaCostoMinimoDelAGM() {
		grafo.agregarLocalidad(caba);
		grafo.agregarLocalidad(laPlata);
		grafo.agregarLocalidad(rosario);
		grafo.agregarLocalidad(cordoba);

		grafo.agregarConexion(new Conexion(caba, laPlata, 10.0));
		grafo.agregarConexion(new Conexion(caba, rosario, 50.0));
		grafo.agregarConexion(new Conexion(caba, cordoba, 30.0));
		grafo.agregarConexion(new Conexion(laPlata, rosario, 20.0));
		grafo.agregarConexion(new Conexion(laPlata, cordoba, 40.0));
		grafo.agregarConexion(new Conexion(rosario, cordoba, 15.0));

		Grafo agm = prim.generarAGM(grafo);

		double costoEsperado = 10.0 + 20.0 + 15.0;
		double costoReal = agm.calcularCostoTotal();

		assertEquals("El costo del AGM debe ser mínimo", costoEsperado, costoReal, 0.001);
	}

	@Test
	public void testGenerarAGMConGrafoCompleto() {
		grafo.agregarLocalidad(caba);
		grafo.agregarLocalidad(laPlata);
		grafo.agregarLocalidad(rosario);
		grafo.agregarLocalidad(cordoba);
		grafo.agregarLocalidad(mendoza);

		grafo.agregarConexion(new Conexion(caba, laPlata, 10.0));
		grafo.agregarConexion(new Conexion(caba, rosario, 20.0));
		grafo.agregarConexion(new Conexion(caba, cordoba, 30.0));
		grafo.agregarConexion(new Conexion(caba, mendoza, 40.0));
		grafo.agregarConexion(new Conexion(laPlata, rosario, 15.0));
		grafo.agregarConexion(new Conexion(laPlata, cordoba, 25.0));
		grafo.agregarConexion(new Conexion(laPlata, mendoza, 35.0));
		grafo.agregarConexion(new Conexion(rosario, cordoba, 5.0));
		grafo.agregarConexion(new Conexion(rosario, mendoza, 45.0));
		grafo.agregarConexion(new Conexion(cordoba, mendoza, 50.0));

		Grafo agm = prim.generarAGM(grafo);

		assertEquals("AGM de grafo completo debe tener todas las localidades", 5, agm.getLocalidades().size());
		assertEquals("AGM de 5 nodos debe tener 4 aristas", 4, agm.getTodasLasConexiones().size());
	}

	@Test
	public void testGenerarAGMConGrafoCiclos() {
		grafo.agregarLocalidad(caba);
		grafo.agregarLocalidad(laPlata);
		grafo.agregarLocalidad(rosario);
		grafo.agregarLocalidad(cordoba);

		grafo.agregarConexion(new Conexion(caba, laPlata, 10.0));
		grafo.agregarConexion(new Conexion(laPlata, rosario, 20.0));
		grafo.agregarConexion(new Conexion(rosario, cordoba, 15.0));
		grafo.agregarConexion(new Conexion(cordoba, caba, 25.0)); // Crea un ciclo
		grafo.agregarConexion(new Conexion(caba, rosario, 50.0)); // Otro ciclo

		Grafo agm = prim.generarAGM(grafo);

		int numNodos = agm.getLocalidades().size();
		int numAristas = agm.getTodasLasConexiones().size();

		assertEquals("AGM debe incluir todas las localidades", 4, numNodos);
		assertEquals("Un árbol de N nodos debe tener N-1 aristas (sin ciclos)", numNodos - 1, numAristas);
	}

	@Test
	public void testAGMSeleccionaAristaMasBarata() {
		grafo.agregarLocalidad(caba);
		grafo.agregarLocalidad(laPlata);
		grafo.agregarLocalidad(rosario);

		grafo.agregarConexion(new Conexion(caba, laPlata, 100.0));
		grafo.agregarConexion(new Conexion(caba, rosario, 50.0));
		grafo.agregarConexion(new Conexion(laPlata, rosario, 30.0));

		Grafo agm = prim.generarAGM(grafo);

		double costoEsperado = 50.0 + 30.0;
		double costoReal = agm.calcularCostoTotal();

		assertEquals("AGM debe seleccionar las aristas más baratas", costoEsperado, costoReal, 0.001);
	}
}