package cl.adopciones.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.header.HeaderWriter;
import org.springframework.security.web.header.writers.CacheControlHeadersWriter;
import org.springframework.security.web.header.writers.DelegatingRequestMatcherHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				.anyRequest()
					.permitAll()
					.and()
			.formLogin()
			  .loginPage("/login")
				.and()
			.logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
				.logoutSuccessUrl("/")
				.and()
			.csrf()
				.ignoringAntMatchers("/logout")
				.and()
			.headers()
				.cacheControl().disable().addHeaderWriter(selectiveCacheControlHeaderWriter()).frameOptions().and().xssProtection();
				
				
	}
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);
	}
	
	//Workaround
	@Bean
    public HeaderWriter selectiveCacheControlHeaderWriter() {

        RequestMatcher paths = new OrRequestMatcher(
                new AntPathRequestMatcher("/mascotas/*/fotos/**")
        );

        return new DelegatingRequestMatcherHeaderWriter(
                new NegatedRequestMatcher(paths),
                new CacheControlHeadersWriter());
    }
}
