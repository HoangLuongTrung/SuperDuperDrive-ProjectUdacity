package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
public class CredentialService {
    private final CredentialMapper credentialMapper;
    private final EncryptionService encryptionService;


    public CredentialService(CredentialMapper credentialMapper, EncryptionService encryptionService) {
        this.credentialMapper = credentialMapper;
        this.encryptionService = encryptionService;
    }

    public List<Credential> getCredentialByUserId(Integer userId){
        return credentialMapper.getCredentialByUserId(userId);
    }

    public void addCredentials(Credential credential, Integer userId){
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), encodedKey);

        Credential newCredential = new Credential();
        newCredential.setUrl(credential.getUrl());
        newCredential.setUsername(credential.getUsername());
        newCredential.setKey(encodedKey);
        newCredential.setPassword(encryptedPassword);
        newCredential.setUserid(userId);

        credentialMapper.saveCredential(newCredential);
    }

    public int deleteCredential(Integer credentialId){
        return credentialMapper.deleteCredential(credentialId);
    }

    public void updateCredential(Credential credential){
        Credential storedCredential = credentialMapper.getCredentialById(credential.getCredentialid());

        credential.setKey(storedCredential.getKey());
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), credential.getKey());
        credential.setPassword(encryptedPassword);
        credentialMapper.updateCredential(credential);
    }
}
