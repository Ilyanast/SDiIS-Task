package actors.impl;

import actors.Secretary;
import storage.Storage;
import storage.structure.Document;

import java.util.List;

public class ConcreteSecretary extends BaseRole implements Secretary {
    private final String name;

    public ConcreteSecretary(Storage storage, String name) {
        super(storage);

        this.name = name;
    }

    @Override
    public void createDocument(String fullPath, String content) {
        storage.createDocument(fullPath, name, content);
    }

    @Override
    public List<Document> findByAuthorName(String authorName) {
        return storage.findByAuthorName(authorName);
    }

    @Override
    public List<Document> findByName(String name) {
        return storage.findByName(name);
    }
}
