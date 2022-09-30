package edu.upc.essi.dtim.odin.storage.filestorage;


import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

@Service()
public class FileSystemStorageService implements StorageService {

    private final Path persistentDir;
    private final Path temporalDir;

    @Autowired
    public FileSystemStorageService(StorageProperties properties) {
        this.persistentDir = Paths.get(properties.getPersistentDSDir());
        this.temporalDir = Paths.get(properties.getTemporalDSDir());
    }

    @Override
    public String storePersistent(String filename){
        File f = new File( temporalDir.resolve(filename).toString() );
        Path destPath = persistentDir.resolve(filename);
        f.renameTo(new File( destPath.toString() ));
        return destPath.toString();
    }

    @Override
    public String storeTemporal(MultipartFile file) {

        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file.");
            }
            String filename = RandomStringUtils.randomAlphanumeric(16) +"_"+ file.getOriginalFilename();
            Path destinationFile = this.temporalDir.resolve(
                    Paths.get(filename))
                    .normalize().toAbsolutePath();
            if (!destinationFile.getParent().equals(this.temporalDir.toAbsolutePath())) {
                // This is a security check
                throw new StorageException(
                        "Cannot store file outside current directory.");
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile,
                        StandardCopyOption.REPLACE_EXISTING);
            }
            return filename;
        } catch (IOException e) {
            throw new StorageException("Failed to store file.", e);
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.persistentDir, 1)
                    .filter(path -> !path.equals(this.persistentDir))
                    .map(this.persistentDir::relativize);
        }
        catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }

    }

    @Override
    public Path load(String filename) {
        return persistentDir.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageFileNotFoundException(
                        "Could not read file: " + filename);

            }
        }
        catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(persistentDir.toFile());

        if (! persistentDir.toFile().exists()){
            persistentDir.toFile().mkdir();
            // If you require it to make the entire directory path including parents,
            // use directory.mkdirs(); here instead.
        }
        FileSystemUtils.deleteRecursively(temporalDir.toFile());
        if (! temporalDir.toFile().exists()){
            temporalDir.toFile().mkdir();
        }
    }

    @Override
    public void delete(String path) throws IOException {
        if(path == null || path.equals("") ) {
            return;
        }
        FileSystemUtils.deleteRecursively(Paths.get(path));
    }

    @Override
    public String getPersistentDir() {
        return persistentDir.toString();
    }

    @Override
    public String getTemporalDir() {
        return temporalDir.toString();
    }

    @Override
    public void init() {
        try {
            System.out.println("init directory");
            Files.createDirectories(persistentDir);
            Files.createDirectories(temporalDir);
        }
        catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }
}