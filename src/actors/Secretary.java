package actors;

import storage.structure.Document;

import java.util.List;

public interface Secretary {
    void addDocumentToCatalog(String catalogPath, Document document);
    List<Document> findByAuthorName(String authorName);
    List<Document> findByName(String name);
}
