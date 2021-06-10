package actors;

import storage.structure.Document;

public interface Writer {
    Document createDocument(String name, String content);
    void updateDocument(Document document, String name, String content);
}
