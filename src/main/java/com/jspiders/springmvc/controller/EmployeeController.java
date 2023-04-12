package com.jspiders.springmvc.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.jspiders.springmvc.pojo.AdminPOJO;
import com.jspiders.springmvc.pojo.EmployeePOJO;
import com.jspiders.springmvc.services.AdminService;
import com.jspiders.springmvc.services.EmployeeService;

@Controller
public class EmployeeController {
	
	@Autowired
	private EmployeeService service;
	
	@Autowired
	private AdminService adminService;
	
	
	//Home Controller
	@GetMapping("/home")
	public String home(@SessionAttribute(name = "login", required = false) AdminPOJO login, ModelMap map) {
		if(login !=null) {
			return "Home";
		}
		map.addAttribute("msg", "Please login to proceed..!!");
		return "Login";
	}
	
	//Add page controller	
	@GetMapping("/add")
	public String  addPage(@SessionAttribute(name = "login", required = false)AdminPOJO login, ModelMap map) {
		if(login!=null) {
			return "Add";
		}
		map.addAttribute("msg", "Please login to proceeed");
		return "Login";
	}
	
	//Add data controller
	@PostMapping("/add")
	public String add(@SessionAttribute(name = "login",required = false)AdminPOJO login,
					  @RequestParam String name,@RequestParam String email,
					  @RequestParam long contact,@RequestParam String designation,
					  @RequestParam double salary, ModelMap map) {
		if(login!=null) {
			EmployeePOJO employee=service.addEmployee(name,email,contact,designation,salary);
			if (employee!=null) {
				//success response
				map.addAttribute("msg","Data added successfully...!!!");
				return "Add";
			}
			//Failure response
			map.addAttribute("msg", "Data not added successfully...!!!");
			return "Add";
		}
		map.addAttribute("msg", "Please login to proceed..!!");
		return "Login";
	}
	
	//Search page controller
	@GetMapping("/search")
	public String searchPage(@SessionAttribute(name = "login", required = false)AdminPOJO login, ModelMap map) {
		if(login!=null) {
			return "Search";
		}
		map.addAttribute("msg","Please login to proceed");
		return "Login";
		
	}
	
	//Search data controller
	@PostMapping("/search")
	public String search(@SessionAttribute(name = "login", required = false)AdminPOJO login,@RequestParam int id, ModelMap map) {
		if(login!=null) {
			EmployeePOJO employee=service.search(id);
			if(employee!=null) {
				//Success response
				map.addAttribute("msg","Employee record found successfully...!!!");
				map.addAttribute("emp",employee);
				return "Search";
			}
			//Failure Response
			map.addAttribute("msg", "Employee record not found..!!!");
			return "Search";
		}
		map.addAttribute("msg", "Please login to proceed..!!");
		return "Login";
	}
	
	//Remove page controller
	@GetMapping("/remove")
	public String remove(@SessionAttribute(name="login", required = false)AdminPOJO login, ModelMap map) {
		if(login!=null) {
			List<EmployeePOJO>employees=service.searchAllEmployees();
			map.addAttribute("empList", employees);
			return "Remove";
		}
		map.addAttribute("msg","Please login to proceed..!!");
		return "Login";
	}
	
	//Remove data controller
	@PostMapping("/remove")
	public String remove(@SessionAttribute(name = "login", required = false)AdminPOJO login,@RequestParam int id,ModelMap map) {
		if(login!=null){
			EmployeePOJO employee=service.search(id);
			if(employee!=null) {
				//Success response
				service.removeEmployee(id);
				List<EmployeePOJO> employees=service.searchAllEmployees();
				map.addAttribute("empList",employees);
				map.addAttribute("msg","Employee removed successfully...!!!");
				return "Remove";
			}
			//Failure response
			List<EmployeePOJO> employees=service.searchAllEmployees();
			map.addAttribute("empList",employees);
			map.addAttribute("msg","Employee data does not exist..!!");
			return "Remove";
		}
		map.addAttribute("msg", "Please login to proceed..!!");
		return "Login";
	}
	
	//Update page controller
	@GetMapping("/update")
	public String updatePage(@SessionAttribute(name = "login", required = false)AdminPOJO login,
			ModelMap map) {
		if(login!=null) {
			List<EmployeePOJO> employees= service.searchAllEmployees();
			map.addAttribute("empList",employees);
			return "Update";
		}
		map.addAttribute("msg", "Please login to proceed..!!");
		return "Login";
	}
	
	
	//Update form Controller
	@PostMapping("/update")
	public String updateForm(@SessionAttribute(name = "login", required = false)AdminPOJO login, 
							@RequestParam int id,ModelMap map) {
		if(login!=null) {
			EmployeePOJO employee=service.search(id);
			if(employee!=null) {
				//Success response
				map.addAttribute("emp",employee);
				return "Update";
			}
			//Failure response
			map.addAttribute("msg","Employee record not found..!!");
			List<EmployeePOJO> employees=service.searchAllEmployees();
			map.addAttribute("empList",employees);
			return "Update";
		}
		map.addAttribute("msg","Please login to proceed..!!");
		return "Login";
	}
	
	//Update Data Controller
	@PostMapping("/updateData")
	public String update(@SessionAttribute(name = "login",required = false)AdminPOJO login, @RequestParam int id,
						@RequestParam String name,@RequestParam String email,@RequestParam long contact,@RequestParam String designation,
						@RequestParam double salary, ModelMap map) {
		if(login!=null) {
			EmployeePOJO employee=service.updateEmployee(id, name, email, contact, designation, salary);
			if(employee!=null) {
				//Success response
				map.addAttribute("msg","Employee record updated successfully..!!");
				List<EmployeePOJO> employees=service.searchAllEmployees();
				map.addAttribute("empList",employees);
				return "Update";
			}
			//Failure response
			map.addAttribute("msg","Employee record not updated..!!");
			List<EmployeePOJO> employees=service.searchAllEmployees();
			map.addAttribute("empList",employees);
			return "Update";
		}
		map.addAttribute("msg","Please login to proceed..!!");
		return "Login";
	}

	//Login Controller
	@PostMapping("/login")
	public String login(HttpServletRequest request, @RequestParam String username, @RequestParam String password, ModelMap map) {
		AdminPOJO admin=adminService.login(username, password);
		if(admin!=null) {
			//Success response
			HttpSession session=request.getSession();
			session.setAttribute("login", admin);
			return "Home";
		}
		//Failure response
		map.addAttribute("msg","Invalid username or password");
		return "Login";
	}
	
	//Logout Controller
	@GetMapping("/logout")
	public String  logout(HttpSession session, ModelMap map) {
		map.addAttribute("msg","Logged out successfully");
		session.invalidate();
		return "Login";
	}
	
	//Create Admin Page Controller
	@GetMapping("/createAdmin")
	public String adminPage() {
		return "AddAdmin";
	}
	
	//Create Admin Controller
	@PostMapping("/createAdmin")
	public String admin(@RequestParam String username,@RequestParam String password, ModelMap map) {
		AdminPOJO admin=adminService.addAdmin(username,password);
		if(admin !=null) {
			//Success response
			map.addAttribute("msg","Account created successfully..!!");
			return "Login";
		}
		//Failure response
		map.addAttribute("msg", "Account not created..!!");
		return "Login";
	}
}
	

