package R.VD.goomong.file.service;

import R.VD.goomong.file.model.Files;
import R.VD.goomong.file.repository.FilesRepository;
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
public class FilesService {

    private final FilesRepository filesRepository;

    @Value("${File.upload.path}")
    private String path;

    // 이미지 저장 로직 및 DB에 관련 내용 반영
    public List<Files> saveFiles(MultipartFile[] fileList) {
        List<Files> result = new ArrayList<>();
        if (fileList == null) {
            return null;
        }
        for (MultipartFile file : fileList) {
            if (file.isEmpty()) {
                return null;
            }
            String fileName = file.getOriginalFilename();
            String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String newPath = path + "\\file\\" + date;
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
                Files saveImage = Files.builder()
                        .fileName(fileName)
                        .saveFileName(saveFileName)
                        .path(newPath + "\\" + saveFileName)
                        .build();
                Files save = filesRepository.save(saveImage);
                result.add(save);
            } catch (Exception e) {
                throw new RuntimeException("파일을 저장할 수 없습니다.");
            }
        }
        return result;
    }
}
