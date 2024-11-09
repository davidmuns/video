package com.example.video;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class VideoService {

    @Autowired
    private VideoRepository videoRepository;

    private final String videoFolderPath = "C:/Users/LENOVO/Desktop/videos/";

    public Video saveVideo(MultipartFile file) throws IOException {
        String uniqueFileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
        String filePath = videoFolderPath + uniqueFileName;
        file.transferTo(new File(filePath));

        Video video = new Video();
        video.setUrl(uniqueFileName);
        return videoRepository.save(video);
    }

    public List<Video> getAllVideos() {
        return videoRepository.findAll();
    }

    public void deleteVideo(Long videoId) {
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new RuntimeException("Video no encontrado"));

        // Borra el archivo del sistema de archivos
        Path filePath = Paths.get(videoFolderPath + video.getUrl());
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo borrar el archivo de video", e);
        }

        // Borra el registro en la base de datos
        videoRepository.deleteById(videoId);
    }
}
