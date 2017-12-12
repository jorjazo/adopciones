package io.rebelsouls.chile;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

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
		assertEquals(Region.Biobío.getOrdinal(), Provincia.Bio_Bío.getRegion().getOrdinal());
		assertEquals(Region.Biobío.getId(), Provincia.Bio_Bío.getIdRegion());
	}
	
	@Test
	public void shouldGet2ProvinciaFromAricaRegion() {
		assertEquals(2, Region.Arica_y_Parinacota.getProvincias().size());
	}
	
	@Test
	public void shouldGetSameComunasFromRegionAndProvincias() {
		Set<Comuna> comunasFromRegion = Region.Arica_y_Parinacota.getComunas();
		Set<Comuna> comunasFromProvincias = new HashSet<>();
		comunasFromProvincias.addAll(Provincia.Arica.getComunas());
		comunasFromProvincias.addAll(Provincia.Parinacota.getComunas());
		assertTrue(comunasFromProvincias.containsAll(comunasFromRegion));
		assertEquals(comunasFromProvincias.size(), comunasFromRegion.size());
	}
}
