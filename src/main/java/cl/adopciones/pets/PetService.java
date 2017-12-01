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

	private static final int PET_MAX_PHOTOS = 6;
	private static final String TEMP_FILE_PATH_PREFIX = "/tmp/";
	private static final String THUMB_TEMP_FILE_SUFFIX = "-thumb.png";
	private static final int THUMB_HEIGHT = 200;

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
		return storageService.list(getPhotoStorageFolder(pet));
	}
	
	public void addPetPhoto(Pet pet, File photo) throws PetPhotoException {
		List<String> files = listPetPhotos(pet);
		
		int newPhotoNumber = files.size();
		if(newPhotoNumber >= PET_MAX_PHOTOS) {
			throw new PetPhotoLimitException();
		}
		
		File thumbFile = new File(getThumbTempFilePath(pet, newPhotoNumber));
		try {
			Thumbnails.of(photo).height(THUMB_HEIGHT).toFile(thumbFile);;
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

	private String getThumbTempFilePath(Pet pet, int photoNumber) {
		return TEMP_FILE_PATH_PREFIX + pet.getId() + "-" + photoNumber + THUMB_TEMP_FILE_SUFFIX;
	}
	
	private String getPhotoStoragePath(Pet pet, int photoNumber, PhotoSize size) {
		return getPhotoStorageFolder(pet) + photoNumber + "/" + size.name();
	}
	
	private String getPhotoStorageFolder(Pet pet) {
		return "/" + pet.getId() + "/photos/";
	}
}
