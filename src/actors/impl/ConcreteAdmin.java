package actors.impl;

import actors.Admin;
import storage.Storage;

public class ConcreteAdmin extends BaseRole implements Admin {
    public ConcreteAdmin(Storage storage) {
        super(storage);
    }

    @Override
    public void createCatalog(String fullPath) {
        storage.createCatalog(fullPath);
    }

    @Override
    public void deleteDocument(String documentPath) {
        storage.deleteStorageUnit(documentPath);
    }

    @Override
    public void moveDocument(String sourceFilePath, String destinationCatalogPath) {
        storage.moveStorageUnit(sourceFilePath, destinationCatalogPath);
    }
}
