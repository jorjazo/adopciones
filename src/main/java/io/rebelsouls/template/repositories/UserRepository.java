package io.rebelsouls.template.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;

import io.rebelsouls.core.users.User;

public interface UserRepository extends PagingAndSortingRepository<User, Long>{
	public User findFirstByUsername(String username);
}
