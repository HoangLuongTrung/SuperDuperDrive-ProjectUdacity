package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("home/notes")
public class NoteController {
    private final NoteService noteService;
    private final UserMapper userMapper;

    public NoteController(NoteService noteService, UserMapper userMapper) {
        this.noteService = noteService;
        this.userMapper = userMapper;
    }

    @PostMapping
    public String CreateOrUpdateNote(Authentication authentication, Note note) {
        String getUserInfo = (String) authentication.getPrincipal();
        User user = userMapper.getUser(getUserInfo);
        Integer userId = user.getUserId();
        if (note.getNoteid() == null) {
            this.noteService.addNote(note.getNotetitle(), note.getNotedescription(), userId);
        } else {
            this.noteService.updateNote(note.getNotetitle(), note.getNotedescription(), userId);
        }
        return "redirect:/result?success";
    }

    @GetMapping("/delete")
    public String DeleteFile(@RequestParam("id") int noteid, RedirectAttributes redirectAttributes){
        if(noteid > 0){
            noteService.deleteNote(noteid);
            return "redirect:/result?success";
        }

        return "redirect:/result?deleteNotFail";
    }
}
