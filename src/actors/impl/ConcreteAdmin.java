package actors.impl;

import actors.Admin;
import storage.Storage;
import storage.structure.Catalog;

public class ConcreteAdmin implements Admin {

    private final Storage storage;

    public ConcreteAdmin(Storage storage) {
        this.storage = storage;
    }

    @Override
    public void createCatalog(String catalogPath, Catalog catalog) {
        storage.addCatalog(catalogPath, catalog);
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
