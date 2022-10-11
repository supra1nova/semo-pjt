package com.selfdriven.semo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {
	
	 @GetMapping("/")
	 public String index() {
		 return "redirect:/swagger-ui/index.html";
	 }

}
