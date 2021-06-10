package actors;

import storage.structure.Catalog;

public interface Admin {
    void createCatalog(String catalogPath, Catalog catalog);
    void deleteDocument(String documentPath);
    void moveDocument(String sourceFilePath, String destinationCatalogPath);
}
