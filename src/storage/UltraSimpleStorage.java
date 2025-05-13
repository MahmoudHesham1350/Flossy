package storage;

import java.util.ArrayList;
import java.io.*;
import java.util.List;

/**
 * Abstract class for file-based storage of serializable objects in the Flossy Personal Finance Manager system.
 * Handles save, load, add, remove, and getAll operations for a list of objects.
 * @param <T> The type of object to store (must be Serializable)
 */
public abstract class UltraSimpleStorage<T extends Serializable> {
    /** The filename for storing the objects. */
    private final String filename;
    /** The in-memory list of stored objects. */
    protected List<T> storage;

    /**
     * Constructs an UltraSimpleStorage with the given filename.
     * Loads existing data if available, otherwise starts with an empty list.
     * @param filename The file to store the objects
     */
    public UltraSimpleStorage(String filename) {
        this.filename = filename;
        try {
            this.storage = this.load();
        } catch (Exception e) {
            System.out.println("No previous data found. Starting fresh.");
            this.storage = new ArrayList<>();
        }
    }

    /**
     * Saves the current list of objects to the file.
     * @throws IOException if file writing fails
     */
    public void save() throws IOException {
        try (FileOutputStream fos = new FileOutputStream(filename);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(storage);
        }
        System.out.println("Objects saved successfully to file: " + filename);
    }

    /**
     * Loads the list of objects from the file.
     * @return The loaded list of objects
     * @throws IOException if file reading fails
     * @throws ClassNotFoundException if deserialization fails
     */
    public List<T> load() throws IOException, ClassNotFoundException {
        try (FileInputStream fis = new FileInputStream(filename);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (List<T>) ois.readObject();
        }
    }

    /**
     * Adds an object to the storage.
     * @param object The object to add
     */
    public void add(T object) {
        storage.add(object);
    }

    /**
     * Removes an object from the storage.
     * @param object The object to remove
     */
    public void remove(T object) {
        storage.remove(object);
    }

    /**
     * Gets all objects currently in storage.
     * @return The list of stored objects
     */
    public List<T> getAll() {
        return storage;
    }
}