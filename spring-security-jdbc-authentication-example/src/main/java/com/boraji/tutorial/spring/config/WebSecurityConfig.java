package com.boraji.tutorial.spring.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private DataSource dataSource;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		auth.jdbcAuthentication().dataSource(dataSource)
				.usersByUsernameQuery("select username, password, enabled" + " from users where username=?")
				.authoritiesByUsernameQuery("select username, authority " + "from authorities where username=?")
				.passwordEncoder(new BCryptPasswordEncoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.authorizeRequests().anyRequest().hasAnyRole("ADMIN", "USER").and().httpBasic().and().logout()
				.logoutUrl("/j_spring_security_logout").logoutSuccessUrl("/"); // Authenticate users with HTTP basic
																				// authentication
	}

	public static void main(String[] args) {
		String encoded = new BCryptPasswordEncoder().encode("admin");
		System.out.println(encoded);
	}
}
