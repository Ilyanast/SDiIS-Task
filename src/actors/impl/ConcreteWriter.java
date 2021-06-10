package actors.impl;

import actors.Writer;
import storage.structure.Document;

public class ConcreteWriter implements Writer {

    private final String authorName;

    public ConcreteWriter(String authorName) {
        this.authorName = authorName;
    }

    @Override
    public Document createDocument(String name, String content) {
        return new Document(name, authorName, content);
    }

    @Override
    public void updateDocument(Document document, String name, String content) {
        document.setName(name);
        document.setContent(content);
    }
}
