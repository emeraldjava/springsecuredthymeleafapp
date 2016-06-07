package com.learning.securedapp.web.controllers;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.learning.securedapp.domain.User;
import com.learning.securedapp.web.events.OnRegistrationCompleteEvent;
import com.learning.securedapp.web.services.IUserService;
import com.learning.securedapp.web.services.SecurityService;
import com.learning.securedapp.web.utils.GenericResponse;
import com.learning.securedapp.web.utils.WebUtils;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class RegistrationController {
    
    @Autowired private SecurityService securityService;
    @Autowired private ApplicationEventPublisher eventPublisher;
    @Autowired private IUserService userService;
    @Autowired private MessageSource messages;
    
    //Registration
    @PostMapping(value = "/user/registration")
    public GenericResponse registerUserAccount(@Valid final User accountDto, final HttpServletRequest request) {
        log.debug("Registering user account with information: {}", accountDto);

        final User registered = securityService.createUser(accountDto);
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered, request.getLocale(), WebUtils.getURLWithContextPath(request)));
        return new GenericResponse("success");
    }
    
    @GetMapping(value = "/registrationConfirm")
    public String confirmRegistration(Locale locale, Model model, @RequestParam("token") String token) {
        String result = userService.validateVerificationToken(token);
        if (result == null) {
            model.addAttribute("message", messages.getMessage("message.accountVerified", null, locale));
            return "redirect:/login?lang=" + locale.getLanguage();
        }
        if (result == "expired") {
            model.addAttribute("expired", true);
            model.addAttribute("token", token);
        }
        model.addAttribute("message", messages.getMessage("auth.message." + result, null, locale));
        return "redirect:/badUser?lang=" + locale.getLanguage();
    }
}