package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CredentialMapper {
    @Insert("INSERT INTO CREDENTIALS (url, username, key, password, userid) VALUES (#{url}, #{username}, #{key}, #{password}, #{userid}))")
    @Options(useGeneratedKeys = true, keyProperty = "credentialid")
    void saveCredential(Credential credential);

    @Select("SELECT * FROM CREDENTIALS WHERE userid = #{userid}")
    List<Credential> getCredentialByUserId(Integer userid);

    @Select("SELECT * FROM CREDENTIALS WHERE credentialid = #{credentialid}")
    Credential getCredentialById(Integer credentialid);

    @Update("UPDATE CREDENTIALS set url = #{url}, username = #{username}, key = #{key}, password= #{password} WHERE credentialid = #{credentialid}")
    void updateCredential(Credential credential);

    @Delete("DELETE FROM CREDENTIALS credentialid = #{credentialid}")
    int deleteCredential(Integer credentialid);
}
