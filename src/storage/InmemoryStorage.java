package storage;

import storage.exceptions.IncorrectPathException;
import storage.structure.Catalog;
import storage.structure.Document;
import storage.structure.StorageUnit;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class InmemoryStorage implements Storage {
    public static String PATH_SEPARATOR = "/";
    public static String ROOT_CATALOG_NAME = "root";

    private final Catalog rootCatalog;

    private static class PathAndName {
        public String path;
        public String fileName;

        public PathAndName(String path, String fileName) {
            this.path = path;
            this.fileName = fileName;
        }
    }

    public InmemoryStorage() {
        rootCatalog = new Catalog(ROOT_CATALOG_NAME);
    }

    @Override
    public synchronized void addDocumentToCatalog(String catalogPath, Document document) {
        Catalog parentCatalog = getCatalogForPath(catalogPath);
        parentCatalog.addStorageUnit(document);
    }

    @Override
    public synchronized void addCatalog(String catalogPath, Catalog catalog) {
        Catalog parentCatalog = getCatalogForPath(catalogPath);
        parentCatalog.addStorageUnit(catalog);
    }

    @Override
    public synchronized void deleteStorageUnit(String fullPath) {
        PathAndName pathAndName = getPahAndName(fullPath);
        Catalog parentCatalog = getCatalogForPath(pathAndName.path);

        parentCatalog.removeStorageUnit(pathAndName.fileName);
    }

    @Override
    public synchronized void moveStorageUnit(String sourceFilePath, String destinationCatalogPath) {
        PathAndName pathAndName = getPahAndName(sourceFilePath);
        Catalog sourceCatalog = getCatalogForPath(pathAndName.path);
        Catalog destinationCatalog = getCatalogForPath(destinationCatalogPath);

        if (!sourceCatalog.isStorageUnitExist(pathAndName.fileName)
                || destinationCatalog.isStorageUnitExist(pathAndName.fileName)) {
            throw new IncorrectPathException();
        }

        StorageUnit removedStorageUnit = sourceCatalog.removeStorageUnit(pathAndName.fileName);
        destinationCatalog.addStorageUnit(removedStorageUnit);
    }

    @Override
    public synchronized List<Document> findByAuthorName(String authorName) {
        return findByCondition(
                storageUnit -> storageUnit instanceof Document
                                && ((Document) storageUnit).getAuthorName().equals(authorName)
            )
            .stream()
            .map(element -> (Document) element)
            .collect(Collectors.toList());
    }

    @Override
    public synchronized List<Document> findByName(String name) {
        return findByCondition(
                storageUnit -> storageUnit instanceof Document
                                && storageUnit.getName().equals(name)
            )
            .stream()
            .map(element -> (Document) element)
            .collect(Collectors.toList());
    }

    private List<StorageUnit> findByCondition(Predicate<StorageUnit> isSuitable) {
        List<StorageUnit> suitableStorageUnits = new ArrayList<>();
        findByConditionRecursive(rootCatalog, suitableStorageUnits, isSuitable);
        return suitableStorageUnits;
    }

    private void findByConditionRecursive(Catalog catalog,
                                   List<StorageUnit> suitableStorageUnits, Predicate<StorageUnit> isSuitable) {
        for (StorageUnit storageUnit : catalog.getStorageUnits()) {
            if (isSuitable.test(storageUnit)) {
                suitableStorageUnits.add(storageUnit.getCopy());
            }

            if (storageUnit instanceof Catalog) {
                findByConditionRecursive((Catalog) storageUnit, suitableStorageUnits, isSuitable);
            }
        }
    }

    private PathAndName getPahAndName(String fullPath) {
        int separatorIndex = fullPath.lastIndexOf(PATH_SEPARATOR);
        return new PathAndName(fullPath.substring(0, separatorIndex), fullPath.substring(separatorIndex + 1));
    }

    private Catalog getCatalogForPath(String path) {
        StorageUnit storageUnit = getStorageUnitForPath(path);

        if (!(storageUnit instanceof Catalog)) {
            throw new IncorrectPathException();
        }

        return (Catalog) storageUnit;
    }

    private StorageUnit getStorageUnitForPath(String path) {
        String[] pathParts = path.split(PATH_SEPARATOR);  
        if (!pathParts[0].equals(ROOT_CATALOG_NAME)) {
            throw new IncorrectPathException();
        }

        StorageUnit currentStorageUnit = rootCatalog;
        for (int i = 1; i < pathParts.length; i++) {
            if (!(currentStorageUnit instanceof Catalog)) {
                throw new IncorrectPathException();
            }
            final String pathPartName = pathParts[i];
            currentStorageUnit = ((Catalog) currentStorageUnit).findByName(pathPartName);
        }

        return currentStorageUnit;
    }
}
