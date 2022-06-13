package com.deta.kopmart_backend;

import com.sun.xml.bind.v2.schemagen.xmlschema.Appinfo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@SpringBootApplication
@EnableSwagger2
public class KopmartBackendApplication {

	/**
	 * @return the passwordEncoder
	 * @description PasswordEncoder Bean
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * @return WebMvcConfigurer
	 * @description CORS configuration for the application
	 */
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurerAdapter() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**");
			}
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(KopmartBackendApplication.class, args);
	}

	/**
	 * @return Swagger2 API documentation
	 * @description Swagger2 API documentation
	 */
	@Bean
	public Docket swaggerConfiguration() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.deta.kopmart_backend.controller"))
				.paths(PathSelectors.any())
				.build()
				.apiInfo(apiInfo());
	}

	/**
	 * @return ApiInfo
	 * @author deta
	 */
	private ApiInfo apiInfo() {
		ApiInfo appinfo = new ApiInfo(
				"Kopmart API Documentation",
				"Ini adalah demo API dari Kopmart dengan Spring Boot",
				"1.0",
				"Terms of service",
				new Contact("Putra Nugraha","www.detadev.com","222011683@stis.ac.id"),
				"Apache License",
				"https://www.apache.org/licenses/LICENSE-2.0",
				Collections.emptyList()
		);
		return appinfo;
	}
}
