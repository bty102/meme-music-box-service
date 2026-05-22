package com.bty.karaoke.mememusicboxservice.service;

import com.bty.karaoke.mememusicboxservice.dto.response.ImageUploadResult;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    public ImageUploadResult uploadImage(MultipartFile file);
}
