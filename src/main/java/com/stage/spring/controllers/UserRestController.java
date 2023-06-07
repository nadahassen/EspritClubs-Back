package com.stage.spring.controllers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import com.stage.spring.service.ServiceUser;
import org.apache.catalina.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stage.spring.JwtAndAuthConf.JwtResponse;
import com.stage.spring.JwtAndAuthConf.JwtUtils;
import com.stage.spring.JwtAndAuthConf.LoginRequest;
import com.stage.spring.JwtAndAuthConf.UserDetailsImpl;
import com.stage.spring.configuration.SecurityUtility;
import com.stage.spring.entity.AccountResponse;
import com.stage.spring.entity.Mail;
import com.stage.spring.entity.ResetPassword;
import com.stage.spring.entity.User;
import com.stage.spring.repository.RoleRepository;
import com.stage.spring.repository.UserRepository;
import com.stage.spring.service.IServiceMail;
import com.stage.spring.service.IServiceUser;
import com.stage.spring.utils.MailConstructor;
import com.stage.spring.utils.UserCode;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@RestController
@RequestMapping("/User")
@CrossOrigin(origins = "http://localhost:4200")

public class UserRestController {
	@Autowired
	UserRepository userRepository;
	@Autowired 
	IServiceUser serviceUser;
	@Autowired
	RoleRepository roleRepository;
	@Autowired
	AuthenticationManager authenticationManager;
	@Autowired
	JwtUtils jwtUtils;
	@Autowired
	IServiceMail sericeMail;
	
	 @Autowired
	    private MailConstructor mailConstructor;
	 @Autowired
	 private JavaMailSender mailSender;
	
	
	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser( @RequestBody LoginRequest loginRequest) {
		
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);
		
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();		
		List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());

		return ResponseEntity.ok(new JwtResponse(jwt, 
												 userDetails.getId(), 
												 userDetails.getUsername(), 
												 userDetails.getEmail(), 
												 roles));
	}
	@PostMapping("/adduser")
	public String registration(@RequestParam("user") String user,@RequestParam("file")
	MultipartFile file) 
			throws JsonMappingException, JsonProcessingException {
		User u = new ObjectMapper().readValue(user, User.class);
	

		return serviceUser.addUser(u,file);
	}
	@GetMapping("/confirm/{token}")
	public String confirm(@PathVariable("token") String token) {
		return serviceUser.confirmToken(token);
	}
	@PostMapping("/forgot/{username}")
	public String forgotPassworProcess(@PathVariable("username") String username,HttpServletRequest request) {
		return serviceUser.forgotPassword(username, request);
	}
	@PostMapping("/reset/{token}/{newpassword}")
	public String resetPassword(@PathVariable("token") String token,@PathVariable("newpassword") String newpassword ) {
		return serviceUser.fogetPasswordSetting(token, newpassword);
	}
	@GetMapping("/getusers")
	public List<User> retrieveAllUsers(){
		return serviceUser.getusers();
	}
	@GetMapping("/getusers2")
	public List<User> retrieveAllUndeletedUsers(){
		return serviceUser.getUndeletedUsers();
	}
	@PutMapping("/updateuser")
	public void updateUser(@RequestBody User u) {
		serviceUser.updateUser(u);
	}
	// **************testing a methode down
	@PutMapping("/deleteuser/{iduser}")
	public void deleteUser(@PathVariable("iduser")Long id) {
		serviceUser.deleteUser(id);
	}
	@GetMapping("/getConnectedUser")
	public User getTheConnectedUser(){
		User user=null;
		String username;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		username  = ((UserDetails) principal).getUsername();
		user = serviceUser.getOne(username);
		return user;
	}

	@GetMapping("/getCurrentUserName")
	public String getCurrentUserName() {
		log.info("in method get current userName--------");
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = null;
		if (principal instanceof UserDetails) {
			username = ((UserDetails) principal).getUsername();
		} else {
			username = principal.toString();
		}
		return username;
	}

	@GetMapping("/getCurrentUserId")
	public Long getCurrentUserId() {
		log.info("in method get current userId--------");
		String username;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		username  = ((UserDetails) principal).getUsername();
		return serviceUser.getIdUSer(username);
	}
	
	@GetMapping("/getuserByUsername/{username}")
	public User getByUsername(@PathVariable("username") String username) {
		return serviceUser.getOne(username);
	}
	
	@PostMapping("/checkemail")
	public AccountResponse  resetPasswordEmail (@RequestBody ResetPassword resetPassword){
		//boolean result = this.serviceUser.ifEmailExists(resetPassword.getEmail());
		User user = this.serviceUser.getUserByEmail(resetPassword.getEmail());
		AccountResponse accountResponse = new AccountResponse();
		if (user!=null){
			String code =UserCode.getCode();
			Mail mail=new Mail(resetPassword.getEmail(),code);
			sericeMail.sendCodeByMail(mail);
			user.getCode().setCode(code);
			this.serviceUser.updateUser(user);
			accountResponse.setResult(1);
		}else{
			accountResponse.setResult(0);
		}
		return accountResponse;
	}
	
	@PostMapping("/forgotPassword")
	public ResponseEntity ForgotPassword(
			HttpServletRequest request,
			@RequestBody String email
			) throws Exception {
		User user = serviceUser.getUserByEmail(email);
		if (user==null){
			return new ResponseEntity("Email not found",HttpStatus.BAD_REQUEST);
		}
		String password = SecurityUtility.randomPassword();
		String encryptedPassword = SecurityUtility.passwordEncoder().encode(password);
		user.setPassword(encryptedPassword);
		userRepository.save(user);
		SimpleMailMessage newEmail = mailConstructor.constructNewUserEmail(user, password);
		mailSender.send(newEmail);
		return new ResponseEntity("Email Sent!",HttpStatus.OK);
		
		
	}
	@PutMapping("/updateUserPassword/{uid}")
	public User updateUserPassword(@PathVariable("uid") Long uid,@RequestBody String password){
		return serviceUser.updateUserPassword(uid,password);
	}
 //--------accepts a user ID as a path variable and deletes the user from the database. chatgpt solution------------------------
 /*@DeleteMapping("/{id}")
 public ResponseEntity<?> deleteUser(@PathVariable("id") Long id) {
	 Optional<User> user = userRepository.findById(id);
	 if (!user.isPresent()) {
		 return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
	 }
	 try {
		 userRepository.delete(user.get());
		 return new ResponseEntity<>(HttpStatus.OK);
	 } catch (Exception e) {
		 return new ResponseEntity<>("Failed to delete user", HttpStatus.INTERNAL_SERVER_ERROR);
	 }
 }*/

	///***** ACTIVATE DESACTIVATE USER
	@GetMapping("/retrieve-user-by-state/{user-state}")
	@ResponseBody
	public List<User> retrieveUserByState(@PathVariable("user-state") boolean stateUser) {
		return serviceUser.retrieveUserByState(stateUser); // Invoke the method on the instance
	}

	@PutMapping("/activate-user")
	public User activateUser(@RequestBody User user) throws Exception {
		return serviceUser.activateUser(user); // Invoke the method on the instance
	}

}
