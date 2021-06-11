package storage.structure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import storage.exceptions.ExistingNameException;
import storage.exceptions.StorageUnitNotFoundException;

public class Catalog extends StorageUnit {
    private List<StorageUnit> storageUnits;

    public Catalog(String name) {
        super(name);
        this.storageUnits = new ArrayList<>();
    }

    private Catalog(String name, List<StorageUnit> storageUnits) {
        this(name);
        this.storageUnits = storageUnits;
    }

    @Override
    public StorageUnit getCopy() {
        return new Catalog(getName(), List.copyOf(storageUnits));
    }

    public List<StorageUnit> getStorageUnits() {
        return Collections.unmodifiableList(storageUnits);
    }

    public void addStorageUnit(StorageUnit storageUnit) {
        boolean isExistingName = isStorageUnitExist(storageUnit.getName());
        if (isExistingName) {
            throw new ExistingNameException();
        }
        storageUnits.add(storageUnit);
    }

    public StorageUnit findByName(String name) {
        return storageUnits.stream()
                .filter(unit -> unit.getName().equals(name))
                .findFirst()
                .orElseThrow(StorageUnitNotFoundException::new);
    }

    public boolean isStorageUnitExist(String name) {
        return storageUnits.stream().anyMatch(unit -> unit.getName().equals(name));
    }

    public StorageUnit removeStorageUnit(String name) {
        int storageIndex = storageUnits.indexOf(findByName(name));
        return storageUnits.remove(storageIndex);
    }
}
