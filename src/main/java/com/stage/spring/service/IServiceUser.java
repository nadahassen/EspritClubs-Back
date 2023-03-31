package com.stage.spring.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

import com.stage.spring.entity.User;

public interface IServiceUser {
	String addUser(User u,MultipartFile file);
	List<User> getusers();
	List<User> getUndeletedUsers();
	void deleteUser(Long id);
	void updateUser(User u);
	void send(String to, String email);
	public String confirmToken(String token);
	public String forgotPassword(String userName,HttpServletRequest request);
	public String fogetPasswordSetting(String token,String newPass);
	public User getOne(String u);
	public User getProfile(Long id);
	public Long getIdUSer(String username);
	public boolean ifEmailExists(String email);
	public User getUserByEmail(String email);
	public User updateUserPassword(Long uid,String password);
}
