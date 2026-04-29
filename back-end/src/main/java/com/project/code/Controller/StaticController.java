package com.project.code.Controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StaticController {

    @GetMapping("/")
    public ResponseEntity<byte[]> index() throws IOException {
        Path indexPath = Paths.get("front-end", "index.html").toAbsolutePath().normalize();
        byte[] content = Files.readAllBytes(indexPath);
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(content);
    }
}
