package ru.vramaz.bookshelf.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.ejb.Stateless;

import ru.vramaz.bookshelf.util.ApplicationConfig;

@Stateless
public class ImageService {

    private static final String IMG_STORE;

    static {
        String storePath = ApplicationConfig.instance().getParameter("imgstore.path");
        IMG_STORE = (storePath == null || storePath.trim().isEmpty()) ? "/Bookshelf/ImgStore" : storePath;
    }

    public InputStream findImg(String imgPath) throws IOException {
        if (imgPath == null || imgPath.trim().isEmpty()) throw new IllegalArgumentException("Image path is null or empty");
        String pathToFile = IMG_STORE + imgPath;
        if (Files.exists(Paths.get(pathToFile)) && Files.isReadable(Paths.get(pathToFile))) {
            return new FileInputStream(new File(pathToFile));
        }
        return null;
    }

    public void saveImg(String imgPath, InputStream in) throws IOException {
        if (imgPath == null || imgPath.trim().isEmpty()) throw new IllegalArgumentException("Image path is null or empty");
        if (in == null ) throw new IllegalArgumentException("Image stream cannot be null");
        String pathToFile = IMG_STORE + imgPath;
        if (!Files.exists(Paths.get(pathToFile.substring(0, pathToFile.lastIndexOf("/"))))) {
            Files.createDirectories(Paths.get(pathToFile.substring(0, pathToFile.lastIndexOf("/"))));
        }
        if (Files.exists(Paths.get(pathToFile))) {
            Files.delete(Paths.get(pathToFile));
        }
        Files.copy(in, Paths.get(pathToFile));
    }

    public void deleteImg(String imgPath) throws IOException {
        if (imgPath == null || imgPath.trim().isEmpty()) throw new IllegalArgumentException("Image path is null or empty");
        String pathToFile = IMG_STORE + imgPath;
        Files.deleteIfExists(Paths.get(pathToFile));
    }

}
