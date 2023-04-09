package com.stage.spring.configuration;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.stage.spring.JwtAndAuthConf.AuthEntryPointJwt;
import com.stage.spring.JwtAndAuthConf.AuthTokenFilter;
import com.stage.spring.JwtAndAuthConf.UserDetailsServiceImpl;

import lombok.extern.slf4j.Slf4j;



@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        //securedEnabled = true,
       //jsr250Enabled = true,
        prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	@Autowired
	UserDetailsServiceImpl userDetailsService;
	@Autowired
	private AuthEntryPointJwt unauthorizedHandler;
	/*@Bean
    public AuthenticationFailureHandler failureHandler() {
        return new CustomLoginFailureHandler("User/signin?error=true");
    }*/
	@Bean
	public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
	@Bean
	public AuthTokenFilter authenticationJwtTokenFilter() {
		return new AuthTokenFilter();
	}
	@Override
	public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
		authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	

    @Override
    protected void configure(HttpSecurity http) throws Exception {

		http.authorizeRequests()
				.antMatchers("/").permitAll()
				.antMatchers("/login").permitAll()
				.antMatchers("/dashboard/**").hasAnyRole("admin", "responsable", "membre")
				.and().formLogin(); }


	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication()
				.withUser("admin").password("{noop}password").roles("admin")
				.and()
				.withUser("responsible").password("{noop}password").roles("responsable")
				.and()
				.withUser("member").password("{noop}password").roles("membre")
				.and()
				.withUser("etudiant").password("{noop}password").roles("etudiant");
	}











		/*http.cors().and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests().antMatchers("/**").permitAll()
                .antMatchers("/api/test/**","/Offer/**","/Quizz/**","/Rating/**","/Quizz/Question/**","/Quizz/Answer/**","/Quizz/Score/**","/Quizz/Question/Answers/**","/Offer/Rating/**","/genrateAndDownloadQRCode/**","/genrateQRCode/**","/pdf/generate/**",
                		"/Article/**","/Article_comments/**","/Article_reactions/**","/User/checkemail").permitAll()
                
                .anyRequest().authenticated();
               /* .and()
                .formLogin()//.loginPage("http://127.0.0.1:8089/SpringMVC/User/signin")
                .failureHandler(failureHandler())
                .permitAll();*/

       /* http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);*/
    }

