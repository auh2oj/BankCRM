package com.bank.crm.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.bank.crm.service.AuthService;
import com.bank.crm.service.BankService;

@Controller
public class BankController {
	
//	@Autowired
//	@Qualifier("AuthServiceImpl")
	private AuthService authService;
	
	private BankService bankService;

	@RequestMapping(value="/test")
	public String test() {
		String test = "Hello world!";
		System.out.println(test);
		return "test";
	}
	
	@RequestMapping(value="bankManagerHome")
	public String bankManagerHome() {
		String test = "Hello Bank Manager!";
		System.out.println(test);
		return "bankManagerHome";
	}
	
	@RequestMapping(value="acceptCustomer")
	public String acceptCustomer(@RequestParam("cid") int cid) {
		String result = bankService.acceptCustomer(cid);
		return "bankManagerHome";
	}
	
	@RequestMapping(value="rejectCustomer")
	public String rejectCustomer(@RequestParam("cid") int cid) {
		String result = bankService.rejectCustomer(cid);
		return "bankManagerHome";
	}
	
	@RequestMapping(value="auth")
	public String authUser(String username, String password, HttpSession session, Model model) {
		String redirect;
		String role = authService.authorizeUser(username, password);
		if (role.equals("ROLE_BANK_MANAGER")) {
			session.setAttribute("username", username);
			session.setAttribute("role", role);
			redirect = "bankManagerHome";
		} else {
			return "redirect:/login.jsp?message=Incorrect username or password.";
		}
		return redirect;
	}
	
	@RequestMapping(value="logout", method=RequestMethod.GET)
	public String logout(HttpSession session) {
		if (session != null) {
			session.invalidate();
		}
		return "redirect:/login.jsp?message=You have successfully logged out.";
	}
}
