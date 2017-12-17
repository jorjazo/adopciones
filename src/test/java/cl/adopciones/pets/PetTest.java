package cl.adopciones.pets;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import cl.adopciones.organizations.Organization;
import cl.adopciones.users.Role;
import cl.adopciones.users.User;

public class PetTest {

	@Test
	public void testIsInOwnersOrganization() {
		Organization o = new Organization();
		o.setId(0L);
		
		Pet pet = new Pet();
		pet.setOrganization(o);
		assertTrue(pet.isInOrganization(o));
		
		o = new Organization();
		o.setId(0L);
		assertTrue(pet.isInOrganization(o));
		
		o.setId(1L);
		assertFalse(pet.isInOrganization(o));
	}
	
	@Test
	public void userInSameOrganizationShouldBeAbleToUpload() {
		Organization o = new Organization();
		o.setId(123L);
		
		User owner = new User();
		owner.setId(100L);
		
		User other = new User();
		other.setId(200L);
		
		Pet p = new Pet();
		p.setOrganization(o);
		p.setOwner(owner);
		
		assertFalse(p.canUploadPhotos(other));
		assertTrue(p.canUploadPhotos(owner));
		
		other.setOrganization(o);
		assertTrue(p.canUploadPhotos(other));
		
		other.setOrganization(null);
		other.getRoles().add(Role.ADMIN);
		assertTrue(p.canUploadPhotos(other));
	}
}
