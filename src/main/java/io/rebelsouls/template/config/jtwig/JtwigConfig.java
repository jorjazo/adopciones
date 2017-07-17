package io.rebelsouls.template.config.jtwig;

import org.jtwig.environment.EnvironmentConfigurationBuilder;
import org.jtwig.spring.JtwigViewResolver;
import org.jtwig.spring.boot.config.JtwigViewResolverConfigurer;
import org.jtwig.translate.spring.SpringTranslateExtension;
import org.jtwig.translate.spring.SpringTranslateExtensionConfiguration;
import org.jtwig.web.servlet.JtwigRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import io.rebelsouls.jtwig.DefaultConfig;

@Configuration
@Profile("prod")
public class JtwigConfig implements JtwigViewResolverConfigurer {


    @Autowired
    private MessageSource messageSource;
      
  	@Override
  	public void configure(JtwigViewResolver viewResolver) {
  		viewResolver.setRenderer(
  				new JtwigRenderer(EnvironmentConfigurationBuilder.configuration()
  						.extensions()
  							.add(new DefaultConfig())
  							.and()
  						.build()));
  	}
  
  	@Bean
  	public MessageSource getMessageSource() {
  	    ResourceBundleMessageSource source = new ResourceBundleMessageSource();
  	    source.setBasename("tamplate-text");
  	    return source;
  	}
  	
  	public SpringTranslateExtension buildTranslateExtension() {
  	    SessionLocaleResolver resolver = new SessionLocaleResolver();
        SpringTranslateExtension translateExtension = new SpringTranslateExtension(
                SpringTranslateExtensionConfiguration
                    .builder(messageSource)
                    .withLocaleResolver(resolver)
                    .build()
                );
        return translateExtension;
  	}
}
