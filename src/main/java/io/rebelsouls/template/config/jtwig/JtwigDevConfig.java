package io.rebelsouls.template.config.jtwig;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.jtwig.environment.EnvironmentConfigurationBuilder;
import org.jtwig.hot.reloading.HotReloadingExtension;
import org.jtwig.spring.JtwigViewResolver;
import org.jtwig.spring.boot.config.JtwigViewResolverConfigurer;
import org.jtwig.web.servlet.JtwigRenderer;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import io.rebelsouls.jtwig.DefaultConfig;

@Configuration
@Profile("dev")
public class JtwigDevConfig extends JtwigConfig implements JtwigViewResolverConfigurer {

	@Override
	public void configure(JtwigViewResolver viewResolver) {
  		viewResolver.setRenderer(
  			new JtwigRenderer(EnvironmentConfigurationBuilder.configuration()
  					.extensions()
  						.add(new DefaultConfig())
  						.add(new HotReloadingExtension(TimeUnit.MILLISECONDS, 500))
  						.add(buildTranslateExtension())
  						.and()
  					.build()));
  		viewResolver.setPrefix(String.format("file:%s/", new File("src/main/resources/templates/").getAbsolutePath()));
	}
	
	@Bean
  public MessageSource getMessageSource() {
	    ReloadableResourceBundleMessageSource source = new ReloadableResourceBundleMessageSource();
      source.setBasename("i18n/templates/root");
      return source;
  }
}
