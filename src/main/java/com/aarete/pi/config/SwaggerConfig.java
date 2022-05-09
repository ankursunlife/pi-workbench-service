package com.aarete.pi.config;

import static com.google.common.base.Predicates.or;
import static springfox.documentation.builders.PathSelectors.regex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.common.base.Predicate;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	public static final String AUTHORIZATION_HEADER = "Authorization";

	@Bean
	public Docket postsApi() {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo())
				.securityContexts(Arrays.asList(securityContext())).securitySchemes(Arrays.asList(apiKey())).select()
				.paths(postPaths()).build().globalResponseMessage(RequestMethod.GET, getCustomizedResponseMessages())
				.globalResponseMessage(RequestMethod.POST, getCustomizedResponseMessages())
				.globalResponseMessage(RequestMethod.PUT, getCustomizedResponseMessages());
	}

	private Predicate<String> postPaths() {
		return or(regex("/claim/*.*"), regex("/*"),regex("/playlist/*.*"),regex("/list/*.*"), regex("/v1/ad-graph/*.*"), regex("/datalake/*.*"));
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("Claims API")
				.description("This is Workbench service. It manages the data related to MyQueue and Claims.")
				.termsOfServiceUrl("https://www.aarete.com/").license("AArete License")
				.licenseUrl("https://www.aarete.com/our-solutions/").version("1.0").build();
	}

	private List<ResponseMessage> getCustomizedResponseMessages() {
		List<ResponseMessage> responseMessages = new ArrayList<>();
		responseMessages.add(new ResponseMessageBuilder().code(500).message("Request failed at server")
				.responseModel(new ModelRef("Error")).build());
		responseMessages.add(new ResponseMessageBuilder().code(403).message("You dont have access").build());
		return responseMessages;
	}

	private ApiKey apiKey() {
		return new ApiKey("Bearer", AUTHORIZATION_HEADER, "header");
	}

	private SecurityContext securityContext() {
		return SecurityContext.builder().securityReferences(defaultAuth()).build();
	}

	List<SecurityReference> defaultAuth() {
		AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
		AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
		authorizationScopes[0] = authorizationScope;
		return Arrays.asList(new SecurityReference("Bearer", authorizationScopes));
	}
}
