package com.stage.spring.service;
import org.springframework.mail.SimpleMailMessage;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.stage.spring.JwtAndAuthConf.ConfirmationToken;
import com.stage.spring.configuration.SecurityUtility;
import com.stage.spring.entity.Image;
import com.stage.spring.entity.User;
import com.stage.spring.repository.UserRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class ServiceUser implements IServiceUser {
	@Autowired
	UserRepository userRepository;
	@Autowired
    PasswordEncoder encoder;
	

	@Autowired
	ConfirmationTokenService confirmationTokenService;
	private final JavaMailSender mailSender;

	@Autowired
	IServiceImage imageService;


	//------------fail------------------------
  /*  public void increaseFailedAttempts(User user) {
	        int newFailAttempts = user.getFailedAttempt() + 1;
	        userRepository.updateFailedAttempts(newFailAttempts, user.getUserName());
	    }
	 public void resetFailedAttempts(String userName) {
	        userRepository.updateFailedAttempts(0, userName);
	    }
	 public void lock(User user) {
	        user.setLocked(true);
	        userRepository.save(user);
	    }*/
//--------------------fail----------------------------
	@Override
	public String addUser(User u, MultipartFile file) {
		Date date = new Date(System.currentTimeMillis());
		User searchedUser = null;
		String token = null;
		try {
			log.info("in method addUser");
			searchedUser = userRepository.findOneByUserName(u.getUserName());
			if (searchedUser == null) {
				log.info("userName don't exist");
				u.setCreatedAt(date);
				if (u.getBirthDate() != null) {
					Period period = Period.between(u.getBirthDate()
									.toInstant()
									.atZone(ZoneId.systemDefault())
									.toLocalDate(),
							date
									.toInstant()
									.atZone(ZoneId.systemDefault())
									.toLocalDate());
					u.setAge(period.getYears());
				}
				u.setPassword(encoder.encode(u.getPassword()));

				Image image = new Image(file.getOriginalFilename());
				u.setImage(image);
				imageService.save(file);

				userRepository.save(u);

				// Send email with username and password
				String message = "Your username: " + u.getUserName() + "\nYour password: " + u.getPassword();
				sendEmail(u.getEmail(), "Account Details", message);

				// Generate token
				token = UUID.randomUUID().toString();
				ConfirmationToken confirmationToken = new ConfirmationToken(
						token,
						LocalDateTime.now(),
						LocalDateTime.now().plusMinutes(15),
						u
				);
				confirmationTokenService.saveConfirmationToken(confirmationToken);

				// Send email with confirmation link
				String link = "http://127.0.0.1:8089/SpringMVC/User/confirm/" + token;
				send(u.getEmail(), buildEmail(u.getUserName(), u.getPassword(), link));
			} else {
				log.info("userName already exists!!");
			}
			log.info("out of method addUser without errors");
		} catch (Exception e) {
			log.error("error in method addUser" + e);
		}
		return token;
	}

	// Mail creation user
	public void sendEmail(String to, String subject, String text) {
		MimeMessage message = mailSender.createMimeMessage(); // set to true, the email content will be interpreted as HTML
		MimeMessageHelper helper;
		try {
			helper = new MimeMessageHelper(message, true);
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(text, true);
			mailSender.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
			// Handle the exception accordingly
		}
	}

	/*public void sendEmail(String to, String subject, String text) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setSubject(subject);
		message.setText(text);
		mailSender.send(message);
	}*/

	private String buildEmail(String name, String password, String link) {
		return "<div style=\"font-family: Helvetica, Arial, sans-serif; font-size: 16px; margin: 0; color: #0b0c0c;\">\n" +
				"  <table role=\"presentation\" width=\"100%\" style=\"border-collapse: collapse; min-width: 100%; width: 100%!important;\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
				"    <tbody>\n" +
				"      <tr>\n" +
				"        <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
				"          <table role=\"presentation\" width=\"100%\" style=\"border-collapse: collapse; max-width: 580px;\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
				"            <tbody>\n" +
				"              <tr>\n" +
				"                <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
				"                  <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse: collapse;\">\n" +
				"                    <tbody>\n" +
				"                      <tr>\n" +
				"                        <td style=\"padding-left: 10px;\"></td>\n" +
				"                        <td style=\"font-size: 28px; line-height: 1.315789474; Margin-top: 4px; padding-left: 10px;\">\n" +
				"                          <span style=\"font-family: Helvetica, Arial, sans-serif; font-weight: 700; color: #ffffff; text-decoration: none; vertical-align: top; display: inline-block;\">Account Credentials</span>\n" +
				"                        </td>\n" +
				"                      </tr>\n" +
				"                    </tbody>\n" +
				"                  </table>\n" +
				"                </td>\n" +
				"              </tr>\n" +
				"            </tbody>\n" +
				"          </table>\n" +
				"        </td>\n" +
				"      </tr>\n" +
				"    </tbody>\n" +
				"  </table>\n" +
				"  <table role=\"presentation\" class=\"content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse: collapse; max-width: 580px; width: 100%!important;\" width=\"100%\">\n" +
				"    <tbody>\n" +
				"      <tr>\n" +
				"        <td height=\"30\"><br></td>\n" +
				"      </tr>\n" +
				"      <tr>\n" +
				"        <td width=\"10\" valign=\"middle\"><br></td>\n" +
				"        <td style=\"font-family: Helvetica, Arial, sans-serif; font-size: 19px; line-height: 1.315789474; max-width: 560px;\">\n" +
				"          <p style=\"Margin: 0 0 20px 0; font-size: 19px; line-height: 25px; color: #0b0c0c;\">Hi " + name + ",</p>\n" +
				"          <p style=\"Margin: 0 0 20px 0; font-size: 19px; line-height: 25px; color: #0b0c0c;\">Thank you for registering. Here are your account details:</p>\n" +
				"          <p style=\"Margin: 0 0 20px 0; font-size: 19px; line-height: 25px; color: #0b0c0c;\">Username: " + name + "</p>\n" +
				"          <p style=\"Margin: 0 0 20px 0; font-size: 19px; line-height: 25px; color: #0b0c0c;\">Password: " + password + "</p>\n" +
				"          <blockquote style=\"Margin: 0 0 20px 0; border-left: 10px solid #b1b4b6; padding: 15px 0 0.1px 15px; font-size: 19px; line-height: 25px;\">\n" +
				"            <p style=\"Margin: 0 0 20px 0; font-size: 19px; line-height: 25px; color: #0b0c0c;\"><a href=\"" + link + "\">Activate Now</a></p>\n" +
				"          </blockquote>\n" +
				"          <p>Link will expire in 15 minutes.</p>\n" +
				"          <p>See you soon</p>\n" +
				"        </td>\n" +
				"        <td width=\"10\" valign=\"middle\"><br></td>\n" +
				"      </tr>\n" +
				"      <tr>\n" +
				"        <td height=\"30\"><br></td>\n" +
				"      </tr>\n" +
				"    </tbody>\n" +
				"  </table>\n" +
				"</div>";
	}


	@Override
	public List<User> getusers() {
		List<User> searchedUsers = null;
		try {
			log.info("in method getUsers");
			searchedUsers = userRepository.findAll();
			log.info("out of  method getUsers without errors");
		} catch (Exception e) {
			log.error("error in method getUsers" + e);
		}
		return searchedUsers;
	}

	@Override
	public List<User> getUndeletedUsers() {
		List<User> undeletedUsers = null;
		try {
			log.info("in method getUndeletedUsers");
			undeletedUsers = userRepository.getUndeletedUsers();
			log.info("out of  method getUndeletedUsers without errors");
		} catch (Exception e) {
			log.error("error in method getUndeletedUsers" + e);
		}
		return undeletedUsers;
	}
//-----------Delete-----------------

	@Override
	public void deleteUser(Long id) {
		Date date = new Date(System.currentTimeMillis());
	
		try {
			log.info("in method deleteUser");
			User deletedUser = userRepository.findById(id).orElse(null);
			deletedUser.setDeleted(true);
			deletedUser.setDeletedAt(date);

			userRepository.save(deletedUser);
			log.info("out of  method deleteUser without errors");
		} catch (Exception e) {
			log.error("error in method deleteUser" + e);
		}

	}
//----------------- Update ---------------------

	@Override
	public void updateUser(User u) {
		Date date = new Date(System.currentTimeMillis());
		try {
			log.info("in method updateUser");
			Period period = Period.between(u.getBirthDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
					date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
			u.setAge(period.getYears());
			u.setModifiedAt(date);
			userRepository.save(u);
			log.info("out of  method updateUser without errors");
		} catch (Exception e) {
			log.error("error in method upadateUser" + e);
		}

	}

	
	public int enableAppUser(String email) {
        return userRepository.enableAppUser(email);
    }

		@Override
		public void send(String to, String email) {
			 try {
		         MimeMessage mimeMessage = mailSender.createMimeMessage();
		         MimeMessageHelper helper =
		                 new MimeMessageHelper(mimeMessage, "utf-8");
		         helper.setText(email, true);
		         helper.setTo(to);
		         helper.setSubject("Confirm your email");
		         helper.setFrom("spacepro711@gmail.com");
		         mailSender.send(mimeMessage);
		     } catch (MessagingException e) {
		         log.error("failed to send email", e);
		         throw new IllegalStateException("failed to send email");
		     }
			
		}
		@Override
		@Transactional
		public String confirmToken(String token) {
		    ConfirmationToken confirmationToken = confirmationTokenService
		            .getToken(token)
		            .orElseThrow(() ->
		                    new IllegalStateException("token not found"));

		    if (confirmationToken.getConfirmedAt() != null) {
		        throw new IllegalStateException("email already confirmed");
		    }

		    LocalDateTime expiredAt = confirmationToken.getExpiresAt();

		    if (expiredAt.isBefore(LocalDateTime.now())) {
		        throw new IllegalStateException("token expired");
		    }

		    confirmationTokenService.setConfirmedAt(token);
		    enableAppUser(
		            confirmationToken.getUser().getUserName());
		    return "confirmed";
		}

		private String buildEmail(String name, String link) {
		    return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
		            "\n" +
		            "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
		            "\n" +
		            "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
		            "    <tbody><tr>\n" +
		            "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
		            "        \n" +
		            "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
		            "          <tbody><tr>\n" +
		            "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
		            "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
		            "                  <tbody><tr>\n" +
		            "                    <td style=\"padding-left:10px\">\n" +
		            "                  \n" +
		            "                    </td>\n" +
		            "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
		            "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n" +
		            "                    </td>\n" +
		            "                  </tr>\n" +
		            "                </tbody></table>\n" +
		            "              </a>\n" +
		            "            </td>\n" +
		            "          </tr>\n" +
		            "        </tbody></table>\n" +
		            "        \n" +
		            "      </td>\n" +
		            "    </tr>\n" +
		            "  </tbody></table>\n" +
		            "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
		            "    <tbody><tr>\n" +
		            "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
		            "      <td>\n" +
		            "        \n" +
		            "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
		            "                  <tbody><tr>\n" +
		            "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
		            "                  </tr>\n" +
		            "                </tbody></table>\n" +
		            "        \n" +
		            "      </td>\n" +
		            "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
		            "    </tr>\n" +
		            "  </tbody></table>\n" +
		            "\n" +
		            "\n" +
		            "\n" +
		            "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
		            "    <tbody><tr>\n" +
		            "      <td height=\"30\"><br></td>\n" +
		            "    </tr>\n" +
		            "    <tr>\n" +
		            "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
		            "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
		            "        \n" +
		            "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for registering. Please click on the below link to activate your account: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Activate Now</a> </p></blockquote>\n Link will expire in 15 minutes. <p>See you soon</p>" +
		            "        \n" +
		            "      </td>\n" +
		            "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
		            "    </tr>\n" +
		            "    <tr>\n" +
		            "      <td height=\"30\"><br></td>\n" +
		            "    </tr>\n" +
		            "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
		            "\n" +
		            "</div></div>";
		}
		@Override
		public String forgotPassword(String userName,HttpServletRequest request) {
			User user=null;
			try {log.info("in method fogotPassword");
			user=userRepository.findOneByUserName(userName);
			if (user == null) {
				return "user not found";
			} else {
				
				user.setResettoken(UUID.randomUUID().toString());
				userRepository.save(user);
				String appUrl = request.getServerName()+":"+request.getServerPort()+request.getContextPath();
				
				String link="http://"+appUrl+"/User/reset/"+user.getResettoken();
				log.info(link);
				sendForgotPassworMail(user.getEmail(),buildResetPasswordEmail(user.getUserName(), link));
				
			}
			log.info("out of method fogotPassword without errors");
			} catch (Exception e) {
				log.error("error in method forgotPassword"+e);
			}
			
			
			return "";
		}
		public void sendForgotPassworMail(String to, String email) {
			 try {
		        MimeMessage mimeMessage = mailSender.createMimeMessage();
		        MimeMessageHelper helper =
		                new MimeMessageHelper(mimeMessage, "utf-8");
		        helper.setText(email, true);
		        helper.setTo(to);
		        helper.setSubject("Reset your password");
		        helper.setFrom("spacepro711@gmail.com");
		        mailSender.send(mimeMessage);
		    } catch (MessagingException e) {
		        log.error("failed to send email", e);
		        throw new IllegalStateException("failed to send email");
		    }
			
		}
		private String buildResetPasswordEmail(String name, String link) {
		    return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
		            "\n" +
		            "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
		            "\n" +
		            "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
		            "    <tbody><tr>\n" +
		            "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
		            "        \n" +
		            "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
		            "          <tbody><tr>\n" +
		            "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
		            "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
		            "                  <tbody><tr>\n" +
		            "                    <td style=\"padding-left:10px\">\n" +
		            "                  \n" +
		            "                    </td>\n" +
		            "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
		            "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Reset your Password</span>\n" +
		            "                    </td>\n" +
		            "                  </tr>\n" +
		            "                </tbody></table>\n" +
		            "              </a>\n" +
		            "            </td>\n" +
		            "          </tr>\n" +
		            "        </tbody></table>\n" +
		            "        \n" +
		            "      </td>\n" +
		            "    </tr>\n" +
		            "  </tbody></table>\n" +
		            "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
		            "    <tbody><tr>\n" +
		            "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
		            "      <td>\n" +
		            "        \n" +
		            "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
		            "                  <tbody><tr>\n" +
		            "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
		            "                  </tr>\n" +
		            "                </tbody></table>\n" +
		            "        \n" +
		            "      </td>\n" +
		            "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
		            "    </tr>\n" +
		            "  </tbody></table>\n" +
		            "\n" +
		            "\n" +
		            "\n" +
		            "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
		            "    <tbody><tr>\n" +
		            "      <td height=\"30\"><br></td>\n" +
		            "    </tr>\n" +
		            "    <tr>\n" +
		            "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
		            "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
		            "        \n" +
		            "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Please click on the below link to reset your password: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Reset Now</a>  <p>See you soon</p>" +
		            "        \n" +
		            "      </td>\n" +
		            "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
		            "    </tr>\n" +
		            "    <tr>\n" +
		            "      <td height=\"30\"><br></td>\n" +
		            "    </tr>\n" +
		            "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
		            "\n" +
		            "</div></div>";
		}

		@Override
		public String fogetPasswordSetting(String token, String newPass) {
			User user = userRepository.findUserByresettoken(token);
			if (user != null) {
				user.setPassword(encoder.encode(newPass));
				user.setResettoken(null);
				userRepository.save(user);
				return "passwored reseted";

			} else {
				return "operation regected";
			}
			
		}
		@SuppressWarnings("deprecation")
		public List<User> getTodaysUsersBirthday(){
			List<User> todaysBU= new ArrayList<User>(); ;
			List<User> allUsers=null;
			Date date = new Date(System.currentTimeMillis());
			try {log.info("in method getTodaysUsersBirthday");
			allUsers=userRepository.getUndeletedUsers();
			
			for(User u:allUsers) {
				if((date.getMonth()==u.getBirthDate().getMonth())&&(date.getDay()==u.getBirthDate().getDay())) {
					todaysBU.add(u);
				}
			}
			log.info("*------------------->"+todaysBU.toString());
			log.info("out of method getTodaysUsersBirthday without errors");
			} catch (Exception e) {
				log.error(" error in method getTodaysUsersBirthday"+e);
				
			}
			return todaysBU;
		}

		private String buildBirthDayEmail(String name,List<String> links) {
			String part1="<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
		            "\n" +
		            "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
		            "\n" +
		            "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
		            "    <tbody><tr>\n" +
		            "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
		            "        \n" +
		            "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
		            "          <tbody><tr>\n" +
		            "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
		            "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
		            "                  <tbody><tr>\n" +
		            "                    <td style=\"padding-left:10px\">\n" +
		            "                  \n" +
		            "                    </td>\n" +
		            "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
		            "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">happy birthday!!!</span>\n" +
		            "                    </td>\n" +
		            "                  </tr>\n" +
		            "                </tbody></table>\n" +
		            "              </a>\n" +
		            "            </td>\n" +
		            "          </tr>\n" +
		            "        </tbody></table>\n" +
		            "        \n" +
		            "      </td>\n" +
		            "    </tr>\n" +
		            "  </tbody></table>\n" +
		            "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
		            "    <tbody><tr>\n" +
		            "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
		            "      <td>\n" +
		            "        \n" +
		            "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
		            "                  <tbody><tr>\n" +
		            "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
		            "                  </tr>\n" +
		            "                </tbody></table>\n" +
		            "        \n" +
		            "      </td>\n" +
		            "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
		            "    </tr>\n" +
		            "  </tbody></table>\n" +
		            "\n" +
		            "\n" +
		            "\n" +
		            "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
		            "    <tbody><tr>\n" +
		            "      <td height=\"30\"><br></td>\n" +
		            "    </tr>\n" +
		            "    <tr>\n" +
		            "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
		            "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
		            "        \n" +
		            "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Prospace team wishes you a happy birthday!!.And  offers you these trainings for free </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">";
			List<String> ll = new ArrayList<String>();
		    List<String> sub= new ArrayList<String>();
			//log.info("links in buld-------111------>"+links.toString());

			
			for(int i=0;i<links.size();i++) {
				
				ll.add(" <a href=\"" + links.get(i).toString() + "\">"+sub.get(i).toString()+"</a>");
				
			}
			//log.info("ll in buld-------2222------>"+ll.toString());
		     String part3="<p>See you soon</p>"+
		            "        \n" +
		            "      </td>\n" +
		            "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
		            "    </tr>\n" +
		            "    <tr>\n" +
		            "      <td height=\"30\"><br></td>\n" +
		            "    </tr>\n" +
		            "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
		            "\n" +
		            "</div></div>";
		     return part1+ll.toString()+part3;
		}

		public void sendBdMail(String to, String email) {
			 try {
		       MimeMessage mimeMessage = mailSender.createMimeMessage();
		       MimeMessageHelper helper =
		               new MimeMessageHelper(mimeMessage, "utf-8");
		       helper.setText(email, true);
		       helper.setTo(to);
		       helper.setSubject("HAPPY BIRTHDAY");
		       helper.setFrom("spacepro711@gmail.com");
		       mailSender.send(mimeMessage);
		   } catch (MessagingException e) {
		       log.error("failed to send email", e);
		       throw new IllegalStateException("failed to send email");
		   }	
		}

		public void finalBDMethod() {
			List<User> users= new ArrayList<User>();
			List<String> links=new ArrayList<>();
			List<String> links2=new ArrayList<>();
			//String appUrl = request.getServerName()+":"+request.getServerPort()+request.getContextPath();
			try {log.info("in method finalBDMethod");
				users=getTodaysUsersBirthday();

				for(User u:users) {
					for(String l:links) {
					l=l.concat("/"+u.getIdUser());
						//log.info(l);
						links2.add(l);
					}
					sendBdMail(u.getEmail(),buildBirthDayEmail(u.getUserName(),links2));
					log.info("before clean---->"+links2);
					links2.clear();
					log.info("after clean---->"+links2);
				}
			log.info("out of method finalBDMethod without errors");
			} catch (Exception e) {
				log.error("error in method finalBDMethod"+e);
			}
		}
		public void updateBirthday() {
			List<User> users=new ArrayList<User>();
			try {log.info("in updateBirthday ");
			users=getTodaysUsersBirthday();
			for(User u:users) {
				u.setAge(u.getAge()+1);
				userRepository.save(u);
			}
			
			log.info("out of method without errors updateBirthday ");
			} catch (Exception e) {
				log.error("error in method  updateBirthday "+e);
			}
		}

		

		@Override
		public User getOne(String username) {
			return userRepository.findOneByUserName(username);
		}

		@Override
		public User getProfile(Long id) {
			User user=null;
			user=	userRepository.getById(id);
				return user;
		}

		@Override
		public Long getIdUSer(String username) {
			User user=null;
			user=userRepository.findOneByUserName(username);
			return user.getIdUser();
		}

		@Override
	public boolean ifEmailExists(String email){
		if( userRepository.findByEmail(email)!=null)
			return true;
			else return false;
	}
		@Override
		public User getUserByEmail(String email){
			return userRepository.findByEmail(email);
		}

	@Override
		public User updateUserPassword(Long uid,String password) {
		User u = userRepository.findById(uid).orElse(null);
		
		Date date = new Date(System.currentTimeMillis());
		String encryptedPassword = SecurityUtility.passwordEncoder().encode(password);
				u.setPassword(encoder.encode(encryptedPassword));
				u.setModifiedAt(date);
				userRepository.save(u);
		log.info(u.getPassword())	;	
			return	userRepository.save(u);
			
		}


	@Override
	public User activateUser(User user) {

		if (user.isStateUser() == false) {

			user.setStateUser(true);
		} else {
			user.setStateUser(false);
		}
		return userRepository.save(user);
	}

	@Override
	public List<User> retrieveUserByState(boolean stateUser) {
		return (List<User>) userRepository.findByStateUser(stateUser);
	}

	public boolean getstateUser(User user) {
		if (user.isStateUser()) {
			return true;
		} else {
			return false;
		}

	}



	/*	@Scheduled(cron="*//*5 * * * * *")
		public void birthdayUsers() {
			log.info(getTodaysUsersBirthday().toString());
			finalBDMethod();
			updateBirthday();
		}*/

}
