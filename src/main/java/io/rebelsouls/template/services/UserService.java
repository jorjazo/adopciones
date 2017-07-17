package io.rebelsouls.template.services;

import java.util.Arrays;
import java.util.HashSet;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import io.rebelsouls.core.users.Role;
import io.rebelsouls.core.users.User;
import io.rebelsouls.core.users.UserAlreadyValidatedException;
import io.rebelsouls.template.repositories.UserRepository;
import io.rebelsouls.util.LogBuilder;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findFirstByUsername(username);
		if(user == null)
			throw new UsernameNotFoundException("Could not find user: " + username);
		
		return user;
	}

	public Page<User> getPageOfUsers(int page, int pagesize) {
		return userRepository.findAll(new PageRequest(page, pagesize));
	}

	public User createUser(String email, String password, String displayName, Role initialRole) {
		User newUser = new User();
		newUser.setDisplayName(displayName);
		newUser.setEmail(email);
		newUser.setUsername(email);
		newUser.setPassword(password);
		newUser.setApproved(false);
		newUser.setCredentialsExpired(false);
		newUser.setEnabled(false);
		newUser.setExpired(false);
		newUser.setLocked(false);
		newUser.setValidationToken(UUID.randomUUID().toString());
		newUser.setRoles(new HashSet<>(Arrays.asList(initialRole)));
		return userRepository.save(newUser);
	}

	public User loadUserById(Long userId) {
		return userRepository.findOne(userId);
	}
	
	public User validateUser(Long userId, String validationToken) throws UserAlreadyValidatedException {
		User user = userRepository.findOne(userId);
		if(user.isEnabled()) {
			log.warn(new LogBuilder().with("event", "validateUser").with("result", "userAlreadyValidated").build());
			throw new UserAlreadyValidatedException();
		}
		
		user.setEnabled(true);
		user.setValidationToken(null);
		return userRepository.save(user);
	}
}
