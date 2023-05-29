package myblog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import myblog.service.UserService;

@RequestMapping("/user")
@Controller
public class UserRegisterController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/register")
	public String getUserRegisterPage() {
		return "register.html";
	}

	
	@PostMapping("/register/process")
	public String register(
			@RequestParam String userLoginName,
			@RequestParam String userEmail,
			@RequestParam String userDisplayName,
			@RequestParam String password) {
		userService.createAccount(userLoginName, userDisplayName, userEmail, password);
		return"redirect:/user/login";
		
	}
	
	
}
