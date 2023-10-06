package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {
    private final NoteMapper noteMapper;

    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public List<Note> getListNotesByUserId(Integer userid) {
        return this.noteMapper.getListNotesByUserId(userid);
    }

    public void addNote(String noteTitle, String noteDescription, Integer userid) {
        Note note = new Note();
        note.setNotetitle(noteTitle);
        note.setNotedescription(noteDescription);
        note.setUserid(userid);
        this.noteMapper.saveNote(note);
    }

    public void updateNote(String noteTitle, String noteDescription, Integer noteId) {
        Note note = new Note();
        note.setNotetitle(noteTitle);
        note.setNotedescription(noteDescription);
        note.setNoteid(noteId);
        this.noteMapper.updateNote(note);
    }

    public int deleteNote(Integer noteId) {
        return this.noteMapper.deleteNote(noteId);
    }
}
