package io.rebelsouls.jtwig;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.jtwig.environment.EnvironmentConfigurationBuilder;
import org.jtwig.extension.Extension;
import org.jtwig.functions.FunctionRequest;
import org.jtwig.functions.JtwigFunction;

public class DefaultConfig implements Extension {

	@Override
	public void configure(EnvironmentConfigurationBuilder configurationBuilder) {
		configurationBuilder
			.functions()
				.filter(function -> !function.name().equals("date"))
				.add(parseCustomFunctions())
			.and();

	}
	
	private List<JtwigFunction> parseCustomFunctions() {
		List<JtwigFunction> functions = new LinkedList<>();
		
		CustomFormatFunctions customFunctions = new CustomFormatFunctions();
		for(Method m : customFunctions.getClass().getMethods()) {
			org.jtwig.functions.annotations.JtwigFunction annotation = m.getAnnotation(org.jtwig.functions.annotations.JtwigFunction.class);
			if(annotation == null)
				continue;
			
			JtwigFunction newFunction = new JtwigFunction() {
				
				@Override
				public String name() {
					return annotation.value();
				}
				
				@Override
				public Object execute(FunctionRequest request) {
					try {
						return m.invoke(customFunctions, request);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
				
				@Override
				public Collection<String> aliases() {
					return Arrays.stream(annotation.aliases()).filter(s -> s != null && !"".equals(s)).collect(Collectors.toList());
				}
			};
			
			functions.add(newFunction);
		}
		
		return functions;
	}
}
