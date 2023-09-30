package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {
    @Insert("INSERT INTO FILES (filename, contenttype, filesize, userid, filedata) VALUES (#{filename}, #{contenttype}, #{filesize}, #{userid}, #{filedata})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    int saveFile(File file);

    @Select("SELECT * FROM FILES WHERE filename = #{filename}")
    File getFileByName(String filename);

    @Select("SELECT * FROM FILES WHERE fileId = #{fileId}")
    File getFileById(Integer fileId);

    @Select("SELECT * FROM FILES WHERE userid = #{userid} AND filename = #{filename}")
    File getFile(Integer fileId, String filename);

    @Select("SELECT * FROM FILES WHERE userid = #{userid}")
    List<File> getListFiles(Integer userid);

    @Delete("DELETE FROM FILES WHERE fileId = #{fileId}")
    int deleteFile(Integer fileId);
}
