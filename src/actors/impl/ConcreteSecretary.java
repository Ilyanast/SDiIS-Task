package actors.impl;

import actors.Secretary;
import storage.Storage;
import storage.structure.Document;

import java.util.List;

public class ConcreteSecretary implements Secretary {

    private final Storage storage;

    public ConcreteSecretary(Storage storage) {
        this.storage = storage;
    }

    @Override
    public void addDocumentToCatalog(String catalogPath, Document document) {
        storage.addDocumentToCatalog(catalogPath, document);
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
