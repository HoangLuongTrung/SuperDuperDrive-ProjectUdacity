package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class FileService {
    private final FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public List<File> getListFiles(Integer userid) {
        return this.fileMapper.getListFiles(userid);
    }

    public void uploadFile(MultipartFile fileUpload, Integer userid) throws IOException {
        File file = new File();
        try {
            file.setContenttype(fileUpload.getContentType());
            file.setFiledata(fileUpload.getBytes());
            file.setFilename(fileUpload.getOriginalFilename());
            file.setFilesize(Long.toString(fileUpload.getSize()));
            file.setUserid(userid);
        } catch (IOException e) {
            throw e;
        }
        fileMapper.saveFile(file);
    }

    public int deleteFile(Integer fileId) {
        return this.fileMapper.deleteFile(fileId);
    }

    public File getFileById(Integer fileId) {
        return this.fileMapper.getFileById(fileId);
    }
}
