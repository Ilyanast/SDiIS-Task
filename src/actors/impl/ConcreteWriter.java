package actors.impl;

import actors.Writer;
import storage.Storage;

public class ConcreteWriter extends BaseRole implements Writer {
    private final String name;

    public ConcreteWriter(Storage storage, String name) {
        super(storage);

        this.name = name;
    }

    @Override
    public void createDocument(String fullPath, String content) {
        storage.createDocument(fullPath, name, content);
    }

    @Override
    public void updateDocument(String fullPath, String newName, String newContent) {
        storage.updateDocument(fullPath, newName, newContent);
    }
}
