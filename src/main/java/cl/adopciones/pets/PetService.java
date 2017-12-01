package cl.adopciones.pets;

import static cl.adopciones.pets.PetSpecifications.ageIn;
import static cl.adopciones.pets.PetSpecifications.genderIn;
import static cl.adopciones.pets.PetSpecifications.hasName;
import static cl.adopciones.pets.PetSpecifications.inLocation;
import static cl.adopciones.pets.PetSpecifications.sizeIn;
import static cl.adopciones.pets.PetSpecifications.typeIn;
import static org.springframework.data.jpa.domain.Specifications.where;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import io.rebelsouls.chile.Comuna;
import io.rebelsouls.chile.Provincia;
import io.rebelsouls.chile.Region;
import io.rebelsouls.services.StorageService;
import io.rebelsouls.storage.StorageResource;
import net.coobird.thumbnailator.Thumbnails;

@Service
public class PetService {

	@Autowired
	private PetRepository petRepository;

	@Autowired
	private StorageService storageService;
	
	public Pet save(Pet item) {
		return petRepository.save(item);
	}

	@Transactional
	public Pet getPet(Long petId) {
		Pet pet = petRepository.findOne(petId);
		pet.getOwner().getOrganization().getContactEmail();  //preload
		return pet;
	}

	public Page<Pet> searchPets(String name, PetType[] petType, Gender[] gender, PetAgeCategory[] petAgeCategory,
			PetSizeCategory[] petSizeCategory, Region region, Provincia provincia, Comuna comuna, Pageable page) {

		return petRepository.findAll(
				where(hasName(name))
				.and(typeIn(petType))
				.and(genderIn(gender))
				.and(sizeIn(petSizeCategory))
				.and(ageIn(petAgeCategory))
				.and(inLocation(region, provincia, comuna))
				, page);
	}
	
	public List<String> listPetPhotos(Pet pet) {
		return storageService.list("/" + pet.getId() + "/photos/");
	}
	
	public void addPetPhoto(Pet pet, File photo) throws PetPhotoException {
		List<String> files = listPetPhotos(pet);
		
		int newPhotoNumber = files.size();
		if(newPhotoNumber >= 10) {
			throw new PetPhotoLimitException();
		}
		
		File thumbFile = new File("/tmp/" + pet.getId() + "-" + newPhotoNumber + "-thumb.png");
		try {
			Thumbnails.of(photo).width(150).toFile(thumbFile);;
		} catch (IOException e) {
			throw new PetPhotoException(e);
		}
		
        storageService.store(getPhotoStoragePath(pet, newPhotoNumber, PhotoSize.original), photo);
        storageService.store(getPhotoStoragePath(pet, newPhotoNumber, PhotoSize.thumb), thumbFile);
	}
	
	public StorageResource getPetPhoto(Pet pet, int photoNumber, PhotoSize size) {
		StorageResource resource = storageService.load(getPhotoStoragePath(pet, photoNumber, size));
		return resource;
	}
	
	private String getPhotoStoragePath(Pet pet, int photoNumber, PhotoSize size) {
		return "/" + pet.getId() + "/photos/" + photoNumber + "/" + size.name();
	}
}
