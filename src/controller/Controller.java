package controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.Dictionary;
import java.util.List;

import service.IService;
import storage.UltraSimpleStorage;

/**
 * Generic controller for managing CRUD operations on a specific type in the Flossy Personal Finance Manager system.
 * Uses a service for validation and a storage for persistence.
 * @param <T> The type of object managed (must be Serializable)
 */
public class Controller <T extends Serializable> implements IController<T> {
    /** The service for validation. */
    protected IService<T> service;
    /** The storage for persistence. */
    private UltraSimpleStorage<T> storage;

    /**
     * Constructs a Controller with the given service and storage.
     * @param service The validation service
     * @param storage The persistent storage
     */
    public Controller(IService<T> service, UltraSimpleStorage<T> storage) {
        this.service = service;
        this.storage = storage;
    }

    /**
     * Creates a new object from the provided data and adds it to storage.
     * @param data The data dictionary for the object
     * @throws IllegalArgumentException if validation fails
     */
    public void create(Dictionary <String, String> data) throws IllegalArgumentException {
        T object = service.validate(data);
        storage.add(object);
    }

    /**
     * Gets all objects managed by the controller.
     * @return A list of all objects
     */
    @Override
    public List<T> getAll() {
        return storage.getAll();
    }

    /**
     * Removes the specified object from storage.
     * @param object The object to remove
     */
    public void remove(T object) {
        storage.remove(object);
    }

    /**
     * Saves all managed objects to persistent storage.
     * Throws RuntimeException if saving fails.
     */
    public void save() {
        try {
            storage.save();
        } catch (IOException e) {
            throw new RuntimeException("Error saving data", e);
        } catch (Exception e) {
            throw new RuntimeException("Error saving data", e);
        }
    }
}
