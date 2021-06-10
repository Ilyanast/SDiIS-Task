package storage;

import storage.structure.Document;

import java.util.List;

public interface Storage {
    void createDocument(String fullPath, String authorName, String content);
    void createCatalog(String fullPath);
    void deleteStorageUnit(String fullPath);
    void updateDocument(String fullPath, String newName, String newContent);
    void moveStorageUnit(String sourceFilePath, String destinationCatalogPath);
    List<Document> findByAuthorName(String authorName);
    List<Document> findByName(String name);
}
