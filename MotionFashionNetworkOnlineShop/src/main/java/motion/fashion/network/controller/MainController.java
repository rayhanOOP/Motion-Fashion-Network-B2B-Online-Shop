package motion.fashion.network.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
// import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import motion.fashion.network.dao.CountryDAO;
import motion.fashion.network.dao.UserDAO;
import motion.fashion.network.formbean.UserForm;
import motion.fashion.network.model.Country;
import motion.fashion.network.model.User;
import motion.fashion.network.validator.UserValidator;
 
@Controller
public class MainController {
 
   @Autowired
   private UserDAO UserDAO;
 
   @Autowired
   private CountryDAO countryDAO;
 
   @Autowired
   private UserValidator UserValidator;
 
   // Set a form validator
   @InitBinder
   protected void initBinder(WebDataBinder dataBinder) {
      // Form target
      Object target = dataBinder.getTarget();
      if (target == null) {
         return;
      }
      System.out.println("Target=" + target);
 
      if (target.getClass() == UserForm.class) {
         dataBinder.setValidator(UserValidator);
      }
      // ...
   }
 
  
   /*
   //For switching between SignedIn and NotSignedIn page
   @GetMapping("/")
   String index(Principal principal) {
       return principal != null ? "home/homeSignedIn" : "home/homeNotSignedIn";
   }
   
   */
   
  
 
   // Show Register page.
   @RequestMapping(value ="/register", method = RequestMethod.GET)
   public String viewRegister(Model model){
 
      UserForm form = new UserForm();
 
      model.addAttribute("appUserForm", form);
 
      return "registerPage";
   }
   
   
// Show Register page.
   @RequestMapping("/home")
   public String viewHOmePage(Model model){
 
	   UserForm form = new UserForm();
	   
	  model.addAttribute("appUserForm", form);
	  
      return "homeNotSignedIn";
   }
   
   
// This method is called to save the registration information.
   // @Validated: To ensure that this Form
   // has been Validated before this method is invoked.
   @RequestMapping(value ="/register", method = RequestMethod.POST)
   public String saveRegister(Model model, //
         @ModelAttribute("appUserForm") @Validated UserForm appUserForm, //
         BindingResult result, //
         final RedirectAttributes redirectAttributes) {
 
      // Validate result
      if (result.hasErrors()) {
         List<Country> countries = countryDAO.getCountries();
         model.addAttribute("countries", countries);
         return "registerPage";
      }
      User newUser= null;
      try {
         newUser = UserDAO.createUser(appUserForm);
      }
      // Other error!!
      catch (Exception e) {
         List<Country> countries = countryDAO.getCountries();
         model.addAttribute("countries", countries);
         model.addAttribute("errorMessage", "Error: " + e.getMessage());
         return "registerPage";
      }
 
      redirectAttributes.addFlashAttribute("flashUser", newUser);
      
      return "redirect:/registerSuccessful";
   }
   
   @RequestMapping("/registerSuccessful")
   public String viewRegisterSuccessful(Model model) {
	   
	   User user = new User();
	  model.addAttribute(user);
      return "registerSuccessfulPage";
   }
     
   
   // Show Login page.
   @RequestMapping(value ="/login", method = RequestMethod.GET)
   public String viewLogin(Model model){
 
    
 
      return "loginPage";
   }
   

   
 
}