package Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static java.nio.file.Files.copy;
import static java.nio.file.Paths.get;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@RestController
@RequestMapping("/images")
public class UploadController {

    //public static String UPLOAD_DIRECTORY = "C:\\NomadImages";
    public static String UPLOAD_DIRECTORY = File.listRoots()[0].getAbsolutePath() + "NomadImages";

    @PostMapping("/upload")
    public ResponseEntity<List<String>> uploadFiles(@RequestParam("images") List<MultipartFile> files) throws IOException {
        List<String> filenames = new ArrayList<String>();

        for(MultipartFile file: files){
            String filename = StringUtils.cleanPath(file.getOriginalFilename());
            Path fileStorage = get(UPLOAD_DIRECTORY, filename).toAbsolutePath().normalize();
            try{
                copy(file.getInputStream(), fileStorage, REPLACE_EXISTING);
                filenames.add(filename);
            }catch (java.nio.file.FileSystemException e) {
                System.out.println("File already exists in this folder!");
            }

        }

        return ResponseEntity.ok().body(filenames);
    }
}
