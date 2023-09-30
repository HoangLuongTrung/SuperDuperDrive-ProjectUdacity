package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoteMapper {
    @Insert("INSERT INTO NOTES (notetitle, notedescription, userid) VALUES (#{notetitle}, #{notedescription}, #{userid})")
    @Options(useGeneratedKeys = true, keyProperty = "noteid")
    void saveNote(Note note);

    @Select("SELECT * FROM NOTES WHERE userid = #{userid}")
     List<Note> getListNotesByUserId(Integer userid);

    @Update("UPDATE NOTES SET notetitle = #{notetitle}, notedescription = #{notedescription} WHERE noteid = #{noteid}")
    void updateNote(Note note);

    @Delete("DELETE FROM NOTES WHERE noteid = #{noteid}")
    int deleteNote(Integer noteid);
}