package cl.adopciones.pets;

import java.io.File;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import io.rebelsouls.chile.Comuna;
import io.rebelsouls.chile.Provincia;
import io.rebelsouls.chile.Region;
import io.rebelsouls.storage.StorageResource;
import io.rebelsouls.storage.StorageResourceDescription;

public interface PetService {

	public Pet save(Pet item);
	public Pet getPet(Long petId);

	public Page<Pet> searchPets(String name, PetType[] petType, Gender[] gender, PetAgeCategory[] petAgeCategory,
			PetSizeCategory[] petSizeCategory, Region region, Provincia provincia, Comuna comuna, Pageable page);

	public List<StorageResourceDescription> listPetPhotos(Pet pet);
	public void addPetPhoto(Pet pet, File photo) throws PetPhotoException;
	public StorageResource getPetPhoto(Pet pet, int photoNumber, PhotoSize size);
	public StorageResourceDescription getPhotoCache(Pet pet, int photoNumber, PhotoSize size);

}