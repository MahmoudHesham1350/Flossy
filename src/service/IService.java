package service;

import java.io.Serializable;
import java.util.Dictionary;

/**
 * Interface for validation services in the Flossy Personal Finance Manager system.
 * @param <T> The type of object to validate and create
 */
public interface IService <T extends Serializable> {
    /**
     * Validates input data and creates an object of type T if valid.
     * @param data The input data dictionary
     * @return The created object of type T
     * @throws IllegalArgumentException if validation fails
     */
    public T validate(Dictionary<String, String> data) throws IllegalArgumentException;
}
