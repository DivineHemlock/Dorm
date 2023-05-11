package com.airbyte.dorm.files;

import com.airbyte.dorm.dto.ResponseFile;
import com.airbyte.dorm.dto.ResponseMessage;
import com.airbyte.dorm.model.FileDB;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/v1/supervisor/file")
@CrossOrigin("*")
public class FileController {
    //TODO : ADD NAME FOR SEARCH FOR FILE
    private final FileStorageService storageService;

    public FileController(FileStorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping()
    public ResponseEntity<ResponseMessage> uploadFile(HttpServletResponse response, Authentication authentication, @RequestParam("file") MultipartFile file) {
        response.setHeader("role", authentication.getAuthorities().toString().replace("[", "").replace("]", ""));
        Map<String, String> message = new HashMap<>();
        try {
            FileDB fileDB = storageService.store(file);
            message.put("Message", "Uploaded the file successfully: " + file.getOriginalFilename());
            message.put("id", fileDB.getId());
            message.put("name", fileDB.getName());
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            message.put("Message:", "Could not upload the file: " + file.getOriginalFilename() + "!");
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }

    @GetMapping()
    public ResponseEntity<List<ResponseFile>> getListFiles(HttpServletResponse response, Authentication authentication) {
        response.setHeader("role", authentication.getAuthorities().toString().replace("[", "").replace("]", ""));
        List<ResponseFile> files = storageService.getAllFiles().map(dbFile -> {
            String fileDownloadUri = "https://api.saadatportal.com/api/v1/file/" + dbFile.getId();

            return new ResponseFile(
                    dbFile.getName(),
                    fileDownloadUri,
                    dbFile.getType(),
                    dbFile.getData().length);
        }).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(files);
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getFile(Authentication authentication,@PathVariable String id, HttpServletResponse response) {
        response.setHeader("role", authentication.getAuthorities().toString().replace("[", "").replace("]", ""));
        FileDB fileDB = storageService.getFile(id);
        response.addHeader("fileName", fileDB.getName());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB.getName() + "\"")
                .body(fileDB.getData());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteFile(HttpServletResponse response, Authentication authentication,@PathVariable(name = "id") String id) {
        response.setHeader("role", authentication.getAuthorities().toString().replace("[", "").replace("]", ""));
        storageService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
