package com.seattleacademy.team20;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles requests for the application home page.
 */
@Controller
public class SkillController {

	private static final Logger logger = LoggerFactory.getLogger(SkillController.class);

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/upload", method = RequestMethod.GET)
	public String skillUpload(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);





		return "skillUpload";
	}

}
