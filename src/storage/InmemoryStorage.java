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

    private ReadWriteLock readWriteLock;
    private final Catalog rootCatalog;

    private class PathAndName {
        public String path;
        public String fileName;

        public PathAndName(String path, String fileName) {
            this.path = path;
            this.fileName = fileName;
        }
    }

    public InmemoryStorage() {
        rootCatalog = new Catalog(ROOT_CATALOG_NAME);
        readWriteLock = new ReentrantReadWriteLock();
    }

    @Override
    public void createDocument(String fullPath, String authorName, String content) {
        readWriteLock.writeLock().lock();

        try {
            PathAndName pathAndName = getPahAndName(fullPath);
            Catalog parentCatalog = getCatalogForPath(pathAndName.path);

            Document document = new Document(pathAndName.fileName, authorName, content);
            parentCatalog.addStorageUnit(document);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @Override
    public void createCatalog(String fullPath) {
        readWriteLock.writeLock().lock();

        try {
            PathAndName pathAndName = getPahAndName(fullPath);
            Catalog parentCatalog = getCatalogForPath(pathAndName.path);

            Catalog catalog = new Catalog(pathAndName.fileName);
            parentCatalog.addStorageUnit(catalog);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @Override
    public void deleteStorageUnit(String fullPath) {
        readWriteLock.writeLock().lock();

        try {
            PathAndName pathAndName = getPahAndName(fullPath);
            Catalog parentCatalog = getCatalogForPath(pathAndName.path);

            parentCatalog.removeStorageUnit(pathAndName.fileName);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @Override
    public void updateDocument(String fullPath, String newName, String newContent) {
        readWriteLock.writeLock().lock();

        try {
            PathAndName pathAndName = getPahAndName(fullPath);
            Catalog parentCatalog = getCatalogForPath(pathAndName.path);

            Document document = (Document) parentCatalog.findByName(pathAndName.fileName);
            document.setName(newName);
            document.setContent(newContent);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @Override
    public void moveStorageUnit(String sourceFilePath, String destinationCatalogPath) {
        readWriteLock.writeLock().lock();

        try {
            PathAndName pathAndName = getPahAndName(sourceFilePath);
            Catalog sourceCatalog = getCatalogForPath(pathAndName.path);
            Catalog destinationCatalog = getCatalogForPath(destinationCatalogPath);

            if (!sourceCatalog.isStorageUnitExist(pathAndName.fileName)
                    || destinationCatalog.isStorageUnitExist(pathAndName.fileName)) {
                throw new IncorrectPathException();
            }

            StorageUnit removedStorageUnit = sourceCatalog.removeStorageUnit(pathAndName.fileName);
            destinationCatalog.addStorageUnit(removedStorageUnit);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @Override
    public List<Document> findByAuthorName(String authorName) {
        readWriteLock.readLock().lock();

        try {
            return findByCondition(
                    storageUnit -> storageUnit instanceof Document
                                    && ((Document) storageUnit).getAuthorName().equals(authorName)
                )
                .stream()
                .map(element -> (Document) element)
                .collect(Collectors.toList());
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    @Override
    public List<Document> findByName(String name) {
        readWriteLock.readLock().lock();

        try {
            return findByCondition(
                    storageUnit -> storageUnit instanceof Document
                                    && storageUnit.getName().equals(name)
                )
                .stream()
                .map(element -> (Document) element)
                .collect(Collectors.toList());
        } finally {
            readWriteLock.readLock().unlock();
        }
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
                suitableStorageUnits.add(storageUnit);
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
