package tests.entities;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import model.entities.CostCalculator;
import model.entities.Localidad;

public class CostCalculatorTest {

	private CostCalculator costCalculator;
	private Localidad SanMiguel;
	private Localidad ManuelAlberti;
	private Localidad Cordoba;

	@Before
	public void setUp() throws Exception {
		// costoKm=10, recargo=15%, costoDifProv=50
		this.costCalculator = new CostCalculator(10.0, 15.0, 50.0);
		this.SanMiguel = new Localidad("San Miguel", "Buenos Aires", -34.603722, -58.381592);
		this.ManuelAlberti = new Localidad("Manuel Alberti", "Buenos Aires", -34.45878, -58.77661);
		this.Cordoba = new Localidad("Córdoba", "Córdoba", -31.420083, -64.188776);
	}

	@Test
	public void testCostoBasicoSinRecargo() {
		// 100 km * 10 $/km = 1000 (sin recargo porque es < 300km, misma provincia)
		double costo = costCalculator.calcularCosto(SanMiguel, ManuelAlberti, 100.0);
		assertEquals(1000.0, costo, 0.001);
	}

	@Test
	public void testCostoConRecargo() {
		// 400 km * 10 = 4000, recargo = 4000 * 0.15 = 600, total = 4600 (misma provincia)
		double costo = costCalculator.calcularCosto(SanMiguel, ManuelAlberti, 400.0);
		double costoEsperado = (400.0 * 10.0) + ((400.0 * 10.0) * 0.15);
		assertEquals(costoEsperado, costo, 0.001);
	}

	@Test
	public void testCostoConDiferenciaProvincia() {
		// 100 km * 10 = 1000 + 50 (diferente provincia) = 1050
		double costo = costCalculator.calcularCosto(SanMiguel, Cordoba, 100.0);
		assertEquals(1050.0, costo, 0.001);
	}

	@Test
	public void testCostoConRecargoYDiferenciaProvincia() {
		// 400 km * 10 = 4000, recargo = 600, diferente provincia = 50
		// total = 4000 + 600 + 50 = 4650
		double costo = costCalculator.calcularCosto(SanMiguel, Cordoba, 400.0);
		double costoEsperado = (400.0 * 10.0) + ((400.0 * 10.0) * 0.15) + 50.0;
		assertEquals(costoEsperado, costo, 0.001);
	}

	@Test
	public void testCostoCero() {
		// 0 km * 10 = 0 (sin recargo, sin diferencia de provincia)
		double costo = costCalculator.calcularCosto(SanMiguel, ManuelAlberti, 0.0);
		assertEquals(0.0, costo, 0.001);
	}

	@Test
	public void testCostoCon300kmExacto() {
		// A los 300km exacto no aplica recargo (> 300)
		double costo = costCalculator.calcularCosto(SanMiguel, ManuelAlberti, 300.0);
		assertEquals(3000.0, costo, 0.001);
	}

	@Test
	public void testCostoCon301km() {
		// A los 301km sí aplica recargo (> 300)
		double costoSinRecargo = 301.0 * 10.0;
		double recargo = costoSinRecargo * 0.15;
		double costoEsperado = costoSinRecargo + recargo;
		double costo = costCalculator.calcularCosto(SanMiguel, ManuelAlberti, 301.0);
		assertEquals(costoEsperado, costo, 0.001);
	}

	// TODO Agregar test con CostCalculator que tenga recargo=0
	// TODO Agregar test con CostCalculator que tenga costoDifProv=0
	// TODO Agregar test con valores negativos para costos (si es caso que debe validar)
	// TODO Agregar test verificando case-insensitivity en comparación de provincias
}
