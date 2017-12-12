package cl.adopciones.users;

import java.util.Arrays;
import java.util.HashSet;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import io.rebelsouls.events.Event;
import io.rebelsouls.events.EventLogger;
import io.rebelsouls.events.EventResult;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	@Override
	@Transactional
//	@PreAuthorize("!isAuthenticated() or hasRole('ADMIN') or #username == authentication.name")
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findFirstByUsername(username);
		if(user == null)
			throw new UsernameNotFoundException("Could not find user: " + username);
		
		return user;
	}

//	@PreAuthorize("hasRole('ADMIN')")
	public Page<User> getPageOfUsers(int page, int pagesize) {
		return userRepository.findAll(new PageRequest(page, pagesize));
	}

//	@PreAuthorize("hasRole('ADMIN')")
	public User createUser(String email, String password, String displayName, Role initialRole) {
	    Event event = new Event("createUser");
	    event.extraField("username", email);
	    event.extraField("role", initialRole.name());
	    User newUser = new User();

	    try {
    		newUser.setDisplayName(displayName);
    		newUser.setEmail(email);
    		newUser.setUsername(email);
    		newUser.setPassword(password);
    		newUser.setCredentialsExpired(false);
    		newUser.setEnabled(true);
    		newUser.setExpired(false);
    		newUser.setLocked(false);
    		newUser.setRoles(new HashSet<>(Arrays.asList(initialRole)));
    		newUser = userRepository.save(newUser);
    		event.extraField("id", String.valueOf(newUser.getId()));
    		event.setResult(EventResult.OK);
	    }
	    catch(Exception e) {
	        event.setError(e);
	        event.setResult(EventResult.ERROR);
	    }
	    finally {
            EventLogger.logEvent(log, event);
        }
	    
		return newUser;
	}

//	@PreAuthorize("hasRole('ADMIN') or (isAuthenticated() and principal.id == #userId)")
	public User loadUserById(Long userId) {
		return userRepository.findOne(userId);
	}
	
}
