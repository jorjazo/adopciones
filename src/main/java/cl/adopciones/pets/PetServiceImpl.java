package cl.adopciones.pets;

import static cl.adopciones.pets.PetSpecifications.ageIn;
import static cl.adopciones.pets.PetSpecifications.genderIn;
import static cl.adopciones.pets.PetSpecifications.hasName;
import static cl.adopciones.pets.PetSpecifications.inLocation;
import static cl.adopciones.pets.PetSpecifications.sizeIn;
import static cl.adopciones.pets.PetSpecifications.typeIn;
import static org.springframework.data.jpa.domain.Specifications.where;

import java.io.File;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import cl.adopciones.users.User;
import io.rebelsouls.chile.Comuna;
import io.rebelsouls.chile.Provincia;
import io.rebelsouls.chile.Region;
import io.rebelsouls.photos.PhotoSize;
import io.rebelsouls.photos.ThumbnailGenerator;
import io.rebelsouls.services.StorageService;
import io.rebelsouls.storage.StorageResource;
import io.rebelsouls.storage.StorageResourceDescription;
import lombok.Data;

@Service
@Data
public class PetServiceImpl implements PetService {

	@Autowired
	private PetRepository petRepository;

	@Autowired
	private StorageService storageService;

	private Map<String, StorageResourceDescription> cache = new HashMap<>();
	
	@Value("6")
	private int maxPhotosPerPet;
	
	@Override
	@PreAuthorize("isAuthenticated() && #item.isInOrganization(principal.organization)")
	@Transactional
	public Pet save(Pet pet) {
		Assert.notNull(pet, "Cannot save a null pet");
		Assert.notNull(pet.getId(), "Cannot save a pet without an Id");
		return petRepository.save(pet);
	}

	@Override
	@PreAuthorize("isAuthenticated() && hasRole('USER')")
	@Transactional
	public Pet create(Pet pet, User owner) {
		Assert.notNull(pet,	"Pet can not be null");
		Assert.isNull(pet.getId(),	"Id must not be set to create a pet");
		Assert.notNull(owner,	"Owner can not be null");
		Assert.notNull(owner.getOrganization(),	"Owner's organization can not be null");
		
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
	public void addPetPhoto(Pet pet, File original) throws Exception {
		List<StorageResourceDescription> files = listPetPhotos(pet);
		
		int newPhotoNumber = files.size();
		if(newPhotoNumber >= getMaxPhotosPerPet()) {
			throw new PetPhotoLimitException(newPhotoNumber, getMaxPhotosPerPet());
		}
		
		storageService.store(getPhotoStoragePath(pet, newPhotoNumber, PhotoSize.ORIGINAL), original);
		
		ThumbnailGenerator generator = new ThumbnailGenerator();
		storageService.store(
				getPhotoStoragePath(pet, newPhotoNumber, PhotoSize.FIXED_WIDTH),
				generator.generateThumb(original, PhotoSize.FIXED_WIDTH));
		storageService.store(
				getPhotoStoragePath(pet, newPhotoNumber, PhotoSize.FIXED_HEIGHT),
				generator.generateThumb(original, PhotoSize.FIXED_HEIGHT));
		
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

	private String getPhotoStoragePath(Pet pet, int photoNumber, PhotoSize size) {
		return getPhotoStorageFolder(pet) + photoNumber + "/" + size.getName();
	}
	
	private String getPhotoStorageFolder(Pet pet) {
		return pet.getId() + "/photos/";
	}
}
