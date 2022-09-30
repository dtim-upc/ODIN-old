package edu.upc.essi.dtim.odin.storage.filestorage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {

    void init();

    String storePersistent(String filename);

    String storeTemporal(MultipartFile file);

    Stream<Path> loadAll();

    Path load(String filename);

    Resource loadAsResource(String filename);

    void deleteAll();

    void delete(String path) throws IOException;

    String getPersistentDir();

    String getTemporalDir();

}