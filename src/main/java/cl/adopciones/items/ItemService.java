package cl.adopciones.items;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemService {

	@Autowired
	private ItemRepository itemRepository;
	
	public Item save(Item item) {
		return itemRepository.save(item);
	}

	public Item getItem(Long itemId) {
		return itemRepository.findOne(itemId);
	}
	
}
