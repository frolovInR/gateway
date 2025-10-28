package com.example.apigateway;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;


@EnableDiscoveryClient
@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "API Gateway", version = "1.0", description = "Documentation API Gateway v1.0"))
public class ApigatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApigatewayApplication.class, args);
	}

	@Bean
	public RouteLocator routeLocator(RouteLocatorBuilder builder) {
		return builder
				.routes()
				.route(r -> r.path("/service/calendar/v3/api-docs").and().method(HttpMethod.GET)
						.uri("https://devfrolov.ru/service/calendar/v3/api-docs"))
				.route(r -> r.path("/service/socialNetwork/v3/api-docs").and().method(HttpMethod.GET)
						.uri("https://devfrolov.ru/service/socialNetwork/v3/api-docs"))
				.route(r -> r.path("/service/id/v3/api-docs").and().method(HttpMethod.GET)
						.uri("https://devfrolov.ru/service/id/v3/api-docs"))
				.route(r -> r.path("/service/screener/v3/api-docs").and().method(HttpMethod.GET)
						.uri("https://devfrolov.ru/service/screener/v3/api-docs"))
				.route(r -> r.path("/service/portfolio/v3/api-docs").and().method(HttpMethod.GET)
						.uri("https://devfrolov.ru/service/portfolio/v3/api-docs"))
				.build();
	}
}
