package io.rebelsouls.chile;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class GeoTest {

	@Test
	public void testGetProvinciaFromComuna() {
		assertEquals(Provincia.Tocopilla, Comuna.Tocopilla.getProvincia());
		assertEquals(Provincia.Tocopilla.getId(), Comuna.Tocopilla.getIdProvincia());
	}
	
	@Test
	public void getRegionFromProvincia() {
		assertEquals(Region.Biobío, Provincia.Bio_Bío.getRegion());
		assertEquals(Region.Biobío.getId(), Provincia.Bio_Bío.getIdRegion());
	}
}
