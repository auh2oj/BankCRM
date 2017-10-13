package com.bank.crm.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bank.crm.entity.User;
import com.bank.crm.entity.form.AgentCustomerAssignmentForm;
import com.bank.crm.entity.form.UserForm;
import com.bank.crm.service.AuthService;
import com.bank.crm.service.BankService;
import com.web.bank.controller.model.CustomerForm;

@Controller
public class BankController {
	
//	@Autowired
//	@Qualifier("AuthServiceImpl")
	private AuthService authService;
	
	@Autowired
	@Qualifier("BankServiceImpl")
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
	
	@RequestMapping(value="teamLeaderHome")
	public String teamAgentHome() {
		String test = "Hello Team Agent!";
		System.out.println(test);
		return "teamLeaderHome";
	}
	
	//Change the status of a Customer to Accepted
	@RequestMapping(value="/acceptCustomer/{pk}", method=RequestMethod.PUT,
			produces = {"application/json", "application/xml"})
	@ResponseBody public String acceptCustomer(@PathVariable("pk") int cid) {
		return bankService.acceptCustomer(cid);
	}
	
	//Change the status of a Customer to Rejected
	@RequestMapping(value="/rejectCustomer/{pk}", method=RequestMethod.PUT,
			produces = {"application/json", "application/xml"})
	@ResponseBody public String rejectCustomer(@PathVariable("pk") int cid) {
		return bankService.rejectCustomer(cid);
	}
	
	@RequestMapping(value="/customers", method=RequestMethod.GET)
	@ResponseBody public List<CustomerForm> showPendingCustomers() {
		List<CustomerForm> customerList = bankService.showPendingCustomers();
		for (CustomerForm cf : customerList) {
			System.out.println(cf.getName()+" "+cf.getCid());
		}
		return customerList;
	}
	
	@RequestMapping(value="/teamLeaderHomeCustomers", method=RequestMethod.GET,
			consumes= {"application/json"},
			produces= {"application/json"})
	@ResponseBody public List<CustomerForm> showAcceptedCustomers(Model model) {
		List<CustomerForm> customerList = bankService.showAcceptedCustomers();
		model.addAttribute("customerList", customerList);
		for (CustomerForm cf : customerList) { //for testing purposes
			System.out.println(cf.getName());
		}
		return customerList;
	}
	
	@RequestMapping(value="/teamLeaderHomeAgents", method=RequestMethod.GET,
			consumes= {"application/json"},
			produces= {"application/json"})
	@ResponseBody public List<String> showOnlineAgents(Model model) {
		List<String> agentList = bankService.showOnlineAgents();
		model.addAttribute("agentList", agentList);
		for (String agent : agentList) { //for testing purposes
			System.out.println(agent.toString());
		}
		return agentList;
	}
	
	@RequestMapping(value="/teamLeaderHomeAssignments", method=RequestMethod.GET,
			consumes= {"application/json"},
			produces= {"application/json"})
	@ResponseBody public List<AgentCustomerAssignmentForm> showAssignments(Model model) {
		List<AgentCustomerAssignmentForm> assignmentList = bankService.showAssignments();
		model.addAttribute("assignmentList", assignmentList);
		for (AgentCustomerAssignmentForm assignment : assignmentList) { //for testing purposes
			System.out.println(assignment.toString());
		}
		return assignmentList;
	}
	
	@RequestMapping(value="/teamLeaderHomeAssign/{a}/{cid}", method=RequestMethod.PUT,
			consumes= {"application/json"},
			produces= {"application/json"})
	public void assign(@PathVariable("a") String agentUsername, @PathVariable("cid") int cid) {
		System.out.println("In controller");
		System.out.println("Agent Username: " + agentUsername);
		System.out.println("Customer ID: " + cid);
		bankService.assign(agentUsername, cid);
	}
	
//	@RequestMapping(value="/update", method=RequestMethod.PUT,
//			consumes = {"application/json", "application/xml"},
//			produces = {"application/json", "application/xml"})
//	@ResponseBody public ApplicationMessageResponse updateEmployee(@RequestBody EmployeeForm ef) {
//		System.out.println(ef);
//		employeeService.addEmployee(ef);
//		ApplicationMessageResponse amr = new ApplicationMessageResponse();
//		amr.setStatus("success");
//		amr.setMessage("Employee data has been successfully updated.");
//		return amr;
//	}
	
	/* ################# Below is Security stuff, disregard for now ######################## */
	
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
