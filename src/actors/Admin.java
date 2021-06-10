package actors;

public interface Admin {
    void createCatalog(String fullPath);
    void deleteDocument(String documentPath);
    void moveDocument(String sourceFilePath, String destinationCatalogPath);
}
