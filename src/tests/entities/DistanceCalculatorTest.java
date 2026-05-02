package tests.entities;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import model.entities.DistanceCalculator;
import model.entities.Localidad;

public class DistanceCalculatorTest {

	private DistanceCalculator distanceCalculator;
	private Localidad SanMiguel;
	private Localidad ManuelAlberti;
	private Localidad BuenosAires;
	private Localidad Cordoba;
	private Localidad JoseCPaz;

	@Before
	public void setUp() throws Exception {
		this.distanceCalculator = new DistanceCalculator();
		this.SanMiguel = new Localidad("San Miguel", "Buenos Aires", -34.603722, -58.381592);
		this.ManuelAlberti = new Localidad("Manuel Alberti", "Buenos Aires", -34.4563, -58.7753);
		this.BuenosAires = new Localidad("Buenos Aires", "Buenos Aires", -34.603722, -58.381592);
		this.Cordoba = new Localidad("Córdoba", "Córdoba", -31.420083, -64.188776);
	}

	@Test
	public void testDistanciaEntreDosPuntosProximos() {
		// San Miguel a Manuel Alberti (~39 km)
		double distancia = distanceCalculator.calcularDistanciaHaversine(SanMiguel, ManuelAlberti);
		assertTrue(distancia > 30 && distancia < 45);
	}

	@Test
	public void testDistanciaMismoPunto() {
		// La distancia entre un punto y sí mismo debe ser 0 o muy cercana a 0
		double distancia = distanceCalculator.calcularDistanciaHaversine(SanMiguel, BuenosAires);
		assertEquals(0.0, distancia, 0.001);
	}

	@Test
	public void testDistanciaEntreLocalidadesDistantes() {
		// San Miguel a Córdoba (646 km aproximadamente)
		double distancia = distanceCalculator.calcularDistanciaHaversine(SanMiguel, Cordoba);
		System.out.println(distancia);
		assertTrue(distancia > 600 && distancia < 700);
	}

	@Test
	public void testDistanciaEsSimetrica() {
		double distanciaAB = distanceCalculator.calcularDistanciaHaversine(SanMiguel, Cordoba);
		double distanciaBA = distanceCalculator.calcularDistanciaHaversine(Cordoba, SanMiguel);
		assertEquals(distanciaAB, distanciaBA, 0.001);
	}

	@Test
	public void testDistanciaNoNegativa() {
		double distancia = distanceCalculator.calcularDistanciaHaversine(SanMiguel, ManuelAlberti);
		assertTrue(distancia >= 0);
	}

	// TODO Agregar test con coordenadas de Ecuador (latitud=0)
	// TODO Agregar test con coordenadas cercanas a Antípodas (puntos opuestos en la
	// tierra)
	// TODO Agregar test con coordenadas en diferentes hemisferios
	// TODO Agregar test con coordenadas en los polos
	// TODO Agregar test para validar que el radio de la tierra (6371) se usa
	// correctamente
}
