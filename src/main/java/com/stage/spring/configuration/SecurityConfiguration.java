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
        http.cors().and().csrf().disable()
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

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
/*@Slf4j
class CustomLoginFailureHandler implements AuthenticationFailureHandler {
    private String defaultFailureUrl;
   // private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    public CustomLoginFailureHandler(String defaultFailureUrl){
        this.defaultFailureUrl = defaultFailureUrl;
    }

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception
    ) throws ServletException, IOException {
    	String message = "";
    	if(exception.getClass() == UsernameNotFoundException.class) {
			//message = "cannot find a user";
		} else if(exception.getClass() == BadCredentialsException.class) {
			message = "check your password-----------mchet";
			log.info("check your password-----------mchet");
		}
     
    	request.getRequestDispatcher(String.format("/error?message=%s", message)).forward(request, response);
    }
}*/
