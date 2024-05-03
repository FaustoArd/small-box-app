package com.lord.small_box.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import com.lord.small_box.utils.RSAKeyProperties;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

	@Autowired
	private final RSAKeyProperties keys;

	public SecurityConfiguration(RSAKeyProperties keys) {
		this.keys = keys;
	}

	@Bean
	AuthenticationManager authManager(UserDetailsService detailsService) {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(detailsService);
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		return new ProviderManager(daoAuthenticationProvider);
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http.csrf(csrf -> csrf.disable()).authorizeHttpRequests(auth -> {
			auth.requestMatchers("/api/v1/smallbox/authorization/**").permitAll();
			auth.requestMatchers("/api/v1/smallbox/containers/**").hasAnyRole("USER", "SUPERUSER", "ADMIN");
			auth.requestMatchers("/api/v1/smallbox/inputs/**").hasAnyRole("USER", "SUPERUSER", "ADMIN");
			auth.requestMatchers("/api/v1/smallbox/smallboxes/**").hasAnyRole("USER", "SUPERUSER", "ADMIN");
			auth.requestMatchers("/api/v1/smallbox/registration/**").hasRole("ADMIN");
			auth.requestMatchers("/api/v1/smallbox/organization/org/**").hasAnyRole("USER", "SUPERUSER", "ADMIN");
			auth.requestMatchers("/api/v1/smallbox/organization/all-orgs-by-id").hasRole("ADMIN");
			auth.requestMatchers("/api/v1/smallbox/organization/new-organization").hasRole("ADMIN");
			auth.requestMatchers("/api/v1/smallbox/organization/update-organization").hasRole("ADMIN");
			auth.requestMatchers("/api/v1/smallbox/organization/add-organization").hasRole("ADMIN");
			auth.requestMatchers("/api/v1/smallbox/organization/new-responsible").hasRole("ADMIN");
			auth.requestMatchers("/api/v1/smallbox/organization/responsible/**").hasRole("ADMIN");
			auth.requestMatchers("/api/v1/smallbox/organization/update-responsible").hasRole("ADMIN");
			auth.requestMatchers("/api/v1/smallbox/organization/all-responsibles").hasRole("ADMIN");
			auth.requestMatchers("/api/v1/smallbox/organization/all-orgs-by-user").hasAnyRole("USER", "SUPERUSER",
					"ADMIN");
			auth.requestMatchers("/api/v1/smallbox/organization/set-user-organization").hasAnyRole("USER", "SUPERUSER",
					"ADMIN");
			auth.requestMatchers("/api/v1/smallbox/organization/get-user-organization").hasAnyRole("USER", "SUPERUSER",
					"ADMIN");
			auth.requestMatchers("/api/v1/smallbox/organization/get-max-amount").hasAnyRole("USER", "SUPERUSER",
					"ADMIN");
			auth.requestMatchers("/api/v1/smallbox/organization/all-orgs").hasAnyRole("USER", "SUPERUSER",
					"ADMIN");
			auth.requestMatchers("/api/v1/smallbox/work-templates/create").hasAnyRole("SUPERUSER", "ADMIN");
			auth.requestMatchers("/api/v1/smallbox/work-templates/by-id/**").hasAnyRole("SUPERUSER", "ADMIN");
			auth.requestMatchers("/api/v1/smallbox/work-templates/by-user-id/**").hasAnyRole("SUPERUSER", "ADMIN");
			auth.requestMatchers("/api/v1/smallbox/work-templates/all-template-destinations").hasAnyRole("SUPERUSER",
					"ADMIN");
			auth.requestMatchers("/api/v1/smallbox/work-templates/create-template-destination").hasAnyRole("SUPERUSER",
					"ADMIN");
			auth.requestMatchers("/api/v1/smallbox/work-templates/delete-template-destination/**")
					.hasAnyRole("SUPERUSER", "ADMIN");
			auth.requestMatchers("/api/v1/smallbox/work-templates/delete-work-template-by-id/**").hasRole("ADMIN");
			auth.requestMatchers("/api/v1/smallbox/location-contracts/**").hasAnyRole("SUPERUSER", "ADMIN");
			auth.requestMatchers("/api/v1/smallox/dispatchs/**").hasAnyRole("SUPERUSER", "ADMIN");
			auth.requestMatchers("/api/v1/smallbox/template-destination/**").hasAnyRole("SUPERUSER", "ADMIN");
			auth.requestMatchers("/api/v1/smallbox/users/**").hasRole("ADMIN");
			auth.requestMatchers("/api/v1/smallbox/csv-utils/**").hasRole("ADMIN");
			auth.requestMatchers("/api/v1/smallbox/deposit-control/**").hasAnyRole("SUPERUSER", "ADMIN");
			auth.requestMatchers("/api/v1/smallbox/supply/**").hasAnyRole("USER","SUPERUSER", "ADMIN");
			auth.requestMatchers("/api/v1/smallbox/pdf-to-text/**").hasAnyRole("SUPERUSER", "ADMIN");
			auth.requestMatchers("/api/v1/smallbox/deposit-request/**").hasAnyRole("USER","SUPERUSER", "ADMIN");
			auth.requestMatchers("/api/v1/smallbox/deposit-receiver/**").hasAnyRole("SUPERUSER", "ADMIN");
			auth.anyRequest().authenticated();

		});
		http.oauth2ResourceServer(
				oauth -> oauth.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())));
		http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		return http.build();

	}

	@Bean
	JwtDecoder jwtDecoder() {
		return NimbusJwtDecoder.withPublicKey(keys.getPublicKey()).build();
	}

	@Bean
	JwtEncoder jwtEncoder() {
		JWK jwk = new RSAKey.Builder(keys.getPublicKey()).privateKey(keys.getPrivateKey()).build();
		JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
		return new NimbusJwtEncoder(jwks);
	}

	@Bean
	JwtAuthenticationConverter jwtAuthenticationConverter() {
		JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
		jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
		jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
		JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
		return jwtAuthenticationConverter;
	}

}
