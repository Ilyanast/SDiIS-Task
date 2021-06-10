package actors;

import storage.structure.Document;

import java.util.List;

public interface Secretary {
    void createDocument(String fullPath, String content);
    List<Document> findByAuthorName(String authorName);
    List<Document> findByName(String name);
}
