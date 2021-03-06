package cl.adopciones.web.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cl.adopciones.pets.Pet;
import cl.adopciones.pets.PetPhotoLimitException;
import cl.adopciones.pets.PetService;
import cl.adopciones.users.User;
import cl.adopciones.web.forms.AdoptionForm;
import cl.adopciones.web.forms.PetForm;
import io.rebelsouls.events.Event;
import io.rebelsouls.events.EventLogger;
import io.rebelsouls.events.EventResult;
import io.rebelsouls.photos.PhotoSize;
import io.rebelsouls.storage.StorageResource;
import io.rebelsouls.storage.StorageResourceDescription;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/mascotas")
@Slf4j
public class PetsController {
	public static final String URL_PREFIX = "/mascotas";

	@Autowired
	private PetService petService;

	@GetMapping("nuevo")
	public String newItemForm(@ModelAttribute PetForm form, Model model) {
		return "pets/new";
	}

	@PostMapping("")
	public ModelAndView createItem(@Valid PetForm form, BindingResult result, Model model, @AuthenticationPrincipal User user, HttpServletResponse response) {
		
		if(result.hasErrors()) {
			ModelAndView mav = new ModelAndView("pets/new");
			mav.addObject("petForm", form);
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return mav;
		}
		
		Pet newItem = form.toItem();
		newItem = petService.create(newItem, user);

		response.setStatus(HttpServletResponse.SC_CREATED);
		response.setHeader(HttpHeaders.LOCATION, getPetUrl(newItem));
		return null;
	}

	@GetMapping("/{petId}")
	public String displayItem(
			@PathVariable("petId") Pet pet,
			Model model, @AuthenticationPrincipal User user,
			Locale locale,
			AdoptionForm form
			) {
		model.addAttribute("pet", pet);
		
		String photoUrlPrefix = "/mascotas/" + pet.getId() + "/fotos/";
		final String originalSuffix = "/original";
		final String fixedHeightSuffix = "/fixed_height";
		final String fixedWidthSuffix = "/fixed_width";
		
		int petPhotos = petService.listPetPhotos(pet).size();
		List<String> originalPhotos = new ArrayList<>(petPhotos);
		List<String> fixedHeightPhotos = new ArrayList<>(petPhotos);
		List<String> fixedWidthPhotos = new ArrayList<>(petPhotos);
		
		for (int i = 0; i < petPhotos; i++) {
			originalPhotos.add(photoUrlPrefix + i + originalSuffix);
			fixedHeightPhotos.add(photoUrlPrefix + i + fixedHeightSuffix);
			fixedWidthPhotos.add(photoUrlPrefix + i + fixedWidthSuffix);
		}
		
		model.addAttribute("originalPhotos", originalPhotos);
		model.addAttribute("fixedHeightPhotos", fixedHeightPhotos);
		model.addAttribute("fixedWidthPhotos", fixedWidthPhotos);
		
		model.addAttribute("canUploadPhotos", pet.canUploadPhotos(user));
		
		model.addAttribute("locale", locale);
		
		return "pets/display";
	}

	private String getPetUrl(Pet newItem) {
		return "/mascotas/" + newItem.getId();
	}
	
	@PostMapping("/{petId}/fotos")
    public void handleFileUpload(@RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes, @PathVariable("petId") Pet pet, HttpServletResponse response) {

		Event e = new Event("petPhotoUpload", EventResult.NOOK);
		if(pet == null) {
			e.extraField("reason", "Pet does not exists");
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			response.setHeader(HttpHeaders.LOCATION, "/");
			return;
		}
		
		String petUrl = getPetUrl(pet);
		try {
			
			String tempFileName = "/tmp/" + pet.getId() + "-" + file.getOriginalFilename();
			File tmpFile = new File(tempFileName);
			file.transferTo(tmpFile);
			petService.addPetPhoto(pet, tmpFile);
	        e.setResult(EventResult.OK);
		}
		catch (PetPhotoLimitException e1) {
			e.setResult(EventResult.ERROR);
			e.setError(e1);
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			petUrl += "?error=photolimit";
		}
		catch (Exception ex) {
			e.setResult(EventResult.ERROR);
			e.setError(ex);
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			petUrl += "?error=error";
		}
		finally {
			EventLogger.logEvent(log, e);
		}
		response.setStatus(HttpServletResponse.SC_SEE_OTHER);
		response.setHeader(HttpHeaders.LOCATION, petUrl);
    }
	
	@GetMapping("/{petId}/fotos/{photoNumber}/{photoSize}")
	@ResponseBody
	public ResponseEntity<InputStreamResource> showPhoto(@PathVariable("petId") Pet pet, @PathVariable("photoNumber") int photoNumber, @PathVariable("photoSize") String photoSizeName, HttpServletResponse response, HttpServletRequest request) throws IOException {
		
		long ifModifiedSinceHeader = request.getDateHeader(HttpHeaders.IF_MODIFIED_SINCE);
		PhotoSize photoSize = PhotoSize.fromName(photoSizeName);
		StorageResourceDescription metadata = petService.getPhotoCache(pet, photoNumber, photoSize);
		long lastModified = metadata != null ? metadata.getLastModified().getTime() : -1;
		

		if(ifModifiedSinceHeader > 0 && ifModifiedSinceHeader <= lastModified) {
			return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
		}

		StorageResource resource = petService.getPetPhoto(pet, photoNumber, photoSize);
		if(resource == null) {
			response.sendRedirect("/static/img/sin-foto-" + photoSize.name() + ".jpg");
			return new ResponseEntity<>(HttpStatus.FOUND);
		}

		return ResponseEntity
				.ok()
				.cacheControl(CacheControl.maxAge(1, TimeUnit.DAYS))
				.lastModified(resource.getLastModified().getTime())
				.contentLength(resource.getContentLength())
				.contentType(MediaType.parseMediaType(resource.getContentType()))
				.body(new InputStreamResource(resource.getContentStream()));
	}
	
	@PostMapping("/{petId}/adopciones")
	public void adopt(
			@Valid AdoptionForm form,
			BindingResult validation,
			@PathVariable("petId") Pet pet,
			HttpServletResponse response
			) {
		
		if(validation.hasErrors()) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.setHeader(HttpHeaders.LOCATION, URL_PREFIX + "/" + pet.getId());
		}
	}
}
