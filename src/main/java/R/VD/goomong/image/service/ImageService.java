package R.VD.goomong.image.service;

import R.VD.goomong.image.model.Image;
import R.VD.goomong.image.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 이미지 관련 서비스 로직
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ImageService {

    private final ImageRepository imageRepository;

    @Value("${File.upload.path}")
    private String path;

    // 이미지 저장 로직 및 DB에 관련 내용 반영
    public List<Image> saveImage(MultipartFile[] fileList) {
        List<Image> result = new ArrayList<>();
        if(fileList == null) {
            return null;
        }
        for (MultipartFile file : fileList) {
            if (file.isEmpty()) {
                return null;
            }
            String fileName = file.getOriginalFilename();
            String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String newPath = path + "\\image\\" + date;
            File Folder = new File(newPath);
            if (!Folder.exists()) {
                try {
                    Folder.mkdirs();
                } catch (Exception e) {
                    throw new RuntimeException("폴더를 생성할 수 없습니다.");
                }
            }

            UUID uuid = UUID.randomUUID();
            String saveFileName = uuid + "_" + fileName;
            File saveFile = new File(newPath + "\\" + saveFileName);
            try {
                file.transferTo(saveFile);
                Image saveImage = Image.builder()
                        .fileName(fileName)
                        .saveFileName(saveFileName)
                        .path(newPath + "\\" + saveFileName)
                        .build();
                Image save = imageRepository.save(saveImage);
                result.add(save);
            } catch (Exception e) {
                throw new RuntimeException("파일을 저장할 수 없습니다.");
            }
        }
        return result;
    }
}
