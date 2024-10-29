package be.atheneumboom.bibliotheek.controller;

import be.atheneumboom.bibliotheek.config.Settings;
import be.atheneumboom.bibliotheek.model.token.ConfirmationTokenService;
import be.atheneumboom.bibliotheek.service.UserService;
import be.atheneumboom.bibliotheek.service.impl.RegistrationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("api/registration")
@AllArgsConstructor
public class ConfirmationController {
    @Autowired
    private final RegistrationService registrationService;
    private final UserService userService;
    private final ConfirmationTokenService confirmationTokenService;

    @GetMapping("/confirm")
    public RedirectView confirm(@RequestParam("token") String token, HttpServletRequest request) {

        /*try{
            registrationService.confirmToken(token);

        }catch (IllegalStateException e){
            Long userId = confirmationTokenService.getToken(token).get().getUser().getId();
            registrationService.sendNewConfirmationMail(userService.getRealUser(userId));
            RedirectView redirectView = new RedirectView();
            redirectView.setUrl(Settings.BASE_URL_FRONT+"/expired");
            return redirectView;
        }*/
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(Settings.BASE_URL_FRONT+"/confirmed");
        return redirectView;
    }


    
}
