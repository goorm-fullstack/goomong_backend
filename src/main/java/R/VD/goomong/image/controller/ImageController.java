package R.VD.goomong.image.controller;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/image")
public class ImageController {

    @GetMapping
    public ResponseEntity<?> getImage(@RequestParam String imagePath) {
        Resource resource = new FileSystemResource(imagePath);
        return ResponseEntity.ok(resource);
    }
}
