package com.noithat.WebNoiThat.controller.authen;

import java.security.SecureRandom;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.noithat.WebNoiThat.entity.Role;
import com.noithat.WebNoiThat.model.RoleDTO;
import com.noithat.WebNoiThat.model.UserDTO;
import com.noithat.WebNoiThat.service.UserService;

@Controller
public class RegisterController {

	@Autowired
	private UserService userService;

	@Autowired
	private MailSender mailSender;

	@GetMapping(value = "/register")
	public String register() {
		return "authen/register";
	}

	@PostMapping(value = "/register")
	public String register(HttpServletRequest request, @RequestParam(name = "email") String email,
			@RequestParam(name = "password") String password, @RequestParam(name = "repassword") String repassword) {
		String code = randomString(8);
		if (userService.findByEmail(email) != null) {
			UserDTO userDTO = userService.findByEmail(email);
			if (userDTO.isVerify() == true) {
				request.setAttribute("error", "The email address is already exist!");
				return "authen/register";
			} else {
				if (!password.equals(repassword)) {
					request.setAttribute("error", "The password do not match!");
					request.setAttribute("email", email);
					userDTO.setPassword(repassword);
					userDTO.setAvatar("user2.png");
					userService.update(userDTO);
					return "authen/register";
				} else {
					userDTO.setPassword(new BCryptPasswordEncoder().encode(password));
					userService.update(userDTO);
					sendEmail("quant882@gmail.com", "quant882@gmail.com", "Welcome to NoiThat!",
							"Hello, " + email.split("@")[0] + "! Please confirm that you can login in NoiThat!" + " Your confirmation code is: " + code);
				}
			}
		} else {
			if (!password.equals(repassword)) {
				request.setAttribute("error", "The password do not match!");
				request.setAttribute("email", email);
				return "authen/register";
			} else {
				UserDTO userDTO = new UserDTO();
				userDTO.setEmail(email);
				userDTO.setPassword(new BCryptPasswordEncoder().encode(password));
				userDTO.setAvatar("user2.png");
				RoleDTO roleDTO = new RoleDTO();
				roleDTO.setRoleId(3);
				userDTO.setRoleDTO(roleDTO);
				userService.insert(userDTO);
				sendEmail("quant882@gmail.com", "quant882@gmail.com", "Welcome to NoiThat!",
						"Hello, " + email.split("@")[0] + "! Please confirm that you can login in NoiThat!" + " Your confirmation code is: " + code);
			}
		}
		HttpSession session = request.getSession();
		session.setAttribute("emailRegister", email);
		session.setAttribute("codeVerify", code);
		return "authen/verify";
	}
	
	@GetMapping(value = "/resend-code")
	public String resendCode(HttpSession session, HttpServletRequest request) {
		String code = randomString(8);
		String email = (String) session.getAttribute("emailRegister");
		sendEmail("quant882@gmail.com", email, "Welcome to NoiThat!",
				"Hello, " + email.split("@")[0] + "! Please confirm that you can login in NoiThat!" + " Your confirmation code is: " + code);
		request.setAttribute("resend", "resend");
		session.setAttribute("codeVerify", code);
		return "authen/verify";
	}

	@PostMapping(value = "/verify")
	public String verify(HttpServletRequest request, HttpSession session) {
		String code = request.getParameter("code");
		String codeVerify = (String) session.getAttribute("codeVerify");
		if (!code.equals(codeVerify)) {
			request.setAttribute("verifyFail", "Invalid code, please try again!");
		} else {
			String email = (String) session.getAttribute("emailRegister");
			UserDTO userDTO = userService.findByEmail(email);
			userDTO.setVerify(true);
			request.setAttribute("verifySuccess", "Verification successfull!");
			request.setAttribute("active", "active");
			userService.update(userDTO);
		}
		return "authen/verify";
	} 
	
	@PostMapping(value = "get-news")
	public String getNews(@RequestParam(name = "email") String email) {
		sendEmail("quant882@gmail.com", email, "Welcome to NoiThat!",
				"Thank you for your interest, we will send you the latest notice if any. Please pay attention to your mail.");
		return "client/get_news";
	}
	
	public void sendEmail(String from, String to, String subject, String content) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setFrom(from);
		mailMessage.setTo(to);
		mailMessage.setSubject(subject);
		mailMessage.setText(content);

		mailSender.send(mailMessage);
	}
	
	static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	static SecureRandom rnd = new SecureRandom();

	String randomString(int len){
	   StringBuilder sb = new StringBuilder(len);
	   for(int i = 0; i < len; i++)
	      sb.append(AB.charAt(rnd.nextInt(AB.length())));
	   return sb.toString();
	}
}
