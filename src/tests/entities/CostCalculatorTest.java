package tests.entities;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import model.entities.CostCalculator;
import model.entities.Localidad;

public class CostCalculatorTest {

	private CostCalculator costCalculator;
	private CostCalculator costoSinRecargo;
	private Localidad SanMiguel;
	private Localidad ManuelAlberti;
	private Localidad Cordoba;
	private Localidad cordobaMinus;

	@Before
	public void setUp() throws Exception {
		this.costCalculator = new CostCalculator(10.0, 15.0, 50.0);
		this.costoSinRecargo = new CostCalculator(10.0, 0, 50.0);
		this.SanMiguel = new Localidad("San Miguel", "Buenos Aires", -34.603722, -58.381592);
		this.ManuelAlberti = new Localidad("Manuel Alberti", "Buenos Aires", -34.45878, -58.77661);
		this.Cordoba = new Localidad("Córdoba", "Córdoba", -31.420083, -64.188776);
		this.cordobaMinus = new Localidad("Villa Maria", "córdoba", -32.4103, -63.2314);
	}

	@Test
	public void testCostoBasicoSinRecargo() {
		double costo = costCalculator.calcularCosto(SanMiguel, ManuelAlberti, 100.0);
		assertEquals(1000.0, costo, 0.001);
	}

	@Test
	public void testCostoConRecargo() {
		double costoEsperado = (400.0 * 10.0) + ((400.0 * 10.0) * 0.15);
		double costo = costCalculator.calcularCosto(SanMiguel, ManuelAlberti, 400.0);
		assertEquals(costoEsperado, costo, 0.001);
	}
	
	@Test
	public void testCostoSinRecargoMismaProvincia() {
		double costoEsperado = (400.0 * 10.0);
		double costo = costoSinRecargo.calcularCosto(SanMiguel, ManuelAlberti, 400.0);
		assertEquals(costoEsperado, costo, 0.001);
	}
	
	@Test
	public void testCostoSinRecargoDifProvincia() {
		double costoEsperado = (400.0 * 10.0) + 50;
		double costo = costoSinRecargo.calcularCosto(SanMiguel, Cordoba, 400.0);
		assertEquals(costoEsperado, costo, 0.001);
	}

	@Test
	public void testCostoConDiferenciaProvincia() {
		double costo = costCalculator.calcularCosto(SanMiguel, Cordoba, 100.0);
		assertEquals(1050.0, costo, 0.001);
	}

	@Test
	public void testCostoConRecargoYDiferenciaProvincia() {
		double costoEsperado = (400.0 * 10.0) + ((400.0 * 10.0) * 0.15) + 50.0;
		double costo = costCalculator.calcularCosto(SanMiguel, Cordoba, 400.0);
		assertEquals(costoEsperado, costo, 0.001);
	}

	@Test
	public void testCostoCero() {
		double costo = costCalculator.calcularCosto(SanMiguel, ManuelAlberti, 0.0);
		assertEquals(0.0, costo, 0.001);
	}

	@Test
	public void testCostoCon300kmExacto() {
		double costo = costCalculator.calcularCosto(SanMiguel, ManuelAlberti, 300.0);
		assertEquals(3000.0, costo, 0.001);
	}

	@Test
	public void testCostoCon301km() {
		double costoSinRecargo = 301.0 * 10.0;
		double recargo = costoSinRecargo * 0.15;
		double costoEsperado = costoSinRecargo + recargo;
		double costo = costCalculator.calcularCosto(SanMiguel, ManuelAlberti, 301.0);
		assertEquals(costoEsperado, costo, 0.001);
	}
	
	@Test
	public void testCostoConLocalidadProvMinusculaMismaProvincia() {
		double costoEsperado = (300.0 * 10.0);
		double costo = costCalculator.calcularCosto(cordobaMinus, Cordoba, 300.0);
		
		assertEquals(costoEsperado, costo, 0.001);
	}

	@Test
	public void testCostoConCostoKmCero() {
		CostCalculator calc = new CostCalculator(0.0, 15.0, 75.0);
		double costo = calc.calcularCosto(SanMiguel, Cordoba, 500.0);
		assertEquals(75.0, costo, 0.001);
	}
}