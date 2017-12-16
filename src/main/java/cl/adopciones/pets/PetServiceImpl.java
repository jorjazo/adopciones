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
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.adopciones.users.User;
import io.rebelsouls.chile.Comuna;
import io.rebelsouls.chile.Provincia;
import io.rebelsouls.chile.Region;
import io.rebelsouls.services.StorageService;
import io.rebelsouls.storage.StorageResource;
import io.rebelsouls.storage.StorageResourceDescription;
import net.coobird.thumbnailator.Thumbnails;

@Service
public class PetServiceImpl implements PetService {

	//debería mejorar la configuración del número máximo de fotos
	private static final int PET_MAX_PHOTOS = 6;
	private static final String TEMP_FILE_PATH_PREFIX = "/tmp/";
	private static final String THUMB_TEMP_FILE_SUFFIX = ".png";
	private static final int FIXED_PHOTO_SIZE = 200;

	@Autowired
	private PetRepository petRepository;

	@Autowired
	private StorageService storageService;

	private Map<String, StorageResourceDescription> cache = new HashMap<>();
	
	@Override
	@PreAuthorize("isAuthenticated() && #item.isInOrganization(principal.organization)")
	@Transactional
	public Pet save(Pet pet) {
		if(pet.getId() == null)
			throw new IllegalArgumentException("Cannot save a pet without an Id");
		return petRepository.save(pet);
	}

	@Override
	@PreAuthorize("isAuthenticated() && hasRole('USER')")
	@Transactional
	public Pet create(Pet pet, User owner) {
		if(pet.getId() != null)
			throw new IllegalArgumentException("Id must not be set to create a pet");
		
		pet.setCreationDateTime(LocalDateTime.now());
		pet.setOwner(owner);
		pet.setOrganization(owner.getOrganization());
		
		return petRepository.save(pet);
	}
	
	@Override
	@Transactional
	public Pet getPet(Long petId) {
		Pet pet = petRepository.findOne(petId);
		pet.getOwner().getOrganization().getContactEmail().length();  //preload
		return pet;
	}

	@Override
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
	
	@Override
	public List<StorageResourceDescription> listPetPhotos(Pet pet) {
		List<StorageResourceDescription> files = storageService.list(getPhotoStorageFolder(pet));
		files.forEach(srd -> cache.put(srd.getPath(), srd));
		return files;
	}
	
	@Override
	@PreAuthorize("isAuthenticated() and #pet.canUploadPhotos(principal)")
	public void addPetPhoto(Pet pet, File photo) throws PetPhotoException {
		List<StorageResourceDescription> files = listPetPhotos(pet);
		
		int newPhotoNumber = files.size();
		if(newPhotoNumber >= PET_MAX_PHOTOS) {
			throw new PetPhotoLimitException();
		}
		
		File fixedHeightFile = new File(getThumbTempFilePath(pet, newPhotoNumber, PhotoSize.fixed_height));
		File fixedWidthFile = new File(getThumbTempFilePath(pet, newPhotoNumber, PhotoSize.fixed_width));
		try {
			Thumbnails.of(photo).height(FIXED_PHOTO_SIZE).toFile(fixedHeightFile);
			Thumbnails.of(photo).width(FIXED_PHOTO_SIZE).toFile(fixedWidthFile);
		} catch (IOException e) {
			throw new PetPhotoException(e);
		}
		
        storageService.store(getPhotoStoragePath(pet, newPhotoNumber, PhotoSize.original), photo);
        storageService.store(getPhotoStoragePath(pet, newPhotoNumber, PhotoSize.fixed_height), fixedHeightFile);
        storageService.store(getPhotoStoragePath(pet, newPhotoNumber, PhotoSize.fixed_width), fixedWidthFile);
	}
	
	@Override
	public StorageResource getPetPhoto(Pet pet, int photoNumber, PhotoSize size) {
		String path = getPhotoStoragePath(pet, photoNumber, size);
		StorageResource resource = storageService.load(path);
		if(resource == null)
			cache.remove(path);
		else
			cache.put(path, resource);
		return resource;
	}
	
	@Override
	public StorageResourceDescription getPhotoCache(Pet pet, int photoNumber, PhotoSize size) {
		return cache.get(getPhotoStoragePath(pet, photoNumber, size));
	}

	private String getThumbTempFilePath(Pet pet, int photoNumber, PhotoSize size) {
		return TEMP_FILE_PATH_PREFIX + pet.getId() + "-" + photoNumber + "-" + size.name() + THUMB_TEMP_FILE_SUFFIX;
	}
	
	private String getPhotoStoragePath(Pet pet, int photoNumber, PhotoSize size) {
		return getPhotoStorageFolder(pet) + photoNumber + "/" + size.name();
	}
	
	private String getPhotoStorageFolder(Pet pet) {
		return pet.getId() + "/photos/";
	}
}
