package com.bty.karaoke.mememusicboxservice.service.impl;

import com.bty.karaoke.mememusicboxservice.dto.response.ImageUploadResult;
import com.bty.karaoke.mememusicboxservice.exception.AppException;
import com.bty.karaoke.mememusicboxservice.exception.ErrorCode;
import com.bty.karaoke.mememusicboxservice.service.FileStorageService;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CloudinaryService implements FileStorageService {

    private final Cloudinary cloudinary;

    @Override
    public ImageUploadResult uploadImage(MultipartFile file) {
        try {
            validateImage(file);
        } catch (Exception e) {
            throw new AppException(ErrorCode.INVALID_UPLOADED_IMAGE);
        }

        try {

            Map<?, ?> uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", "meme-music-box/images",
                            "public_id", generateFileName(file),
                            "resource_type", "image"
                    )
            );

//            return uploadResult.get("secure_url").toString();
            return ImageUploadResult.builder()
                    .imageUrl(uploadResult.get("secure_url").toString())
                    .build();

        } catch (IOException e) {
//            e.printStackTrace();
//            throw new RuntimeException("Cannot upload image");
            throw new AppException(ErrorCode.IMAGE_UPLOAD_EXCEPTION);
        }
    }

    private void validateImage(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new RuntimeException("Image is empty");
        }

        String contentType = file.getContentType();

        if (contentType == null ||
                !contentType.startsWith("image/")) {

            throw new RuntimeException("File must be image");
        }
    }

    private String generateFileName(MultipartFile file) {

        String originalFilename = file.getOriginalFilename();

        String extension = "";

        if (originalFilename != null &&
                originalFilename.contains(".")) {

            extension = originalFilename.substring(
                    originalFilename.lastIndexOf(".")
            );
        }

        return UUID.randomUUID() + extension;
    }
}
