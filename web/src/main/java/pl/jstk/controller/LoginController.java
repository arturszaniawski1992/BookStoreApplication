package pl.jstk.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import pl.jstk.constants.ModelConstants;
import pl.jstk.constants.ViewNames;

@Controller
public class LoginController {

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login() {
		return "login";
	}

	@RequestMapping(value = "/loginfailed", method = RequestMethod.GET)
	public String loginerror(Model model) {
		model.addAttribute("error", "true");
		return ViewNames.LOGIN;
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(Model model) {
		return ViewNames.LOGIN;
	}

	@RequestMapping(value = "/403", method = RequestMethod.GET)
	public ModelAndView accesssDenied(Principal user) {
		ModelAndView model = new ModelAndView();
		String message = user.getName() + "! You dont have access here!";
		model.addObject(ModelConstants.ERRORMESSAGE, message);
		return model;
	}

}
