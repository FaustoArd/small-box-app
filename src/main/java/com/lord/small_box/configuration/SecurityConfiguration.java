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
	SecurityFilterChain filterChain(HttpSecurity http)throws Exception{
		
		http.csrf(csrf -> csrf.disable()).authorizeHttpRequests(auth ->{
			auth.requestMatchers("/api/v1/small-box/authorization/**").permitAll();
			auth.requestMatchers("/api/v1/small-box/containers/**").hasAnyRole("USER","SUPERUSER","ADMIN");
			auth.requestMatchers("/api/v1/small-box/inputs/**").hasAnyRole("USER","SUPERUSER","ADMIN");
			auth.requestMatchers("/api/v1/small-box/smallboxes/**").hasAnyRole("USER","SUPERUSER","ADMIN");
			auth.requestMatchers("/api/v1/small-box/registration/**").hasRole("ADMIN");
			auth.requestMatchers("/api/v1/small-box/organization/all-orgs").hasRole("ADMIN");
			auth.requestMatchers("/api/v1/small-box/organization/org/**").hasRole("ADMIN");
			auth.requestMatchers("/api/v1/small-box/organization/all-orgs-by-id").hasRole("ADMIN");
			auth.requestMatchers("/api/v1/small-box/organization/new-organization").hasRole("ADMIN");
			auth.requestMatchers("/api/v1/small-box/organization/update-organization").hasRole("ADMIN");
			auth.requestMatchers("/api/v1/small-box/organization/add-organization").hasRole("ADMIN");
			auth.requestMatchers("/api/v1/small-box/organization/new-responsible").hasRole("ADMIN");
			auth.requestMatchers("/api/v1/small-box/organization/responsible/**").hasRole("ADMIN");
			auth.requestMatchers("/api/v1/small-box/organization/update-responsible").hasRole("ADMIN");
			auth.requestMatchers("/api/v1/small-box/organization/all-responsibles").hasRole("ADMIN");
			auth.requestMatchers("/api/v1/small-box/organization/all-orgs-by-user").hasAnyRole("USER","SUPERUSER","ADMIN");
			auth.requestMatchers("/api/v1/small-box/work-templates/create").hasAnyRole("SUPERUSER","ADMIN");
			auth.requestMatchers("/api/v1/small-box/work-templates/by_id/**").hasAnyRole("SUPERUSER","ADMIN");
			auth.requestMatchers("/api/v1/small-box/work-templates/by_user_id/**").hasAnyRole("SUPERUSER","ADMIN");
			auth.requestMatchers("/api/v1/small-box/work-templates/all_template_destinations").hasAnyRole("SUPERUSER","ADMIN");
			auth.requestMatchers("/api/v1/small-box/work-templates/create_template_destination").hasAnyRole("SUPERUSER","ADMIN");
			auth.requestMatchers("/api/v1/small-box/work-templates/delete_template_destination/**").hasAnyRole("SUPERUSER","ADMIN");
			auth.requestMatchers("/api/v1/small-box/work-templates/delete_work_template_by_id/**").hasRole("ADMIN");
			auth.requestMatchers("/api/v1/small-box/location-contracts/**").hasAnyRole("SUPERUSER","ADMIN");
			auth.requestMatchers("/api/v1/small-box/users/**").hasRole("ADMIN");
			auth.anyRequest().authenticated();
			
			
		});
		http.oauth2ResourceServer(oauth -> oauth.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())));
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
