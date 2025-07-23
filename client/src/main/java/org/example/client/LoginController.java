package org.example.client;


import org.example.client.dto.UserDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login(Authentication authentication,Model model) {
        model.addAttribute("user", new UserDto());
        if(authentication != null &&  authentication.isAuthenticated()){
            return "redirect:/";
        }
        return "login";
    }

 
    @GetMapping("/")
    public String root() {
        return "redirect:/index";
    }

    @GetMapping("/index")
    public String index(Model model, @AuthenticationPrincipal OAuth2User principal) {
        model.addAttribute("username", principal.getAttributes().get("sub"));
        return "index";
    }


    @GetMapping("/dashboard")
    public String dashboard(Model model, @AuthenticationPrincipal OAuth2User principal) {
        model.addAttribute("username", principal.getAttributes().get("sub"));
        return "dashboard";
    }




}
