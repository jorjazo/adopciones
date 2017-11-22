package cl.adopciones.users;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserRepository extends PagingAndSortingRepository<User, Long>{
	public User findFirstByUsername(String username);
}
