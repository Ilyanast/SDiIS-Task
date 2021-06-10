package actors.impl;

import storage.Storage;

public class BaseRole {
    protected final Storage storage;

    public BaseRole(Storage storage) {
        this.storage = storage;
    }
}
