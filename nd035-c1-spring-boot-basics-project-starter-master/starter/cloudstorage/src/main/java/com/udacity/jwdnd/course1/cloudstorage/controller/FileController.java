package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;


@Controller
@RequestMapping("/home/files")
public class FileController {
    private final FileService fileService;
    private final FileMapper fileMapper;
    private final UserMapper userMapper;

    public FileController(FileService fileService, FileMapper fileMapper, UserMapper userMapper) {
        this.fileService = fileService;
        this.fileMapper = fileMapper;
        this.userMapper = userMapper;
    }

    @PostMapping
    public String uploadFile(@RequestParam("fileUpload") MultipartFile fileUpload, Authentication authentication, RedirectAttributes redirectAttributes) throws IOException {
        String error = null;
        String getUserInfo = (String) authentication.getPrincipal();
        User user = userMapper.getUser(getUserInfo);
        File getFileInfo = fileMapper.getFile(user.getUserId(), fileUpload.getOriginalFilename());
        if (getFileInfo != null) {
            error = "The file already exists!!";
            redirectAttributes.addFlashAttribute("error", error);
            return "redirect:/result?errorDuplicate";
        } else if (fileUpload.getOriginalFilename().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", error);
            return "redirect:/result?notUploadYet";
        } else if (fileUpload.getSize() > 3000000) {
            return "redirect:/result?upLoadExceeds";
        }


        fileService.uploadFile(fileUpload, user.getUserId());
        return "redirect:/result?success";
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable Integer fileId){
        File file = fileService.getFileById(fileId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContenttype()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+ file.getFilename()+"\"")
                .body(new ByteArrayResource(file.getFiledata()));
    }

    @GetMapping("/delete")
    public String deleteFile(@RequestParam("id") int fileid, RedirectAttributes redirectAttributes){
        int fileIsDelete = fileService.deleteFile(fileid);
        if(fileIsDelete > 0){
            return "redirect:/result?success";
        }

        redirectAttributes.addAttribute("error", "Delete File is fault!");
        return "redirect:/result?error";
    }
}
