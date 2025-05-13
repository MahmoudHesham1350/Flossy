package controller;

import java.util.Dictionary;
import java.util.List;

/**
 * Interface for controllers in the Flossy Personal Finance Manager system.
 * Provides CRUD operations for a specific type.
 * @param <T> The type of object the controller manages
 */
public interface IController<T> {
    /**
     * Creates a new object from the provided data.
     * @param data The data dictionary for the object
     */
    public void create(Dictionary <String, String> data);
    /**
     * Gets all objects managed by the controller.
     * @return A list of all objects
     */
    public List<T> getAll();
    /**
     * Removes the specified object.
     * @param object The object to remove
     */
    public void remove(T object);
    /**
     * Saves all managed objects to persistent storage.
     */
    public void save();
}
