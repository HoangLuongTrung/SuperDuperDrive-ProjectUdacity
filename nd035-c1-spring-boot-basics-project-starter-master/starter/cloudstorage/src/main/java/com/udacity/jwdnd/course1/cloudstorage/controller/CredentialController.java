package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/home/credential")
public class CredentialController {
    private final CredentialService credentialService;
    private final UserMapper userMapper;

    public CredentialController(CredentialService credentialService, UserMapper userMapper) {
        this.credentialService = credentialService;
        this.userMapper = userMapper;
    }

    @PostMapping
     String CreateOrUpdateCredential(Authentication authentication, Credential credential) {
        String getUserInfo = (String) authentication.getPrincipal();
        User user = userMapper.getUser(getUserInfo);
        if (credential.getCredentialid() == null) {
            credentialService.addCredentials(credential, user.getUserId());
        } else {
            credentialService.updateCredential(credential);
        }
        return "redirect:/result?success";
    }

    @GetMapping("/delete")
    public String DeleteCredential(@RequestParam("id") int credentialid){
        if(credentialid > 0){
            credentialService.deleteCredential(credentialid);
            return "redirect:/result?success";
        }
        return "redirect:/result?deleteCredentialFail";
    }
}
