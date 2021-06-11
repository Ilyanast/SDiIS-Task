package storage.structure;

public abstract class StorageUnit {
    private String name;

    public StorageUnit(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract StorageUnit getCopy();

    public void setName(String newName) {
        this.name = name;
    }
}
