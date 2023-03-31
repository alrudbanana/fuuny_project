package com.project;


import java.io.File;
import java.io.IOException;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;


public class FileUploadUtil {
	
    public static void saveFile(String uploadDir, String fileName, MultipartFile multipartFile) throws IOException {
        File file = new File(uploadDir);
        if (!file.exists()) {
            file.mkdirs();
        }
        byte[] bytes = multipartFile.getBytes();
        File uploadedFile = new File(file.getAbsolutePath() + File.separator + fileName);
        FileCopyUtils.copy(bytes, uploadedFile);
    }

}
