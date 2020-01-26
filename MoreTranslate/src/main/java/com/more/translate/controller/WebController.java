package com.more.translate.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {
	@GetMapping("/moreTranslate")
	public String moreTranslateMain(HttpServletRequest req, HttpServletResponse resp) {
		return "moreTranslate";
	}
}
