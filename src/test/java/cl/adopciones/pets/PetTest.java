package cl.adopciones.pets;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import cl.adopciones.organizations.Organization;
import cl.adopciones.users.User;

public class PetTest {

	@Test
	public void testIsInOwnersOrganization() {
		Organization o = new Organization();
		o.setId(0L);
		User user = new User();
		user.setId(1L);
		user.setOrganization(o);
		
		Pet pet = new Pet();
		pet.setOwner(user);
		pet.setOrganization(o);
		assertTrue(pet.isInOrganization(o));
		
		o = new Organization();
		o.setId(0L);
		assertTrue(pet.isInOrganization(o));
	}
	
}
