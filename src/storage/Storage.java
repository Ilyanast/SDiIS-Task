package storage;

import storage.structure.Catalog;
import storage.structure.Document;

import java.util.List;

public interface Storage {
    void addDocumentToCatalog(String catalogPath, Document document);
    void addCatalog(String catalogPath, Catalog catalog);
    void deleteStorageUnit(String fullPath);
    void moveStorageUnit(String sourceFilePath, String destinationCatalogPath);
    List<Document> findByAuthorName(String authorName);
    List<Document> findByName(String name);
}
