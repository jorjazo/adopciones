package io.rebelsouls.test;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;

import lombok.Builder;
import lombok.Singular;

@Builder
public class CompositeResultMatcher implements ResultMatcher {

	@Singular
	private List<ResultMatcher> matchers;
	
	@Override
	public void match(MvcResult result) throws Exception {
		if(matchers == null) return;
		for(ResultMatcher rm: matchers)
			rm.match(result);
	}

	public static CompositeResultMatcher htmlOkResponse() {
		return CompositeResultMatcher.builder()
				.matcher(status().isOk())
				.matcher(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
				.matcher(content().encoding("UTF-8"))
				.build();
	}
	
	public static CompositeResultMatcher createdAndRedirectResponse(final String urlAntPattern) {
		return CompositeResultMatcher.builder()
				.matcher(status().isCreated())
				.matcher(redirectedUrlPattern(urlAntPattern))
				.build();
	}
	
	public static CompositeResultMatcher redirectResponseWithUrlPattern(final String urlAntPattern) {
		return CompositeResultMatcher.builder()
				.matcher(status().is3xxRedirection())
				.matcher(redirectedUrlPattern(urlAntPattern))
				.build();
	}
	
	public static CompositeResultMatcher redirectResponseWithUrl(final String url) {
		return CompositeResultMatcher.builder()
				.matcher(status().is3xxRedirection())
				.matcher(redirectedUrl(url))
				.build();
	}
	
	public static CompositeResultMatcher badRequestWithRedirect(final String url) {
		return CompositeResultMatcher.builder()
				.matcher(status().isBadRequest())
				.matcher(redirectedUrl(url))
				.build();
	}
}
